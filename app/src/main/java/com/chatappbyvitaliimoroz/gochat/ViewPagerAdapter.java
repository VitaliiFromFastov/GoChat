package com.chatappbyvitaliimoroz.gochat;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static android.R.attr.fragment;

/**
 * Created by Admin on 13.07.2017.
 */

class ViewPagerAdapter extends FragmentPagerAdapter{


    private Context mContext;

    public ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new RequestFragment();

            case 1:
                return new ChatFragment();

            case 2:
                return new FriendsFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
         super.getPageTitle(position);
        switch (position)
        {
            case 0:
                return mContext.getString(R.string.request_fragment_title);
            case 1:
                return mContext.getString(R.string.chat_fragment_title);
            case 2:
                return mContext.getString(R.string.friends_fragment_title);

            default:
                return null;
        }
    }
}
