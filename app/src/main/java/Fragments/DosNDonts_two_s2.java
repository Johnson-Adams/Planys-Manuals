package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.adams.planysmanuals.DosNDonts;
import com.awesome.adams.planysmanuals.DosNDonts_one;
import com.awesome.adams.planysmanuals.DosNDonts_two;
import com.awesome.adams.planysmanuals.Lists;
import com.awesome.adams.planysmanuals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class DosNDonts_two_s2 extends android.support.v4.app.Fragment {

    private Context mcontext;
    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton,nButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference2,databaseReference3;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private int i=0;

    public DosNDonts_two_s2() {
    }

    @SuppressLint("ValidFragment")


    public DosNDonts_two_s2(Context context) {
        mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View pageone = inflater.inflate(R.layout.fragment_dosndonts2_s2,container,false);
        mLayout = (LinearLayout) pageone.findViewById(R.id.dontscreatelinearlayout);
        mEditText = (EditText) pageone.findViewById(R.id.dosndontstextfragadd);
        mButton = (Button) pageone.findViewById(R.id.createdndtext);
        nButton = (Button) pageone.findViewById(R.id.adddontstextpoint);
        return pageone;
    }

    @Override
    public void onStart() {
        super.onStart();

        final SharedPreferences sharedPreferences0 = mcontext.getSharedPreferences("dnd_do_text", MODE_PRIVATE);
        final SharedPreferences sharedPreferences = mcontext.getSharedPreferences("dnd_dont_text", MODE_PRIVATE);
        final SharedPreferences sharedPreferences3 = mcontext.getSharedPreferences("count_list",MODE_PRIVATE);
        final SharedPreferences sharedPreferences2 = mcontext.getSharedPreferences("article_into",MODE_PRIVATE);

        SharedPreferences.Editor sav = sharedPreferences.edit();
        sav.putInt("size",0);
        sav.apply();

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = firebaseDatabase.getReference().child("Contents");
        databaseReference2 = firebaseDatabase.getReference().child("List_type");
        databaseReference3 = firebaseDatabase.getReference().child("Lists");

        nButton.setOnClickListener(onClick());
        TextView textView = new TextView(mcontext);
        textView.setText("New text");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences s = mcontext.getSharedPreferences("dnd",MODE_PRIVATE);
                databaseReference.child(s.getString("dnd_contentkey_creating","s")).child("basicinfo").child("active").setValue("true");
                databaseReference3.child(s.getString("dnd_listkey_creating","s")).child("active").setValue("true");
                databaseReference2.child(s.getString("dnd_listtypekey_creating","s")).child("active").setValue("true");

                Toast.makeText(mcontext,"Content Added",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mcontext,Lists.class);
                startActivity(intent);

            }
        });
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mLayout.addView(createNewTextView(mEditText.getText().toString()));
                mEditText.setText("");
            }
        };
    }

    @SuppressLint("ResourceAsColor")
    private TextView createNewTextView(String text) {
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("dnd_dont_text", MODE_PRIVATE);
        SharedPreferences.Editor sav = sharedPreferences.edit();

        sav.putInt("size",sharedPreferences.getInt("size",0)+1);
        sav.putString(String.valueOf(sharedPreferences.getInt("size",1)-1),text);
        sav.apply();
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(mcontext);
        textView.setLayoutParams(lparams);
        textView.setText("Point "+ sharedPreferences.getInt("size",1)+ " : " + text);
        textView.setTextColor(R.color.colorPrimaryDark);
        textView.setTextSize(20);

        textView.setPadding(8,8,8,8);

        SharedPreferences s = mcontext.getSharedPreferences("dnd",MODE_PRIVATE);

        DatabaseReference news = databaseReference.child(s.getString("dnd_contentkey_creating","s"));

        DatabaseReference data = news.child("data");

        DatabaseReference dnddo = data.child("donttext");
        dnddo.child(String.valueOf(sharedPreferences.getInt("size",1) - 1 )).setValue(text);
        return textView;
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences0 = mcontext.getSharedPreferences("dnd_do_text", MODE_PRIVATE);
        sharedPreferences0.getAll().clear();
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("dnd_dont_text", MODE_PRIVATE);
        sharedPreferences.getAll().clear();
    }
}
