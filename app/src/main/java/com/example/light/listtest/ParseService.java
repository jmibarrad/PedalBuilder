package com.example.light.listtest;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseService extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "MNsckKzXmwFgf2mu7t1PsrZt0oZQKI39uhMeN1de", "WVx1RVB7qnFQP5Rv1YRyh0dcnqL8P8zIiQbmuOmr");
        ParseUser.enableAutomaticUser();
        ParseACL defaultAcl = new ParseACL();
        defaultAcl.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultAcl, true);
    }


}
