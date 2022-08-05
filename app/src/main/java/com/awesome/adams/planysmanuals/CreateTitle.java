package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.gms.flags.impl.DataUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateTitle extends AppCompatActivity {

    private FirebaseDatabase mdatabase;
    private DatabaseReference mdatabasereference;
    private FirebaseAuth mauth;
    private FirebaseUser muser;
    private EditText title;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_title);

        mauth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        mdatabasereference = mdatabase.getReference().child("Topics");
        muser = mauth.getCurrentUser();

        title = (EditText) findViewById(R.id.addtopic);
        submit = (Button) findViewById(R.id.submittopicbutton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newtopic = title.getText().toString().trim();

                String output = newtopic.substring(0, 1).toUpperCase() + newtopic.substring(1);

                mdatabasereference = mdatabasereference.push();

                if (!TextUtils.isEmpty(newtopic)) {
                    Map<String, String> addtitle = new HashMap<>();
                    addtitle.put("active", "true");
                    addtitle.put("name", output);
                    addtitle.put("numberarticles", "0");
                    addtitle.put("key",mdatabasereference.getKey());
                    addtitle.put("userid", muser.getUid());
                    addtitle.put("created_at", String.valueOf(System.currentTimeMillis()));
                    addtitle.put("updated_at", String.valueOf(System.currentTimeMillis()));
                    mdatabasereference.setValue(addtitle);
                    Intent intent = new Intent(CreateTitle.this,Titles.class);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(CreateTitle.this,"Topic shouldn't be empty",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateTitle.this,Titles.class);
        startActivity(intent);
        finish();
    }
}
