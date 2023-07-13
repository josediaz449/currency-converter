package com.example.application.data;

import com.example.application.model.Currency;
import com.google.gson.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

public class ExchangeData {
    private static HashMap<String, HashMap<String, Currency>> conversionMap= new HashMap<>();
    private static HashSet<Currency> currencies = new HashSet<>();

    public static void initiateCurrencies(){
        try {
            HttpURLConnection request = connectToCodesAPI();
            request.connect();
            JsonObject jsonObject = getJsonCurrencyObject(request);
            JsonArray codesObj = jsonObject.getAsJsonArray("supported_codes");
            for(JsonElement e : codesObj){
                Currency c = new Currency(e.getAsJsonArray().get(1).getAsString(), e.getAsJsonArray().get(0).getAsString(),1);
                currencies.add(c);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static JsonObject getJsonCurrencyObject(HttpURLConnection request) throws IOException {
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        return root.getAsJsonObject();
    }

    public static HttpURLConnection connectToCodesAPI() throws IOException, URISyntaxException {
        String url_str = "https://v6.exchangerate-api.com/v6/1ece87b59a264caa4a79fb4f/codes";
        URI URI = new URI(url_str);
        URL url = URI.toURL();
        return  (HttpURLConnection) url.openConnection();
    }
    public static HttpURLConnection connectToPairAPI(String base, String target) throws IOException, URISyntaxException {
        String url_str = "https://v6.exchangerate-api.com/v6/1ece87b59a264caa4a79fb4f/pair/"+base+"/"+target;
        URI URI = new URI(url_str);
        URL url = URI.toURL();
        return  (HttpURLConnection) url.openConnection();
    }
    public static double getConversion(String base, String target){
        boolean keyExists = false;
        double conversion =1;
        if(conversionMap.containsKey(base)){
            keyExists = true;
            HashMap<String,Currency> map = conversionMap.get(base);
            if(map.containsKey(target)){
                return map.get(target).getConversion();
            }
        }
        try {
            HttpURLConnection request = connectToPairAPI(base,target);
            request.connect();
            JsonObject jsonObject = getJsonCurrencyObject(request);
            conversion = jsonObject.get("conversion_rate").getAsDouble();
            if(keyExists){
                updateExistingCurrencyMapping(base, target, conversion);
            }
            else {
                createNewCurrencyMapping(base, target, conversion);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return conversion;
    }

    private static void createNewCurrencyMapping(String base, String target, double conversion) {
        HashMap<String,Currency> mapBase = new HashMap<>();
        HashMap<String,Currency> mapTarget = new HashMap<>();
        conversionMap.put(base,mapBase);
        conversionMap.put(target,mapTarget);
        conversionMap.get(base).put(target,new Currency(target, conversion));
        conversionMap.get(target).put(base,new Currency(base,(double)1/ conversion));
    }

    private static void updateExistingCurrencyMapping(String base, String target, double conversion) {
        conversionMap.get(base).put(target,new Currency(target, conversion));
        HashMap<String,Currency> mapTarget = new HashMap<>();
        conversionMap.put(target,mapTarget);
        conversionMap.get(target).put(base,new Currency(base,(double)1/ conversion));
    }

    public static HashSet<Currency> getCurrencies() {
        return currencies;
    }
}
