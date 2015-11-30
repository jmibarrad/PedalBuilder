package com.example.light.listtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ParseFile Img;
    Button rotateButton;
    Button deleteButton;
    ImageView selectedImg;
    ImageView Board;
    float angle = 0;
    boolean moving = true;
    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseService.MakeConnection(this);

        layout = (FrameLayout)findViewById(R.id.fmlayout);
        rotateButton = (Button)findViewById(R.id.rotateButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        // preparing list data
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, final int childPosition, long id) {

                switch (groupPosition) {
                    case 0:
                        ParseQuery<ParseObject> boardQuery = ParseQuery.getQuery("Board");
                        boardQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                                if (e == null) {
                                    Img = objects.get(childPosition).getParseFile("Image");
                                    final float w = objects.get(childPosition).getInt("Width");
                                    final float h = objects.get(childPosition).getInt("Height");
                                    Img.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, com.parse.ParseException e) {
                                            if (e == null) {
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                AddBoard(bitmap, w, h);
                                            }
                                        }
                                    });

                                }
                            }
                        });
                        break;
                    case 1:
                        if(Board != null){

                            ParseQuery<ParseObject> pedalQuery = ParseQuery.getQuery("Pedals");
                            pedalQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, com.parse.ParseException e) {
                                    if (e == null) {
                                        Img = objects.get(childPosition).getParseFile("Image");
                                        final float w = objects.get(childPosition).getInt("Width");
                                        final float h = objects.get(childPosition).getInt("Height");
                                        Img.getDataInBackground(new GetDataCallback() {
                                            @Override
                                            public void done(byte[] data, com.parse.ParseException e) {
                                                if (e == null) {
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    AddNewPedal(bitmap, w, h);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "ERROR: Choose Board First" ,
                                    Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                if(Board != null)
                    Toast.makeText(getApplicationContext(), listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
                                   Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(selectedImg != null){
                    angle += 90;
                    selectedImg.setRotation(angle);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedImg != null)
                    selectedImg.setImageBitmap(null);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Boards");
        listDataHeader.add("Pedals");
        listDataHeader.add("Power Supplies");

        // Adding child data
        final List<String> boardsList = new ArrayList<>();

        ParseQuery<ParseObject> boardQuery = ParseQuery.getQuery("Board");
        boardQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject obj : objects) {
                        boardsList.add(obj.getString("Name"));
                    }

                } else {
                    //objectRetrievalFailed();
                }
            }
        });

        final List<String> pedalsList = new ArrayList<>();
        ParseQuery<ParseObject> pedalQuery = ParseQuery.getQuery("Pedals");
        pedalQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject obj : objects) {
                        pedalsList.add(obj.getString("Name"));
                    }

                } else {
                    //objectRetrievalFailed();
                }
            }
        });

        List<String> powerSuppliesList = new ArrayList<String>();
        /*
        ParseQuery<ParseObject> powerSuppliesQuery = ParseQuery.getQuery("PowerSupplies");
        powerSuppliesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject obj : objects) {
                        boardsList.add(obj.getString("Name"));
                    }

                } else {
                    //objectRetrievalFailed();
                }
            }
        });*/

        listDataChild.put(listDataHeader.get(0), boardsList);
        listDataChild.put(listDataHeader.get(1), pedalsList);
        listDataChild.put(listDataHeader.get(2), powerSuppliesList);
    }

    public void AddBoard(Bitmap bitmap, float w, float h){
        Board = new Pedal(this);
        ((Pedal)Board).PedalWidth = w;
        ((Pedal)Board).PedalHeight = h;
        ((Pedal)Board).type = "Board";
        Board.setImageBitmap(bitmap);
        layout.addView(Board);
    }

    public int getProportion(int a, int b, int c)
    {
        return (b * c)/a ;
    }

    public void AddNewPedal(Bitmap bitmap, float w, float h)
    {
        final ImageView p = new Pedal(this);
        p.setImageBitmap(bitmap);
        ((Pedal)p).PedalWidth = w;
        ((Pedal)p).PedalHeight = h;
        p.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        p.bringToFront();
        p.setScaleType(ImageView.ScaleType.FIT_CENTER);

        int boardWidth = (int) ((Pedal)Board).PedalWidth;
        int pedalWidth = (int) ((Pedal)p).PedalWidth;

        int boardHeight = (int) ((Pedal)Board).PedalHeight;
        int pedalHeight = (int) ((Pedal)p).PedalHeight;

        int wi = getProportion(boardWidth, Board.getWidth(), pedalWidth);
        int he = getProportion(boardHeight, Board.getHeight(), pedalHeight);

        p.getLayoutParams().width = wi;
        p.getLayoutParams().height = he;

        p.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        moving = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (moving) {
                            p.setX(event.getRawX() - p.getWidth());
                            p.setY(event.getRawY() - p.getHeight());
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        moving = false;
                        selectedImg = p;
                        p.bringToFront();
                        break;
                }
                return true;
            }
        });
        layout.addView(p);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
