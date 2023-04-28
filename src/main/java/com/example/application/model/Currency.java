package com.example.application.model;

public class Currency {
    private String symbol;
    private String name;
    private double conversion;

    public Currency(String name, String symbol, double conversion){
        this.name=name;
        this.symbol=symbol;
        this.conversion=conversion;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
    }

    @Override
    public String toString(){
        return symbol+" ("+name+")";
    }
}
