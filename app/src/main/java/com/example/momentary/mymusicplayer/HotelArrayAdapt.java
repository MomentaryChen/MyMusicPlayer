package com.example.momentary.mymusicplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HotelArrayAdapt extends ArrayAdapter {
    Context context;
    public HotelArrayAdapt(@NonNull Context context, ArrayList<MainActivity.Hotel> items) {
        super(context,0,items);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout itemlayout = null;
        if(convertView==null){
            itemlayout = (LinearLayout) inflater.inflate(R.layout.item_hotel,null);

        }else{
            itemlayout = (LinearLayout)convertView;
        }
        MainActivity.Hotel item = (MainActivity.Hotel) getItem(position);
        TextView tvShelter = (TextView)itemlayout.findViewById(R.id.shelter);
        tvShelter.setText(item.getName());
        TextView tvKind = (TextView) itemlayout.findViewById(R.id.kind);
        tvKind.setText(item.getContext());
        ImageView ivPet = (ImageView) itemlayout.findViewById(R.id.petImg);
        if(item.getImg()!=null) ivPet.setImageBitmap(item.getImg());

        return itemlayout;
    }

}
