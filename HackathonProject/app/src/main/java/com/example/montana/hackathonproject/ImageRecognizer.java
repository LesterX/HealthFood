package com.example.montana.hackathonproject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;



public class ImageRecognizer
{
    private ClarifaiClient client;
    private Concept con;
    private Map<String,Float> result_list;
    private String top_result;

    public ImageRecognizer(){
        client = new ClarifaiBuilder("d948fb5be9644b039df3b68e68e251bf")
                .client(new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .build()
                )
                .buildSync();
        result_list = new HashMap<String,Float>();
    }

    public void readImage(String url){

        Model<Concept> generalModel = client.getDefaultModels().foodModel();

        PredictRequest<Concept> request = generalModel.predict().withInputs(
                ClarifaiInput.forImage(url));
        List<ClarifaiOutput<Concept>> result = request.executeSync().get();

        ClarifaiOutput<Concept> output = result.get(0);

        for (int i = 0; i < output.data().size(); i ++)
        {
            Concept con = output.data().get(i);
            if (con != null)
                result_list.put(con.name(), con.value());

            if (i == 0)
                top_result = con.name();
        }
    }

    //Return the possible results with the probabilities
    public Map<String,Float> getResults(){
        if (result_list.size() == 0)
            return null;
        else
            return result_list;
    }

    //Return the most probable result
    public String getTopResult(){
        if (top_result == null)
            return null;
        else
            return top_result;
    }

}
