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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.List;

import Adapters.ArticlesRecycleAdapter;
import Model.Article;
import Model.ListObject;
import Model.Title;

public class Articles extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,data;
    private RecyclerView recyclerView;
    private Button plus;
    private List<Article> articleList;
    private ArticlesRecycleAdapter articlesRecycleAdapter;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Article");


        recyclerView = (RecyclerView) findViewById(R.id.articlerecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        plus = (Button) findViewById(R.id.articleplus);

        mauth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseDatabase = FirebaseDatabase.getInstance();
        data = firebaseDatabase.getReference();

        data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("admin").getValue(String.class).equals("false"))
                    plus.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        articleList = new ArrayList<>();
        final SharedPreferences sharedPreferences = getSharedPreferences("title_into",MODE_PRIVATE);
        String title_id = sharedPreferences.getString("title","null");

        data.child("Topics").child(title_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Title object = dataSnapshot.getValue(Title.class);
                String list = object.getName();
                getSupportActionBar().setTitle("ARTICLES - " + list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.articlerecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Article article = dataSnapshot.getValue(Article.class);
                Log.d("topic_id",article.getTopic_id());
                if (article.getTopic_id().equals(sharedPreferences.getString("title",null)) && article.getActive().equals("true"))
                articleList.add(article);
                articlesRecycleAdapter = new ArticlesRecycleAdapter(Articles.this,articleList);
                recyclerView.setAdapter(articlesRecycleAdapter);
                articlesRecycleAdapter.notifyDataSetChanged();

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


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Articles.this,CreateArticle.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mauth = FirebaseAuth.getInstance();

        switch (item.getItemId()) {

            case R.id.edittitlefromarticle:
                data = firebaseDatabase.getReference();

                data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("admin").getValue(String.class).equals("true")) {
                            Log.d("eedit","reached");
                            Intent intentprofile = new Intent(Articles.this,EditthisTitle.class);
                            Log.d("intent","profile declared");
                            startActivity(intentprofile);
                            finish();
                            Log.d("start edit","reached");
                        }
                        else
                            Toast.makeText(Articles.this,"Get Admin Status to edit",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                break;

            case R.id.logoutfromarticles:
                mauth.signOut();
                Intent intent = new Intent(Articles.this,Login.class);
                SharedPreferences prefs = getSharedPreferences("rem_user",MODE_PRIVATE);
                SharedPreferences.Editor changer = prefs.edit();
                changer.putString("rem_useremail",null);
                changer.putString("rem_pass",null);
                changer.apply();
                startActivity(intent);
                finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.articlesmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Articles.this,Titles.class);
        startActivity(intent);
        finish();
    }
}
