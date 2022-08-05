package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import Model.Article;
import Model.Title;

public class EditthisArticle extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText name;
    private Button ok,delete;
    private TextView createempid,createdate,updateempid,updatedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editthis_article);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        name = (EditText) findViewById(R.id.editarticle);
        ok = (Button) findViewById(R.id.submitarticlebuttonafteredit);
        createempid = (TextView) findViewById(R.id.articlecreatedbyempid);
        createdate = (TextView) findViewById(R.id.articlecreatedatebyempid);
        delete = (Button) findViewById(R.id.deletearticle);

        updatedate = (TextView) findViewById(R.id.articleupdateddatebyempid);

        final SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);

        final String article_key = sharedPreferences.getString("article","null");

        databaseReference.child("Article").child(article_key).child("name").addValueEventListener(new ValueEventListener() {
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

                databaseReference.child("Article").child(article_key).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String newtopic = name.getText().toString().trim();

                        String newname = newtopic.substring(0, 1).toUpperCase() + newtopic.substring(1);
                        String oldname = dataSnapshot.getValue(String.class);
                        if (!newname.equals("")) {
                            if (!newname.equals(oldname)) {
                                databaseReference.child("Article").child(article_key).child("name").setValue(newname);
                                databaseReference.child("Article").child(article_key).child("updated_at").setValue(String.valueOf(System.currentTimeMillis()));
                                Toast.makeText(EditthisArticle.this, "Article " + oldname + " changed to " + newname, Toast.LENGTH_SHORT).show();

                            } else {
                                Intent intent = new Intent(EditthisArticle.this, Articles.class);
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
                databaseReference.child("Articles").child(article_key).child("active").setValue("false");
                Toast.makeText(EditthisArticle.this,"Article " + databaseReference.child("Articles").child(article_key).child("name") + "is Deleted",Toast.LENGTH_LONG);
                Intent intent = new Intent(EditthisArticle.this,Articles.class);
                startActivity(intent);
                finish();
            }
        });

        databaseReference.child("Article").child(article_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Article listbasicinfos = dataSnapshot.getValue(Article.class);
                long createdDate = Long.valueOf(listbasicinfos.getCreated_at());
                long updatedDate = Long.valueOf(listbasicinfos.getUpdated_at());
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = formatter.format(createdDate);
                String formattedDate2 = formatter.format(updatedDate);
                createdate.setText(formattedDate);
                updatedate.setText(formattedDate2);
                if (listbasicinfos.getCreated_at().equals(listbasicinfos.getUpdated_at())) {
                    updatedate.setVisibility(View.INVISIBLE);
                }
                String userid = listbasicinfos.getUser_id();
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
        Intent intent = new Intent(EditthisArticle.this,Lists.class);
        startActivity(intent);
        finish();
    }
}
