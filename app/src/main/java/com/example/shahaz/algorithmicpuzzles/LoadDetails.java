package com.example.shahaz.algorithmicpuzzles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

public class LoadDetails extends AppCompatActivity {
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loaddetailsly);
        Intent intent = getIntent();
        Integer d= intent.getIntExtra("id", 0);
        id=d.intValue();
        new DownloadImageTask((ImageView) findViewById(R.id.puzzle_image)).execute(intent.getStringExtra("address"));
    }

    public void moareficlick(View v) {
        Intent intent = new Intent(this, introact.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    public void rahnamiiclick (View view){
        Intent intent = new Intent(this, hintact.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    public void martabeclick (View view){
        Intent intent = new Intent(this, notationact.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
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
