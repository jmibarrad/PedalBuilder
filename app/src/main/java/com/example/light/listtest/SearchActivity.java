package com.example.light.listtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Gabriel Paz on 12/17/2015.
 */
public class SearchActivity extends AppCompatActivity {
    Button search;
    Button cancel;
    Spinner Category;
    EditText presetName;
    String presetmot;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        search = (Button)findViewById(R.id.searchBtn);
        cancel = (Button)findViewById(R.id.cancelBtn2);
        String categories[] = new String[] { "Metal", "Rock", "Jazz", "Blues", "Others"};
        Category = (Spinner) findViewById(R.id.categoryDropDown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        Category.setAdapter(adapter);
        presetName = (EditText)findViewById(R.id.searchboardName);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presetmot = presetName.getText().toString();
                if (!presetmot.equals("")) {
                    Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                    intent.putExtra("Name", presetmot);
                    intent.putExtra("Category", Category.getSelectedItem().toString());
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error: Must Input Boardname",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
