package com.example.light.listtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;

/**
 * Created by Gabriel Paz on 12/13/2015.
 */
public class SaveBoard extends AppCompatActivity {
    String boardmot;
    String userId;
    String data;
    Button savebutton;
    Button cancelbutton;
    EditText boardname;
    Spinner Category;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_board);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getString("UserID");
            data = extras.getString("Data");
        }

        savebutton = (Button)findViewById(R.id.SaveBtn);
        cancelbutton = (Button)findViewById(R.id.cancelBtn);
        boardname = (EditText)findViewById(R.id.boardName);
        String categories[] = new String[] { "Metal", "Rock", "Jazz", "Blues", "Others"};
        Category = (Spinner) findViewById(R.id.categoryDropDown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        Category.setAdapter(adapter);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardmot = boardname.getText().toString();
                if (!boardmot.equals("")) {
                    ParseObject preset = new ParseObject("Presets");
                    preset.put("Name", boardmot);
                    preset.put("Content", data);
                    preset.put("User", userId);
                    preset.put("Category", Category.getSelectedItem().toString());
                    preset.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Succes! Pedalboard saved",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SaveBoard.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Error: Must Input Boardname",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
