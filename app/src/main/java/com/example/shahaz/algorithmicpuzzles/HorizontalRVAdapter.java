package com.example.shahaz.algorithmicpuzzles;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.id;

public class HorizontalRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Puzzle> mDataList;
    private int mRowIndex = -1;
    private Context mContext;
    public HorizontalRVAdapter(Context context) {
        this.mContext=context;
    }

    public void setData(List<Puzzle> data) {
        if (mDataList != data) {
            mDataList = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ImageView iv;
        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.horizontal_item_text);
            iv= (ImageView) itemView.findViewById(R.id.horizontal_item_image);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.test_horizontal_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder) rawHolder;
        holder.text.setText(mDataList.get(position).getTitle());//mDataList.get(position).getTitle());
        holder.itemView.setTag(position);
        new DownloadImageTask(holder.iv,mContext).execute(mDataList.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView iv;
        Context c;
        String fullPath="";
        public  DownloadImageTask(ImageView iv, Context context){
            this.iv=iv;
            c=context;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            String []name=urldisplay.split("/");
            Bitmap mIcon11 = null;
            try {
                File f=new File(c.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/"+id,name[name.length-1]);
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
            Database db=new Database(c);
            Puzzle puzzle=db.getFullPuzzle(id);
            puzzle.setImageAddress(fullPath);
            db.updatePuzzle(puzzle);
        }
    }

}