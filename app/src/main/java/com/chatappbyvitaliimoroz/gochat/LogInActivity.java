package com.chatappbyvitaliimoroz.gochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private TextInputLayout mEmailTil;
    private TextInputLayout mPasswordTil;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mEmailTil= (TextInputLayout)findViewById(R.id.activity_login_email_til);
        mPasswordTil = (TextInputLayout) findViewById(R.id.activity_login_password_til);
        mLoginBtn = (Button)findViewById(R.id.activity_login_btn);
        mToolbar = (Toolbar) findViewById(R.id.activity_login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.login_activity_title);

        mAuth= FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String emailString=  mEmailTil.getEditText().getText().toString();
                String passwordString= mPasswordTil.getEditText().getText().toString();

                if (!TextUtils.isEmpty(emailString)&&!TextUtils.isEmpty(passwordString)){
                    mProgressDialog.setTitle(R.string.login_activity_progress_dialog_title);
                    mProgressDialog.setMessage(getString(R.string.login_activity_progress_dialog_message));
                    mProgressDialog.show();
                    login(emailString,passwordString);
                }
            }
        });

    }

    private void login(String email,String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            mProgressDialog.hide();
                            Toast.makeText(LogInActivity.this, R.string.login_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {
                            mProgressDialog.dismiss();
                            Intent intent=new Intent(LogInActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                     }
                });

    }
}
