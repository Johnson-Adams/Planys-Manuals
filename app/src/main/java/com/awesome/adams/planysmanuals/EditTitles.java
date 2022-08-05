package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Adapters.EdittitleRecyclerAdapter;
import Adapters.EdittitleRecyclerAdapter2;
import Adapters.TitleRecyclerAdapter;
import Model.Article;
import Model.Title;

public class EditTitles extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private DatabaseReference mdatabasereference;
    private RecyclerView mrecyclerView;
    private EdittitleRecyclerAdapter titlerecycleradapter;
    private EdittitleRecyclerAdapter titlerecycleradapter2;
    private List<Title> cardList;
    private FirebaseUser muser;
    private FirebaseAuth mauth;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_titles);

        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        mdatabasereference = mdatabase.getReference().child("Topics");
        muser = mauth.getCurrentUser();
        mdatabasereference.keepSynced(true);
        save = (Button) findViewById(R.id.saveedittitle);


    }

    @Override
    protected void onStart() {
        super.onStart();

        mrecyclerView = (RecyclerView) findViewById(R.id.recyclerviewedittopics);
        mrecyclerView.setHasFixedSize(true);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardList = new ArrayList<>();

        mdatabasereference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Title title = dataSnapshot.getValue(Title.class);
                if (title.getActive().equals("true"))
                cardList.add(title);
                titlerecycleradapter = new EdittitleRecyclerAdapter(EditTitles.this, cardList);
                mrecyclerView.setAdapter(titlerecycleradapter);
                titlerecycleradapter.notifyDataSetChanged();
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTitles.this,Titles.class);
                startActivity(intent);
                finish();


            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditTitles.this, Articles.class);
        startActivity(intent);
        finish();
    }
}
