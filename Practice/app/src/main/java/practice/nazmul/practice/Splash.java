package practice.nazmul.practice;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by nazmul on 8/24/15.
 */
public class Splash extends Activity{

    MediaPlayer ourSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ourSong =MediaPlayer.create(Splash.this,R.raw.sleep);


        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean music = getPrefs.getBoolean("checkbox",true);

        if(music==true)
        ourSong.start();

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent openStartingPoint = new Intent("practice.nazmul.practice.MENUE");

                    startActivity(openStartingPoint);

                }
            }
        };

        timer.start();

        //
    }

    // Here we add Pause mathod to release memory.
    @Override
    protected void onPause() {
        super.onPause();
        ourSong.release();//this will stop music
        finish();//this mathod will release memory of splashActivity .

    }
}



