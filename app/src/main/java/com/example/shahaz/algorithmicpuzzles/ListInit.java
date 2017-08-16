package com.example.shahaz.algorithmicpuzzles;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ListInit extends BaseAdapter {

    LayoutInflater inflater;
    ImageView thumb_image;
    ArrayList<Puzzle> puzzleList;
    boolean[] isLoaded;
    ViewHolder holder;
    Context context;
    public ListInit() {
        // TODO Auto-generated constructor stub
    }

    public ListInit(Activity act, ArrayList<Puzzle> map) {
        this.puzzleList = map;
        isLoaded=new boolean[map.size()];
            for(boolean b:isLoaded)
                b=false;
        this.context=act;
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
            vi.setId(puzzleList.get(position).getId());
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

        holder.tvTitle.setText(puzzleList.get(position).getTitle());
        holder.tvTrivia.setText(puzzleList.get(position).getTrivia());

        //Setting an image
        try {
            new DownloadImageTask(holder.ivIcon,holder.tvTrivia,position).execute(puzzleList.get(position).getIcon(),
                    new Integer(puzzleList.get(position).getId()).toString());
        } catch (Exception e) {
            holder.tvTitle.setText(puzzleList.get(position).getTitle()+"1");
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
        TextView tr;
        String mm="temp";
        int position=-1;
        public  DownloadImageTask(ImageView iv,TextView trivia,int pos){
            this.iv=iv;
            tr=trivia;
            position=pos;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            String []name=urldisplay.split("/");
            Bitmap mIcon11 = null;
            try {
                File f=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+urls[1],name[name.length-1]);
                if(f.exists()&&isLoaded[position]==false){
                    FileInputStream fis=new FileInputStream(f);
                    mIcon11 = BitmapFactory.decodeStream(fis);
                }else {
                    f.getParentFile().mkdirs();
                    isLoaded[position]=true;
                    //f.createNewFile();
                    OkHttpClient client = new OkHttpClient();
                    Request r = new Request.Builder().url(urldisplay).build();
                    Response res = client.newCall(r).execute();
                    BufferedInputStream input = new BufferedInputStream(res.body().byteStream());
                    mIcon11 = BitmapFactory.decodeStream(input);
                    FileOutputStream output = new FileOutputStream(f);
                    mIcon11.compress(Bitmap.CompressFormat.PNG,100,output);
                    /*byte[] data = new byte[1024];
                    long total = 0;
                    int count=0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }
*/
                    output.flush();
                    output.close();
                    input.close();

                }

            } catch (Exception e) {
                e.printStackTrace();
                mm=e.getMessage();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            iv.setImageBitmap(result);
        }
    }


}