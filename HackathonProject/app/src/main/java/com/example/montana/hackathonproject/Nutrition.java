package com.example.montana.hackathonproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class Nutrition extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

    }

    public void calculate(View v) {

        new NutritionRead().execute(".");
    }


    private class NutritionRead extends AsyncTask<String,Integer,String> {
        protected String doInBackground(String... params) {

            FoodNutrition nutri = new FoodNutrition();

            EditText et = (EditText) findViewById(R.id.input_nutrition);
            if (et.getText() == null){
                Toast.makeText(getApplicationContext(),
                        "Please enter food",Toast.LENGTH_SHORT).show();
                return "";
            }
            Map<String, Float> nutrition_list = nutri.getNutrition(et.getText().toString());
            if (nutrition_list == null)
                Toast.makeText(getApplicationContext(),
                        "Sorry, food not found",Toast.LENGTH_SHORT).show();
            else {
                TextView t1 = (TextView) findViewById(R.id.text_result_calories);
                TextView t2 = (TextView) findViewById(R.id.text_result_fat);
                TextView t3 = (TextView) findViewById(R.id.text_result_sugar);

                t1.setText(nutrition_list.get("calories").toString());
                t2.setText(nutrition_list.get("fat").toString());
                t3.setText(nutrition_list.get("sugars").toString());
            }

                publishProgress(0);

                return "Test complete";
        }

    }

    public void recordCalories(View v){

        TextView totalCalories=(TextView)findViewById(R.id.text_result_calories);
        if (totalCalories.getText() == null)
            return;

        Float num = Float.parseFloat(totalCalories.getText().toString());
        Log.d("MYBUG",String.valueOf(num));

        Intent myIntent = new Intent(Nutrition.this, Profilestats.class);
        myIntent.putExtra("caloriesAdd", num);
        startActivity(myIntent);
    }
}
