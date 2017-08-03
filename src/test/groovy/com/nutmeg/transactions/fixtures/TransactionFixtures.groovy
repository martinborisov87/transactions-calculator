package com.nutmeg.transactions.fixtures

import com.nutmeg.transactions.model.Transaction

import static com.nutmeg.transactions.model.enums.TxnType.*
import static com.nutmeg.transactions.service.account.AccountHoldingCalculatorImpl.CASH_ASSET_CODE

/**
 * Created by Martin on 8/1/2017.
 */
class TransactionFixtures {
    static Transaction aTransactionForAccount(account, date) {
        new Transaction(
                account: account,
                date: date
        )
    }

    static Transaction aTransaction(type, asset, units, price = 1) {
        new Transaction(
                account: "ACC1",
                type: type,
                units: units,
                asset: asset,
                price: price
        )
    }

    static Transaction aBoughtStockTransaction(asset = "DEF_ASSET", units = 1, price = 1) {
        aTransaction(BOT, asset, units, price)
    }

    static Transaction aSoldStockTransaction(asset = "DEF_ASSET", units = 1, price = 1) {
        aTransaction(SLD, asset, units, price)
    }

    static Transaction aDepositTransaction(units = 1, price = 1) {
        aTransaction(DEP, CASH_ASSET_CODE, units, price)
    }

    static Transaction aWithdrawTransaction(units = 1, price = 1) {
        aTransaction(WDR, CASH_ASSET_CODE, units, price)
    }

    static Transaction aDividendTransaction(units = 1, price = 1) {
        aTransaction(DIV, "ASSE", units, price)
    }
}