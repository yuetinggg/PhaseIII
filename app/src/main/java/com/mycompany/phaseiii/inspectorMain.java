package com.mycompany.phaseiii;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class inspectorMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inspector_main, menu);
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

    public void goToInsertInspection(View view) {
        Intent intent = new Intent(this, insertInspection.class);
        startActivity(intent);
    }

    public void goToMonthlyReports(View view) {
        Intent intent = new Intent(this, MonthlyReport.class);
        startActivity(intent);
    }

    public void goToYearlyReports(View view) {
        Intent intent = new Intent(this, YearlyReport.class);
        startActivity(intent);
    }

    public void goToBestRestaurants(View view) {
        Intent intent = new Intent(this, BestRestaurant.class);
        startActivity(intent);
    }

    public void goToWorstRestaurants(View view) {
        Intent intent = new Intent(this, WorstRestaurant.class);
        startActivity(intent);
    }



}
