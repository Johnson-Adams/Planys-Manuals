package com.awesome.adams.planysmanuals;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import Fragments.CreateProcessFragment;
import Fragments.DosNDonts_fragment;

public class CreateProcess extends AppCompatActivity {

    private Button plus;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_process);

        plus = (Button) findViewById(R.id.addstepprocess);
        pager = (ViewPager) findViewById(R.id.page_process);


        //SetUpViewPager(pager);
        final SharedPreferences sharedPreferences = getSharedPreferences("processs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < 1; i++)
            adapter.AddFragmentPage(new CreateProcessFragment(i, getBaseContext(), adapter, pager));
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = Integer.parseInt(sharedPreferences.getString("size","1"));
                editor.putString("size", String.valueOf(Integer.valueOf(sharedPreferences.getString("size","1")) +  1));
                adapter.AddFragmentPage(new CreateProcessFragment(size,CreateProcess.this,adapter,pager));
                adapter.notifyDataSetChanged();
                pager.setCurrentItem(size);
            }
        });
    }

    public void SetUpViewPager(ViewPager viewPager) {

        SharedPreferences sharedPreferences = getSharedPreferences("process", MODE_PRIVATE);

        CreateProcess.MyViewPagerAdapter adapter = new CreateProcess.MyViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < 1 /*sharedPreferences.getInt("size", 1)*/; i++)
            adapter.AddFragmentPage(new CreateProcessFragment(i, getBaseContext(), adapter, viewPager));
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        SharedPreferences sharedPreferences = getSharedPreferences("processs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        public List<Fragment> myfragment = new ArrayList<>();

        public MyViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void AddFragmentPage(Fragment page) {
            editor.putString("size", String.valueOf(Integer.valueOf(sharedPreferences.getString("size", "1")) + 1));
            editor.apply();
            myfragment.add(page);
        }

        public void RemoveFragmentPage(int index,ViewPager v,MyViewPagerAdapter a) {
            myfragment.remove(index);
            editor.putString("size", String.valueOf(Integer.valueOf(sharedPreferences.getString("size", "1")) - 1));
            editor.apply();
            v.setAdapter(a);
        }

        @Override
        public Fragment getItem(int position) {
            return myfragment.get(position);
        }

        @Override
        public int getCount() {
            return myfragment.size();
        }

        public List<Fragment> listfun() {
            return myfragment;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //SharedPreferences sharedPreferences = getSharedPreferences("process", MODE_PRIVATE);
        //int i = sharedPreferences.getInt("imgselect", 1);
        Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.page_process+":"+pager.getCurrentItem());
        if (page != null) {
            Log.d("abc", "detected");
            page.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
        /*SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("imguri", String.valueOf(data.getData()));
        editor.apply();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateProcess.this,Processs.class);
        startActivity(intent);
        finish();
    }
}
