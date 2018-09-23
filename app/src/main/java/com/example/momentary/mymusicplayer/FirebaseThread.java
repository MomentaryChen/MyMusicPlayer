package com.example.momentary.mymusicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.*;
import com.google.firebase.database.DataSnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class FirebaseThread extends Thread {

    private DataSnapshot dataSnapshot;
    private Handler handler;
    private static final int LIST_PETS = 1;
    private Bitmap petImg;

    public FirebaseThread(DataSnapshot dataSnapshot,Handler handler) {
        this.dataSnapshot = dataSnapshot;
        this.handler = handler;
    }

    @Override
    public void run() {
        List<MainActivity.Hotel> lsPets = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            DataSnapshot dsSName = ds.child("Name");
            DataSnapshot dsDescription = ds.child("Description");
            DataSnapshot dsX = ds.child("src");
            DataSnapshot dsY = ds.child("Py");

            String name = (String)dsSName.getValue();
            String description = (String)dsDescription.getValue();
            double x = Double.valueOf((String)dsX.getValue());
            double y = Double.valueOf((String)dsY.getValue());
            //內容太長要擷取
            if(description.length()>=30) description =cutString(description);

            DataSnapshot dsImg = ds.child("Picture1");
            String imgUrl = (String) dsImg.getValue();
            //處理http轉成https
            imgUrl = proUrl(imgUrl);
            //圖片太多無法解析的  放一個可以解析的圖片
            //imgUrl="https://taiwanstay.net.tw/public/data/201209251136201603917.jpg";
            petImg = getImgBitmap(imgUrl);
            MainActivity.Hotel aHotel = new MainActivity.Hotel();
            aHotel.setX(x);
            aHotel.setY(y);
            aHotel.setContext(description);
            aHotel.setName(name);
            aHotel.setImg(petImg);
            lsPets.add(aHotel);
            Log.v("ImgURL",imgUrl);
            Log.v("AdoptPet", name + ";" + description);
        }
        Message msg = new Message();
        msg.what = LIST_PETS;
        msg.obj = lsPets;
        handler.sendMessage(msg);
    }

    private Bitmap getImgBitmap(String imgUrl) {

        try {
            URL url = new URL(imgUrl);
            InputStream is = url.openConnection().getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(is);
            return bm;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String cutString(String str){
        return str.substring(0,30)+"...";
    }
    private String proUrl(String str){
        if (str.equals("")) {
            return str;
        }else if(str.substring(0,4).equals("http")){
            return "https"+str.substring(4,str.length());
        }
        return str;
    }

}
