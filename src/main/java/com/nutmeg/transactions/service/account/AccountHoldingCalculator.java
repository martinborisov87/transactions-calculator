package com.nutmeg.transactions.service.account;

import com.nutmeg.transactions.model.Holding;
import com.nutmeg.transactions.model.Transaction;

import java.util.List;

/**
 * Created by Martin on 8/1/2017.
 * <p>
 * An interface to calculate holdings based on list of transactions for a single account
 */
public interface AccountHoldingCalculator {
    List<Holding> calculateHoldings(List<Transaction> accountTransactions);
}
