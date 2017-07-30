package com.example.shahaz.algorithmicpuzzles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class obor_az_pol_act extends AppCompatActivity {
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obor_az_pol);
        Intent intent = getIntent();
        Integer d= intent.getIntExtra("id", 0);
        id=d.intValue();
        Log.d("k", d.toString());



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
}
