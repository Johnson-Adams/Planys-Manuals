package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.adams.planysmanuals.Articles;
import com.awesome.adams.planysmanuals.DosNDonts;
import com.awesome.adams.planysmanuals.EditDosNDonts;
import com.awesome.adams.planysmanuals.EditthisTitle;
import com.awesome.adams.planysmanuals.Lists;
import com.awesome.adams.planysmanuals.Login;
import com.awesome.adams.planysmanuals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import Model.listbasicinfo;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class DosNDonts_fragment extends android.support.v4.app.Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference data,databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    int i,j=0,m=0;
    private Context context;
    private ImageView do_img,dont_img;
    private TextView do_txt,dont_txt;
    int size = 1;
    private String key;
    private FirebaseAuth mauth;

    public DosNDonts_fragment(int i, Context contexts) {
        this.i = i;
        context = contexts;
    }

    public DosNDonts_fragment(String key, Context context) {
        this.key = key;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View pageone = inflater.inflate(R.layout.fragment_dosndonts,container,false);
        do_img = (ImageView) pageone.findViewById(R.id.dnd_do_img);
        dont_img = (ImageView) pageone.findViewById(R.id.dnd_dont_img);
        do_txt = (TextView) pageone.findViewById(R.id.dnd_do_text);
        dont_txt = (TextView) pageone.findViewById(R.id.dnd_dont_text);

        return pageone;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        final SharedPreferences sharedPreferences = context.getSharedPreferences("dndfile", MODE_PRIVATE);
        final SharedPreferences.Editor dnd = sharedPreferences.edit();
        dnd.apply();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Contents");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        databaseReference.child(key).child("data").child("image").child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                Log.d("do_url",url);
                Picasso.with(context)
                        .load(url)
                        .into(do_img);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child(key).child("data").child("image").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                Log.d("dont_url",url);
                Picasso.with(context)
                        .load(url)
                        .into(dont_img);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child(key).child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dotext = dataSnapshot.child("dotext").child("0").getValue(String.class);
                String donttext = dataSnapshot.child("donttext").child("0").getValue(String.class);
                do_txt.setText(dotext);
                dont_txt.setText(donttext);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                m = 1;

                if (j == i) {
                    Log.d("key", dataSnapshot.getKey());
                    Log.d("dnd+frag", "1");
                    Log.d("dnd_frag_child_count", String.valueOf(dataSnapshot.getChildrenCount()));

                    databaseReference.child(dataSnapshot.getKey()).child("basicinfo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            listbasicinfo infos = dataSnapshot.getValue(listbasicinfo.class);
                            if (infos.getType_id().equals(String.valueOf(1))) {
                                databaseReference.child(dataSnapshot.getKey()).child("data").child("image").child("0").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.d("dnd+frag","2");
                                        //dnd.putString("image " + String.valueOf(finalK), dataSnapshot.getValue(String.class));
                                        //dnd.apply();
                                        //Log.d("image url",sharedPreferences.getString("image "+String.valueOf(finalK),"null"));
                                        Picasso.with(context).load(dataSnapshot.getValue(String.class)).into(do_img);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                databaseReference.child(dataSnapshot.getKey()).child("data").child("image").child("1").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.d("dnd+frag","2.1");
                                        //dnd.putString("image " + String.valueOf(finalK), dataSnapshot.getValue(String.class));
                                        //dnd.apply();
                                        //Log.d("image url",sharedPreferences.getString("image "+String.valueOf(finalK),"null"));
                                        Picasso.with(context).load(dataSnapshot.getValue(String.class)).into(dont_img);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                for (int k = 0; k < size; k++) {

                                    final int finalK = k;

                                    databaseReference.child(dataSnapshot.getKey()).child("data").child("dotext").child(String.valueOf(k)).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            size = (int) dataSnapshot.getChildrenCount();
                                            Log.d("size of dotext", String.valueOf(dataSnapshot.getChildrenCount()));
                                            dnd.putString("dotext size", String.valueOf(dataSnapshot.getChildrenCount()));
                                            dnd.putString("dotext " + String.valueOf(finalK), dataSnapshot.getValue(String.class));
                                            dnd.apply();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                                for (int k = 0; k < size; k++) {

                                    final int finalK = k;

                                    databaseReference.child(dataSnapshot.getKey()).child("data").child("donttext").child(String.valueOf(k)).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            size = (int) dataSnapshot.getChildrenCount();
                                            Log.d("size of donttext",String.valueOf(dataSnapshot.getChildrenCount()));
                                            dnd.putString("donttext size", String.valueOf(dataSnapshot.getChildrenCount()));
                                            dnd.putString("donttext " + String.valueOf(finalK), dataSnapshot.getValue(String.class));
                                            dnd.apply();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
                j++;
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

        /*if (m  == 1) {
            Log.d("dnd+frag", "4");
            //Picasso.with(context).load(sharedPreferences.getString("image " + String.valueOf(0), null)).into(do_img);

            Log.d("image url 0", sharedPreferences.getString("image " + String.valueOf(0), "null"));

            Picasso.with(context).load(sharedPreferences.getString("image " + String.valueOf(1), null)).into(dont_img);

            do_txt.setText(sharedPreferences.getString(dotextsend(), null));

            dont_txt.setText(sharedPreferences.getString(donttextsend(), null));
        }*/

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

                data = firebaseDatabase.getReference();
                mauth  = FirebaseAuth.getInstance();

                data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("admin").getValue(String.class).equals("true")) {
                            Intent intent = new Intent(context, EditDosNDonts.class);
                            final SharedPreferences sharedPreferences = context.getSharedPreferences("dndfile", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("keytoedit",key);
                            editor.apply();
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

                data = firebaseDatabase.getReference();
                mauth  = FirebaseAuth.getInstance();

                data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("admin").getValue(String.class).equals("true")) {
                            databaseReference.child(key).child("basicinfo").child("active").setValue("false");
                            Intent intent1 = new Intent(context, Lists.class);
                            startActivity(intent1);
                        }
                        else
                            Toast.makeText(context,"Get Admin Status to delete",Toast.LENGTH_SHORT).show();

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
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.dondontsmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
