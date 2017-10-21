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

import java.util.ArrayList;
import java.util.List;

public class AllBorrowedActivity extends AppCompatActivity {
    private List<RecyclerAllBorrowDataHolder> movieList = new ArrayList<>();
    private RecyclerView recyclerView_borrowed;
    private RecyclerAllBorrowDataHolderAdapter mAdapter;

    /** borrow_by Table Attribute*/
    public static final String BOOK_ID ="Book_ID";
    public static final String MEMBER_ID ="Member_ID";
    public static final String DUE_DATE = "Due_Date";
    public static final String RETURN_DATE = "Return_Date";
    public static final String ISSUE= "Issue";
    public static final String BORROW_TABLE = "borrow_by";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_borrowed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView_borrowed = (RecyclerView) findViewById(R.id.recycler_view_borrowed);

        mAdapter = new RecyclerAllBorrowDataHolderAdapter(movieList);

        recyclerView_borrowed.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_borrowed.setLayoutManager(mLayoutManager);
        recyclerView_borrowed.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView_borrowed.setItemAnimator(new DefaultItemAnimator());
        recyclerView_borrowed.setAdapter(mAdapter);



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

        RecyclerAllBorrowDataHolder recyclerDataHolder;
        DataBaseHelper info = new DataBaseHelper(this);

        try {
            info.open();

            Cursor myCursor = info.getRecyclerBorrow_By_table();
            int iBook_id = myCursor.getColumnIndex(BOOK_ID);
            int iRow = myCursor.getColumnIndex(DUE_DATE);
            //int iTitle = myCursor.getColumnIndex(T);
            int iMember_id = myCursor.getColumnIndex(MEMBER_ID);
           // int iAvailable = myCursor.getColumnIndex(AVAILABLE);

            for (myCursor.moveToFirst(); !myCursor.isAfterLast(); myCursor.moveToNext()) {


                recyclerDataHolder = new RecyclerAllBorrowDataHolder(myCursor.getString(iMember_id),myCursor.getString(iBook_id),myCursor.getString(iRow),info.getMemberName(myCursor.getString(iMember_id)),info.getBookTitle(myCursor.getString(iBook_id)));// , info.getAllAuthorName(myCursor.getString(iAuthor_Group)));

                movieList.add(recyclerDataHolder);
            }

            info.close();

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener_1 implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener_1(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
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

}
