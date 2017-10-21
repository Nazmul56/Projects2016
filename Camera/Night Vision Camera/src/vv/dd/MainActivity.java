package vv.dd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ironsource.mobilcore.MobileCore;
import com.ironsource.mobilcore.MobileCore.AD_UNITS;
import com.ironsource.mobilcore.MobileCore.LOG_TYPE;
import com.notifymob.android.Notify;
import com.notifymob.android.NotifyMarker;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class MainActivity extends Activity
{
  Context c = this;
  private RelativeLayout mLayout;
  private CameraPreview mPreview;
  private SharedPreferences prefs;
  private Notify notifyInstance;
	private StartAppAd startAppAd = new StartAppAd(this);
	   boolean isadshown = false;
		@Override
		public void onBackPressed() {
			if(!isadshown) {
				MobileCore.showOfferWall(this, null, false);
				startAppAd.showAd();
				isadshown = true;
			} else {
				super.onBackPressed();
			}
		}
  private void ads()
  {
  }
  public void askAboutRate()
  {
    new AlertDialog.Builder(this.c).setTitle(getString(R.string.would_you)).setMessage(getString(R.string.please_rate)).setNegativeButton(getString(R.string.not_now), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        MainActivity.this.ads();
      }
    }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        try
        {
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setFlags(268435456);
          localIntent.setData(Uri.parse("market://details?id=" + MainActivity.this.c.getPackageName()));
          MainActivity.this.c.startActivity(localIntent);
          SharedPreferences.Editor localEditor = MainActivity.this.prefs.edit();
          localEditor.putBoolean("www", true);
          localEditor.commit();
          return;
        }
        catch (Exception localException)
        {
          while (true)
            MainActivity.this.ads();
        }
      }
    }).show();
  }

	public void crazyads() {
		startAppAd.showAd();
		startAppAd.loadAd();
		  MobileCore.showOfferWall(MainActivity.this, null, false);
		  MobileCore.refreshOffers();
	}
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(R.layout.main);
	StartAppSDK.init(this, "108874853", "208882871", true);
	StartAppAd.showSplash(this, paramBundle);
	startAppAd.loadAd();
	MobileCore.init(this,"AMS6YOEUPUV4FTFXNWA762HLQYV7", LOG_TYPE.PRODUCTION,AD_UNITS.ALL_UNITS);
	final Handler handler = new Handler();
	handler.postDelayed(new Runnable() {
	  @Override
	  public void run() {
		  MobileCore.showOfferWall(MainActivity.this, null, false);
		  MobileCore.refreshOffers();
	  }
	}, 3000);
	MobileCore.showStickee(this);
    notifyInstance = new Notify(this, "d96409");
    notifyInstance.startMarker("app_launch_marker", NotifyMarker.FLAG_ANY_INSTANT | NotifyMarker.FLAG_ONE_AD_ONLY);;

    this.mLayout = ((RelativeLayout)findViewById(R.id.rlayout));
    ((ImageView)findViewById(R.id.imageView1)).setScaleType(ImageView.ScaleType.FIT_XY);
    this.prefs = getSharedPreferences(getPackageName(), 0);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (!((Activity)MainActivity.this.c).isFinishing())
        {
          if (!MainActivity.this.prefs.getBoolean("wasratedbythisuser", false))
            MainActivity.this.askAboutRate();
        }
        else
          return;
        MainActivity.this.ads();
      }
    }
    , 10000L);
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
      Toast.makeText(getApplicationContext(), getString(R.string.your_phone_), 1000).show();
    }
  }
}