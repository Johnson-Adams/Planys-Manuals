package com.awesome.adams.planysmanuals;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.TitleRecyclerAdapter;
import Model.Title;

public class
Titles extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private DatabaseReference mdatabasereference,databaseReference;
    private RecyclerView mrecyclerView;
    private TitleRecyclerAdapter titlerecycleradapter;
    private List<Title> cardList;
    private FirebaseUser muser;
    private FirebaseAuth mauth;
    private Button createbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titles);

        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        mdatabasereference = mdatabase.getReference().child("Topics");
        databaseReference = mdatabase.getReference().child("Userlist");
        muser = mauth.getCurrentUser();
        mdatabasereference.keepSynced(true);

        getSupportActionBar().setTitle("TOPICS");

    }



    @Override
    protected void onStart() {
        super.onStart();

        if (muser.isEmailVerified()) {

            mrecyclerView = (RecyclerView) findViewById(R.id.recyclerviewtopics);
            mrecyclerView.setHasFixedSize(true);
            mrecyclerView.setLayoutManager(new LinearLayoutManager(this));

            cardList = new ArrayList<>();
            Log.d("titles","1");

            createbutton = (Button) findViewById(R.id.plusbutton);

            createbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Titles.this, CreateTitle.class);
                    startActivity(intent);
                }
            });

            databaseReference.child(muser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("admin").getValue(String.class).equals("false"))
                        createbutton.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mdatabasereference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Log.d("homess", "homess");

                    Title title = dataSnapshot.getValue(Title.class);
                    if (title.getActive().equals("true"))
                    cardList.add(title);
                    Log.d("a", "a");
                    titlerecycleradapter = new TitleRecyclerAdapter(Titles.this, cardList);
                    Log.d("d", "d");
                    mrecyclerView.setAdapter(titlerecycleradapter);
                    Log.d("r", "r");
                    titlerecycleradapter.notifyDataSetChanged();
                    Log.d("home", "home");


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else
            emailVerifyDialog();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logoutfromtitles:
                mauth.signOut();
                Intent intent = new Intent(Titles.this,Login.class);
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
        getMenuInflater().inflate(R.menu.titlesmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void emailVerifyDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Verify Your Email")
                .setMessage("Please Verify your Email using the url sent to your registered email")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //Toast.makeText(Titles.this,"wow",Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mauth.signOut();
                        startActivity(new Intent(Titles.this,Login.class));
                        finish();
                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
    }
}
