package com.example.shahaz.algorithmicpuzzles;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
    private static final String TAG_TRIVIA = "trivia";
    private static final String TAG_ICON = "icon";

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
            vi.setId(Integer.parseInt(puzzleList.get(position).get(TAG_ID)));
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
        holder.tvTrivia.setText(puzzleList.get(position).get(TAG_TRIVIA));

        //Setting an image
        try {
            URL url=new URL(puzzleList.get(position).get(TAG_ICON));
            new DownloadImageTask(holder.ivIcon).execute(puzzleList.get(position).get(TAG_ICON));
        } catch (Exception e) {
            holder.tvTitle.setText(puzzleList.get(position).get(TAG_TITLE)+"1");
            e.printStackTrace();
        }
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