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


public class Complain extends Activity {
    private final String serverURL = "http://yuetinglee.host56.com/4400/complain.php";
    protected EditText restaurant;
    protected EditText street;
    protected EditText cityState;
    protected EditText zipcode;
    protected EditText date;
    protected EditText first;
    protected EditText last;
    protected EditText phone;
    protected EditText description;
    protected String restaurantSubmission;
    protected String streetSubmission;
    protected String cityStateSubmission;
    protected String zipcodeSubmission;
    protected String dateSubmission;
    protected String firstSubmission;
    protected String lastSubmission;
    protected String phoneSubmission;
    protected String descriptionSubmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        android.app.ActionBar actionBar = getActionBar();
        actionBar.hide();

        restaurant = (EditText) findViewById(R.id.restaurantSubmission);
        street = (EditText) findViewById(R.id.streetSubmission);
        cityState = (EditText) findViewById(R.id.cityStateSubmission);
        zipcode = (EditText) findViewById(R.id.zipcodeSubmission);
        date = (EditText) findViewById(R.id.dateSubmission);
        first = (EditText) findViewById(R.id.customerFirstSubmission);
        last = (EditText) findViewById(R.id.customerLastSubmission);
        phone = (EditText) findViewById(R.id.customerPhoneSubmission);
        description = (EditText) findViewById(R.id.complainSubmission);

        Button complainButton = (Button) findViewById(R.id.complainButton);
        complainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantSubmission = restaurant.getText().toString();
                streetSubmission = street.getText().toString();
                cityStateSubmission = cityState.getText().toString();
                zipcodeSubmission = zipcode.getText().toString();
                dateSubmission = date.getText().toString();
                firstSubmission = first.getText().toString();
                lastSubmission = last.getText().toString();
                phoneSubmission = phone.getText().toString();
                descriptionSubmission = description.getText().toString();

                if(restaurantSubmission.equals("")) {
                    Toast.makeText(Complain.this, "Restaurant name must be filled.", Toast.LENGTH_LONG).show();
                    return;
                }

                AsyncDataClass request = new AsyncDataClass();
                request.execute(serverURL, restaurantSubmission, dateSubmission, firstSubmission, lastSubmission, phoneSubmission, descriptionSubmission);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_complain, menu);
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("restaurant", params[1]));
                nameValuePairs.add(new BasicNameValuePair("date", params[2]));
                nameValuePairs.add(new BasicNameValuePair("firstName", params[3]));
                nameValuePairs.add(new BasicNameValuePair("lastName", params[4]));
                nameValuePairs.add(new BasicNameValuePair("phone", params[5]));
                nameValuePairs.add(new BasicNameValuePair("description", params[6]));
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
                Toast.makeText(Complain.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
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
                Toast.makeText(Complain.this, "Unable to file complain.", Toast.LENGTH_LONG).show();
            }

            if (jsonResult == 1) {
                Toast.makeText(Complain.this, "Complain filed.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Complain.this, guestMain.class);
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
