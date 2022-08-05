package com.awesome.adams.planysmanuals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Handler delay = new Handler();

    private FirebaseAuth auth;

    public static final String PREFS_NAME = "MyPrefsFile";
    private Intent intents;

    String useremail = null,password = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("la","1");
                SharedPreferences prefs = getSharedPreferences("rem_user",MODE_PRIVATE);
                Log.d("useremail",prefs.getString("rem_useremail","null"));
                Log.d("la","2");
                useremail = prefs.getString("rem_useremail",null);
                password = prefs.getString("rem_pass",null);
                Log.d("la","3");

                if (useremail != null && password != null) {
                    Log.d("la","4");
                    auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(useremail,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                intents = new Intent(MainActivity.this,Titles.class);
                                startActivity(intents);
                            }
                            else
                            {
                                intents = new Intent(MainActivity.this,Titles.class);
                                startActivity(intents);
                            }

                        }
                    });
                } else {
                    Log.d("la","5");
                    intents= new Intent(MainActivity.this, Login.class);
                    Log.d("la","6");
                    startActivity(intents);
                    Log.d("la","7") ;
                    finish();
                    Log.d("la","8");
                }
            }
        } , 2500);


    }
}
