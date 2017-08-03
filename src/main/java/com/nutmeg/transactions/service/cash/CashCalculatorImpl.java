package com.nutmeg.transactions.service.cash;

import com.nutmeg.transactions.model.Transaction;
import com.nutmeg.transactions.model.enums.TxnType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CashCalculatorImpl implements CashCalculator {

    @Override
    public BigDecimal calculateCash(Transaction transaction) {
        if (transaction.getType() != TxnType.BOT && transaction.getType() != TxnType.SLD) {
            return transaction.getUnits();
        }

        BigDecimal cash = round(transaction.getUnits().multiply(transaction.getPrice()));
        if (transaction.getType() == TxnType.BOT) {
            cash = cash.negate();
        }
        return cash;
    }

    private static BigDecimal round(BigDecimal number) {
        return number.setScale(4, RoundingMode.HALF_UP);
    }
}
