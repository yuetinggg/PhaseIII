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


public class YearlyReportResults extends Activity {
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_report_results);

        Intent intent = getIntent();
        result = intent.getStringExtra("results");
        int[] numRestaurantsInspected = new int[13];

        try {
            JSONArray basicArr = new JSONArray(result);
            int total = 0;
            JSONObject basicInfo;
            basicInfo = basicArr.getJSONObject(0);
            numRestaurantsInspected[0] = basicInfo.getInt("January");
            numRestaurantsInspected[1] = basicInfo.getInt("February");
            numRestaurantsInspected[2] = basicInfo.getInt("March");
            numRestaurantsInspected[3] = basicInfo.getInt("April");
            numRestaurantsInspected[4] = basicInfo.getInt("May");
            numRestaurantsInspected[5] = basicInfo.getInt("June");
            numRestaurantsInspected[6] = basicInfo.getInt("July");
            numRestaurantsInspected[7] = basicInfo.getInt("August");
            numRestaurantsInspected[8] = basicInfo.getInt("September");
            numRestaurantsInspected[9] = basicInfo.getInt("October");
            numRestaurantsInspected[10] = basicInfo.getInt("November");
            numRestaurantsInspected[11] = basicInfo.getInt("December");



            for (int i = 0; i < 12; i++) {
                total += numRestaurantsInspected[i];
            }
            numRestaurantsInspected[12] = total;

            String[] months = new String[13];
            months[0] = "January"; months[1] = "February"; months[2] = "March";
            months[3] = "April"; months[4] = "May"; months[5] = "June";
            months[6] = "July"; months[7] = "August"; months[8] = "September";
            months[9] = "October"; months[10] = "November"; months[11] = "December";
            months[12] = "Total";

            TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
            View tableRow = getLayoutInflater().inflate(R.layout.activity_yearly_report_results_row, null, false);

            TextView monthHeader = (TextView) tableRow.findViewById(R.id.monthHeader);
            monthHeader.setText("Month");

            TextView numInspections= (TextView) tableRow.findViewById(R.id.numInspectionsHeader);
            numInspections.setText("Number Of Inspections");

            table.addView(tableRow);
            System.out.println(numInspections);

            for (int i = 0; i < 13; i++) {
                tableRow = getLayoutInflater().inflate(R.layout.activity_yearly_report_results_row, null, false);

                TextView enumMonths = (TextView) tableRow.findViewById(R.id.monthHeader);
                enumMonths.setText(months[i]);

                TextView myNumInspections = (TextView) tableRow.findViewById(R.id.numInspectionsHeader);

                myNumInspections.setText(numRestaurantsInspected[i] + "");
                table.addView(tableRow);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_yearly_report_results, menu);
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
