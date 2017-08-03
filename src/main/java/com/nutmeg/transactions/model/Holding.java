package com.nutmeg.transactions.model;

import java.math.BigDecimal;

public class Holding implements java.io.Serializable {
    private String asset;
    private BigDecimal holding;

    public Holding() {
    }

    public Holding(String asset, BigDecimal holding) {
        this.asset = asset;
        this.holding = holding;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public BigDecimal getHolding() {
        return holding;
    }

    public void setHolding(BigDecimal holding) {
        this.holding = holding;
    }

    public String toString() {
        return getAsset() + ":\t" + getHolding();
    }
}