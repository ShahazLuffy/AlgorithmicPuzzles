package com.example.shahaz.algorithmicpuzzles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class list_puzzle extends AppCompatActivity{

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<HashMap<String, String>> puzzleList;
    private static String url_all_products = "http://shahaz.000webhostapp.com/sik/get_all_products.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_ID = "id";
    private static final String TAG_HINT = "hint";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_NOTATION = "notation";
    private static final String TAGE_HARDNESS = "hardness";
    private static final String TAG_RATE1 = "rate1";
    private static final String TAG_RATE2 = "rate2";
    private static final String TAG_RATE3 = "rate3";
    private static final String TAG_RATE4 = "rate4";
    private static final String TAG_RATE5 = "rate5";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TRIVIA = "trivia";
    private static final String TAG_ICON = "icon";
    private static final String TAG_ADDRESS= "imageAddress";
    JSONArray products = null;
    private ListInit listInit;
    private  ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_puzzle);

        // Hashmap for ListView
        puzzleList = new ArrayList<HashMap<String, String>>();
        list= (ListView) findViewById(R.id.listAll);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(view,position);
            }
        });
        // Loading products in Background Thread
        new LoadAllProducts(this).execute();
        //list= (ListView) findViewById(R.id.listAll);
        //list.setAdapter(listInit);



    }

    public void onClick(View v,int position) {
        Integer id = v.getId();
        Intent intent = new Intent(this, LoadDetails.class);
        intent.putExtra("id", id);
        intent.putExtra("address",puzzleList.get(position).get(TAG_ADDRESS));
        startActivity(intent);
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
        private AppCompatActivity parent;

        public LoadAllProducts(AppCompatActivity p){
            parent=p;
        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(list_puzzle.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_ID);
                        String hint = c.getString(TAG_HINT);
                        String intro = c.getString(TAG_INTRO);
                        String notation = c.getString(TAG_NOTATION);
                        String hardness = c.getString(TAGE_HARDNESS);
                        String rate1 = c.getString(TAG_RATE1);
                        String rate2 = c.getString(TAG_RATE2);
                        String rate3 = c.getString(TAG_RATE3);
                        String rate4 = c.getString(TAG_RATE4);
                        String rate5 = c.getString(TAG_RATE5);
                        String title = c.getString(TAG_TITLE);
                        String trivia = c.getString(TAG_TRIVIA);
                        String icon = c.getString(TAG_ICON);
                        String addr = c.getString(TAG_ADDRESS);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, id);
                        map.put(TAG_HINT, hint);
                        map.put(TAG_INTRO, intro);
                        map.put(TAG_NOTATION,notation);
                        map.put(TAGE_HARDNESS, hardness);
                        map.put(TAG_RATE1, rate1);
                        map.put(TAG_RATE2, rate2);
                        map.put(TAG_RATE3, rate3);
                        map.put(TAG_RATE4, rate4);
                        map.put(TAG_RATE5, rate5);
                        map.put(TAG_TITLE, title);
                        map.put(TAG_TRIVIA,trivia);
                        map.put(TAG_ICON,icon);
                        map.put(TAG_ADDRESS,addr);
                        // adding HashList to ArrayList
                        puzzleList.add(map);
                    }
                    listInit=new ListInit(parent,puzzleList);

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    list.setAdapter(listInit);
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                }
            });

        }

    }
}