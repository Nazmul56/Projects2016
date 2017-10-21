package com.droidking.nazmul.librarymanagementsystem;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.SQLException;

public class ReturnBook extends AppCompatActivity {
    DataBaseHelper info = new DataBaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_book = (Button)findViewById(R.id.re_b_return);
        Button submit_button =(Button)findViewById(R.id.re_b_submit);




        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText member_id = (EditText) findViewById(R.id.re_mem_id_et);
                EditText book_id = (EditText) findViewById(R.id.re_book_id_et);

                TextView Mem_name = (TextView) findViewById(R.id.re_member_name_tv);
                TextView Book_title = (TextView) findViewById(R.id.re_book_name_tv);

                TextView Book_days =(TextView) findViewById(R.id.re_days_tv);
                TextView Book_Status = (TextView) findViewById(R.id.re_book_status);
                TextView Fine = (TextView) findViewById(R.id.re_fine);

                String Mem_id = member_id.getText().toString().trim();
                String Book_id = book_id.getText().toString().trim();
                String book_status = "Not Found";
                String days;
                long fineInTK = 0;
                if (TextUtils.isEmpty(Mem_id) && TextUtils.isEmpty(Book_id)) {

                    Snackbar.make(view, "Enter Member and Book ID", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                } else {
                    try {
                        info.open();
                        Mem_name.setText(info.getMemberName(Mem_id));
                        Book_title.setText(info.getBookTitle(Book_id));
                        book_status = info.BookStatus(Book_id, Mem_id);
                        Book_Status.setText(book_status);

                        if (book_status.equals("Borrowed")) {
                            days = info.FineCalculate(Book_id, Mem_id);
                            Book_days.setText(days);
                            Long ldays=Long.parseLong(days);
                            if(ldays > 15)
                            {
                                fineInTK = (ldays-15)*10;
                                Fine.setText(""+fineInTK+"TK");
                            }

                        }
                        Snackbar.make(view, "Book Has been returned", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        info.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
            }
            }

            );


            return_book.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   EditText member_id = (EditText) findViewById(R.id.re_mem_id_et);
                                                   EditText book_id = (EditText) findViewById(R.id.re_book_id_et);

                                                   String book_status = "Not Found";
                                                   String mem = member_id.getText().toString().trim();
                                                   String book = book_id.getText().toString().trim();

                                                   if (TextUtils.isEmpty(mem) && TextUtils.isEmpty(book)) {

                                                       Snackbar.make(view, "Enter Member and Book ID", Snackbar.LENGTH_LONG)
                                                               .setAction("Action", null).show();
                                                       return;
                                                   } else {
                                                       try {
                                                           info.open();
                                                           book_status = info.BookStatus(book,mem);

                                                           if (book_status.equals("Borrowed")) {
                                                               info.return_book(book, mem);
                                                               Snackbar.make(view, "Book has been returned", Snackbar.LENGTH_LONG)
                                                                       .setAction("Action", null).show();
                                                           }
                                                           else{
                                                               Snackbar.make(view, "Book not returned", Snackbar.LENGTH_LONG)
                                                                       .setAction("Action", null).show();
                                                           }
                                                           info.close();



                                                       } catch (SQLException e) {

                                                           e.printStackTrace();
                                                           Snackbar.make(view, "Error", Snackbar.LENGTH_LONG)
                                                                   .setAction("Action", null).show();
                                                       }


                                                   }
                                               }
                                           }

            );


            getSupportActionBar()

            .

            setDisplayHomeAsUpEnabled(true);
        }

    }
