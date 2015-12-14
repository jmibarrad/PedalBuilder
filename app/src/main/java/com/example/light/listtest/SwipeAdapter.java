package com.example.light.listtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
    int total_found;

    private Context ctx;
    final ParseUser currentUser = ParseUser.getCurrentUser();
    public SwipeAdapter(Context ctx){
        this.ctx = ctx;


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        item_view = li.inflate(R.layout.swipe_layout, container, false);
        preview = (ImageView)item_view.findViewById(R.id.preview);
        preview_name = (TextView)item_view.findViewById(R.id.preview_name);
        query(position, "", "");


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
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null){
                    Img = objects.get(position).getParseFile("Preview");
                    Img.getDataInBackground(new GetDataCallback(){
                        @Override
                        public void done(byte[] data, com.parse.ParseException e) {
                            if (e == null) {
                                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            }
                        }
                    });
                    total_found =objects.size();
                    preview.setImageBitmap(bitmap);
                    preview_name.setText(objects.get(position).getString("Name"));
                }

            }
        });
    }
}
