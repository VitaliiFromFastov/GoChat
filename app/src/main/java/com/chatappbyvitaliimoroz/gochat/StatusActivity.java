package com.chatappbyvitaliimoroz.gochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextInputLayout mStatusTil;
    private Button mSaveChangesBtn;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mToolbar=(Toolbar)findViewById(R.id.activity_status_toolbar);
        mStatusTil= (TextInputLayout)findViewById(R.id.status_activity_status_til);
        mSaveChangesBtn=(Button)findViewById(R.id.status_activity_save_changes_btn);


        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle(R.string.activity_status_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
       String stringExtra= intent.getStringExtra("status");
        if (stringExtra!=null) {
            mStatusTil.getEditText().setText(stringExtra);
        }
        mDatabase=FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser!=null){
        String uid = mUser.getUid();
            mReference= mDatabase.getReference().child("Users").child(uid);
        }




        mSaveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog= new ProgressDialog(StatusActivity.this);
                mProgressDialog.setTitle(R.string.saving_changes_progress_dialog_title);
                mProgressDialog.setMessage(getString(R.string.saving_changes_progress_dialog_message));
                mProgressDialog.show();

                String status = mStatusTil.getEditText().getText().toString();
                mReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mProgressDialog.dismiss();
                        } else {

                            Toast.makeText(getApplicationContext(),R.string.toast_error_with_saving_changes,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
