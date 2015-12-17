package com.example.light.listtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchBoards extends AppCompatActivity {
    ViewPager viewPager;
    SwipeAdapter adapter;
    List<Bitmap>bitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_boards);
        final Context ctx = this;
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        ParseQuery<ParseObject> presetBoardQuery = ParseQuery.getQuery("Presets");
        presetBoardQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    adapter = new SwipeAdapter(ctx, objects);
                    viewPager.setAdapter(adapter);
                }

            }
        });


    }
}
