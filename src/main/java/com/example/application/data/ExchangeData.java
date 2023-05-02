package com.example.application.data;

import com.example.application.model.Currency;
import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
            // Convert to JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();
            JsonArray codesObj = jsonobj.getAsJsonArray("supported_codes");
            for(JsonElement e : codesObj){
                Currency c = new Currency(e.getAsJsonArray().get(1).getAsString(), e.getAsJsonArray().get(0).getAsString(),1);
                currencies.add(c);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public static HttpURLConnection connectToCodesAPI() throws IOException {
        String url_str = "https://v6.exchangerate-api.com/v6/1ece87b59a264caa4a79fb4f/codes";
        // Making Request
        URL url = new URL(url_str);
        return  (HttpURLConnection) url.openConnection();
    }
    public static HttpURLConnection connectToPairAPI(String base, String target) throws IOException {
        String url_str = "https://v6.exchangerate-api.com/v6/1ece87b59a264caa4a79fb4f/pair/"+base+"/"+target;
        // Making Request
        URL url = new URL(url_str);
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
            // Convert to JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();
            conversion = jsonobj.get("conversion_rate").getAsDouble();
            if(keyExists){
                conversionMap.get(base).put(target,new Currency(target,conversion));
                HashMap<String,Currency> mapTarget = new HashMap<>();
                conversionMap.put(target,mapTarget);
                conversionMap.get(target).put(base,new Currency(base,(double)1/conversion));
            }
            else {
                HashMap<String,Currency> mapBase = new HashMap<>();
                HashMap<String,Currency> mapTarget = new HashMap<>();
                conversionMap.put(base,mapBase);
                conversionMap.put(target,mapTarget);
                conversionMap.get(base).put(target,new Currency(target,conversion));
                conversionMap.get(target).put(base,new Currency(base,(double)1/conversion));
            }
            System.out.println(jsonobj.get("base_code").getAsString()+"-->"+jsonobj.get("target_code").getAsString()+" "+jsonobj.get("conversion_rate").getAsDouble());
            System.out.println(conversionMap.toString());

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return conversion;
    }

    public static HashSet<Currency> getCurrencies() {
        return currencies;
    }
}
