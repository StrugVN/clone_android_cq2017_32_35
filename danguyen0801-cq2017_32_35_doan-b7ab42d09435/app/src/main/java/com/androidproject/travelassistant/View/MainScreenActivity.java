package com.androidproject.travelassistant.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.View.Fragment.ExploreFragment;
import com.androidproject.travelassistant.View.Fragment.NotificationFragment;
import com.androidproject.travelassistant.View.Fragment.TourHistoryFragment;
import com.androidproject.travelassistant.View.Fragment.TourListFragment;
import com.androidproject.travelassistant.View.Fragment.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreenActivity extends AppCompatActivity {
    private BottomNavigationView nav;

    final private static String UserProfileTag = "UP";
    final private static String TourListTag = "TL";
    final private static String TourHistoryTag = "TH";
    final private static String NotificationTag = "NT";
    final private static String ExploreTag = "EP";
    private String currentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        nav = findViewById(R.id.nav_view);

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                String fragmentTag = "";
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:{
                        fragmentTag = TourListTag;

                        break;
                    }

                    case R.id.navigation_profile:{
                        fragmentTag = UserProfileTag;
                        break;
                    }

                    case R.id.navigation_history:{
                        fragmentTag = TourHistoryTag;
                        break;
                    }

                    case R.id.navigation_notification:
                        fragmentTag = NotificationTag;
                        break;

                    case R.id.navigation_explore:
                        fragmentTag = ExploreTag;
                }
                return loadFragment(fragmentTag);
            }
        });

        loadFragment(TourListTag);
    }

    private Fragment getFragmentFromTag(String tag){
        if(TextUtils.equals(tag, UserProfileTag))
            return new UserProfileFragment();
        else if(TextUtils.equals(tag, TourHistoryTag))
            return new TourHistoryFragment();
        else if(TextUtils.equals(tag, TourListTag))
            return new TourListFragment();
        else if(TextUtils.equals(tag, NotificationTag))
            return new NotificationFragment();
        else if(TextUtils.equals(tag, ExploreTag))
            return new ExploreFragment();
        return null;
    }

    private boolean loadFragment(String tag){
        if(TextUtils.equals(tag, currentFragmentTag))
            return false;

        FragmentManager fm = getSupportFragmentManager();
        if(!TextUtils.isEmpty(tag)){
            if(TextUtils.isEmpty(currentFragmentTag)) {
                currentFragmentTag = tag;

                if(fm.findFragmentByTag(tag) == null){
                    Fragment newFragment = null;

                    newFragment = getFragmentFromTag(tag);

                    fm.beginTransaction()
                            .add(R.id.frame, newFragment, tag)
                            .commit();
                }
                else
                    fm.beginTransaction()
                        .show(fm.findFragmentByTag(tag))
                        .commit();
            }
            else {
                fm.beginTransaction()
                        .hide(fm.findFragmentByTag(currentFragmentTag))
                        .commit();

                if (fm.findFragmentByTag(tag) != null)
                    fm.beginTransaction()
                        .show(fm.findFragmentByTag(tag))
                        .commit();
                else{
                    Fragment newFragment = null;

                    newFragment = getFragmentFromTag(tag);

                    fm.beginTransaction()
                            .add(R.id.frame, newFragment, tag)
                            .commit();
                }

                currentFragmentTag = tag;
            }


            return true;
        }
        return false;
    }

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (currentFragmentTag != TourListTag){
            loadFragment(TourListTag);
            nav.getMenu().getItem(0).setChecked(true);
        }
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
}
