package com.example.shahaz.algorithmicpuzzles;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Puzzle> puzzleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       drawer.setDrawerListener(toggle);
        toggle.syncState();

       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Database db=new Database(this);
        //int r=db.deletePuzzle(0);
        //Puzzle puzzle=new Puzzle();
        //puzzle.setId(3);
        //puzzle.setTitle("San");
        //db.addPuzzle(puzzle);
        //Puzzle p=db.getFullPuzzle(1);
        //if(p!=null)
        //    ((TextView)findViewById(R.id.txtMain1)).setText(p.getTitle());
        //p.setTitle("this");
        //db.updatePuzzle(p);
        puzzleList=db.getPuzzles(null,true);
            for(int i=0;i<puzzleList.size();i++){
                Button b=new Button(this);
                Puzzle p=puzzleList.get(i);
                b.setText(p.getTitle());
                b.setId(p.getId());
                ConstraintLayout c=(ConstraintLayout)findViewById(R.id.contentMain);
                b.setX(10);
                b.setId(i);
                b.setY(i*100+50);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        puzzleShow(v.getId());
                    }
                });
                c.addView(b);
            }
        // str="";
        //if(arr!=null)
        //    for(int i=0;i<arr.size();i++)
        //        str=str+"-"+arr.get(i).getTitle();
        //((TextView)findViewById(R.id.txtMain1)).setText(new Integer(r).toString());
}

    public void puzzleShow(int position) {
        Integer id = puzzleList.get(position).getId();
        Intent intent = new Intent(this, LoadDetails.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void temp (View v){
        Intent intent = new Intent(this, AllProductsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.puzzle_list) {
            Intent intent = new Intent(this, list_puzzle.class);
            startActivity(intent);
        } else if (id == R.id.top_rated_puzzle) {

        } else if (id == R.id.new_puzzles) {


        } else if (id == R.id.recommended_puzzle) {

        }else if (id == R.id.about_app) {

        } else if (id == R.id.about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
