package com.example.shahaz.algorithmicpuzzles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.shahaz.algorithmicpuzzles.JSONParser;
import com.example.shahaz.algorithmicpuzzles.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class hintact extends AppCompatActivity {
    private int idd;
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    String get_intro = "https://shahaz.000webhostapp.com/sik/get_product_details.php";
    JSONArray puzzles = null;
    String id , hint, intro, notation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hitnactlay);


        new LoadPuzzle().execute();
        Intent intent = getIntent();
        Integer d= intent.getIntExtra("id", 0);
        idd=d.intValue();

    }

    class LoadPuzzle extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(hintact.this);
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


            params.add(new BasicNameValuePair("id", new Integer(idd).toString()));
            JSONObject json = jParser.makeHttpRequest(get_intro, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {


                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray puzzle = json.getJSONArray("product");

                    // looping through All Products

                    JSONObject c = puzzle.getJSONObject(0);

                    // Storing each json item in variable
                    id = c.getString("id");
                    hint = c.getString("hint");
                    intro = c.getString("intro");
                    notation = c.getString("notation");



                } else {
                   /* // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(),
                            NewProductActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/

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
            // updating UI from Backgroasaaaund Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    TextView textView = (TextView) findViewById(R.id.hintactlaytv);
                    textView.setText(hint);

                }
            });

        }

    }
}
