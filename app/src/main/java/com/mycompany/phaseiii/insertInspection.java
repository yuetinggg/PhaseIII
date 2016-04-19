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
import java.util.Arrays;
import java.util.List;


public class insertInspection extends Activity {
    private final String serverURL = "http://yuetinglee.host56.com/4400/insertInspection.php";
    private String inspectorID;
    private String restaurantID;
    private String dateSubmission;
    private String[] items;
    private String[] criticality;
    private ArrayList<EditText> scoreViews;
    private int[] scores;
    private EditText iid;
    private EditText rid;
    private EditText date;
    private String passfail;
    private int totalscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_inspection);

        scores = new int[15];
        scoreViews = new ArrayList<>(15);
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

        criticality = new String[15];
        criticality[0] = "Yes";
        criticality[1] = "Yes";
        criticality[2] = "Yes";
        criticality[3] = "Yes";
        criticality[4] = "Yes";
        criticality[5] = "Yes";
        criticality[6] = "Yes";
        criticality[7] = "Yes";
        criticality[8] = "Yes";
        criticality[9] = "No";
        criticality[10] = "No";
        criticality[11] = "No";
        criticality[12] = "No";
        criticality[13] = "No";
        criticality[14] = "No";

        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        View tableRow = getLayoutInflater().inflate(R.layout.inspector_submit_report_row, null, false);
        TextView itemNumHeader = (TextView) tableRow.findViewById(R.id.itemnum);
        itemNumHeader.setText("Item Number");
        TextView itemDescriptionHeader = (TextView) tableRow.findViewById(R.id.itemDescription);
        itemDescriptionHeader.setText("Item Description");
        TextView CriticalHeader = (TextView) tableRow.findViewById(R.id.criticality);
        CriticalHeader.setText("Critical");
        TextView ScoreHeader = (TextView) tableRow.findViewById(R.id.score);
        ScoreHeader.setText("Score");
        table.addView(tableRow);

        for (int i = 0; i < 15; i++) {
            tableRow = getLayoutInflater().inflate(R.layout.inspector_submit_report_row, null, false);
            TextView itemNum = (TextView) tableRow.findViewById(R.id.itemnum);
            itemNum.setText((i+1)+"");
            TextView itemDescription = (TextView) tableRow.findViewById(R.id.itemDescription);
            itemDescription.setText(items[i]);
            TextView critical = (TextView) tableRow.findViewById(R.id.criticality);
            critical.setText(criticality[i]);
            scoreViews.add((EditText) tableRow.findViewById(R.id.score));
            table.addView(tableRow);
        }
        iid = (EditText) findViewById(R.id.inspectorIDSubmission);
        rid = (EditText) findViewById(R.id.restaurantIDSubmission);
        date = (EditText) findViewById(R.id.dateSubmission);

        Button submitButton = (Button) findViewById(R.id.submitReportButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inspectorID = iid.getText().toString();
                restaurantID = rid.getText().toString();
                dateSubmission = date.getText().toString();

                for (int i = 0; i < 15; i++) {
                    scores[i] = Integer.parseInt(scoreViews.get(i).getText().toString());
                    totalscore = totalscore + scores[i];
                }
                System.out.println(totalscore);
                System.out.println(scores);
                passfail = "PASS";
                for (int i = 0; i < 9; i++) {
                    if (scores[i] < 8) {
                        passfail = "FAIL";
                    }
                }
                if (passfail.equals("PASS")) {
                    if (totalscore < 75) {
                        passfail = "FAIL";
                    }
                }
                JSONArray obj = new JSONArray();
                JSONObject obj1 = new JSONObject();
                try {
                    for (int i = 1; i < 16; i++) {
                        obj1 = new JSONObject();
                        obj1.put("1", scores[i - 1]);
                        obj.put(obj1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsyncDataClass request = new AsyncDataClass();
                request.execute(serverURL, inspectorID, restaurantID, dateSubmission, totalscore + "", passfail, obj.toString());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insert_inspection, menu);
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

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("iid", params[1]));
                nameValuePairs.add(new BasicNameValuePair("rid", params[2]));
                nameValuePairs.add(new BasicNameValuePair("date", params[3]));
                nameValuePairs.add(new BasicNameValuePair("totalscore", params[4]));
                nameValuePairs.add(new BasicNameValuePair("passfail", params[5]));
                nameValuePairs.add(new BasicNameValuePair("scores", params[6]));
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
                Toast.makeText(insertInspection.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
                return;
            }
            int jresult = 0;
            try {
                JSONObject jsonResult = new JSONObject(result);
                jresult = jsonResult.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jresult == 1) {
                Toast.makeText(insertInspection.this, "Report has been filed, continue to next page please.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(insertInspection.this, comments.class);
                intent.putExtra("iid", inspectorID);
                intent.putExtra("rid", restaurantID);
                intent.putExtra("date", dateSubmission);
                startActivity(intent);
            } else {
                Toast.makeText(insertInspection.this, "Report was not filed, try again.", Toast.LENGTH_LONG).show();
                return;
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
