package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateArticle extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button submit;
    private EditText title;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Article");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        final SharedPreferences sharedPreferences = getSharedPreferences("title_into",MODE_PRIVATE);

        title = (EditText) findViewById(R.id.addarticle);
        submit = (Button) findViewById(R.id.submitarticlebutton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newtopic = title.getText().toString().trim();

                String output = newtopic.substring(0, 1).toUpperCase() + newtopic.substring(1);

                DatabaseReference newreference = databaseReference.push();
                Map<String,String> newarticle = new HashMap<>();
                newarticle.put("active","true");
                newarticle.put("created_at",String.valueOf(System.currentTimeMillis()));
                newarticle.put("key",newreference.getKey());
                newarticle.put("name",output);
                newarticle.put("topic_id",sharedPreferences.getString("title","s"));
                newarticle.put("updated_at",String.valueOf(System.currentTimeMillis()));
                newarticle.put("user_id",user.getUid());
                newreference.setValue(newarticle);
                Intent intent = new Intent(CreateArticle.this,Articles.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateArticle.this,Articles.class);
        startActivity(intent);
        finish();
    }
}
