package com.restaurant.td5springboot.entity;

public class StockValue {
    double quantity;
    Unit unit;

    public StockValue() {
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
