package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import Fragments.DosNDonts_two_s1;
import Fragments.DosNDonts_two_s2;
import Fragments.ShowTutorial;
import Fragments.TutorialList;

public class Tutorial extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        getSupportActionBar().setTitle("Tutorials");

        viewPager = (ViewPager) findViewById(R.id.page_tutorials);

        SetUpViewPager(viewPager);
    }

    public void SetUpViewPager(ViewPager viewPager) {
        Tutorial.MyViewPagerAdapter adapter = new Tutorial.MyViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragmentPage(new TutorialList(getBaseContext(),viewPager,adapter));
        viewPager.setAdapter(adapter);

    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> myfragment = new ArrayList<>();

        public MyViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void AddFragmentPage(Fragment page) {
            myfragment.add(page);
        }

        public void RemoveFragmentPage(Fragment page) {myfragment.remove(page);}

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return myfragment.get(position);
        }

        @Override
        public int getCount() {
            return myfragment.size();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Tutorial.this,Lists.class);
        startActivity(intent);
        finish();
    }
}
