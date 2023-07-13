package com.example.application.model;

public class Currency {
    private final String symbol;
    private String name;
    private final double conversion;

    public Currency(String name, String symbol, double conversion){
        this.name=name;
        this.symbol=symbol;
        this.conversion=conversion;
    }
    public Currency(String symbol, double conversion){
        this.symbol=symbol;
        this.conversion=conversion;
        this.name="";
    }

    public String getSymbol() {
        return symbol;
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

    @Override
    public String toString(){
        return symbol+" ("+name+")";
    }
}
