package com.example.nitishprasad.musicplayer;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.widget.ListView;

public class viewPagerAdapter extends FragmentPagerAdapter{


    Context context;
    FragmentManager fm;
    ListView songList,playList;
    LayoutInflater inflater;
    Fragment fragment;

    public viewPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:

                songListFragment fragment = new songListFragment();
                return fragment;

            case 1:
                playListFragment fragment1 = new playListFragment();
                return fragment1;


        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
