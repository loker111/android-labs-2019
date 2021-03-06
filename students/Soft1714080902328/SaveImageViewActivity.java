package com.example.soft1714080902328;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.ImageView;

import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SaveImageViewActivity extends AppCompatActivity {
    private ImageView img, showImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_image_view);
        img = (ImageView) findViewById(R.id.img);
        showImg = (ImageView) findViewById(R.id.showImg);
    }

    public void readImg(View v) {
        String path = Environment.getExternalStorageDirectory() + "/1.png";
        showImg.setImageURI(Uri.parse(path));
    }
    public void saveImg(View v) {
        //获取BitmapDrawable对象
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        /*
        通过Bitmap(位图)压缩的方法（compress）保存图片到SD卡
        参数1:图片格式（PNG JPEJ WEBP）
        参数2：图片质量（0-100）
        参数3：输出流
         */
        //取得SD卡根目录
        File root = Environment.getExternalStorageDirectory();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(root + "/1.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //从网络获取图片
    public void getUrlImg(View v) {
        new GetImg().execute("http://image.so.com/i?q=%E6%9F%AF%E5%8D%97&src=srp#/liteflow/list?imgkey=t018058f1a75ffa66fb.jpg&prevsn=-1&currsn=0");
    }

    public  void saveHttpImg(View v){
        new SaveHttpImg().execute(" http://image.so.com/i?q=%E6%9F%AF%E5%8D%97&src=srp#/liteflow/list?imgkey=t018058f1a75ffa66fb.jpg&prevsn=-1&currsn=0");
    }
    public class GetImg extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            HttpURLConnection con = null;
            //拿数据
            InputStream is = null;
            try {
                URL url=new URL(params[0]);
                con= (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5000);
                con.setReadTimeout(5*1000);
                if (con.getResponseCode()==200){
                    is=con.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    return bitmap;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (con!=null){
                    con.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            showImg.setImageBitmap(bitmap);
        }
    }

    public class SaveHttpImg extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection con = null;
            InputStream is = null;
            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5*1000);
                con.setReadTimeout(5*1000);
                File root = Environment.getExternalStorageDirectory();
                FileOutputStream fos = new FileOutputStream(root+"/http.jpg");
                if(con.getResponseCode()==200){
                    is = con.getInputStream();
                    int next=0;
                    byte[] bytes = new byte[1024];
                    while ( (next = is.read(bytes))>0){
                        fos.write(bytes,0,next);
                    }
                    fos.flush();
                    fos.close();
                    return  root+"/http.jpg";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(is!=null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(con!=null){
                    con.disconnect();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(!s.equals("")){
                Toast.makeText(SaveImageViewActivity.this, "保存路径:" + s, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SaveImageViewActivity.this,"保存失败:",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
