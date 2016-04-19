package com.mycompany.phaseiii;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class AddRestaurant extends Activity {
    private final String serverURL = "http://yuetinglee.host56.com/4400/restaurant.php";
    protected EditText pID;
    protected EditText pExpiration;
    protected EditText cuisine;
    protected EditText name;
    protected EditText address;
    protected EditText county;
    protected EditText phone;
    protected String idSubmission;
    protected String expirationSubmission;
    protected String cuisineSubmission;
    protected String nameSubmission;
    protected String add;
    protected String street;
    protected String city;
    protected String state;
    protected String zipcode;
    protected String countySubmission;
    protected String phoneSubmission;
    protected String username;
    private userLocalStore local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        android.app.ActionBar actionBar = getActionBar();
        actionBar.hide();

        local = new userLocalStore(this);

        pID = (EditText) findViewById(R.id.permitID);
        pExpiration = (EditText) findViewById(R.id.permitExpiration);
        cuisine = (EditText) findViewById(R.id.cuisine);
        name = (EditText) findViewById(R.id.restaurantName);
        address = (EditText) findViewById(R.id.address);
        county = (EditText) findViewById(R.id.county);
        phone = (EditText) findViewById(R.id.phone);
        username = local.getLoggedInUser().getId();

        Button addRestaurantButton = (Button) findViewById(R.id.addRestaurantButton);
        addRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idSubmission = pID.getText().toString();
                expirationSubmission = pExpiration.getText().toString();
                cuisineSubmission = cuisine.getText().toString();
                nameSubmission = name.getText().toString();
                add = address.getText().toString();
                String[] arr = add.split(",");
                street = arr[0].trim();
                city = arr[1].trim();
                String[] arr2 = arr[2].split(" ");
                state = arr2[0];
                zipcode = arr2[1];
                System.out.println(street + city + state + zipcode);
                countySubmission = county.getText().toString();
                phoneSubmission = phone.getText().toString();

                AsyncDataClass request = new AsyncDataClass();
                request.execute(serverURL, idSubmission, expirationSubmission, cuisineSubmission, nameSubmission, street, city, state, zipcode, countySubmission, phoneSubmission, username);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_restaurant, menu);
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

    private class AsyncDataClass extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";

            try{
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(11);
                nameValuePairs.add(new BasicNameValuePair("permitID", params[1]));
                nameValuePairs.add(new BasicNameValuePair("permitExpiration", params[2]));
                nameValuePairs.add(new BasicNameValuePair("cuisine", params[3]));
                nameValuePairs.add(new BasicNameValuePair("rname", params[4]));
                nameValuePairs.add(new BasicNameValuePair("street", params[5]));
                nameValuePairs.add(new BasicNameValuePair("city", params[6]));
                nameValuePairs.add(new BasicNameValuePair("state", params[7]));
                nameValuePairs.add(new BasicNameValuePair("zipcode", params[8]));
                nameValuePairs.add(new BasicNameValuePair("county", params[9]));
                nameValuePairs.add(new BasicNameValuePair("phone", params[10]));
                nameValuePairs.add(new BasicNameValuePair("username", params[11]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = client.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e){
                e.printStackTrace();
            } catch(IOException e) {
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
            if (result.equals("") || result == null) {
                Toast.makeText(AddRestaurant.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }

            int jsonResult = 0;

            try {
                JSONObject json = new JSONObject(result);
                jsonResult = json.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jsonResult == 0) {
                Toast.makeText(AddRestaurant.this, "Unable to add restaurant/edit information.", Toast.LENGTH_LONG).show();
            }

            if (jsonResult == 1) {
                Toast.makeText(AddRestaurant.this, "Restaurant information has been updated.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddRestaurant.this, ownerMain.class);
                startActivity(intent);
            }

            if (jsonResult == 2) {
                Toast.makeText(AddRestaurant.this, "New restaurant has been added.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddRestaurant.this, ownerMain.class);
                startActivity(intent);
            }

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
