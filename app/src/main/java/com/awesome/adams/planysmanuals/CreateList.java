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

public class CreateList extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference2;
    private EditText topic;
    private Button submit;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Lists");
        databaseReference2 = firebaseDatabase.getReference().child("List_type");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        topic = (EditText) findViewById(R.id.addlist);
        submit = (Button) findViewById(R.id.submitlistbutton);
        final SharedPreferences sharedPreferences = getSharedPreferences("count_list",MODE_PRIVATE);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("article_into",MODE_PRIVATE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference2 = databaseReference2.push();
                Map<String,String> listtypes = new HashMap<>();
                listtypes.put("count_id",String.valueOf(sharedPreferences.getInt("noofart",0)));
                listtypes.put("active","true");
                listtypes.put("created_at",String.valueOf(System.currentTimeMillis()));
                listtypes.put("updated_at",String.valueOf(System.currentTimeMillis()));
                listtypes.put("key",databaseReference2.getKey());
                listtypes.put("user_id",user.getUid());
                databaseReference2.setValue(listtypes);

                databaseReference = databaseReference.push();
                Map<String,String> addlist = new HashMap<>();
                addlist.put("count_id",String.valueOf(sharedPreferences.getInt("noofart",0)));
                addlist.put("type_id",databaseReference2.getKey());
                addlist.put("article_id",sharedPreferences2.getString("article","s"));
                addlist.put("active","true");
                addlist.put("user_id",user.getUid());
                addlist.put("created_at",String.valueOf(System.currentTimeMillis()));
                addlist.put("updated_at",String.valueOf(System.currentTimeMillis()));
                addlist.put("name",topic.getText().toString());
                addlist.put("key",databaseReference.getKey());
                databaseReference.setValue(addlist);

                Intent intent = new Intent(CreateList.this, Lists.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
