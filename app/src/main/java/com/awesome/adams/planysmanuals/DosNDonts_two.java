package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import Fragments.DosNDonts_two_s1;
import Fragments.DosNDonts_two_s2;

public class DosNDonts_two extends AppCompatActivity {

    private TabLayout tab;
    private ViewPager page;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dos_ndonts_two);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tab = (TabLayout) findViewById(R.id.tab);
        page = (ViewPager) findViewById(R.id.page);

        mauth = FirebaseAuth.getInstance();

        tab.setupWithViewPager(page);

        SetUpViewPager(page);
    }

    public void SetUpViewPager(ViewPager viewPager) {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragmentPage(new DosNDonts_two_s1(getBaseContext(),viewPager),"DO'S");
        adapter.AddFragmentPage(new DosNDonts_two_s2(getBaseContext()),"DONT'S");

        viewPager.setAdapter(adapter);

    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> myfragment = new ArrayList<>();
        private List<String> pagetitle = new ArrayList<>();

        public MyViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void AddFragmentPage(Fragment page,String title) {
            myfragment.add(page);
            pagetitle.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            return myfragment.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return pagetitle.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dondontsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logoutfromdosndonts) {
            mauth.signOut();
            Intent intent = new Intent(DosNDonts_two.this,Login.class);
            SharedPreferences prefs = getSharedPreferences("rem_user",MODE_PRIVATE);
            SharedPreferences.Editor changer = prefs.edit();
            changer.putString("rem_useremail",null);
            changer.putString("rem_pass",null);
            changer.apply();
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DosNDonts_two.this,DosNDonts.class);
        startActivity(intent);
        finish();
    }
}
