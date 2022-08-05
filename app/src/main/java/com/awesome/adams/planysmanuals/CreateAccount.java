package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import javax.sql.StatementEvent;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth mauth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    private EditText empid,emailid,pswd,comfmpswd;
    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        empid = (EditText) findViewById(R.id.createempno);
        emailid = (EditText) findViewById(R.id.createemail);
        pswd = (EditText) findViewById(R.id.createpswd);
        comfmpswd = (EditText) findViewById(R.id.confirmpswd);

        create = (Button) findViewById(R.id.createuserbutton);
        mauth = FirebaseAuth.getInstance();
        user = mauth.getCurrentUser();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id = empid.getText().toString().trim();
                final String email = emailid.getText().toString().trim();
                String password = pswd.getText().toString().trim();
                String confirmpass = comfmpswd.getText().toString().trim();

                //Log.d("reg", String.valueOf(user));
                //Log.d("reg",user.getUid());

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("Userlist");

                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmpass)) {
                    if (password.equals(confirmpass)) {

                        String ends = "@planystech.com";
                        int emaillength = email.length();
                        int endslength = ends.length();

                        /*for (int i = 0; i < (emaillength - endslength); i++) {
                            if (email.regionMatches(true,i,ends,0,endslength)) {

                            }
                        }*/

                        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    mauth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Map<String,String> userdetails = new HashMap<>();
                                            DatabaseReference newuser = databaseReference.child(user.getUid());
                                            userdetails.put("empid",id);
                                            userdetails.put("email",email);
                                            userdetails.put("created_at",String.valueOf(java.lang.System.currentTimeMillis()));
                                            userdetails.put("updated_at",String.valueOf(java.lang.System.currentTimeMillis()));
                                            userdetails.put("active","true");
                                            userdetails.put("admin","true");

                                            newuser.setValue(userdetails);

                                            Toast.makeText(CreateAccount.this,"Account Created! Please verify your mail before logging in",Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(CreateAccount.this, Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                                else
                                    Toast.makeText(CreateAccount.this,"Please create account with valid email and password",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                        Toast.makeText(CreateAccount.this,"Passwords not matching",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(CreateAccount.this,"Please fill all the details",Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateAccount.this,Login.class);
        startActivity(intent);
        finish();
    }
}
