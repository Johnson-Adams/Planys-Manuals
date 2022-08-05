package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.awesome.adams.planysmanuals.CreateTut;
import com.awesome.adams.planysmanuals.EditTutorial;
import com.awesome.adams.planysmanuals.Lists;
import com.awesome.adams.planysmanuals.Login;
import com.awesome.adams.planysmanuals.R;
import com.awesome.adams.planysmanuals.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.TutorialRecyclerAdapter;
import Model.ListObject;
import Model.listbasicinfo;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class TutorialList extends android.support.v4.app.Fragment{

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,data;
    private List<listbasicinfo> list_set;
    private FirebaseAuth mauth;
    private RecyclerView rview;
    private ViewPager viewPager;
    private Button plus;
    private Tutorial.MyViewPagerAdapter adapter;

    public TutorialList(Context context, ViewPager viewPager, Tutorial.MyViewPagerAdapter adapter) {
        this.context = context;
        this.viewPager = viewPager;
        this.adapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_tutorial_list,container,false);
        rview = (RecyclerView) view.findViewById(R.id.tut_list_rec_view);
        rview.setHasFixedSize(true);
        rview.setLayoutManager(new LinearLayoutManager(context));
        plus = (Button) view.findViewById(R.id.tut_plus);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        list_set = new ArrayList<>();

        databaseReference.child("Contents").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String content_key = dataSnapshot.getKey();
                databaseReference.child("Contents").child(content_key).child("basicinfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final listbasicinfo listbasicinfos = dataSnapshot.getValue(listbasicinfo.class);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("article_into",MODE_PRIVATE);
                        final String article_id_inside = sharedPreferences.getString("article","null");
                        if (listbasicinfos.getType_id().equals("3")) {
                            databaseReference.child("Lists").child(listbasicinfos.getList_id()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("article_id").getValue(String.class).equals(article_id_inside)) {
                                        list_set.add(listbasicinfos);
                                        TutorialRecyclerAdapter adapters = new TutorialRecyclerAdapter(list_set,context,viewPager,adapter);
                                        rview.setAdapter(adapters);
                                        adapters.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

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

        data = firebaseDatabase.getReference();
        mauth = FirebaseAuth.getInstance();

        Log.d("mauth",mauth.getCurrentUser().getUid());

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

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateTut.class);
                startActivity(intent);
            }
        });
    }

    public boolean article(String list_id) {
        final int[] i = {1};
        final SharedPreferences sharedPreferences = context.getSharedPreferences("article_into",MODE_PRIVATE);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            /*case R.id.addlistmenu:
                Intent intent = new Intent(Lists.this,CreateList.class);
                startActivity(intent);
                finish();
                break;*/

            case R.id.edit_dnd:
                mauth = FirebaseAuth.getInstance();
                data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("admin").getValue(String.class).equals("true")) {
                            Intent intent = new Intent(context, EditTutorial.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(context,"Get Admin Status to edit",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case R.id.delete_dnd:
                data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("admin").getValue(String.class).equals("true")) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("Tutorial",Context.MODE_PRIVATE);
                            databaseReference.child(sharedPreferences.getString("key_selected","null")).child("basicinfo").child("active").setValue("false");
                            Intent intent1 = new Intent(context, Lists.class);
                            startActivity(intent1);
                        }
                        else
                            Toast.makeText(context,"Get Admin Status to edit",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case R.id.logoutfromdosndonts:
                Intent intent2 = new Intent(context,Login.class);
                SharedPreferences prefs = context.getSharedPreferences("rem_user",MODE_PRIVATE);
                SharedPreferences.Editor changer = prefs.edit();
                changer.putString("rem_useremail",null);
                changer.putString("rem_pass",null);
                changer.apply();
                startActivity(intent2);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dondontsmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
