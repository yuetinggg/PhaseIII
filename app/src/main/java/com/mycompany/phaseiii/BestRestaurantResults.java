package com.mycompany.phaseiii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BestRestaurantResults extends Activity {
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_restaurant_results);

        Intent intent = getIntent();
        result = intent.getStringExtra("results");

        TextView cuisineHeader;
        TextView nameHeader;
        TextView addressHeader;
        TextView scoreHeader;

        try {
            JSONArray basicArr = new JSONArray(result);
            int length = basicArr.length();
            String[] listOfCuisines = new String[length];
            String[] listOfNames = new String[length];
            String[] listOfAddresses = new String[length];
            int[] listOfScores = new int[length];

            for (int i = 0; i < length; i++) {
                JSONObject basicInfo = basicArr.getJSONObject(i);
                listOfCuisines[i] = basicInfo.getString("cuisine");

                listOfNames[i] = basicInfo.getString("name");

                String street = basicInfo.getString("street");
                String city = basicInfo.getString("city");
                String state = basicInfo.getString("state");
                String zipcode = basicInfo.getString("zipcode");
                String address = street + "," + city + "," + state + zipcode;

                listOfAddresses[i] = address;
                listOfScores[i] = basicInfo.getInt("totalscore");
            }

            TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
            View tableRow = getLayoutInflater().inflate(R.layout.activity_best_restaurant_results_row, null, false);

            TextView myCuisines = (TextView) tableRow.findViewById(R.id.cuisineHeader);
            myCuisines.setText("Cuisine");

            TextView myNames = (TextView) tableRow.findViewById(R.id.nameHeader);
            myNames.setText("Name");

            TextView myAddresses = (TextView) tableRow.findViewById(R.id.addressHeader);
            myAddresses.setText("Address");

            TextView myScores = (TextView) tableRow.findViewById(R.id.scoreHeader);
            myScores.setText("Score");


            table.addView(tableRow);

            for (int i = 0; i < length; i++) {
                tableRow = getLayoutInflater().inflate(R.layout.activity_best_restaurant_results_row, null, false);

                TextView cuisine = (TextView) tableRow.findViewById(R.id.cuisineHeader);
                cuisine.setText(listOfCuisines[i] + "");


                TextView names = (TextView) tableRow.findViewById(R.id.nameHeader);
                names.setText(listOfNames[i]+ "");

                TextView addresses = (TextView) tableRow.findViewById(R.id.addressHeader);
                addresses.setText(listOfAddresses[i]+ "");

                TextView scores = (TextView) tableRow.findViewById(R.id.scoreHeader);
                scores.setText(listOfScores[i] + "");


                table.addView(tableRow);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_best_restaurant_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
