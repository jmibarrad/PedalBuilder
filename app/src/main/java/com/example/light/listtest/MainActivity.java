package com.example.light.listtest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<ImageView> backup;
    ParseFile Img;
    Button rotateButton;
    Button deleteButton;
    Button saveButton;
    ImageView selectedImg;
    ImageView board;
    boolean moving = true;
    FrameLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null) {


        } else {

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }


        layout = (FrameLayout)findViewById(R.id.fmlayout);
        rotateButton = (Button)findViewById(R.id.rotateButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);
        saveButton = (Button)findViewById(R.id.savebutton);

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

        backup = new ArrayList<>();

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
                                    final String Code = objects.get(childPosition).getObjectId();
                                    final float w = objects.get(childPosition).getInt("Width");
                                    final float h = objects.get(childPosition).getInt("Height");
                                    Img.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] data, com.parse.ParseException e) {
                                            if (e == null) {
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                AddBoard(bitmap, Code, w, h);
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
                                        final String Code = objects.get(childPosition).getObjectId();
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
                                                    AddNewPedal(bitmap, Code, Type, Name, Brand, h, w);

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

                    case 2:
                        ParseQuery<ParseObject> presetQuery = ParseQuery.getQuery("Presets");
                        presetQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                                if (e == null) {
                                    String data = objects.get(childPosition).getString("Content");
                                    String boardid = new String(data.split("-")[0]);
                                    String pedalsdata = new String(data.split("-")[1]);
                                    /*   backup.clear();
                                    backup = new ArrayList<ImageView>(getPedals(pedalsdata[0]));
                                    for(ImageView pedal : backup)
                                    {
                                        layout.addView(pedal);
                                    }*/
                                    ParseQuery<ParseObject> boardQuery = ParseQuery.getQuery("Board");
                                    boardQuery.whereEqualTo("objectId", boardid);
                                    boardQuery.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, com.parse.ParseException e) {
                                            if (e == null) {
                                                Img = objects.get(0).getParseFile("Image");
                                                final String Code = objects.get(0).getObjectId();
                                                final float w = objects.get(0).getInt("Width");
                                                final float h = objects.get(0).getInt("Height");
                                                Img.getDataInBackground(new GetDataCallback() {
                                                    @Override
                                                    public void done(byte[] data, com.parse.ParseException e) {
                                                        if (e == null) {
                                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                            AddBoard(bitmap, Code, w, h);
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    });
                                    backup.clear();

                                    String [] pedalsbuffer = pedalsdata.split("/");
                                    final ArrayList<ProtoPedal> protoPedals = new ArrayList<ProtoPedal>();
                                    String [] Codes = new String[pedalsbuffer.length];
                                    int it = 0;
                                    for(String pedal : pedalsbuffer)
                                    {
                                        ProtoPedal temp = new ProtoPedal();
                                        temp.code = pedal.split(",")[0];
                                        temp.x = Float.parseFloat(pedal.split(",")[1]);
                                        temp.y = Float.parseFloat(pedal.split(",")[2]);
                                        temp.angle = Float.parseFloat(pedal.split(",")[3]);
                                        protoPedals.add(temp);
                                        Codes[it] = temp.code;
                                        it++;

                                    }

                                    ParseQuery<ParseObject> pedalsquery = ParseQuery.getQuery("Pedals");
                                    pedalsquery.whereContainedIn("objectId", Arrays.asList(Codes));
                                    pedalsquery.findInBackground(new FindCallback<ParseObject>() {
                                        public void done(List<ParseObject> objectsl, ParseException e) {
                                            if (e == null) {
                                                for(int i = 0; i < objectsl.size(); i++)
                                                {
                                                    final int j = i;
                                                    final String Code = objectsl.get(i).getObjectId();
                                                    final ParseFile Img2 = objectsl.get(i).getParseFile("Image");
                                                    final String Name= objectsl.get(i).getString("Name");
                                                    final String Type= objectsl.get(i).getString("Type");
                                                    final String Brand= objectsl.get(i).getString("Brand");
                                                    final float w = objectsl.get(i).getInt("Width");
                                                    final float h = objectsl.get(i).getInt("Height");
                                                    float angle = 0;
                                                    float x = 0;
                                                    float y = 0;
                                                    for(ProtoPedal p : protoPedals)
                                                    {
                                                        if(p.code.equals(Code))
                                                        {
                                                            angle = p.angle;
                                                            x = p.x;
                                                            y = p.y;
                                                        }
                                                    }
                                                    final float finalX = x;
                                                    final float finalY = y;
                                                    final float finalAngle = angle;
                                                    Img2.getDataInBackground(new GetDataCallback() {
                                                        @Override
                                                        public void done(byte[] data, com.parse.ParseException e) {
                                                            if (e == null) {
                                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                                AddNewPedal(bitmap, Code, Type, Name, Brand, h, w, finalX, finalY, finalAngle);
                                                            }
                                                        }
                                                    });

                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        //Log.d("ID: ", boardid);
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
                if (selectedImg != null) {
                    ((Pedal) selectedImg).angle = ((Pedal) selectedImg).angle + 90;
                    selectedImg.setRotation(((Pedal) selectedImg).angle);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImg != null) {
                    layout.removeView(selectedImg);
                    backup.remove(selectedImg);
                    selectedImg = null;
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String toStore =  StringBuilderService.StringBuilder((Board) board, backup);
                ParseObject preset = new ParseObject("Presets");
                preset.put("Name","MyPreset");
                preset.put("Content", toStore);
                preset.saveInBackground();

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
        listDataHeader.add("Presets");
       // listDataHeader.add("Power Supplies");

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

        final List<String> presetList = new ArrayList<>();
        ParseQuery<ParseObject> presetQuery = ParseQuery.getQuery("Presets");
        presetQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject obj : objects) {
                        presetList.add(obj.getString("Name"));
                    }
                }
            }
        });


        List<String> powerSuppliesList = new ArrayList<String>();

        listDataChild.put(listDataHeader.get(0), boardsList);
        listDataChild.put(listDataHeader.get(1), pedalsList);
        listDataChild.put(listDataHeader.get(2), presetList);
    }

    public void AddBoard(Bitmap bitmap, String code, float w, float h){
        boolean mustresize = false;
        if(board != null)
        {
            board.setImageBitmap(null);
            mustresize = true;
        }
        board = new Board(this,code,h,w);
        if(mustresize)
            ((Board)board).resize = true;
        board.setImageBitmap(bitmap);
        deleteButton.bringToFront();
        rotateButton.bringToFront();
        layout.addView(board);
    }

    public void RestorePedals()
    {
        if(backup.size() > 0)
        {
            List<ImageView> TmpArray = new ArrayList<>(backup);
            backup.clear();
            for(int i = 0; i < TmpArray.size(); i++)
            {
                layout.removeView(TmpArray.get(i));
                Pedal temp = (Pedal)TmpArray.get(i);
                AddNewPedal(((BitmapDrawable) TmpArray.get(i).getDrawable()).getBitmap(), temp.Id,temp.Type, temp.Name, temp.Brand, temp.PedalHeight, temp.PedalWidth);
            }

            ((Board)board).resize = false;
        }
    }

    public void AddNewPedal(Bitmap bitmap, String code, String Type, String Name, String Brand, float PedalHeight, float PedalWidth)
    {
            ImageView p = new Pedal(this, code, bitmap, Type, Name, Brand, PedalHeight, PedalWidth);
            ((Pedal)p).Resize((int) ((Board) board).BoardWidth, board.getWidth(), (int) ((Board) board).BoardHeight, board.getHeight());
            p.setX((layout.getWidth() / 2));
            p.setY((layout.getHeight() / 2) - 70);
            backup.add(p);
            layout.addView(p);
            deleteButton.bringToFront();
            rotateButton.bringToFront();
    }

    public void AddNewPedal(Bitmap bitmap, String code, String Type, String Name, String Brand, float PedalHeight, float PedalWidth, float x, float y, float angle)
    {
        ImageView p = new Pedal(this, code, bitmap, Type, Name, Brand, PedalHeight, PedalWidth);
        ((Pedal)p).Resize((int) ((Board) board).BoardWidth, board.getWidth(), (int) ((Board) board).BoardHeight, board.getHeight());
        p.setX(x);
        p.setY(y);
        ((Pedal)p).angle = angle;
        p.setRotation(((Pedal)p).angle);
        backup.add(p);
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
