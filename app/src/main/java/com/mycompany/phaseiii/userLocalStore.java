package com.mycompany.phaseiii;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yuetinggg on 7/19/15.
 */
public class userLocalStore {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public userLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor editor = userLocalDatabase.edit();
        editor.putBoolean("isInspector", user.isInspector());
        editor.putString("id", user.getId());

        editor.commit();
    }

    public User getLoggedInUser() {
        boolean isInspector = userLocalDatabase.getBoolean("isInspector", false);
        String id = userLocalDatabase.getString("id","");

        User storedUser = new User(isInspector, id);
        return storedUser;

    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor editor = userLocalDatabase.edit();
        editor.putBoolean("loggedIn", loggedIn);
        editor.commit();
    }

    public boolean userLoggedIn() {
        boolean bool = userLocalDatabase.getBoolean("loggenIn", false);
        return bool;
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = userLocalDatabase.edit();
        editor.clear();
        editor.commit();
    }
}
