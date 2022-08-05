package com.awesome.adams.planysmanuals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.util.ByteBufferUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Fragments.Dos_N_Donts_fragment_text;

public class EditDosNDonts_text extends AppCompatActivity {

    private LinearLayout dolayout,dontlayout;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button add_do,add_dont,updatednd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dos_ndonts_text);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        dolayout = (LinearLayout) findViewById(R.id.edit_text_do_layout);
        dontlayout = (LinearLayout) findViewById(R.id.edit_text_dont_layout);

        add_do = (Button) findViewById(R.id.edit_do_add_button);
        add_dont = (Button) findViewById(R.id.edit_dont_add_button);
        updatednd = (Button) findViewById(R.id.edit_dnd_text_update);

        final SharedPreferences sharedPreferences = getSharedPreferences("dndfile", MODE_PRIVATE);
        final String key = sharedPreferences.getString("keytoedit","null");

        final List<edittexts> origin1 = new ArrayList<>();
        final List<edittexts> origin2 = new ArrayList<>();

        databaseReference.child("Contents").child(key).child("data").child("dotext").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    origin1.add(new edittexts(EditDosNDonts_text.this));
                    dolayout.addView(origin1.get(i).createNewEdittext(dataSnapshot.child(String.valueOf(i)).getValue(String.class)));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin1.add(new edittexts((EditDosNDonts_text.this)));
                dolayout.addView(origin1.get(origin1.size()-1).createNewEdittext(String.valueOf("Add Text for Point "+origin1.size())));
            }
        });

        databaseReference.child("Contents").child(key).child("data").child("donttext").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    origin2.add(new edittexts(EditDosNDonts_text.this));
                    dontlayout.addView(origin2.get(i).createNewEdittext(dataSnapshot.child(String.valueOf(i)).getValue(String.class)));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add_dont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin2.add(new edittexts((EditDosNDonts_text.this)));
                dontlayout.addView(origin2.get(origin2.size()-1).createNewEdittext(String.valueOf("Add Text for Point "+origin2.size())));
            }
        });

        updatednd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i<origin1.size();i++) {
                    databaseReference.child("Contents").child(key).child("data").child("dotext").child(String.valueOf(i)).setValue(origin1.get(i).getEditText().getText().toString());
                    dolayout.removeView(origin1.get(i).getEditText());
                }
                for (int i = 0;i<origin2.size();i++) {
                    databaseReference.child("Contents").child(key).child("data").child("donttext").child(String.valueOf(i)).setValue(origin2.get(i).getEditText().getText().toString());
                    dontlayout.removeView(origin2.get(i).getEditText());
                }


                Intent intent = new Intent(EditDosNDonts_text.this,DosNDonts.class);
                Log.d("edit_text_dnd","1");
                startActivity(intent);
                finish();
            }
        });

    }

    public class edittexts extends android.support.v7.widget.AppCompatEditText {

        private EditText editText = new EditText(EditDosNDonts_text.this);

        public edittexts(Context context) {
            super(context);
        }

        @SuppressLint("ResourceAsColor")
        private EditText createNewEdittext(String text) {
            final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            editText.setLayoutParams(lparams);
            editText.setText(text);
            editText.setTextColor(getResources().getColor(R.color.actionbar));
            editText.setTextSize(20);

            editText.setPadding(10,10,10,10);
            return editText;
        }

        public EditText getEditText() {
            return editText;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditDosNDonts_text.this,DosNDonts.class);
        startActivity(intent);
        finish();
    }
}
