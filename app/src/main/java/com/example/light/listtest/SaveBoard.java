package com.example.light.listtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Gabriel Paz on 12/13/2015.
 */
public class SaveBoard extends AppCompatActivity {
    String boardmot;
    String userId;
    Bitmap preview;
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
            byte[] array = extras.getByteArray("Preview");
            preview = BitmapFactory.decodeByteArray(array, 0, array.length);
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

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    preview.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    ParseFile imgFile = new ParseFile(boardmot+".png", stream.toByteArray());
                    imgFile.saveInBackground();
                    preset.put("Preview", imgFile);


                    preset.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Success! Pedalboard saved",
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
