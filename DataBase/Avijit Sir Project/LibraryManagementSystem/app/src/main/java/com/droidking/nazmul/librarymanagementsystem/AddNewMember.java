package com.droidking.nazmul.librarymanagementsystem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.sql.SQLException;

public class AddNewMember extends AppCompatActivity {
    DataBaseHelper info = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText first_name = (EditText)findViewById(R.id.add_first_name_et);
                EditText last_name = (EditText) findViewById(R.id.add_last_name_et);
                EditText mem_id =(EditText) findViewById(R.id.add_member_id_et);
                EditText address =(EditText) findViewById(R.id.add_address_et);
                EditText mem_type =(EditText) findViewById(R.id.add_member_type);
                EditText mem_since =(EditText) findViewById(R.id.add_member_since);
                EditText mem_expire =(EditText) findViewById(R.id.add_member_expiredate);


                String First_Name = first_name.getText().toString().trim();
                String Last_Name = last_name.getText().toString().trim();
                String Mem_ID = mem_id.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String Mem_Type = mem_type.getText().toString().trim();
                String Mem_Since = mem_since.getText().toString().trim();
                String Mem_Expire = mem_expire.getText().toString().trim();


                if (TextUtils.isEmpty(First_Name) && TextUtils.isEmpty(Last_Name)&& TextUtils.isEmpty(Mem_ID)&& TextUtils.isEmpty(Address)&& TextUtils.isEmpty(Mem_Type)&& TextUtils.isEmpty(Mem_Since)&& TextUtils.isEmpty(Mem_Expire)) {

                    Snackbar.make(view, "Enter All Information", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                    return;
                }
                else{

                   try {
                       info.open();
                       info.add_Member(Mem_ID,First_Name,Last_Name,Address,Mem_Type,Mem_Since,Mem_Expire);
                       info.close();
                   }catch (SQLException e){
                       e.printStackTrace();
                   }

                    Snackbar.make(view, "Member has been added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }

               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
