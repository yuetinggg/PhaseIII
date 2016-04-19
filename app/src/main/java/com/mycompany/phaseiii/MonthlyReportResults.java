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


public class MonthlyReportResults extends Activity {
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report_results);

        Intent intent = getIntent();
        result = intent.getStringExtra("results");

        TextView countyHeader;
        TextView cuisineHeader;
        TextView numInspected;
        TextView numFailed;

        try {
            JSONArray basicArr = new JSONArray(result);
            int length = basicArr.length();
            String[] listOfCounty = new String[length];
            String[] listOfCuisines = new String[length];
            int[] listOfInspected = new int[length];
            int[] listOfFail = new int[length];

            for (int i = 0; i < length; i++) {
                JSONObject basicInfo = basicArr.getJSONObject(i);
                listOfCounty[i] = basicInfo.getString("county");
                listOfCuisines[i] = basicInfo.getString("cuisine");
                listOfInspected[i] = basicInfo.getInt("total");
                listOfFail[i] = basicInfo.getInt("FailCount");
            }

            TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
            View tableRow = getLayoutInflater().inflate(R.layout.activity_monthly_report_row, null, false);

            countyHeader = (TextView) tableRow.findViewById(R.id.countyHeader);
            countyHeader.setText("County");

            cuisineHeader = (TextView) tableRow.findViewById(R.id.cuisineHeader);
            cuisineHeader.setText("Cuisine");

            numInspected = (TextView) tableRow.findViewById(R.id.numRestaurantsInspected);
            numInspected.setText("Number of Restaurants Inspected");

            numFailed = (TextView) tableRow.findViewById(R.id.numFailed);
            numFailed.setText("Number of Inspections Failed");


            table.addView(tableRow);

            for (int i = 0; i < length; i++) {
                tableRow = getLayoutInflater().inflate(R.layout.activity_monthly_report_row, null, false);

                TextView county = (TextView) tableRow.findViewById(R.id.countyHeader);
                if (i > 0 && listOfCounty[i].equals(listOfCounty[i-1])) {
                    county.setText("");
                } else {
                    county.setText(listOfCounty[i] + "");
                }

                TextView cuisine = (TextView) tableRow.findViewById(R.id.cuisineHeader);
                cuisine.setText(listOfCuisines[i]+ "");

                TextView nInspected = (TextView) tableRow.findViewById(R.id.numRestaurantsInspected);
                nInspected.setText(listOfInspected[i]+ "");

                TextView nFailed = (TextView) tableRow.findViewById(R.id.numFailed);
                nFailed.setText(listOfFail[i] + "");


                table.addView(tableRow);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_monthly_report_results, menu);
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
