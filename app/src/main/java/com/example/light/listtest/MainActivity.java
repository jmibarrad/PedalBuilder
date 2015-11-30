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
    ImageView board;
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
                        if(board != null){

                            ParseQuery<ParseObject> pedalQuery = ParseQuery.getQuery("Pedals");
                            pedalQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, com.parse.ParseException e) {
                                    if (e == null) {
                                        Img = objects.get(childPosition).getParseFile("Image");
                                        final String Name= objects.get(childPosition).getString("Name");
                                        final String Type= objects.get(childPosition).getString("Type");
                                        final String Brand= objects.get(childPosition).getString("Brand");
                                        final float w = objects.get(childPosition).getInt("Width");
                                        final float h = objects.get(childPosition).getInt("Height");
                                        Img.getDataInBackground(new GetDataCallback() {
                                            @Override
                                            public void done(byte[] data, com.parse.ParseException e) {
                                                if (e == null) {
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    AddNewPedal(bitmap,Type,Name,Brand,h,w);
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
                if(board != null)
                    Toast.makeText(getApplicationContext(), listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
                                   Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(selectedImg != null){
                    ((Pedal)selectedImg).angle = ((Pedal)selectedImg).angle + 90;
                    selectedImg.setRotation(((Pedal)selectedImg).angle);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImg != null)
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
                }
            }
        });

        List<String> powerSuppliesList = new ArrayList<String>();

        listDataChild.put(listDataHeader.get(0), boardsList);
        listDataChild.put(listDataHeader.get(1), pedalsList);
        listDataChild.put(listDataHeader.get(2), powerSuppliesList);
    }

    public void AddBoard(Bitmap bitmap, float w, float h){
        if(board != null)
            board.setImageBitmap(null);
        board = new Board(this,h,w);
        board.setImageBitmap(bitmap);
        layout.addView(board);
        deleteButton.bringToFront();
        rotateButton.bringToFront();
    }

    public void AddNewPedal(Bitmap bitmap, String Type, String Name, String Brand, float PedalHeight, float PedalWidth)
    {
            ImageView p = new Pedal(this, bitmap, Type, Name, Brand, PedalHeight, PedalWidth);
            ((Pedal)p).Resize((int) ((Board) board).BoardWidth, board.getWidth(), (int) ((Board) board).BoardHeight, board.getHeight());
            p.setX((layout.getWidth() / 2));
            p.setY((layout.getHeight() / 2) - 70);
            layout.addView(p);
            deleteButton.bringToFront();
            rotateButton.bringToFront();
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
