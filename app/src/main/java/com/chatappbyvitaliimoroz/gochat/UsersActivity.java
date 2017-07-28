package com.chatappbyvitaliimoroz.gochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private LinearLayoutManager mManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mToolbar= (Toolbar)findViewById(R.id.activity_users_toolbar);
        mRecyclerView= (RecyclerView)findViewById(R.id.activity_users_recycler_view);
        mManager = new LinearLayoutManager(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mManager);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Users");



        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.users_activity_toolbar_title));
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<Users,UsersViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                Users.class,R.layout.users_item,UsersViewHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users users, int position) {

                viewHolder.setUserName(users.getmName());
                viewHolder.setUserStatus(users.getmStatus());

            }
        };
     mRecyclerView.setAdapter(recyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public UsersViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setUserName(String name){

            TextView mUserNameTv = (TextView) mView.findViewById(R.id.users_item_username_tv);

            mUserNameTv.setText(name);

            // hello

        }

        public void setUserStatus (String status){
            TextView mUserStatusTv = (TextView) mView.findViewById(R.id.users_item_userstatus_tv);
            mUserStatusTv.setText(status);

        }
    }
}
