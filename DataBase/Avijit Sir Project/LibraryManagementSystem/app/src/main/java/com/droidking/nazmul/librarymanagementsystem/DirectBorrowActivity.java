package com.droidking.nazmul.librarymanagementsystem;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

public class DirectBorrowActivity extends AppCompatActivity {

    DataBaseHelper info = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_borrow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button borrow = (Button) findViewById(R.id.d_borrow_b);



        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText book_id = (EditText) findViewById(R.id.d_borrow_id_etx);
                EditText member_id = (EditText) findViewById(R.id.d_borrow_member_id_etx);

                String Book_ID = book_id.getText().toString().trim();
                String Member_ID = member_id.getText().toString().trim();


                if (TextUtils.isEmpty(Member_ID) && TextUtils.isEmpty(Book_ID)) {

                    Snackbar.make(view, "Enter Member and Book ID", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                } else {
                    try {
                        info.open();
                        info.borrow_book(Book_ID, Member_ID);
                        info.close();
                        // Toast.makeText(getApplicationContext(), member_id, Toast.LENGTH_SHORT).show();

                    } catch (SQLException e) {

                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            }

            );





       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

            getSupportActionBar()

            .

            setDisplayHomeAsUpEnabled(true);
        }

    }
