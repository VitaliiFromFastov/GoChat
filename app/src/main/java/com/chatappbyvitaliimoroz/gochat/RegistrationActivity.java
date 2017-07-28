package com.chatappbyvitaliimoroz.gochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout mUserNameTil;
    private TextInputLayout mEmailTil;
    private TextInputLayout mPasswordTil;
    private Button mRegistrationBtn;
    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private FirebaseDatabase mDatabase;
    private DatabaseReference  mReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mProgressDialog = new ProgressDialog(this);

        mUserNameTil = (TextInputLayout)findViewById(R.id.user_name_til);
        mEmailTil = (TextInputLayout) findViewById(R.id.email_til);
        mPasswordTil = (TextInputLayout)findViewById(R.id.password_til);
        mRegistrationBtn = (Button) findViewById(R.id.create_account_btn);
        mToolbar= (Toolbar) findViewById(R.id. registration_toolbar);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(R.string.registration_activity_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userNameString = mUserNameTil.getEditText().getText().toString().trim();
                String emailString = mEmailTil.getEditText().getText().toString().trim();
                String passwordString= mPasswordTil.getEditText().getText().toString().trim();

                if (!TextUtils.isEmpty(userNameString)&&!TextUtils.isEmpty(emailString)&&!TextUtils.isEmpty(passwordString)){
                    mProgressDialog.setTitle(R.string.registration_activity_progress_dialog_title);
                    mProgressDialog.setMessage(getString(R.string.registration_activity_progress_dialog_message));
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                createNewUser(userNameString,emailString,passwordString);}
            }
        });

    }

    private void createNewUser(final String userNameString, String emailString, String passwordString) {

        mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            mProgressDialog.hide();
                            Toast.makeText(RegistrationActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null){
                                String uid = user.getUid();

                                mReference= mDatabase.getReference().child("Users").child(uid);

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("name",userNameString);
                                hashMap.put("status","Hi there!");
                                hashMap.put("image","default");
                                hashMap.put("thumb","default");


                                mReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mProgressDialog.dismiss();

                                            Intent intent=new Intent(RegistrationActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                            }


                        }

                    }
                });
    }
}
