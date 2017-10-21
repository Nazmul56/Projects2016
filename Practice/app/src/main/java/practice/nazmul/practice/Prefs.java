package practice.nazmul.practice;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by nazmul on 8/25/15.
 */
public class Prefs extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs); //Add prefs.xml file



    }
}
