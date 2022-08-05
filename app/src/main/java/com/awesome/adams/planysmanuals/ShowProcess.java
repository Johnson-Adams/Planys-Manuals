package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Fragments.CreateProcessFragment;
import Fragments.ProcessFragment;
import Model.listbasicinfo;

public class ShowProcess extends AppCompatActivity {

    private ViewPager page;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String TAG = "ShowProcess";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_process);

        page = (ViewPager) findViewById(R.id.page_process);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        SharedPreferences sharedPreferences = getSharedPreferences("processs", MODE_PRIVATE);
        String content_key = sharedPreferences.getString("process_in_show_process","null");
        Log.d("showp key",content_key);


        databaseReference.child("Contents").child(content_key).child("basicinfo").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                getSupportActionBar().setTitle(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SetUpViewPager(page);

    }

    public void SetUpViewPager(final ViewPager viewPager) {

        SharedPreferences sharedPreferences = getSharedPreferences("processs", MODE_PRIVATE);

        final MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());

        final String content_key = sharedPreferences.getString("process_in_show_process","null");
        Log.d(TAG,"1");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Contents").child(content_key).child("data").child("size").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String frag_count = String.valueOf(dataSnapshot.child("image").getChildrenCount());
                //Log.d("frag count", dataSnapshot.child("size").getValue(String.class));

                String size = dataSnapshot.getValue(String.class);
                Log.d("datasnap",size);
                for (int i = 0; i < Integer.valueOf(size); i++) {
                    adapter.AddFragmentPage(new ProcessFragment(i, getBaseContext(), adapter, viewPager));

                }

                viewPager.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public List<Fragment> myfragment = new ArrayList<>();

        public MyViewPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        public void AddFragmentPage(Fragment page) {

            myfragment.add(page);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ShowProcess.this,Processs.class);
        startActivity(intent);
        finish();
    }
}
