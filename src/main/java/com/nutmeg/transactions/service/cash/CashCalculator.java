package com.nutmeg.transactions.service.cash;

import com.nutmeg.transactions.model.Transaction;

import java.math.BigDecimal;

/**
 * Created by Martin on 8/1/2017.
 * <p>
 * An interface to calculate holdings based on list of transactions for a single account
 */
public interface CashCalculator {
    BigDecimal calculateCash(Transaction transaction);
}
