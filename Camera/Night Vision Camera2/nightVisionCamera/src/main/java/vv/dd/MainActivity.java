package vv.dd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.notifymob.android.Notify;
import com.startapp.android.publish.StartAppAd;

public class MainActivity extends Activity
{
  Context c = this;
  private RelativeLayout mLayout;
  private CameraPreview mPreview;
  private SharedPreferences prefs;
  private Notify notifyInstance;
	private StartAppAd startAppAd = new StartAppAd(this);
	   boolean isadshown = false;

  private static final String PREFS_NAME = "org.davidsingleton.NNRCCar";
  private FeatureStreamer fs = new FeatureStreamer();
  private SensorManager sensorManager;
  private float[] gravity = new float[3];
  private float[] linear_acceleration = new float[3];



  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(R.layout.main);
    /**Edit By Nazmul Haque
     * Here Is On start Ad Show */

    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


    this.mLayout = ((RelativeLayout)findViewById(R.id.rlayout));
    ((ImageView)findViewById(R.id.imageView1)).setScaleType(ImageView.ScaleType.FIT_XY);
    this.prefs = getSharedPreferences(getPackageName(), 0);

    AlertDialog.Builder addressAlert = new AlertDialog.Builder(this);
    addressAlert.setTitle("Connect to...");
    addressAlert.setMessage("IP address:");
    final EditText input = new EditText(this);
    final Context activityContext = (Context) this;
    input.setText(loadIPAddressPref(this));
    addressAlert.setView(input);
    addressAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        String addr = input.getText().toString();
        saveIPAddressPref(activityContext, addr);
        fs.connect(addr, 6666);
      }
    });
    addressAlert.show();
  }

  static String loadIPAddressPref(Context context) {
  SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
  String prefix = prefs.getString("ipaddr", null);
  if (prefix != null) {
    return prefix;
  } else {
    return "";
  }
}

  static void saveIPAddressPref(Context context, String text) {
    SharedPreferences.Editor prefs = context
            .getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.putString("ipaddr", text);
    prefs.commit();
  }
	@Override
	protected void onDestroy() {
		 notifyInstance.stopAllMarkers();
		super.onDestroy();
	}
  protected void onPause()
  {
    super.onPause();
    startAppAd.onPause();
    try
    {
      this.mPreview.stop();
      this.mLayout.removeView(this.mPreview);
      this.mPreview = null;
      return;
    }
    catch (Exception localException)
    {
    }
  }

  protected void onResume()
  {
    super.onResume();
    startAppAd.onResume();
    try
    {
      this.mPreview = new CameraPreview(this, 0, CameraPreview.LayoutMode.FitToParent);
      RelativeLayout.LayoutParams localLayoutParams = new RelativeLayout.LayoutParams(-2, -2);
      this.mLayout.addView(this.mPreview, 0, localLayoutParams);
      return;
    }
    catch (Exception localException)
    {
      Toast.makeText(getApplicationContext(), getString(R.string.your_phone_), Toast.LENGTH_LONG).show();
    }
  }
}