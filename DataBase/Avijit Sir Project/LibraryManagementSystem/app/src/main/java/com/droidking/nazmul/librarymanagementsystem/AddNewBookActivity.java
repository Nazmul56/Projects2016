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

public class AddNewBookActivity extends AppCompatActivity {

    DataBaseHelper info = new DataBaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {


                                   @Override
                                   public void onClick(View view) {

                                    EditText book_id = (EditText) findViewById(R.id.addbook_id_et);
                                       EditText title = (EditText) findViewById(R.id.addbook_title_et);
                                       EditText author_group_id = (EditText) findViewById(R.id.addbook_group_id_et);
                                       EditText pub_id = (EditText) findViewById(R.id.addbook_pub_id_et);
                                       EditText total = (EditText) findViewById(R.id.addbook_total_et);

                                       EditText price = (EditText) findViewById(R.id.addbook_price_et);

                                       /*assert book_id != null;
                                       assert title != null;
                                       assert author_group_id != null;
                                       assert pub_id != null;
                                       assert total != null;
                                       assert available != null;
                                       assert price != null;*/

                                       String Book_ID = book_id.getText().toString().trim();
                                       String Title = title.getText().toString().trim();
                                       String Author_group_ID = author_group_id.getText().toString().trim();
                                       String Pub_ID = pub_id.getText().toString().trim();
                                       String Total = total.getText().toString().trim();

                                       String Price = price.getText().toString().trim();


                                     if (TextUtils.isEmpty(Book_ID) || TextUtils.isEmpty(Title)  || TextUtils.isEmpty(Author_group_ID) || TextUtils.isEmpty(Pub_ID) || TextUtils.isEmpty(Total)   || TextUtils.isEmpty(Price)) {

                                           Snackbar.make(view, "Enter All Information", Snackbar.LENGTH_LONG)
                                                   .setAction("Action", null).show();
                                           return;
                                       } else {

                                           try {
                                               info.open();
                                               info.add_Book(Book_ID, Title, Author_group_ID, Pub_ID, Total, Price);
                                               info.close();
                                               Snackbar.make(view, "Book has been added", Snackbar.LENGTH_LONG)
                                                       .setAction("Action", null).show();
                                           } catch (SQLException e) {
                                               e.printStackTrace();
                                           }




                                       }

                                   }
                               }

        );

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }
