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


public class WorstRestaurantResults extends Activity {
    private String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worst_restaurant_results);
        Intent intent = getIntent();
        result = intent.getStringExtra("results");

        try {
            JSONArray basicArr = new JSONArray(result);
            System.out.println(basicArr.length());
            int length = basicArr.length();
            String[] listOfNames = new String[length];
            String[] listOfAddresses = new String[length];
            String[] listOfOperatorNames = new String[length];
            String[] listOfOperatorEmails = new String[length];
            int[] listOfScores = new int[length];
            int[] listOfComplaints = new int[length];
            String[][] listOfDescriptions = new String[10][10];
            for (int i = 0; i < length; i++) {
                listOfNames[i] = basicArr.getJSONObject(0).getString("name");

                String street = basicArr.getJSONObject(1).getString("street");
                String city = basicArr.getJSONObject(2).getString("city");
                String state = basicArr.getJSONObject(3).getString("state");
                String zipcode = basicArr.getJSONObject(4).getString("zipcode");
                String address = street + "," + city + "," + state + zipcode;
                listOfAddresses[i] = address;


                String fn = basicArr.getJSONObject(5).getString("firstname");
                String ln = basicArr.getJSONObject(6).getString("lastname");
                String fullname = fn +" "+ ln;
                listOfOperatorNames[i] = fullname;

                listOfOperatorEmails[i] =  basicArr.getJSONObject(7).getString("email");

                listOfScores[i] =  basicArr.getJSONObject(8).getInt("totalscore");


                listOfComplaints[i] = basicArr.getJSONObject(9).getInt("count");

                JSONArray commentArr = new JSONArray(basicArr.getJSONObject(10).getJSONArray("comments"));
                int commentLength = commentArr.length();


                for (int j =  0; j < commentLength; j++) {
                    listOfDescriptions[i][j] = commentArr.getJSONObject(j).getString("description");
                }
            }



            TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
            View tableRow = getLayoutInflater().inflate(R.layout.activity_worst_restaurant_results_row, null, false);

            TextView myName = (TextView) tableRow.findViewById(R.id.nameHeader);
            myName.setText("Name");

            TextView myAddress = (TextView) tableRow.findViewById(R.id.addressHeader);
            myAddress.setText("Address");

            TextView myOperatorName = (TextView) tableRow.findViewById(R.id.operatorNameHeader);
            myOperatorName.setText("Operator Name");

            TextView myAddresses = (TextView) tableRow.findViewById(R.id.operatorEmailHeader);
            myAddresses.setText("Operator Email");

            TextView myScores = (TextView) tableRow.findViewById(R.id.scoreHeader);
            myScores.setText("Score");

            TextView myComplaints = (TextView) tableRow.findViewById(R.id.complaintsHeader);
            myComplaints.setText("Number Of Complaints");


            table.addView(tableRow);

            for (int i = 0; i < length; i++) {
                tableRow = getLayoutInflater().inflate(R.layout.activity_worst_restaurant_results_row, null, false);

                TextView n = (TextView) tableRow.findViewById(R.id.nameHeader);
                n.setText(listOfNames[i] + "");


                TextView a = (TextView) tableRow.findViewById(R.id.addressHeader);
                a.setText(listOfAddresses[i]+ "");

                TextView on = (TextView) tableRow.findViewById(R.id.operatorNameHeader);
                on.setText(listOfOperatorNames[i]+ "");

                TextView oe = (TextView) tableRow.findViewById(R.id.operatorEmailHeader);
                oe.setText(listOfOperatorEmails[i] + "");

                TextView s = (TextView) tableRow.findViewById(R.id.scoreHeader);
                s.setText(listOfScores[i]+ "");

                TextView c = (TextView) tableRow.findViewById(R.id.complaintsHeader);
                c.setText(listOfComplaints[i] + "");

                for (int j = 0; j < length; j++) {
                    TextView description = (TextView) tableRow.findViewById(R.id.descriptionHeader);
                    if ( listOfDescriptions[i][j] != null){
                        description.setText(listOfDescriptions[i][j] + "");
                    }

                }
                table.addView(tableRow);

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_worst_restaurant_results, menu);
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
