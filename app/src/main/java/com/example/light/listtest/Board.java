package com.example.light.listtest;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by Gabriel Paz on 11/29/2015.
 */
public class Board extends ImageView{
    public float BoardHeight;
    public float BoardWidth;

    public Board(Context context, float BoardHeight, float BoardWidth) {
        super(context);
        this.BoardHeight = BoardHeight;
        this.BoardWidth = BoardWidth;
    }
}
