package com.example.sarthak.navigationdrawer.Backend.Backend;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by Sneh_132 on 3/26/2016.
 */
public class LoginRegisterAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    public LoginRegisterAdapter(FragmentManager fm, String[] tabTitles) {
        super(fm);
        this.tabTitles = tabTitles;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0)                // if the position is 0 we are returning the First tab
        {
            Login tab1 = new Login();
            return tab1;
        }
        else                             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            Register tab2 = new Register();
            return tab2;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
