package com.mycompany.phaseiii;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class searchResults extends Activity {
    private final String serverURL = "http://yuetinglee.host56.com/4400/index.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String score = intent.getStringExtra("score");
        String lessThan = intent.getStringExtra("lessThan");
        String zipcode = intent.getStringExtra("zipcode");
        String cuisine = intent.getStringExtra("cuisine");

        AsyncDataClass request = new AsyncDataClass();
        request.execute(serverURL, name, score, lessThan, zipcode, cuisine);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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

    private void intoTable(String result) {

        try {
            JSONArray obj = new JSONArray(result);
            TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
            View tableRow = getLayoutInflater().inflate(R.layout.guest_search_row, null, false);
            TextView nameHeader = (TextView) tableRow.findViewById(R.id.name);
            nameHeader.setText("Restaurant");
            TextView addressHeader = (TextView) tableRow.findViewById(R.id.address);
            addressHeader.setText("Address");
            TextView cuisineHeader = (TextView) tableRow.findViewById(R.id.cuisine);
            cuisineHeader.setText("Cuisine");
            TextView scoreHeader = (TextView) tableRow.findViewById(R.id.lastScore);
            scoreHeader.setText("Last Inspection Score");
            TextView dateHeader = (TextView) tableRow.findViewById(R.id.lastInspection);
            dateHeader.setText("Date of Last Inspection");
            table.addView(tableRow);
            for (int i = 0; i < obj.length() - 1; i++) {
                JSONObject obj1 = obj.getJSONObject(i);
                String name = obj1.getString("rname");
                String address = obj1.getString("rstreet") + ", " + obj1.getString("rstreet") + ", " + obj1.getString("rstate") + ", " + obj1.getString("rzipcode");
                String cuisine = obj1.getString("rcuisine");
                String score = obj1.getString("rtotalscore");
                String date = obj1.getString("ridate");

                tableRow = getLayoutInflater().inflate(R.layout.guest_search_row, null, false);
                TextView nameLabel = (TextView) tableRow.findViewById(R.id.name);
                nameLabel.setText(name);
                TextView addressLabel = (TextView) tableRow.findViewById(R.id.address);
                addressLabel.setText(address);
                TextView cuisineLabel = (TextView) tableRow.findViewById(R.id.cuisine);
                cuisineLabel.setText(cuisine);
                TextView scoreLabel = (TextView) tableRow.findViewById(R.id.lastScore);
                scoreLabel.setText(score);
                TextView dateLabel = (TextView) tableRow.findViewById(R.id.lastInspection);
                dateLabel.setText(date);
                table.addView(tableRow);
            }
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
            HttpPost post = new HttpPost(params[0]);

            String jsonResult = "";

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                nameValuePairs.add(new BasicNameValuePair("name", params[1]));
                nameValuePairs.add(new BasicNameValuePair("score", params[2]));
                nameValuePairs.add(new BasicNameValuePair("lessThan", params[3]));
                nameValuePairs.add(new BasicNameValuePair("zipcode", params[4]));
                nameValuePairs.add(new BasicNameValuePair("cuisine", params[5]));
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(post);
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
            System.out.println("Resulted Value: " + result);
            System.out.println(result);

            if (result.equals("") || result == null) {
                Toast.makeText(searchResults.this, "Server Connection Failer", Toast.LENGTH_LONG).show();
                return;
            }

            intoTable(result);

        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {
                while((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return answer;
        }
    }
}
