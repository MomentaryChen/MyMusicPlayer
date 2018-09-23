package com.example.momentary.mymusicplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.momentary.mymusicplayer.HotelArrayAdapt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HotelArrayAdapt adapter = null;
    Button btn ;
    ListView lvHotels;
    double x ,y ;
    private static final int LIST_PETS = 1;
    List<Hotel> arraylist;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvHotels= (ListView)findViewById(R.id.listview_pet);
        adapter = new HotelArrayAdapt(this, new ArrayList<Hotel>());
        lvHotels.setAdapter(adapter);
        getPetsFromFirebase();

        lvHotels.setOnItemClickListener(listener);

    }

    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            x = arraylist.get(position).x;
            y = arraylist.get(position).y;
            String name = arraylist.get(position).name;
            intent.putExtra("x",x);
            intent.putExtra("y",y);
            intent.putExtra("name",name);
            intent.setClass(MainActivity.this,PlayerActivity.class);
            startActivity(intent);
        }
    };

    private void getPetsFromFirebase() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new FirebaseThread(dataSnapshot,handler).start();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("AdoptPet", databaseError.getMessage());
            }
        });

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_PETS: {
                    List<Hotel> hotels = (List<Hotel>)msg.obj;
                    refreshPetList(hotels);
                    break;
                }
            }
        }
    };

    private void refreshPetList(List<Hotel> hotels) {
        arraylist = hotels;
        adapter.clear();
        adapter.addAll(hotels);

    }
    public static class Hotel {
        private String name;
        private  String context;
        private Bitmap img;
        private double x;
        private double y ;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public Bitmap getImg() {
            return img;
        }

        public void setImg(Bitmap img) {
            this.img = img;
        }
    }
}