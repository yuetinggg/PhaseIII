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


public class Login extends Activity {
    private final String serverURL = "http://yuetinglee.host56.com/4400/index.php";
    userLocalStore localUser;
    protected EditText userId;
    protected EditText password;
    protected String enteredUserId;
    protected String enteredPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        android.app.ActionBar actionBar = getActionBar();
        actionBar.hide();

        localUser = new userLocalStore(this);

        userId = (EditText) findViewById(R.id.usernameSubmission);
        password = (EditText) findViewById(R.id.passwordSubmission);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredUserId = userId.getText().toString();
                enteredPassword = password.getText().toString();

                if (enteredUserId.equals("") || enteredPassword.equals("")) {
                    Toast.makeText(Login.this, "Username or password must be filled", Toast.LENGTH_LONG).show();
                    return;
                }

                AsyncDataClass request = new AsyncDataClass();
                request.execute(serverURL, enteredUserId, enteredPassword);
            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("password", params[2]));
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
                Toast.makeText(Login.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }

            int jsonResult = returnedParsedJsonObject(result);
            if (jsonResult == 0) {
                try {
                    JSONObject json  = new JSONObject(result);
                    String message = json.getString("errorMessage");
                    Toast.makeText(Login.this, "errorMessage", Toast.LENGTH_LONG).show();
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonResult == 1) {
                try {
                    JSONObject json  = new JSONObject(result);
                    int isInspector = json.getInt("isInspector");
                    localUser.storeUserData(new User((isInspector == 1), enteredUserId));
                    if (isInspector == 1) {
                        Intent intent = new Intent(Login.this, inspectorMain.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Login.this, ownerMain.class);
                        startActivity(intent);
                    }

                }catch (JSONException e ) {
                    e.printStackTrace();
                }
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

    private int returnedParsedJsonObject(String result) {
        JSONObject resultO = null;
        int returnedResult = 0;

        try {
            resultO = new JSONObject(result);
            returnedResult = resultO.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnedResult;
    }

    public void loginGuest(View view) {
        Intent intent = new Intent(Login.this, guestMain.class);
        startActivity(intent);
    }
}
