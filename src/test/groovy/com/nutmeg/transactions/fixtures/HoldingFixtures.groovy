package com.nutmeg.transactions.fixtures

import com.nutmeg.transactions.model.Holding

/**
 * Created by Martin on 8/1/2017.
 */
class HoldingFixtures {
    static Holding aHolding(asset, holding) {
        new Holding(
                asset: asset,
                holding: holding
        )
    }

    static Holding aCashHolding(holding) {
        new Holding(
                asset: "CASH",
                holding: holding
        )
    }
}