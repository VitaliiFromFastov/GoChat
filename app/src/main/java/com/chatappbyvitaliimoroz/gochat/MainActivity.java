package com.chatappbyvitaliimoroz.gochat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth= FirebaseAuth.getInstance();

        mViewPager =(ViewPager)findViewById(R.id.activity_main_view_pager);
        mToolbar= (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),MainActivity.this);
        mViewPager.setAdapter(mViewPagerAdapter);

        mTabLayout=(TabLayout) findViewById(R.id.activity_main_tab_layout);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();

        if (user==null){
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.main_log_out:
                mAuth.signOut();
                goToStartActivity();
                return true;
            case R.id.main_settings:
                Intent settingsIntent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.main_menu_all_users:
                Intent intent = new Intent(MainActivity.this,UsersActivity.class);
                startActivity(intent);
                return true;
        }

return super.onOptionsItemSelected(item);

    }

    private void goToStartActivity() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user==null){
        startActivity(new Intent(MainActivity.this,StartActivity.class));
        finish();}
    }
}
