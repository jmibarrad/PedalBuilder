package com.example.light.listtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Light on 13-Dec-15.
 */
public class SwipeAdapter extends PagerAdapter{
    ParseFile Img;
    Bitmap bitmap;
    List<ParseObject> objectsList;
    ImageView preview;
    TextView preview_name;
    View item_view;

    private Context ctx;
    final ParseUser currentUser = ParseUser.getCurrentUser();
    public SwipeAdapter(Context ctx, List<ParseObject> fromClass){
        this.ctx = ctx;
        objectsList = fromClass;
    }

    @Override
    public int getCount() {
        return objectsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        item_view = li.inflate(R.layout.swipe_layout, container, false);
        preview = (ImageView)item_view.findViewById(R.id.preview);
        preview_name = (TextView)item_view.findViewById(R.id.preview_name);

        final ParseFile file = objectsList.get(position).getParseFile("Preview");
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, com.parse.ParseException e) {
                if (e == null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    preview.setImageBitmap(bitmap);

                }


            }
        });

        preview_name.setText(objectsList.get(position).getString("Name")); //

        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView((LinearLayout) object);
    }

    public void query(final int position, String name, String category){
        ParseQuery<ParseObject> presetBoardQuery = ParseQuery.getQuery("Presets");
        /*if(name != null)
            presetBoardQuery.whereContains("Name", name);
        if(category.equals("All"))
            presetBoardQuery.whereEqualTo("Category", category);*/


        presetBoardQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    Img = objects.get(position).getParseFile("Preview");
                    Img.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, com.parse.ParseException e) {
                            if (e == null) {
                                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            }
                        }
                    });
                    initParseObjectList(objects);
                }

            }
        });
    }

    void initParseObjectList(List<ParseObject> objects_query){
        objectsList = objects_query;
    }
}
