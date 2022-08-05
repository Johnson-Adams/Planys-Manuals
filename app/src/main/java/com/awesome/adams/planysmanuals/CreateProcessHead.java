package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateProcessHead extends AppCompatActivity {

    private EditText head;
    private Button back,submit;
    private FirebaseAuth mauth;
    private FirebaseUser muser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_process_head);

        head = (EditText) findViewById(R.id.createprocesshead);
        back = (Button) findViewById(R.id.backfromprocesshead);
        submit  = (Button) findViewById(R.id.submitprocesshead);

        mauth = FirebaseAuth.getInstance();
        muser = mauth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final SharedPreferences sharedPreferences = getSharedPreferences("article_into",MODE_PRIVATE);
        final SharedPreferences sh = getSharedPreferences("processs",MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sh.edit();
        editor2.putString("size", String.valueOf(0));
        editor2.apply();
        final String article_id = sharedPreferences.getString("article","null");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heading = head.getText().toString().trim();
                if (!TextUtils.isEmpty(heading)) {
                    SharedPreferences sharedPreferences2 = getSharedPreferences("process", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences2.edit();
                    editor.putString("head", heading);
                    editor.apply();

                    DatabaseReference newtype = databaseReference.child("List_type").push();
                    Map<String,String> post2 = new HashMap<>();
                    post2.put("active","false");
                    post2.put("count_id","0");
                    post2.put("created_at", String.valueOf(System.currentTimeMillis()));
                    post2.put("key",newtype.getKey());
                    post2.put("updated_at", String.valueOf(System.currentTimeMillis()));
                    post2.put("user_id",muser.getUid());
                    newtype.setValue(post2);

                    DatabaseReference newprocess = databaseReference.child("Lists").push();
                    Map<String,String> post = new HashMap<>();
                    post.put("active","false");
                    post.put("article_id",article_id);
                    post.put("count_id","0");
                    post.put("created_at", String.valueOf(System.currentTimeMillis()));
                    post.put("key",newprocess.getKey());
                    post.put("name","process");
                    post.put("type","2");
                    post.put("type_id",newtype.getKey());
                    post.put("updated_at", String.valueOf(System.currentTimeMillis()));
                    post.put("user_id",muser.getUid());
                    newprocess.setValue(post);

                    DatabaseReference news = databaseReference.child("Contents").push();
                    DatabaseReference basic = news.child("basicinfo");
                    Map<String,String> adddo = new HashMap<>();
                    adddo.put("type_id","2");
                    adddo.put("list_id",newprocess.getKey());
                    adddo.put("key",news.getKey());
                    adddo.put("active","false");
                    adddo.put("created_at", String.valueOf(System.currentTimeMillis()));
                    adddo.put("updated_at", String.valueOf(System.currentTimeMillis()));
                    adddo.put("user_id",muser.getUid());
                    adddo.put("name",head.getText().toString().trim());
                    basic.setValue(adddo);

                    DatabaseReference data = news.child("data");

                    Map<String,String> adddata = new HashMap<>();
                    adddata.put("type","image");
                    data.setValue(adddata);

                    DatabaseReference imagedb = data.child("image");
                    Map<String,String> image = new HashMap<>();
                    image.put("0", "Pls add");
                    imagedb.setValue(image);

                    DatabaseReference textdb = data.child("text");
                    Map<String,String> text = new HashMap<>();
                    text.put("0","Add Text Here");
                    textdb.setValue(text);

                    DatabaseReference dnddo = data.child("dotext");
                    Map<String,String> nt = new HashMap<>();
                    nt.put("0","null");
                    dnddo.setValue(nt);

                    DatabaseReference dnddont = data.child("donttext");
                    Map<String,String> nt1 = new HashMap<>();
                    nt1.put("0","null");
                    dnddont.setValue(nt1);

                    editor.putString("process_in_process",news.getKey());
                    editor.apply();

                    Intent intent = new Intent(CreateProcessHead.this, CreateProcess.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProcessHead.this,Processs.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateProcessHead.this,Processs.class);
        startActivity(intent);
        finish();
    }
}
