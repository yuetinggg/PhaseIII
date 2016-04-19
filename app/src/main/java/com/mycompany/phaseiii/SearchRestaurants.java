package com.mycompany.phaseiii;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;


public class SearchRestaurants extends Activity {
    private final String serverURL = "http://yuetinglee.host56.com/4400/index.php";
    protected EditText name;
    private EditText score;
    private EditText zipcode;
    private Spinner cuisine;
    private Spinner scoreSelection;
    private String[] cuisines;
    private String[] lessThan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_restaurants);

        cuisine = (Spinner) findViewById(R.id.cuisineSelection);
        name = (EditText) findViewById(R.id.nameSearch);
        score = (EditText) findViewById(R.id.scoreSearch);
        zipcode = (EditText) findViewById(R.id.zipcodeSearch);
        scoreSelection = (Spinner) findViewById(R.id.scoreSelection);
        lessThan = new String[2];
        lessThan[0] = "Scores greater than or equal to entered";
        lessThan[1] = "Scores less than or equal to entered";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lessThan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scoreSelection.setAdapter(adapter);

        AsyncDataClass1 request = new AsyncDataClass1();
        request.execute(serverURL);

        Button searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scoreSubmission = score.getText().toString();
                String zipcodeSubmission = zipcode.getText().toString();
                String lessThan;
                if (scoreSelection.getSelectedItem().toString().equals("Scores greater than or equal to entered")) {
                    lessThan = "0";
                } else {
                    lessThan = "1";
                }
                if(scoreSubmission.equals("") || zipcodeSubmission.equals("")) {
                    Toast.makeText(SearchRestaurants.this, "Score and/or Zipcode must be filled in.", Toast.LENGTH_LONG).show();
                    return;
                }
                String cuisineSubmission = cuisine.getSelectedItem().toString();
                System.out.println(cuisineSubmission);
                String restaurantNameSubmission = name.getText().toString();
                System.out.println(restaurantNameSubmission);
                System.out.println(scoreSubmission);
                System.out.println(lessThan);
                System.out.println(zipcodeSubmission);

                Intent intent = new Intent(SearchRestaurants.this, searchResults.class);
                intent.putExtra("name", restaurantNameSubmission);
                intent.putExtra("score", scoreSubmission);
                intent.putExtra("lessThan", lessThan);
                intent.putExtra("zipcode", zipcodeSubmission);
                intent.putExtra("cuisine", cuisineSubmission);
                startActivity(intent);
            }
        });
    }

    public void insertCuisine(String[] arr) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisine.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_restaurants, menu);
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

    private class AsyncDataClass1 extends AsyncTask<String, Void, String> {
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
                nameValuePairs.add(new BasicNameValuePair("getCuisine", "1"));
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
                Toast.makeText(SearchRestaurants.this, "Server Connection Failer", Toast.LENGTH_LONG).show();
                return;
            } else {
                JSONArray json = null;
                try {
                    json = new JSONArray(result);
                    cuisines = new String[json.length() + 1];
                    cuisines[0] = "Any Cuisine";
                    for (int i = 1; i <= json.length(); i++) {
                        JSONObject object = json.getJSONObject(i-1);
                        if (object.getString("cuisine") != null) {
                            cuisines[i] = object.getString("cuisine");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                insertCuisine(cuisines);
            }
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
