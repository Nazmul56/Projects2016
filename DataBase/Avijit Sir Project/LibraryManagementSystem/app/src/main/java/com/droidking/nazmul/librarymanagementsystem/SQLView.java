package com.droidking.nazmul.librarymanagementsystem;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlview);

        TextView tv =(TextView) findViewById(R.id.tvSQLinfo);

        DataBaseHelper info = new DataBaseHelper(this);

        try {
            info.open();

            String data = info.getMember_table();

            //String data = info.getMember_table();
          info.close();
            tv.setText(data);
        } catch (SQLException e) {   //SQLException e
            e.printStackTrace();

            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Dang it!");
            TextView t = new TextView(this);
            t.setText(error);
            d.setContentView(t);
            d.show();
        }



    }

    public String getCurrentDate() throws Exception
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date currentDate = new Date();
        String date = sdf.format(currentDate);
        return date;
    }

    public String getDayDifference(String pastDay)
    {
        String Days ;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
            Date pastDate = sdf.parse("10-Apr-2016");
            Date currentDate = new Date();
            String date = sdf.format(currentDate);

            // tv.setText( "" + String.format("%02d",Days); //
            if (!pastDate.after(currentDate)) {
                long diff = currentDate.getTime()
                        - pastDate.getTime();

                long days = diff / (24 * 60 * 60 * 1000);

                Days =  String.format("%02d", days);
                return Days;
            }
        }catch(ParseException e)
        {
            e.printStackTrace();

            String error = e.toString();
            Dialog d = new Dialog(this);
            d.setTitle("Dang it!");
            TextView t = new TextView(this);
            t.setText(error);
            d.setContentView(t);
            d.show();
        }
        return null;

    }


}
