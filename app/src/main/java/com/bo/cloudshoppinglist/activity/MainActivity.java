package com.bo.cloudshoppinglist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.bo.cloudshoppinglist.R;
import com.parse.ParseUser;
/**
 * Created by bowang on 28/03/15.
 */
public class MainActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable Local Datastore.


        if (ParseUser.getCurrentUser()!=null){
            setContentView(R.layout.activity_main);
// Locate the viewpager in activity_main.xml
            ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

            // Set the ViewPagerAdapter into ViewPager
            viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        }else{
            Intent intent = new Intent(this,LoginActivity.class);
            this.startActivity(intent);
            this.finish();
        }
    }
}
