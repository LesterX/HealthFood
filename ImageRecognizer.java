import clarifai2.api.*;
import clarifai2.api.request.model.PredictRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.Model;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import clarifai2.dto.prediction.Frame;
import clarifai2.exception.ClarifaiException;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.*;
import okhttp3.*;
import okio.*;
import org.json.*;

public class ImageRecognizer 
{
	private ClarifaiClient client;
	private Concept con;
	private Map<String,Float> result_list;
	
	//Constructor
	//Key is from Clarifai website user key
	public ImageRecognizer(String key)
	{
		client = new ClarifaiBuilder("22612b503c5b4276ad23b53af545518a").buildSync();
		result_list = new HashMap<String,Float>();
	}
	
	//Read images from url
	public void readImage(String url)
	{
		Model<Concept> generalModel = client.getDefaultModels().foodModel();
		
		PredictRequest<Concept> request = generalModel.predict().withInputs(
		        ClarifaiInput.forImage(url));
		List<ClarifaiOutput<Concept>> result = request.executeSync().get();
		
		ClarifaiOutput<Concept> output = result.get(0);
		
		for (int i = 0; i < output.data().size(); i ++)
		{
			Concept con = output.data().get(i);
			result_list.put(con.name(), con.value());
		}
	}
	
	//Return the possible results with the probabilities
	public Map<String,Float> getResults()
	{
		if (result_list.size() == 0)
			return null;
		else
			return result_list;
	}
}
