package com.example.socialmediaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Save the current Installation to Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void helloWorldTapped(View view){

        ParseObject boxer = new ParseObject("Boxer");
        boxer.put("Punch_Speed", 200);
        boxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    Toast.makeText(SignUp.this,"Object saved successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
