package com.awesome.adams.planysmanuals;

import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

import Adapters.ListRecyclerAdapter;
import Adapters.MainListRecyclerAdapter;
import Adapters.newListRecyclerAdapter;
import Model.Article;
import Model.ListObject;
import Model.listbasicinfo;

public class Lists extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference  databaseReference,data;
    private RecyclerView recyclerView,recyclerView1;
    private Button plus;
    private MainListRecyclerAdapter mainListRecyclerAdapter;
    private newListRecyclerAdapter newlistRecyclerAdapter;
    private List<ListObject> lists;
    private List<String> listsall,contentlist,noncontentlist;
    private String[] listlist;
    private String[] mainlist,mainlist2,noncontentlist_string,finallistfordialog;
    private List<String> listlist1;
    private List<String> listlist2;
    private List<String> listlist3;
    private int listno,l=0,a = 0,b = 0,c = 0,j = 0,chose = -1;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        //plus = (Button) findViewById(R.id.listplus);
        recyclerView = (RecyclerView) findViewById(R.id.listrecyclerview);
        //recyclerView1 = (RecyclerView) findViewById(R.id.highlistrecyclerview);

        //recyclerView1.setHasFixedSize(true);
        //recyclerView1.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);
        final String article_id_inside = sharedPreferences.getString("article","null");

        databaseReference.child("Article").child(article_id_inside).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Article article = dataSnapshot.getValue(Article.class);
                getSupportActionBar().setTitle("LISTS - "+article.getName() );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        listlist1 = new ArrayList<>();
        listlist1.add("Do's and Dont's");
        listlist1.add("Process");
        listlist1.add("Tutorials");
        /*listlist2 = new ArrayList<>();
        listlist3 = new ArrayList<>();
        contentlist = new ArrayList<>();
        noncontentlist = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        lists = new ArrayList<>();
        listsall = new ArrayList<String>();

        SharedPreferences sharedPreferences = getSharedPreferences("Lists",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        databaseReference.child("Lists").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String lists_key = dataSnapshot.getKey();
                databaseReference.child("Lists").child(lists_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("lists", String.valueOf(0.1));
                        ListObject object = dataSnapshot.getValue(ListObject.class);
                        if (object.getActive().equals("true") && object.getType().equals("1")) {
                            editor.putString("dnd_track","1");
                            editor.apply();
                        } else if (object.getActive().equals("true") && object.getType().equals("2")) {
                            editor.putString("pro_track","2");
                            editor.apply();
                        } else if (object.getActive().equals("true") && object.getType().equals("3")) {
                            editor.putString("tut_track","3");
                            editor.apply();
                        } else if (object.getActive().equals("true") && object.getType().equals("4")) {
                            editor.putString("cust_track", "4");
                            editor.apply();
                        }
                        if (object.getType() != "1" && object.getType() != "2" && object.getType() != "3" && object.getType() != "4") {
                            listsall.add(String.valueOf(object.getName()));
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

        if (sharedPreferences.getString("dnd_track","0").equals("1")) {
            listlist2.add("Do's and Dont's");
        }
        if (sharedPreferences.getString("pro_track","0").equals("2")) {
            listlist2.add("Process");
        }
        if (sharedPreferences.getString("tut_track","0").equals("3")) {
            listlist2.add("Tutorials");
        }
        if (sharedPreferences.getString("cust_track","0").equals("4")) {
            listlist2.add("Coutoms");
        }

        if (!listlist2.contains("Do's and Dont's"))
            listlist3.add("Do's and Dont's");
        if (!listlist2.contains("Process"))
            listlist3.add("Process");
        if (!listlist2.contains("Tutorials"))
            listlist3.add("Tutorials");
        if (!listlist2.contains("Customs"))
            listlist3.add("Customs");

        mainlist = new String[listlist2.size()];

        for (int i = 0;i<listlist2.size();i++) {
            mainlist[i] = listlist2.get(i);
        }

        mainlist2 = new String[listlist3.size()];

        for (int i = 0;i<listlist3.size();i++) {
            mainlist2[i] = listlist3.get(i);
        }

        mainListRecyclerAdapter = new MainListRecyclerAdapter(Lists.this,mainlist);
        recyclerView1.setAdapter(mainListRecyclerAdapter);
        mainListRecyclerAdapter.notifyDataSetChanged();

        databaseReference.child("Contents").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("lists","1");
                String content_key = dataSnapshot.getKey();
                databaseReference.child("Contents").child(content_key).child("basicinfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listbasicinfo info = dataSnapshot.getValue(listbasicinfo.class);
                        if (info.getType_id() != "1" && info.getType_id() != "2" && info.getType_id() != "3" && info.getType_id() != "4") {
                            contentlist.add(info.getName());
                        }
                        int j = 0;
                        for (int i = 0; i<listsall.size(); i++) {
                            if (!listsall.get(i).equals(contentlist.get(j))) {
                                noncontentlist.add(info.getName());
                                if (j<contentlist.size()-1)
                                j++;

                            }
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
        Log.d("lists","2");

        noncontentlist_string = new String[noncontentlist.size()];

        for (int i = 0;i<noncontentlist.size();i++) {
            noncontentlist_string[i] = listlist2.get(i);
        }

        finallistfordialog = concatTwoStringArrays(mainlist2,noncontentlist_string);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog(finallistfordialog);
                /*if (chose < mainlist.length && chose != -1) {
                    Log.d("chose",String.valueOf(chose));
                    String mainlistchosen = mainlist[chose];
                    if (mainlistchosen.equals("Do's and Dont's")) {
                        Intent intent = new Intent(Lists.this,DosNDonts.class);
                        startActivity(intent);
                    } else if (mainlistchosen.equals("Process")) {
                        Intent intent = new Intent(Lists.this,CreateProcess.class);
                        startActivity(intent);
                    }
                    else if (mainlistchosen.equals("Tutorials")) {
                        Intent intent = new Intent(Lists.this,CreateTut.class);
                        startActivity(intent);
                    }
                    else if (mainlistchosen.equals("Customs")) {
                        Intent intent = new Intent(Lists.this,Custom.class);
                        startActivity(intent);
                    }
                } else
                {


                }

            }
        });*/

        newlistRecyclerAdapter = new newListRecyclerAdapter(Lists.this,listlist1);
        recyclerView.setAdapter(newlistRecyclerAdapter);
        newlistRecyclerAdapter.notifyDataSetChanged();

    }

    public static String[] concatTwoStringArrays(String[] s1, String[] s2){
        String[] result = new String[s1.length+s2.length];
        int i;
        for (i=0; i<s1.length; i++)
            result[i] = s1[i];
        int tempIndex =s1.length;
        for (i=0; i<s2.length; i++)
            result[tempIndex+i] = s2[i];
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mauth = FirebaseAuth.getInstance();

        switch (item.getItemId()) {

            case R.id.editarticlefromlists:
                data = firebaseDatabase.getReference();

                data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("admin").getValue(String.class).equals("true")) {
                            Intent intent = new Intent(Lists.this,EditthisArticle.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(Lists.this,"Get Admin Status to Edit",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case R.id.logoutfromlists:
                mauth.signOut();
                Intent intenta = new Intent(Lists.this,Login.class);
                SharedPreferences prefs = getSharedPreferences("rem_user",MODE_PRIVATE);
                SharedPreferences.Editor changer = prefs.edit();
                changer.putString("rem_useremail",null);
                changer.putString("rem_pass",null);
                changer.apply();
                startActivity(intenta);
                finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listsmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void startDialog(final String[] listlist) {

        final SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences("pluslist",MODE_PRIVATE);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("listselected",MODE_PRIVATE);
        final SharedPreferences.Editor saver = sharedPreferences1.edit();
        final SharedPreferences.Editor sa = sharedPreferences2.edit();

        final int[] chose = new int[1];

        new android.app.AlertDialog.Builder(this)
                .setTitle("Choose")
                .setItems(listlist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listno = which;
                        Log.d("whic value", String.valueOf(which));
                        sa.putInt("listsel",listno);
                        sa.apply();

                        Log.d("list selected",String.valueOf(sharedPreferences2.getInt("listsel",-1)));

                        if (listlist[listno] == "Do's and Dont's") {
                            Intent intent = new Intent(Lists.this,DosNDonts.class);
                            startActivity(intent);

                        }

                        else if (listlist[listno].equals("Process")) {
                            Intent intent = new Intent(Lists.this,Process.class);
                            startActivity(intent);
                            finish();
                        }

                        else if (listlist[listno].equals("Tutorials")) {
                            Intent intent = new Intent(Lists.this,CreateTut.class);
                            startActivity(intent);
                            finish();
                        }

                        else if (listlist[listno].equals("Customs")) {
                            Intent intent = new Intent(Lists.this,Custom.class);
                            startActivity(intent);
                            finish();
                        }

                        chose[0] = which;
                    }
                }).create().show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        final SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);
        final SharedPreferences sharedPreferences1 = getSharedPreferences("pluslist",MODE_PRIVATE);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("listselected",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences1.edit();
        final SharedPreferences.Editor sa = sharedPreferences2.edit();

        for (int i = 0 ; i < Integer.valueOf(sharedPreferences1.getString("sizeoflist","0")); i++ ) {
            editor.remove(String.valueOf(i));
        }
        editor.remove("sizeoflist");
        editor.apply();
        /*sa.remove("a");
        sa.remove("c");
        sa.apply();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Lists.this,Articles.class);
        startActivity(intent);
        finish();
    }
}
