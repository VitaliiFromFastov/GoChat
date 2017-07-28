package com.chatappbyvitaliimoroz.gochat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.R.attr.start;

public class StartActivity extends AppCompatActivity {

    private Button mSignInBtn;
    private Button mSignUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mSignUpBtn=(Button)findViewById(R.id.sign_up_btn);
        mSignInBtn = (Button) findViewById(R.id.signin_btn);

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegistrationActivity.class));
                finish();
            }
        });

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LogInActivity.class));
                finish();
            }
        });
    }
}
