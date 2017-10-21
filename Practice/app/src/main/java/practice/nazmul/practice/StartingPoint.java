package practice.nazmul.practice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class StartingPoint extends ActionBarActivity {
    int counter;
    Button add, sub;
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_point);



        counter = 0;
        add = (Button)findViewById(R.id.bAdd);
        sub =(Button)findViewById(R.id.bSub);
        display =(TextView)findViewById(R.id.tvdisplay);

        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
             counter++;
                display.setText("Your Total is : " + counter);

            }


        });
        sub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                counter--;
                display.setText("Your Total is : " + counter);
            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cool_menu, menu);//Add menu  In startingPoint
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {

            case R.id.aboutUs:
                Intent i = new Intent("practice.nazmul.practice.ABOUT");
                startActivity(i);
                break;
            case R.id.preferences:
                Intent p = new Intent("practice.nazmul.practice.PREFS");
                startActivity(p);

                break;
            case R.id.exit:
                finish();
                break;

        }
        return false;
    }
}
