package com.example.socialmediaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignupActivity extends AppCompatActivity {

    private EditText edtUsernameSignup, edtPasswordSignup;
    private Button btnSignup, btnSwitchToLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        edtUsernameSignup = findViewById(R.id.edtUserNameSignup);
        edtPasswordSignup = findViewById(R.id.edtPasswordSignup);

        // Click Signup button if user taps enter on keyboard in password field
        edtPasswordSignup.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    btnSignup.performClick();
                }
                return false;
            }
        });

        btnSignup = findViewById(R.id.btnSignUp);
        btnSwitchToLogin = findViewById(R.id.btnSwitchToLogin);

        // Logout current user
        if (ParseUser.getCurrentUser() != null) {
            transitionToHomeScreen();;
        }



        // Sign up user
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ParseUser appUser = new ParseUser();
                appUser.setUsername(edtUsernameSignup.getText().toString());
                appUser.setPassword(edtPasswordSignup.getText().toString());

                // Loading dialog
                final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                progressDialog.setMessage("Signing up " + edtUsernameSignup.getText().toString());
                progressDialog.show();

                appUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {



                        if (e==null){
                            FancyToast.makeText(SignupActivity.this, appUser.get("username")+ " successfully signed up", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                            // Move to Home screen
                            transitionToHomeScreen();
                            finish();
                        } else{
                            FancyToast.makeText(SignupActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                        }
                        progressDialog.dismiss();
                    }
                });

            }
        });

        // Switch to login page
        btnSwitchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Exit keyboard if screen outside is clicked
    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void transitionToHomeScreen(){
        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
        startActivity(intent);
    }

}
