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


public class comments extends Activity {
    private final String serverURL = "http://yuetinglee.host56.com/4400/insertInspection.php";
    private String iid;
    private String rid;
    private String date;
    private String[] commentsArr;
    private int[] itemsArr;
    private TableLayout table;
    private int numComments = 1;
    private ArrayList<View> rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Intent intent = getIntent();
        iid = intent.getStringExtra("iid");
        rid = intent.getStringExtra("rid");
        date = intent.getStringExtra("date");

        rows = new ArrayList<View>(1);

        table = (TableLayout)findViewById(R.id.tableLayout);
        View tableRow = getLayoutInflater().inflate(R.layout.inspection_comments_table_row, null, false);
        EditText itemNumHeader = (EditText) tableRow.findViewById(R.id.itemnum);
        itemNumHeader.setText("Item Number");
        EditText CommentHeader = (EditText) tableRow.findViewById(R.id.commentDescription);
        CommentHeader.setText("Comments");
        table.addView(tableRow);

        for (int i = 0; i < 1; i ++) {
            tableRow = getLayoutInflater().inflate(R.layout.inspection_comments_table_row, null, false);
            rows.add(tableRow);
            itemNumHeader = (EditText) tableRow.findViewById(R.id.itemnum);
            EditText scoreDescription = (EditText) tableRow.findViewById(R.id.commentDescription);
            table.addView(tableRow);
        }

        TextView iidtv = (TextView) findViewById(R.id.inspectorIDSubmission);
        iidtv.setText(iid + "");
        TextView ridtv = (TextView) findViewById(R.id.restaurantIDSubmission);
        ridtv.setText(rid + "");
        TextView datetv = (TextView) findViewById(R.id.dateSubmission);
        datetv.setText(date);

        Button addCommentButton = (Button) findViewById(R.id.addCommentButton);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tableRow = getLayoutInflater().inflate(R.layout.inspection_comments_table_row, null, false);
                rows.add(tableRow);
                EditText itemNumHeader = (EditText) tableRow.findViewById(R.id.itemnum);
                EditText scoreDescription = (EditText) tableRow.findViewById(R.id.commentDescription);
                table.addView(tableRow);
                rows.add(tableRow);
                numComments++;
            }
        });

        Button submitButton = (Button) findViewById(R.id.submitReportButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsArr = new String[numComments];
                itemsArr = new int[numComments];

                for (int i = 0; i < numComments; i++) {
                    commentsArr[i] = ((EditText) rows.get(i).findViewById(R.id.commentDescription)).getText().toString();
                    itemsArr[i] = Integer.parseInt(((EditText) rows.get(i).findViewById(R.id.itemnum)).getText().toString());
                }
                JSONArray arr1 = new JSONArray();
                JSONObject object = new JSONObject();
                try {
                    for (int i = 0; i < numComments; i++) {
                        object = new JSONObject();
                        object.put("itemnum", itemsArr[i]);
                        object.put("comment", commentsArr[i]);
                        arr1.put(object);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AsyncDataClass request = new AsyncDataClass();
                request.execute(serverURL, rid + "", date, arr1.toString());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comments, menu);
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("rid", params[1]));
                nameValuePairs.add(new BasicNameValuePair("date", params[2]));
                nameValuePairs.add(new BasicNameValuePair("items", params[3]));
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
                Toast.makeText(comments.this, "Server Connection Failed", Toast.LENGTH_LONG).show();
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
                Toast.makeText(comments.this, "Comments has been submitted.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(comments.this, inspectorMain.class);
                startActivity(intent);
            } else {
                Toast.makeText(comments.this, "Comments was not submitted, try again.", Toast.LENGTH_LONG).show();
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
