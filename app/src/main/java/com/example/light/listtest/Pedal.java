package com.example.light.listtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Gabriel Paz on 11/22/2015.
 */
public class Pedal extends ImageView {


    public float PedalHeight;
    public float PedalWidth;
    public String Type;
    public String Name;
    public String Brand;
    public float angle;

    public Pedal(Context context, Bitmap bitmap, String Type, String Name, String Brand, float PedalHeight, float PedalWidth)
    {
        super(context);
        this.Type = Type;
        this.Name = Name;
        this.Brand = Brand;
        this.PedalHeight = PedalHeight;
        this.PedalWidth = PedalWidth;
        this.setImageBitmap(bitmap);
        this.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        this.setScaleType(ImageView.ScaleType.FIT_CENTER);
        angle = 0;
        setListener();

    }

    public void Resize(int RealBoardWidth, int ImgBoardWidth, int RealBoardHeight, int ImgBoardHeigth)
    {
        int wi = ProportionService.getProportionSize(RealBoardWidth, ImgBoardWidth, (int)this.PedalWidth);
        int he = ProportionService.getProportionSize(RealBoardHeight, ImgBoardHeigth, (int)this.PedalHeight);

        this.getLayoutParams().width = wi;
        this.getLayoutParams().height = he;
    }

    private void setListener()
    {
        final ImageView p = this;
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((MainActivity) getContext()).moving = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (((MainActivity) p.getContext()).moving) {
                            p.setX(event.getRawX() - p.getWidth());
                            p.setY(event.getRawY() - p.getHeight());

                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        ((MainActivity) p.getContext()).moving = false;
                        ((MainActivity) p.getContext()).selectedImg = p;
                        break;
                }
                return true;
            }
        });
    }
}
