package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import Model.ListObject;
import Model.Title;
import Model.listbasicinfo;

public class EditthisTitle extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText name;
    private Button ok,delete;
    private TextView createempid,createdate,updateempid,updatedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editthis_title);

        getSupportActionBar().setTitle("EDIT TOPIC");

        final SharedPreferences sharedPreferences = getSharedPreferences("title_into",MODE_PRIVATE);

        final String title_key = sharedPreferences.getString("title",null);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        name = (EditText) findViewById(R.id.edittitle);
        ok = (Button) findViewById(R.id.submittitlebuttonafteredit);
        createempid = (TextView) findViewById(R.id.titlecreatedbyempid);
        createdate = (TextView) findViewById(R.id.titlecreatedatebyempid);
        updatedate = (TextView) findViewById(R.id.titleupdateddatebyempid);
        updateempid = (TextView) findViewById(R.id.titleupdateddate);
        delete = (Button) findViewById(R.id.deletetitle);

        databaseReference.child("Topics").child(title_key).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String oldname = dataSnapshot.getValue(String.class);
                name.setText(oldname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Topics").child(title_key).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String newtopic = name.getText().toString().trim();

                        String newname = newtopic.substring(0, 1).toUpperCase() + newtopic.substring(1);

                        String oldname = dataSnapshot.getValue(String.class);
                        if (!newname.equals("")) {
                            if (!newname.equals(oldname)) {
                                databaseReference.child("Topics").child(title_key).child("name").setValue(newname);
                                databaseReference.child("Topics").child(title_key).child("updated_at").setValue(String.valueOf(System.currentTimeMillis()));
                                Toast.makeText(EditthisTitle.this, "Topic " + oldname + " changed to " + newname, Toast.LENGTH_SHORT).show();

                            } else {
                                Intent intent = new Intent(EditthisTitle.this, Articles.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Topics").child(title_key).child("active").setValue("false");
                Toast.makeText(EditthisTitle.this,"Topic " + databaseReference.child("Topics").child(title_key).child("name") + "is Deleted",Toast.LENGTH_LONG);
                Intent intent = new Intent(EditthisTitle.this,Titles.class);
                startActivity(intent);
                finish();
            }
        });

        databaseReference.child("Topics").child(title_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Title listbasicinfos = dataSnapshot.getValue(Title.class);
                long createdDate = Long.valueOf(listbasicinfos.getCreated_at());
                long updatedDate = Long.valueOf(listbasicinfos.getUpdated_at());
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = formatter.format(createdDate);
                String formattedDate2 = formatter.format(updatedDate);
                createdate.setText(formattedDate);
                updatedate.setText(formattedDate2);
                if (listbasicinfos.getCreated_at().equals(listbasicinfos.getUpdated_at())) {
                    updatedate.setVisibility(View.INVISIBLE);
                    updateempid.setVisibility(View.INVISIBLE);
                }
                String userid = listbasicinfos.getUserid();
                databaseReference.child("Userlist").child(userid).child("empid").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String empid = dataSnapshot.getValue(String.class);
                        createempid.setText(empid);
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditthisTitle.this,Articles.class);
        startActivity(intent);
        finish();
    }
}
