package com.example.light.listtest;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Gabriel Paz on 11/29/2015.
 */
public class Board extends ImageView{
    public float BoardHeight;
    public float BoardWidth;
    public boolean resize;

    public Board(Context context, float BoardHeight, float BoardWidth) {
        super(context);
        this.BoardHeight = BoardHeight;
        this.BoardWidth = BoardWidth;
        resize = false;
        SetListener();
    }

    public void SetListener()
    {
        final ImageView b = this;
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resize)
                {
                    ((MainActivity)b.getContext()).RestorePedals();
                    ((Board)b).resize = false;
                }
            }
        });
    }


}
