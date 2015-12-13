package com.example.light.listtest;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel Paz on 12/3/2015.
 */
public class StringBuilderService {
    public static String StringBuilder(Board board, List<ImageView> Pedals)
    {
        String buffer = "";
        buffer = buffer + board.Id + "split";

        for(int i = 0; i < Pedals.size(); i++)
        {
            buffer = buffer + Pedals.get(i).toString();
            if((i+1)!=Pedals.size())
                buffer = buffer + "/";
        }
        return buffer;
    }
}
