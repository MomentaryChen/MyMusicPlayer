package com.example.momentary.mymusicplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SongArrayAdapt adapter = null;
    Button btn ;
    ListView lvHotels;
    double x ,y ;
    private static final int LIST_SONGS = 1;
    List<Song> arraylist;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvHotels= (ListView)findViewById(R.id.listview_pet);
        adapter = new SongArrayAdapt(this, new ArrayList<Song>());
        lvHotels.setAdapter(adapter);
        getPetsFromFirebase();

        lvHotels.setOnItemClickListener(listener);

    }

    private ListView.OnItemClickListener listener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            String name = arraylist.get(position).name;
            String url = arraylist.get(position).url ;
            intent.putExtra("url",url);
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
                case LIST_SONGS: {
                    List<Song> songs = (List<Song>)msg.obj;
                    refreshPetList(songs);
                    break;
                }
            }
        }
    };

    private void refreshPetList(List<Song> songs) {
        arraylist = songs;
        adapter.clear();
        adapter.addAll(songs);

    }
    public static class Song {
        private String name;
        private  String author;
        private Bitmap img;
        private String url;

        public String getAuthor() {
            return author;
        }
        public void setAuthor(String author) {
            this.author = author;
        }
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Bitmap getImg() {
            return img;
        }
        public void setImg(Bitmap img) {
            this.img = img;
        }
    }
}