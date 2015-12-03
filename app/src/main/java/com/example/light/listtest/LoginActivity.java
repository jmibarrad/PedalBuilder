package com.example.light.listtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    Button signupButton;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.signInBtn);
        signupButton = (Button) findViewById(R.id.signUpBtn);

        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                usernametxt = email.getText().toString();
                passwordtxt = password.getText().toString();

                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {

                            @Override
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {

                                    Intent intent = new Intent(
                                            LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    Toast.makeText(
                                            getApplicationContext(),
                                            "This user doesn't exist. Please signup",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signupButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                usernametxt = email.getText().toString();
                passwordtxt = password.getText().toString();

                if (usernametxt.equals("") && passwordtxt.equals("")) {

                    Toast.makeText(getApplicationContext(),
                            "Please complete the sign up form",
                            Toast.LENGTH_SHORT).show();

                } else {

                    ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);
                    user.signUpInBackground(new SignUpCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                Toast.makeText(getApplicationContext(),
                                        "Successfully Signed up!",
                                        Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(getApplicationContext(),
                                        "Sign up error", Toast.LENGTH_SHORT)
                                        .show();

                            }
                        }
                    });

                }
            }
        });
    }
}

