package com.example.light.listtest;

import android.content.Context;

import com.parse.Parse;

public class ParseService {

    public static void MakeConnection(Context context)
    {
        Parse.enableLocalDatastore(context);
        Parse.initialize(context, "MNsckKzXmwFgf2mu7t1PsrZt0oZQKI39uhMeN1de", "WVx1RVB7qnFQP5Rv1YRyh0dcnqL8P8zIiQbmuOmr");
    }


}
