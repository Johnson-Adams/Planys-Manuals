package com.awesome.adams.planysmanuals;

import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
        import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
import java.util.List;

import Adapters.ProcessRecyclerAdapter;
import Adapters.TutorialRecyclerAdapter;
import Model.ListObject;
        import Model.listbasicinfo;

public class Processs extends AppCompatActivity {

    private ProcessRecyclerAdapter processRecyclerAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button plus;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processs);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        recyclerView = (RecyclerView) findViewById(R.id.processsrecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mauth = FirebaseAuth.getInstance();

        plus = (Button) findViewById(R.id.processsplus);

        databaseReference.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
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
                Intent intent = new Intent(Processs.this,CreateProcessHead.class);
                startActivity(intent);
                finish();
            }
        });

        getSupportActionBar().setTitle("PROCESS");
    }

    @Override
    protected void onStart() {
        super.onStart();

        final List<listbasicinfo> list_set = new ArrayList<>();

        final SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);

        databaseReference.child("Contents").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String content_key = dataSnapshot.getKey();
                databaseReference.child("Contents").child(content_key).child("basicinfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final listbasicinfo listbasicinfos = dataSnapshot.getValue(listbasicinfo.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);
                        final String article_id_inside = sharedPreferences.getString("article","null");
                        if (listbasicinfos.getType_id().equals("2")) {
                            databaseReference.child("Lists").child(listbasicinfos.getList_id()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("article_id").getValue(String.class).equals(article_id_inside)){
                                        list_set.add(listbasicinfos);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                        Log.d("size of proc", String.valueOf(list_set.size()));
                        processRecyclerAdapter = new ProcessRecyclerAdapter(Processs.this, list_set);
                        recyclerView.setAdapter(processRecyclerAdapter);
                        processRecyclerAdapter.notifyDataSetChanged();
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

        /*final List<listbasicinfo> finalListbasicinfos = new ArrayList<>();
        databaseReference.child("Contents").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String key = dataSnapshot.getKey();
                Log.d("process","1");
                databaseReference.child("Contents").child(key).child("basicinfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("process","2");
                        final listbasicinfo list = dataSnapshot.getValue(listbasicinfo.class);

                        if (list.getType_id().equals("2") && list.getActive().equals("true")) {
                            String listkey = list.getList_id();
                            databaseReference.child("Lists").child(listkey).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d("process","3");
                                    ListObject list1 = dataSnapshot.getValue(ListObject.class);
                                    Log.d("process",sharedPreferences.getString("article","null"));
                                    if (list1.getArticle_id().equals(sharedPreferences.getString("article","null")))
                                        finalListbasicinfos.add(list);
                                        Log.d("process name",list.getName());
                                        Log.d("process","4");
                                        processRecyclerAdapter = new ProcessRecyclerAdapter(Processs.this, finalListbasicinfos);
                                        recyclerView.setAdapter(processRecyclerAdapter);
                                        processRecyclerAdapter.notifyDataSetChanged();

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
        });*/


    }

    public boolean article(String list_id) {
        final int[] i = {1};
        final SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);
        databaseReference.child("Lists").child(list_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListObject object = dataSnapshot.getValue(ListObject.class);
                if (sharedPreferences.getString("article",null).equals(object.getArticle_id())) {
                    i[0] = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (i[0] == 1)
            return true;
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Processs.this,Lists.class);
        startActivity(intent);
        finish();
    }
}
