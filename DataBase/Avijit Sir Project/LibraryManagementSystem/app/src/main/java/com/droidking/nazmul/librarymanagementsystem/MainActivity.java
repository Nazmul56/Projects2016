package com.droidking.nazmul.librarymanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<RecyclerCatalogDataHolder> recyclerDataHolderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerCatalogDataHolderAdapter mAdapter;


    public static final String KEY_ROWID = "Pub_ID";
    public static final String KEY_NAME = "Pub_name";
    public static final String KEY_HOTNESS = "Pub_Address";

    /**
     * publisher Table Attribute
     */
    public static final String PUBLISHER_ID = "Pub_ID";
    public static final String PUBLISHER_NAME = "Pub_name";
    public static final String PUB_ADDRESS = "Pub_Address";
    private static final String PUBLISHER_TABLE = "publisher";


    /**
     * books Table Attribute
     */
    public static final String BOOK_ID = "Book_ID";
    public static final String TITLE = "Title";
    public static final String AUTHOR_GROUP = "Author_Group_ID";
    public static final String PUB_ID = "Pub_ID";
    public static final String TOTAL_BOOK = "Total_Book";
    public static final String AVAILABLE = "Available";
    public static final String PRICE = "Price";
    private static final String BOOK_TABLE = "books";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        File dbfile = new File("data/data/" + getPackageName() + "/databases", "LibraryDataBase");

        if (!dbfile.exists()) {
            File f = new File("data/data/" + getPackageName() + "/databases");
            f.mkdirs();
            try {

                copyDatabase(dbfile);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "Sample data is being copied", Toast.LENGTH_LONG).show();
        } else{
          //  Toast.makeText(getApplicationContext(), "Database Exist", Toast.LENGTH_LONG).show();
          }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //    .setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this, SQLiteExample.class);
                startActivity(intent);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new RecyclerCatalogDataHolderAdapter(recyclerDataHolderList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                RecyclerCatalogDataHolder recyclerDataHolder = recyclerDataHolderList.get(position);
              //  Toast.makeText(getApplicationContext(), recyclerDataHolder.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                Intent borrow_intent = new  Intent(MainActivity.this,BorrowBookActivity.class);

                borrow_intent.putExtra("Title", recyclerDataHolder.getTitle());
                borrow_intent.putExtra("Authors",recyclerDataHolder.getAuthors());
                borrow_intent.putExtra("Available",recyclerDataHolder.getAvailable());
                borrow_intent.putExtra("Total",recyclerDataHolder.getTotalBook());
                borrow_intent.putExtra("Book_ID",recyclerDataHolder.getBook_id());
                borrow_intent.putExtra("Pub_Name",recyclerDataHolder.getPub_name());

                startActivity(borrow_intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        prepareMovieData();

        /**Navigation Drawer*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void copyDatabase(File dbFile) throws IOException {
        InputStream is = this.getAssets().open("LibraryDataBase");
        OutputStream os = new FileOutputStream(dbFile);

        byte[] buffer = new byte[1024];

        while (is.read(buffer) > 0) {
            os.write(buffer);
        }

        os.flush();
        os.close();
        is.close();
    }


    private void prepareMovieData() {

        RecyclerCatalogDataHolder recyclerDataHolder;
        DataBaseHelper info = new DataBaseHelper(this);

        try {
            info.open();

            Cursor myCursor = info.getRecyclerData();
            int iBook_id = myCursor.getColumnIndex(BOOK_ID);
            int iRow = myCursor.getColumnIndex(TOTAL_BOOK);
            int iName = myCursor.getColumnIndex(TITLE);
            int iAuthor_Group = myCursor.getColumnIndex(AUTHOR_GROUP);
            int iAvailable = myCursor.getColumnIndex(AVAILABLE);

            for (myCursor.moveToFirst(); !myCursor.isAfterLast(); myCursor.moveToNext()) {

                recyclerDataHolder = new RecyclerCatalogDataHolder(myCursor.getString(iBook_id) ,myCursor.getString(iName), info.getAllAuthorName(myCursor.getString(iAuthor_Group)), myCursor.getString(iRow),myCursor.getString(iAvailable),info.getPublicationName(myCursor.getString(iBook_id)));
                recyclerDataHolderList.add(recyclerDataHolder);
            }

            info.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        RecyclerCatalogDataHolder recyclerDataHolder;
        DataBaseHelper info = new DataBaseHelper(this);

        recyclerDataHolderList.clear();
        try {
            info.open();
            //String name = info.getName(1);
            Cursor myCursor = info.getRecyclerData();
            int iBook_id = myCursor.getColumnIndex(BOOK_ID);
            int iRow = myCursor.getColumnIndex(TOTAL_BOOK);
            int iName = myCursor.getColumnIndex(TITLE);
            int iAuthor_Group = myCursor.getColumnIndex(AUTHOR_GROUP);
            int iAvailable = myCursor.getColumnIndex(AVAILABLE);
            for (myCursor.moveToFirst(); !myCursor.isAfterLast(); myCursor.moveToNext()) {

                recyclerDataHolder = new RecyclerCatalogDataHolder(myCursor.getString(iBook_id),myCursor.getString(iName),info.getAllAuthorName(myCursor.getString(iAuthor_Group)), myCursor.getString(iRow),myCursor.getString(iAvailable),info.getPublicationName(myCursor.getString(iBook_id)));
                recyclerDataHolderList.add(recyclerDataHolder);
            }

            info.close();

            mAdapter.notifyDataSetChanged();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.droidking.nazmul.librarymanagementsystem/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.droidking.nazmul.librarymanagementsystem/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.catalog) {
           // Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.borrow_book) {
            Intent intent = new Intent(this , DirectBorrowActivity.class);
            startActivity(intent);
        } else if (id == R.id.return_book) {
            Intent intent = new Intent(this, ReturnBook.class);
            startActivity(intent);

        }else if (id == R.id.all_borrowed){

            Intent intent = new Intent(this, AllBorrowedActivity.class);
            startActivity(intent);

        }else if(id == R.id.allmember){
            Intent intent = new Intent(this,AllMember.class);
            startActivity(intent);

        }
        else if (id == R.id.add_newmember) {
            Intent intent = new Intent(this,AddNewMember.class);
            startActivity(intent);

        } else if (id == R.id.logout) {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);


        } else if (id == R.id.about_us) {
            Intent intent = new Intent(this, About_Us.class);
            startActivity(intent);

        }else if(id== R.id.add_new_book)
        {
            Intent intent = new Intent(this, AddNewBookActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
