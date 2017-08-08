package com.example.shahaz.algorithmicpuzzles;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_temp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void deleteAll(View v){
        Database db=new Database(this);
        //db.getPuzzles(null,true);
    }

    public void openTest(View v){
        Intent intent=new Intent(this,TestActivity.class);
        startActivity(intent);
    }

    public void openMain(View v){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
