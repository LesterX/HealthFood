package com.example.montana.hackathonproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FoodNutrition {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;

    public FoodNutrition()
    {
        client = new OkHttpClient();
    }

    private String run(String url,String food)
    {
        String json = "{\n\"query\":\"" + food + "\",\n\"timezone\":\"US/Eastern\"\n}";
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).addHeader("x-app-id","0d379c07").addHeader(
                "x-app-key", "f27526ff3f107a3f2fa696786ce47d9d").post(body).build();

        try
        {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                return response.body().string();
            }
        }catch (IOException e)
        {
            System.out.println("Not found");
            return null;
        }

        return null;
    }

    public Map<String,Float> getNutrition(String food)
    {
        Map<String,Float> list = new HashMap<String,Float>();

        String response = run("https://trackapi.nutritionix.com/v2/natural/nutrients",food);

        if (response == null)
            return null;

        System.out.println(response);

        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("foods");
            JSONObject target = arr.getJSONObject(0);

            list.put("calories", BigDecimal.valueOf(target.getDouble("nf_calories")).floatValue());
            list.put("fat", BigDecimal.valueOf(target.getDouble("nf_total_fat")).floatValue());
            list.put("sodium", BigDecimal.valueOf(target.getDouble("nf_sodium")).floatValue());
            list.put("carbohydrate", BigDecimal.valueOf(target.getDouble("nf_total_carbohydrate")).floatValue());
            list.put("dietary fiber", BigDecimal.valueOf(target.getDouble("nf_dietary_fiber")).floatValue());
            list.put("sugars", BigDecimal.valueOf(target.getDouble("nf_sugars")).floatValue());
            list.put("protein", BigDecimal.valueOf(target.getDouble("nf_protein")).floatValue());
        } catch(JSONException e){
            Log.d("JSONException","Incorrect format");
        }
        return list;
    }
}
