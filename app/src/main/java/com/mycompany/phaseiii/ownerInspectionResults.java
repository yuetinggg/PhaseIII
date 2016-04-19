package com.mycompany.phaseiii;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ownerInspectionResults extends Activity {
    private String restaurantName;
    private final String serverURL = "http://yuetinglee.host56.com/4400/ownerInspection.php";
    private String[] items;
    private int[] totalScore;
    private String[] passfail;
    private String[] date;
    private int[][] scores;
    private int numInspections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_inspection_results);

        items = new String[15];
        items[0] = "The food is in good condition and safe for human consumption";
        items[1] = "Potentially hazardous food is stored, prepared, displayed according to specified time and temperature requirements";
        items[2] = "Cross-contamination is prevented";
        items[3] = "Personnel with infections or communicable diseases are restricted from handling food";
        items[4] = "Personnel wash hands and use good hygienic practices";
        items[5] = "Food equipment and utensils are properly sanitized";
        items[6] = "Hot and cold water is from a safe and approved source";
        items[7] = "Insects, rodents and other animals are kept out of the restaurant";
        items[8] = "Proper equipment is used to maintain product temperature";
        items[9] = "Gloves or utensils are used to minimize handling of food and ice";
        items[10] = "Dishwashing facilities are properly  constructed and maintained";
        items[11] = "Restrooms are clean and properly maintained";
        items[12] = "Rubbish containers are covered and properly located";
        items[13] = "Outside garbage disposal areas are enclosed and clean";
        items[14] = "Non-food contact surfaces of equipment and utensils are clean and free of contaminants";



        AsyncDataClass request = new AsyncDataClass();
        request.execute(serverURL, restaurantName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_owner_inspection_results, menu);
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

    private void populateTable(String result) {
        try {
            JSONArray basicArr = new JSONArray(result);
            numInspections = basicArr.length();
            totalScore = new int[numInspections];
            passfail = new String[numInspections];
            date = new String[numInspections];
            scores = new int[numInspections][15];
            for (int i = 0; i < numInspections; i++) {
                JSONObject basicInfo = basicArr.getJSONObject(i);
                totalScore[i] = basicInfo.getInt("totalscore");
                passfail[i] = basicInfo.getString("passfail");
                date[i] = basicInfo.getString("idate");
                for (int j = 0; j < 15; j++) {
                    scores[i][j] = basicInfo.getInt((j+1) + "");
                }
            }

            TableLayout table = (TableLayout)findViewById(R.id.tabelLayout);
            View tableRow = getLayoutInflater().inflate(R.layout.owner_inspection_table_row, null, false);
            TextView itemNumHeader = (TextView) tableRow.findViewById(R.id.itemnum);
            itemNumHeader.setText("Item Number");
            TextView itemDescriptionHeader = (TextView) tableRow.findViewById(R.id.itemDescription);
            itemDescriptionHeader.setText("Item Description");
            TextView Date1 = (TextView) tableRow.findViewById(R.id.score1);
            Date1.setText("Score on " + date[0]);
            if (numInspections == 2) {
                TextView Date2 = (TextView) tableRow.findViewById(R.id.score2);
                Date2.setText("Score on " + date[1]);
            }
            table.addView(tableRow);
            System.out.println(numInspections);
            for (int i = 0; i < 15; i++) {
                tableRow = getLayoutInflater().inflate(R.layout.owner_inspection_table_row, null, false);
                TextView itemnum = (TextView) tableRow.findViewById(R.id.itemnum);
                itemnum.setText((i + 1) + "");
                TextView itemDescription = (TextView) tableRow.findViewById(R.id.itemDescription);
                itemDescription.setText(items[i]);
                TextView score1 = (TextView) tableRow.findViewById(R.id.score1);
                score1.setText(scores[0][i] + "");
                if (numInspections == 2) {
                    TextView score2 = (TextView) tableRow.findViewById(R.id.score2);
                    score2.setText(scores[1][i] + "");
                } else {
                    TextView score2 = (TextView) tableRow.findViewById(R.id.score2);
                    score2.setText("Not Available");
                }
                table.addView(tableRow);
            }
            tableRow = getLayoutInflater().inflate(R.layout.owner_inspection_table_row, null, false);
            TextView totalScoreLabel = (TextView) tableRow.findViewById(R.id.itemnum);
            totalScoreLabel.setText("Total Score");
            TextView totalScore1 = (TextView) tableRow.findViewById(R.id.score1);
            totalScore1.setText(totalScore[0] + "");
            if (numInspections == 2) {
                TextView totalScore2 = (TextView) tableRow.findViewById(R.id.score2);
                totalScore2.setText(totalScore[1] + "");
            }
            table.addView(tableRow);

            tableRow = getLayoutInflater().inflate(R.layout.owner_inspection_table_row, null, false);
            TextView resultLabel = (TextView) tableRow.findViewById(R.id.itemnum);
            resultLabel.setText("Result");
            TextView result1 = (TextView) tableRow.findViewById(R.id.score1);
            result1.setText(passfail[0]);
            if (numInspections == 2) {
                TextView result2 = (TextView) tableRow.findViewById(R.id.score2);
                result2.setText(passfail[1]);
            }
            table.addView(tableRow);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("restaurantName", params[1]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jsonResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value:" + result);
            System.out.println(result);

            if (result.equals("") || result == null) {
                Toast.makeText(ownerInspectionResults.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }

            populateTable(result);

        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return answer;
        }
    }
}
