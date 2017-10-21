package com.droidking.nazmul.librarymanagementsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

public class BorrowBookActivity extends AppCompatActivity {

    String book_id;
    DataBaseHelper info = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView  Title = (TextView) findViewById(R.id.title_tv);
        TextView Authors = (TextView) findViewById(R.id.authors_tv);
        TextView Available =(TextView) findViewById(R.id.available_tv);
        TextView Total = (TextView) findViewById(R.id.totalbook_tv);
        TextView Book_ID =(TextView) findViewById(R.id.book_id_tv);
        TextView publication = (TextView) findViewById(R.id.pub_tv);


        Button b_borrow = (Button) findViewById(R.id.bborrow);

        String title = getIntent().getStringExtra("Title");
        String authors =getIntent().getStringExtra("Authors");
        String available = getIntent().getStringExtra("Available");
        String total = getIntent().getStringExtra("Total");
        book_id = getIntent().getStringExtra("Book_ID");
        String pub = getIntent().getStringExtra("Pub_Name");






        Title.setText(title);
        Authors.setText(authors);
        Available.setText(available);
        Total.setText(total);
        Book_ID.setText(book_id);
        publication.setText(pub);





            b_borrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   Snackbar.make(view, ""+member_id, Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();


                    EditText Member_id = (EditText) findViewById(R.id.member_id_etx);
                    String member_id = Member_id.getText().toString().trim();
                    if (TextUtils.isEmpty(member_id)) {


                        Toast.makeText(getApplicationContext(), "Enter MemberID", Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        try {
                            info.open();
                            info.borrow_book(book_id, member_id);
                            info.close();
                            Toast.makeText(getApplicationContext(), member_id, Toast.LENGTH_SHORT).show();

                        } catch (SQLException e) {

                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }


                }
            });


/***
 *  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
 */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
