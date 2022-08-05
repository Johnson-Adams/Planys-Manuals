package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.ref.Reference;

public class Login extends AppCompatActivity {

    private FirebaseAuth mauth;
    private FirebaseUser user;
    private EditText emailid, pswd;
    private Button loginbutton;
    private ImageView createaccount, forgetpswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailid = (EditText) findViewById(R.id.loginemail);
        pswd = (EditText) findViewById(R.id.loginpswd);
        loginbutton = (Button) findViewById(R.id.loginbutton);
        createaccount = (ImageView) findViewById(R.id.logincreateaccount);
        forgetpswd = (ImageView) findViewById(R.id.loginforgetpswd);
        mauth = FirebaseAuth.getInstance();


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailid.getText().toString().trim();
                String pswdstr = pswd.getText().toString().trim();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pswdstr)) {

                    SharedPreferences mypref = getSharedPreferences("rem_user", MODE_PRIVATE);
                    SharedPreferences.Editor saver = mypref.edit();
                    saver.putString("rem_useremail", email);
                    saver.putString("rem_pass", pswdstr);
                    saver.apply();

                    mauth.signInWithEmailAndPassword(email, pswdstr).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, Titles.class);
                                startActivity(intent);
                            } else
                                Toast.makeText(Login.this, "Couldn't login, please check your entries", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    Toast.makeText(Login.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            }
        });

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
            }
        });

        forgetpswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgetPswd.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();
    }
}



