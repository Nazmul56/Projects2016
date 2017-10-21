package practice.nazmul.practice;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;

/**
 * Created by nazmul on 8/26/15.
 */
public class GFX extends Activity{
    MyBringBack ourView;
    PowerManager.WakeLock wL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Wake-lock

        PowerManager pM =(PowerManager)getSystemService(Context.POWER_SERVICE);
        wL = pM.newWakeLock(PowerManager.FULL_WAKE_LOCK,"whatever");

        super.onCreate(savedInstanceState);
        wL.acquire();
        ourView = new MyBringBack(this);
        setContentView(ourView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        wL.release();
    }
}
