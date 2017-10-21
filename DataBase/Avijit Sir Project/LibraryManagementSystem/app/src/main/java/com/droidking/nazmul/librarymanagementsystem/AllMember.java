package com.droidking.nazmul.librarymanagementsystem;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllMember extends AppCompatActivity {

    private List<RecyclerAllMemberDataHolder> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerAllMemberDataHolderAdapter mAdapter;

    DataBaseHelper info = new DataBaseHelper(this);


    /** member Table Attribute*/
    public static final String MEMBER_ID ="Member_ID";
    public static final String MEMBER_FIRST_NAME="Member_First_name";
    public static final String MEMBER_LAST_NAME="Member_Last_name";
    public static final String MEMBER_ADDRESS ="Member_address";
    public static final String MEMBER_TYPE="Member_type";
    public static final String MEMBER_DATE="Member_date";
    public static final String EXPIRED_DATE="Expired_date";
    public static final String MEMBER_TABLE="member";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_all_member);

        mAdapter = new RecyclerAllMemberDataHolderAdapter(movieList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        /* recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
           @Override
            public void onClick(View view, int position) {
              //  Movie movie = movieList.get(position);
               // Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new MainActivity.ClickListener() {


            @Override
            public void onClick(View view, int position) {
                Snackbar.make(view, "On Click", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }

            @Override
            public void onLongClick(View view, int position) {
                RecyclerAllMemberDataHolder recyclerDataHolder = movieList.get(position);
                try{
                    info.open();
                    info.delete_member(recyclerDataHolder.getGenre());
                    info.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }


                Snackbar.make(view, "Delete Member", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();






            }
        }));

        prepareMovieData();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void prepareMovieData() {

        RecyclerAllMemberDataHolder recyclerDataHolder;
        DataBaseHelper info = new DataBaseHelper(this);

        try {
            info.open();

            Cursor myCursor = info.getRecyclerMember_table();

            int iFirstName = myCursor.getColumnIndex(MEMBER_FIRST_NAME);
            int iLastName  = myCursor.getColumnIndex(MEMBER_LAST_NAME);

            int iMember_ID = myCursor.getColumnIndex(MEMBER_ID);
            int iMember_Exp = myCursor.getColumnIndex(EXPIRED_DATE);

            for (myCursor.moveToFirst(); !myCursor.isAfterLast(); myCursor.moveToNext()) {

                recyclerDataHolder = new RecyclerAllMemberDataHolder(myCursor.getString(iFirstName)+" "+myCursor.getString(iLastName) ,myCursor.getString(iMember_ID), myCursor.getString(iMember_Exp));
                movieList.add(recyclerDataHolder);
            }

            info.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

   ;
}
