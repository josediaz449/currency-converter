package com.example.application.model;

public class Currency {
    private String symbol;
    private String name;
    private float conversion;

    public Currency(String name, String symbol, float conversion){
        this.name=name;
        this.symbol=symbol;
        this.conversion=conversion;
    }
    public Currency(String symbol, float conversion){
        this.symbol=symbol;
        this.conversion=conversion;
        this.name="";
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

    public float getConversion() {
        return conversion;
    }

    public void setConversion(float conversion) {
        this.conversion = conversion;
    }

    @Override
    public String toString(){
        return symbol+" ("+name+")";
    }
}
