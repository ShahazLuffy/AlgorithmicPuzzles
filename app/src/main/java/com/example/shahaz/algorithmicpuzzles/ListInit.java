package com.example.shahaz.algorithmicpuzzles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListInit extends BaseAdapter {

    // XML node keys
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

    LayoutInflater inflater;
    ImageView thumb_image;
    List<HashMap<String,String>> puzzleList;
    ViewHolder holder;
    public ListInit() {
        // TODO Auto-generated constructor stub
    }

    public ListInit(Activity act, List<HashMap<String,String>> map) {

        this.puzzleList = map;

        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return puzzleList.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        if(convertView==null){

            vi = inflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();

            holder.tvTitle = (TextView)vi.findViewById(R.id.tvTitle); // city name
            holder.tvTrivia = (TextView)vi.findViewById(R.id.tvTrivia); // city weather overview
            holder.ivIcon =(ImageView)vi.findViewById(R.id.list_image); // thumb image
            vi.setTag(holder);
        }
        else{
            holder = (ViewHolder)vi.getTag();
        }

        // Setting all values in listview

        holder.tvTitle.setText(puzzleList.get(position).get(TAG_TITLE));
        holder.tvTrivia.setText(puzzleList.get(position).get(TAG_INTRO));
        /*
        //Setting an image
        String uri = "drawable/"+ weatherDataCollection.get(position).get(KEY_ICON);
        int imageResource = vi.getContext().getApplicationContext().getResources().getIdentifier(uri, null, vi.getContext().getApplicationContext().getPackageName());
        Drawable image = vi.getContext().getResources().getDrawable(imageResource);
        holder.tvWeatherImage.setImageDrawable(image);
        */
        return vi;
    }

    /*
     *
     * */
    static class ViewHolder{

        TextView tvTitle;
        TextView tvTrivia;
        ImageView ivIcon;
    }

}