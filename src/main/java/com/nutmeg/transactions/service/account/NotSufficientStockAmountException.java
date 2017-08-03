package com.nutmeg.transactions.service.account;

/**
 * Created by Martin on 8/2/2017.
 */
public class NotSufficientStockAmountException extends RuntimeException {

    public NotSufficientStockAmountException(String accountNumber, String asset) {
        super(String.format("Not enough %s in order to sell, for account %s", asset, accountNumber));
    }
}
