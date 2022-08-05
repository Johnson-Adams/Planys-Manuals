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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPswd extends AppCompatActivity {

    private FirebaseAuth mauth;
    private EditText email;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pswd);

        mauth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.emailforgetpswd);
        button = (Button) findViewById(R.id.submitbuttonforgetpswd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("1","1f");
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(ForgetPswd.this,"Please enter your email",Toast.LENGTH_LONG).show();
                } else {
                    mauth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPswd.this,"Check your mail",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgetPswd.this,Login.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }


            }
        });


    }
}
