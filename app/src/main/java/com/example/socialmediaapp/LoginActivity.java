package com.example.socialmediaapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {


    private EditText edtUsernameLogin, edtPasswordLogin;
    private Button btnLogin, btnSwitchToSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        edtUsernameLogin = findViewById(R.id.edtUserNameLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);

        // Click login button if user clicks enter on keyboard in password field
        edtPasswordLogin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    btnLogin.performClick();
                }
                return false;
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnSwitchToSignup = findViewById(R.id.btnSwitchToSignup);




        // Login user
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Logging in");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                ParseUser.logInInBackground(edtUsernameLogin.getText().toString(), edtPasswordLogin.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {



                        if (user != null && e== null){
                            FancyToast.makeText(LoginActivity.this, user.get("username")+ " logged in successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                            // Switch to Home screen
                            transitionToHomeScreen();
                            finish();

                        } else{
                            FancyToast.makeText(LoginActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

        // Switch to signup screen
        btnSwitchToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // Exit keyboard if outside screen is clicked
    public void rootLayoutTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void transitionToHomeScreen(){
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
