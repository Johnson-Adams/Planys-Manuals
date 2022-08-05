package com.awesome.adams.planysmanuals;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Fragments.DosNDonts_fragment;
import Fragments.Dos_N_Donts_fragment_text;
import Model.listbasicinfo;

public class DosNDonts extends AppCompatActivity {

    private Button plus;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,data;
    private DatabaseReference databaseReference2,databaseReference3;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private String[] dos = {"Image + Text", "Text"};
    int dosno = -1;
    private listbasicinfo info = new listbasicinfo();
    private FirebaseAuth mauth;

    private ViewPager page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dos_ndonts);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Contents");
        databaseReference2 = firebaseDatabase.getReference().child("Users");
        databaseReference3 = firebaseDatabase.getReference();
        data = firebaseDatabase.getReference().child("Lists");
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.dnd_refresh);

        getSupportActionBar().setTitle("Dos and Donts");

        plus = (Button) findViewById(R.id.dosndonts_plus);

        mauth = FirebaseAuth.getInstance();

        databaseReference3.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("admin").getValue(String.class).equals("false"))
                    plus.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDialog();
            }
        });

        page = (ViewPager) findViewById(R.id.page_dosndonts);

    }

    @Override
    protected void onStart() {
        super.onStart();
        String key = null;

        final SharedPreferences sharedPreferences4 = getSharedPreferences("dnd_size",MODE_PRIVATE);
        final SharedPreferences.Editor adder = sharedPreferences4.edit();
        adder.putInt("size",getdetails());
        adder.apply();
        Log.d("dnd","2.4");

        final MyViewPagerAdapter[] adapter = {new MyViewPagerAdapter(getSupportFragmentManager())};

        final int[] i = {0};
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String key = dataSnapshot.getKey();
                databaseReference.child(key).child("basicinfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("dnd","0");
                        //Log.d("dnd text",dataSnapshot.getValue(String.class));
                        String list_id = dataSnapshot.child("list_id").getValue(String.class);
                        final String type = dataSnapshot.child("type_id").getValue(String.class);
                        data.child(list_id).child("article_id").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String article_id = dataSnapshot.getValue(String.class);
                                SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);
                                String article_id_inside = sharedPreferences.getString("article","null");
                                if (article_id.equals(article_id_inside)) {
                                    if (type.equals( String.valueOf(1))) {
                                        Log.d("dnd","1");
                                        databaseReference.child(key).child("basicinfo").child("active").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (Objects.equals(dataSnapshot.getValue(String.class), "true")) {
                                                    i[0]++;
                                                    adder.putInt("size", i[0]);
                                                    adder.apply();
                                                    Log.d("dnd","2");
                                                    databaseReference.child(key).child("data").child("type").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String type = dataSnapshot.getValue(String.class);
                                                            if (type.equals("image"))
                                                                adapter[0] = addtoadapter(adapter[0],key,0);
                                                            else
                                                                adapter[0] = addtoadapter(adapter[0],key,1);
                                                            SetUpViewPager(page,adapter[0]);
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });



                                                }
                                                Log.d("dnd size", String.valueOf(sharedPreferences4.getInt("size",-1)));
                                            }



                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public int getdetails() {

        final SharedPreferences sharedPreferences4 = getSharedPreferences("dnd_size",MODE_PRIVATE);
        final SharedPreferences.Editor adder = sharedPreferences4.edit();
        adder.apply();
        final int[] i = {0};
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String key = dataSnapshot.getKey();
                databaseReference.child(key).child("basicinfo").child("type_id").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("dnd","0");
                        Log.d("dnd text",dataSnapshot.getValue(String.class));
                        if (dataSnapshot.getValue(String.class).equals( String.valueOf(1))) {
                            Log.d("dnd","1");
                            databaseReference.child(key).child("basicinfo").child("active").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (Objects.equals(dataSnapshot.getValue(String.class), "true")) {
                                        i[0]++;
                                        adder.putInt("size", i[0]);
                                        adder.apply();
                                        Log.d("dnd","2");

                                    }
                                    Log.d("dnd size", String.valueOf(sharedPreferences4.getInt("size",-1)));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return sharedPreferences4.getInt("size",0);

    }

    public void startDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Choose Type")
                .setItems(dos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dosno = which;
                        if (dosno == 0) {
                            Intent intent = new Intent(DosNDonts.this,DosNDonts_one.class);
                            startActivity(intent);
                        }

                        else if (dosno == 1) {
                            Intent intent = new Intent(DosNDonts.this,DosNDonts_two.class);
                            startActivity(intent);
                        }
                    }
                }).create().show();

    }

    public MyViewPagerAdapter addtoadapter(MyViewPagerAdapter adapter,String key,int i) {
        if (i == 0)
         adapter.AddFragmentPage(new DosNDonts_fragment(key, getBaseContext()));
        else
         adapter.AddFragmentPage(new Dos_N_Donts_fragment_text(key,getBaseContext()));
        return adapter;
    }

    /*public void SetUpViewPager(ViewPager viewPager, String key+) {

        final SharedPreferences sharedPreferences4 = getSharedPreferences("dnd_size",MODE_PRIVATE);
        getdetails();
        Log.d("dnd","2.5");
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0;i<sharedPreferences4.getInt("size",0);i++) {
            Log.d("dnd","3");
            adapter.AddFragmentPage(new DosNDonts_fragment(i,getBaseContext()));
            adapter.AddFragmentPage(new DosNDonts_fragment(key, getBaseContext()));
        }
        viewPager.setAdapter(adapter);
        Log.d("dnd","4");
        adapter.notifyDataSetChanged();
        Log.d("dnd","5");

    }*/

    public void SetUpViewPager(ViewPager viewPager, MyViewPagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        final SharedPreferences sharedPreferences4 = getSharedPreferences("dnd_size",MODE_PRIVATE);

        private List<Fragment> myfragment = new ArrayList<>();

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final SharedPreferences sharedPreferences4 = getSharedPreferences("dnd_size",MODE_PRIVATE);
        final SharedPreferences.Editor deleter = sharedPreferences4.edit();
        deleter.remove("size");
        deleter.apply();
        Intent intent = new Intent(DosNDonts.this,Lists.class);
        startActivity(intent);
        finish();
    }
}
