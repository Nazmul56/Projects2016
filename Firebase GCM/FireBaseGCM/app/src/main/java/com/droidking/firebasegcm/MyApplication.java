package com.droidking.firebasegcm;

/**
 * Created by nazmul on 6/1/16.
 */

import android.app.Application;
import com.firebase.client.Firebase;
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //Initializing firebase
        Firebase.setAndroidContext(getApplicationContext());
    }
}
