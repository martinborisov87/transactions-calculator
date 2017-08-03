package com.nutmeg.transactions.service.account;

import com.nutmeg.transactions.model.Holding;
import com.nutmeg.transactions.model.Transaction;
import com.nutmeg.transactions.model.enums.TxnType;
import com.nutmeg.transactions.service.cash.CashCalculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class AccountHoldingCalculatorImpl implements AccountHoldingCalculator {

    public static final String CASH_ASSET_CODE = "CASH";

    private final CashCalculator cashCalculator;

    public AccountHoldingCalculatorImpl(CashCalculator cashCalculator) {
        this.cashCalculator = cashCalculator;
    }

    //--------------------------------

    @Override
    public List<Holding> calculateHoldings(List<Transaction> accountTransactions) {
        BigDecimal cashAmount = BigDecimal.ZERO;

        Map<String, BigDecimal> unitsByAsset = new HashMap<>();
        for (Transaction transaction : accountTransactions) {
            updateUnitsByAssetIfNeeded(unitsByAsset, transaction);

            BigDecimal cash = cashCalculator.calculateCash(transaction);
            cashAmount = cashAmount.add(cash);
        }

        List<Holding> holdings = unitsByAsset.entrySet().stream()
                .map(ua -> new Holding(ua.getKey(), ua.getValue()))
                .collect(toList());

        Holding cashHolding = new Holding(CASH_ASSET_CODE, cashAmount);
        holdings.add(cashHolding);

        return holdings;
    }

    private void updateUnitsByAssetIfNeeded(Map<String, BigDecimal> unitsByAsset, Transaction transaction) {
        TxnType type = transaction.getType();
        if (type != TxnType.BOT && type != TxnType.SLD) {
            return;
        }

        BigDecimal transactionUnits = type == TxnType.BOT ? transaction.getUnits() : transaction.getUnits().negate();

        BigDecimal unitsSoFar = unitsByAsset.get(transaction.getAsset());
        BigDecimal updatedUnits = unitsSoFar == null ? transactionUnits : unitsSoFar.add(transactionUnits);

        boolean areNewUnitsLessThanZero = updatedUnits.compareTo(BigDecimal.ZERO) < 0;
        if (areNewUnitsLessThanZero) {
            throw new NotSufficientStockAmountException(transaction.getAccount(), transaction.getAsset());
        }

        unitsByAsset.put(transaction.getAsset(), updatedUnits);
    }
}
