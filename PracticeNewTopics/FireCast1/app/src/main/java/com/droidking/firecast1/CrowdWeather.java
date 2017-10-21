package com.droidking.firecast1;

import com.firebase.client.Firebase;

/**
 * Created by nazmul on 6/20/16.
 */
public class CrowdWeather extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
