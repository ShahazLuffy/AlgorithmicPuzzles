package com.example.shahaz.algorithmicpuzzles;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class introact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introactlay);
        Intent intent = getIntent();
        String intro= intent.getStringExtra("intro");
        TextView introTV=((TextView) findViewById(R.id.intoractlaytv));
        introTV.setText(intro);
        Typeface face=Typeface.createFromAsset(getAssets(),"font/byekan.ttf");
        introTV.setTypeface(face);
        introTV.setTextSize(22);
        introTV.setMovementMethod(new ScrollingMovementMethod());

    }
}
