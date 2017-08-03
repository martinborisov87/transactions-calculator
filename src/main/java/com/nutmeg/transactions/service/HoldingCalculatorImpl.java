package com.nutmeg.transactions.service;

import com.nutmeg.transactions.model.Holding;
import com.nutmeg.transactions.model.Transaction;
import com.nutmeg.transactions.service.account.AccountHoldingCalculator;
import com.nutmeg.transactions.service.filter.TransactionFilterService;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HoldingCalculatorImpl implements HoldingCalculator {

    private final TransactionFilterService transactionFilterService;
    private final AccountHoldingCalculator accountHoldingCalculator;

    public HoldingCalculatorImpl(TransactionFilterService transactionFilterService,
                                 AccountHoldingCalculator accountHoldingCalculator) {
        this.transactionFilterService = transactionFilterService;
        this.accountHoldingCalculator = accountHoldingCalculator;
    }

    //---------------------------------------

    @Override
    public Map<String, List<Holding>> calculateHoldings(File transactionFile, LocalDate date) {
        Map<String, List<Transaction>> transactionsByAccNumber = transactionFilterService.getTransactionsFilterByDateGroupByAccNumber(transactionFile, date);

        Map<String, List<Holding>> result = new HashMap<>();
        for (Map.Entry<String, List<Transaction>> tan : transactionsByAccNumber.entrySet()) {
            List<Holding> accountHoldings = accountHoldingCalculator.calculateHoldings(tan.getValue());
            result.put(tan.getKey(), accountHoldings);
        }

        return result;
    }
}
