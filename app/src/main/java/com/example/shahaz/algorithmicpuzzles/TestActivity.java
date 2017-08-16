package com.example.shahaz.algorithmicpuzzles;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private TestRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_fragment);
        RecyclerView testRV= (RecyclerView) findViewById(R.id.vertical_courses_list);
        //testRV.setHasFixedSize(true);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        testRV.setLayoutManager(llm);
        Database db=new Database(this);
        List<List<Puzzle>> arr=new ArrayList<List<Puzzle>>();
      //arr.add(0, db.getPuzzles(null, true));
        arr.add(db.getPuzzlesCondition("category='math'",null,true));
        arr.add(db.getPuzzlesCondition("category='riddle'",null,true));
        arr.add(db.getPuzzlesCondition("category='logic'",null,true));
        adapter=new TestRVAdapter(this,arr);
        testRV.setAdapter(adapter);
        }


    public void openNewPuzzle(View v) {
        Intent intent = new Intent(this, list_puzzle.class);
        startActivity(intent);
    }
}
