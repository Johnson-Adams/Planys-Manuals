package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.adams.planysmanuals.EditDosNDonts;
import com.awesome.adams.planysmanuals.EditDosNDonts_text;
import com.awesome.adams.planysmanuals.Lists;
import com.awesome.adams.planysmanuals.Login;
import com.awesome.adams.planysmanuals.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class Dos_N_Donts_fragment_text extends android.support.v4.app.Fragment {

    private String key;
    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,data;
    private LinearLayout dol,dontl;

    public Dos_N_Donts_fragment_text(String key, Context context) {
        this.key = key;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View pageone = inflater.inflate(R.layout.fragment_dosndonts_text,container,false);
        dol = (LinearLayout) pageone.findViewById(R.id.dotlinearlayout);
        dontl = (LinearLayout) pageone.findViewById(R.id.donttlinearlayout);
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        data = firebaseDatabase.getReference();
        final int[] k = {0};

        final SharedPreferences sharedPreferences = context.getSharedPreferences("avoid",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("a","0");
        editor.apply();

        databaseReference.child("Contents").child(key).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (sharedPreferences.getString("a","1").equals("0")) {
                    for (int i = 0; i < dataSnapshot.child("dotext").getChildrenCount(); i++) {
                        if (!dataSnapshot.child("dotext").child(String.valueOf(i)).getValue(String.class).equals("") ) {
                            Log.d("dnd_frag_text_do", String.valueOf(i));
                            Log.d("lets check","count");
                            dol.addView(createNewTextView(dataSnapshot.child("dotext").child(String.valueOf(i)).getValue(String.class)));
                        }
                    }

                    for (int j = 0; j < dataSnapshot.child("donttext").getChildrenCount(); j++) {
                        if (!dataSnapshot.child("donttext").child(String.valueOf(j)).getValue(String.class).equals("") ) {
                            Log.d("dnd_frag_text_dont", String.valueOf(j));
                            dontl.addView(createNewTextView(dataSnapshot.child("donttext").child(String.valueOf(j)).getValue(String.class)));
                        }
                    }

                    editor.putString("a","1");
                    editor.apply();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private TextView createNewTextView(String text) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final TextView textView = new TextView(context);
        textView.setLayoutParams(lparams);
        textView.setText("    * "+text);
        textView.setTextColor(getResources().getColor(R.color.actionbar));
        textView.setTextSize(20);
        textView.setPadding(8,8,8,8);
        return textView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.dondontsmenu2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            /*case R.id.addlistmenu:
                Intent intent = new Intent(Lists.this,CreateList.class);
                startActivity(intent);
                finish();
                break;*/

            case R.id.edit_dnd2:
                Intent intent = new Intent(context, EditDosNDonts_text.class);
                final SharedPreferences sharedPreferences = context.getSharedPreferences("dndfile", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("keytoedit",key);
                editor.apply();
                startActivity(intent);
                break;

            case R.id.delete_dnd2:
                databaseReference.child(key).child("basicinfo").child("active").setValue("false");
                databaseReference.child(key).child("basicinfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String list_id = dataSnapshot.child("list_id").getValue(String.class);
                        //Log.d("list_id",list_id);
                        data.child("Lists").child(list_id).child("active").setValue("false");
                        data.child("Lists").child(list_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String type_id = dataSnapshot.child("type_id").getValue(String.class);
                                data.child("List_type").child(type_id).child("active").setValue("false");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent intent1 = new Intent(context, Lists.class);
                startActivity(intent1);
                break;

            case R.id.logoutfromdosndonts2:
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


}
