package com.example.light.listtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gabriel Paz on 12/17/2015.
 */
public class SearchListActivity extends AppCompatActivity {
    Button back;
    Button load;
    String presetName;
    String category;
    List<ProtoPreset> presetList;
    List<String>names;
    ListView list;
    ProtoPreset toSend;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_boards_layout);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            presetName = extras.getString("Name");
            category = extras.getString("Category");
        }

        presetList = new ArrayList<>();
        names = new ArrayList<>();
        list = (ListView)findViewById(R.id.myListView);
        fillLists();
        back = (Button)findViewById(R.id.goback);
        load = (Button)findViewById(R.id.loadButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMyListView();
            }
        });
    }

    private void setMyListView() {

        ArrayAdapter<String> namesToDisplay = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, names);
        list.setAdapter(namesToDisplay);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                for (ProtoPreset p : presetList) {
                    if (p.presetName.equals(textView.getText().toString())) {
                        toSend = p;
                        break;
                    }
                }
                Intent intent = new Intent(SearchListActivity.this, MainActivity.class);
                intent.putExtra("Data",toSend.data);
                startActivity(intent);
                finish();
            }
        });
    }

    public void fillLists(){
        ParseQuery<ParseObject> presetQuery = ParseQuery.getQuery("Presets");
        presetQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject obj : objects) {
                        ProtoPreset temp = new ProtoPreset();
                        temp.presetName = obj.getString("Name");
                        temp.data = obj.getString("Content");
                        presetList.add(temp);
                        names.add(temp.presetName);
                    }
                }
            }
        });
    }
}
