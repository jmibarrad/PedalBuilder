package com.example.light.listtest;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SearchBoards extends AppCompatActivity {
    ViewPager viewPager;
    SwipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_boards);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        adapter = new SwipeAdapter(this);
        viewPager.setAdapter(adapter);
    }
}
