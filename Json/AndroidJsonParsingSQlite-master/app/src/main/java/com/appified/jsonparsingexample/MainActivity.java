package com.appified.jsonparsingexample;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    ListView listView;
    CityAdapter adapter;
    ArrayList<City> cityArrayList;
    DBHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        handler = new DBHandler(this);
        NetworkUtils utils = new NetworkUtils(MainActivity.this);
        if(handler.getCityCount() == 0 && utils.isConnectingToInternet())
        {
            new DataFetcherTask().execute();
        }
        else
        {
            ArrayList<City> cityList = handler.getAllCity();
            adapter = new CityAdapter(MainActivity.this,cityList);
            listView.setAdapter(adapter);
        }
    }

    class DataFetcherTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String serverData = null;// String object to store fetched data from server
            // Http Request Code start
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://beta.json-generator.com/api/json/get/GAqnlDN");
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                serverData = EntityUtils.toString(httpEntity);
                Log.d("response", serverData);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Http Request Code end
            // Json Parsing Code Start
            try {
                cityArrayList = new ArrayList<City>();
                JSONObject jsonObject = new JSONObject(serverData);
                JSONArray jsonArray = jsonObject.getJSONArray("cities");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObjectCity = jsonArray.getJSONObject(i);
                    String cityName = jsonObjectCity.getString("name");
                    String cityState = jsonObjectCity.getString("state");
                    String cityDescription = jsonObjectCity.getString("description");
                    City city = new City();
                    city.setName(cityName);
                    city.setState(cityState);
                    city.setDescription(cityDescription);
                    handler.addCity(city);// Inserting into DB
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Json Parsing code end
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayList<City> cityList = handler.getAllCity();
            adapter = new CityAdapter(MainActivity.this,cityList);
            listView.setAdapter(adapter);
        }
    }
}
