package com.example.shahaz.algorithmicpuzzles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadDetails extends AppCompatActivity {
    private int id;
    Database db;
    Puzzle puzzle;
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();

    private static String url_all_products = "http://shahaz.000webhostapp.com/sik/get_product_details.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_ID = "id";
    private static final String TAG_HINT = "hint";
    private static final String TAG_INTRO = "intro";
    private static final String TAG_NOTATION = "notation";
    private static final String TAGE_HARDNESS = "hardness";
    private static final String TAG_TITLE = "title";
    private static final String TAG_TRIVIA = "trivia";
    private static final String TAG_ICON = "icon";
    private static final String TAG_ADDRESS= "imageAddress";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loaddetailsly);
        Intent intent = getIntent();
        Integer d= intent.getIntExtra("id", -1);
        id=d.intValue();
        db=new Database(this);
        puzzle=db.getFullPuzzle(id);
        if(puzzle.getId()!=-1){
            new DownloadImageTask((ImageView) findViewById(R.id.puzzle_image),LoadDetails.this).execute(puzzle.getImageAddress());
        }else{
            new LoadPuzzle().execute(new Integer(id).toString());
        }
        //((Button) findViewById(R.id.btnGameTest)).setText(intent.getStringExtra("address"));
        //new DownloadImageTask((ImageView) findViewById(R.id.puzzle_image),this).execute(intent.getStringExtra("address"));
    }

    public void moareficlick(View v) {
        Intent intent = new Intent(this, introact.class);
        intent.putExtra("intro", puzzle.getIntro());
        startActivity(intent);
    }
    public void rahnamiiclick (View view){
        Intent intent = new Intent(this, hintact.class);
        intent.putExtra("hint", puzzle.getHint());
        startActivity(intent);
    }
    public void martabeclick (View view){
        Intent intent = new Intent(this, notationact.class);
        intent.putExtra("notation", puzzle.getNotation());
        startActivity(intent);
    }

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView iv;
        AppCompatActivity parent;
        String fullPath="";
        public  DownloadImageTask(ImageView iv, AppCompatActivity p){
            this.iv=iv;
            parent=p;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            String []name=urldisplay.split("/");
            Bitmap mIcon11 = null;
            try {
                File f=new File(parent.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+id,name[name.length-1]);
                fullPath=f.getAbsolutePath();
                if(f.exists()){
                    FileInputStream fis=new FileInputStream(f);
                    mIcon11 = BitmapFactory.decodeStream(fis);
                }else {
                    f.getParentFile().mkdirs();
                    OkHttpClient client = new OkHttpClient();
                    Request r = new Request.Builder().url(urldisplay).build();
                    Response res = client.newCall(r).execute();
                    BufferedInputStream input = new BufferedInputStream(res.body().byteStream());
                    mIcon11 = BitmapFactory.decodeStream(input);
                    FileOutputStream output = new FileOutputStream(f);
                    mIcon11.compress(Bitmap.CompressFormat.JPEG,100,output);
                    output.flush();
                    output.close();
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                fullPath=new Integer(name.length).toString();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
            Database db=new Database(parent);
            Puzzle puzzle=db.getFullPuzzle(id);
            puzzle.setImageAddress(fullPath);
            db.updatePuzzle(puzzle);
        }
    }

    class LoadPuzzle extends AsyncTask<String, String, String> {
        private AppCompatActivity parent;

        public LoadPuzzle(){

        }

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoadDetails.this);
            pDialog.setMessage("Loading puzzle. Please wait...");
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
            params.add(new BasicNameValuePair("id", new Integer(id).toString()));
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("Puzzle: ", json.toString());

            try {


                // Checking for SUCCESS TAG
                int success = json.getInt("success");

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    JSONArray jpuzzle = json.getJSONArray("product");



                    JSONObject c = jpuzzle.getJSONObject(0);

                    // Storing each json item in variable
                    String id = c.getString(TAG_ID);
                    String hint = c.getString(TAG_HINT);
                    String intro = c.getString(TAG_INTRO);
                    String notation = c.getString(TAG_NOTATION);
                    String hardness = c.getString(TAGE_HARDNESS);
                    String title = c.getString(TAG_TITLE);
                    String trivia = c.getString(TAG_TRIVIA);
                    String icon = c.getString(TAG_ICON);
                    String addr = c.getString(TAG_ADDRESS);

                    // creating new HashMap

                    // adding each child node to HashMap key => value
                    puzzle.setId(Integer.parseInt(id));
                    puzzle.setHint(hint);
                    puzzle.setIntro(intro);
                    puzzle.setNotation(notation);
                    puzzle.setHardness(Integer.parseInt(hardness));
                    puzzle.setTitle(title);
                    puzzle.setTrivia(trivia);
                    puzzle.setIcon(icon);
                    puzzle.setImageAddress(addr);
                    db.addPuzzle(puzzle);
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
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    new DownloadImageTask((ImageView) findViewById(R.id.puzzle_image),LoadDetails.this).execute(puzzle.getImageAddress());
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                }
            });

        }

    }

}
