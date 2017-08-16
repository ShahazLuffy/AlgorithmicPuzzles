package com.example.shahaz.algorithmicpuzzles;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class list_puzzle extends AppCompatActivity{

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<Puzzle> puzzleList;
    private static String url_all_products = "http://shahaz.000webhostapp.com/sik/get_brief_products.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_ID = "id";
    //private static final String TAG_HINT = "hint";
    //private static final String TAG_INTRO = "intro";
    //private static final String TAG_NOTATION = "notation";
    //private static final String TAGE_HARDNESS = "hardness";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TRIVIA = "trivia";
    private static final String TAG_ICON = "icon";
    //private static final String TAG_ADDRESS= "imageAddress";
    JSONArray products = null;
    private ListInit listInit;
    private  ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_puzzle);

        // Hashmap for ListView
        puzzleList = new ArrayList<Puzzle>();
        list= (ListView) findViewById(R.id.listAll);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick(position);
            }
        });
        // Loading products in Background Thread
        new LoadAllProducts(this).execute();
        //list= (ListView) findViewById(R.id.listAll);
        //list.setAdapter(listInit);



    }

    public void onClick(int position) {
        Integer id = puzzleList.get(position).getId();
        Intent intent = new Intent(this, LoadDetails.class);
        Database db=new Database(this);
//        if(db.getFullPuzzle(puzzleList.get(position).getId()).getId()==-1) {
//            db.addPuzzle(puzzleList.get(position));
//        }
        intent.putExtra("id", id);
       //intent.putExtra("address",puzzleList.get(position).getImageAddress());
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
                        //String hint = c.getString(TAG_HINT);
                        //String intro = c.getString(TAG_INTRO);
                        //String notation = c.getString(TAG_NOTATION);
                        //String hardness = c.getString(TAGE_HARDNESS);
                        String title = c.getString(TAG_TITLE);
                        String trivia = c.getString(TAG_TRIVIA);
                        String icon = c.getString(TAG_ICON);
                        //String addr = c.getString(TAG_ADDRESS);

                        // creating new HashMap
                        Puzzle p=new Puzzle();
                        // adding each child node to HashMap key => value
                        p.setId(Integer.parseInt(id));
                        //p.setHint(hint);
                        //p.setIntro(intro);
                        //p.setNotation(notation);
                        //p.setHardness(Integer.parseInt(hardness));
                        p.setTitle(title);
                        p.setTrivia(trivia);
                        p.setIcon(icon);
                        //p.setImageAddress(addr);
                        // adding HashList to ArrayList
                        puzzleList.add(p);
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

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView iv;

        public  DownloadImageTask(ImageView iv){
            this.iv=iv;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
        }
    }
}