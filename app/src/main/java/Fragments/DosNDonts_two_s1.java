package Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awesome.adams.planysmanuals.DosNDonts;
import com.awesome.adams.planysmanuals.DosNDonts_two;
import com.awesome.adams.planysmanuals.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class DosNDonts_two_s1 extends android.support.v4.app.Fragment {

    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton,nButton;
    private Context mcontext;
    private ViewPager mviewpager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference2,databaseReference3;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private int i = 0;

    public DosNDonts_two_s1() {
    }

    @SuppressLint("ValidFragment")
    public DosNDonts_two_s1(Context context, ViewPager viewPager) {
        mcontext = context;
        mviewpager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View pageone = inflater.inflate(R.layout.fragment_dosndonts2_s1,container,false);
        mLayout = (LinearLayout) pageone.findViewById(R.id.dosscreatelinearlayout);
        mEditText = (EditText) pageone.findViewById(R.id.dosndontstextfragadd);
        mButton = (Button) pageone.findViewById(R.id.adddostextpoint);
        nButton = (Button) pageone.findViewById(R.id.slidetotextdont);

        SharedPreferences sharedPreferences0 = mcontext.getSharedPreferences("dnd_do_text", MODE_PRIVATE);
        sharedPreferences0.getAll().clear();
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("dnd_dont_text", MODE_PRIVATE);
        sharedPreferences.getAll().clear();


        return pageone;
    }

    @Override
    public void onStart() {
        super.onStart();

        final SharedPreferences sharedPreferences2 = mcontext.getSharedPreferences("article_into",MODE_PRIVATE);

        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("Contents");
        databaseReference2 = firebaseDatabase.getReference().child("List_type");
        databaseReference3 = firebaseDatabase.getReference().child("Lists");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        databaseReference2 = databaseReference2.push();
        databaseReference3 = databaseReference3.push();

        DatabaseReference news = databaseReference.push();
        DatabaseReference basic = news.child("basicinfo");
        Map<String,String> adddo = new HashMap<>();
        adddo.put("type_id","1");
        adddo.put("list_id",databaseReference3.getKey());
        adddo.put("key",news.getKey());
        adddo.put("name","Do's and Dont's");
        adddo.put("active","false");
        adddo.put("created_at", String.valueOf(System.currentTimeMillis()));
        adddo.put("updated_at", String.valueOf(System.currentTimeMillis()));
        adddo.put("user_id",user.getUid());
        basic.setValue(adddo);

        DatabaseReference data = news.child("data");

        DatabaseReference imagedb = data.child("image");
        Map<String,String> image = new HashMap<>();
        image.put("0", "null");
        image.put("1", "null");
        imagedb.setValue(image);
        DatabaseReference textdb = data.child("text");
        Map<String,String> text = new HashMap<>();
        text.put("0","null");
        textdb.setValue(text);

        DatabaseReference dnddo = data.child("dotext");
        Map<String,String> nt = new HashMap<>();
        nt.put("0","null");
        dnddo.setValue(nt);
        DatabaseReference dnddont = data.child("donttext");
        Map<String,String> nt1 = new HashMap<>();
        nt1.put("0","null");
        dnddont.setValue(nt1);

        data.child("type").setValue("text");

        Map<String,String> listtypes = new HashMap<>();
        listtypes.put("count_id","0");
        listtypes.put("active","false");
        listtypes.put("created_at",String.valueOf(System.currentTimeMillis()));
        listtypes.put("updated_at",String.valueOf(System.currentTimeMillis()));
        listtypes.put("key",databaseReference2.getKey());
        listtypes.put("user_id",user.getUid());
        databaseReference2.setValue(listtypes);

        Map<String,String> addlist = new HashMap<>();
        addlist.put("count_id","0");
        addlist.put("type","1");
        addlist.put("type_id",databaseReference2.getKey());
        addlist.put("article_id",sharedPreferences2.getString("article","s"));
        addlist.put("active","false");
        addlist.put("user_id",user.getUid());
        addlist.put("created_at",String.valueOf(System.currentTimeMillis()));
        addlist.put("updated_at",String.valueOf(System.currentTimeMillis()));
        addlist.put("name","dos and donts");
        addlist.put("key",databaseReference3.getKey());
        databaseReference3.setValue(addlist);

        SharedPreferences s = mcontext.getSharedPreferences("dnd",MODE_PRIVATE);
        SharedPreferences.Editor editor = s.edit();
        editor.putString("dnd_contentkey_creating",news.getKey());
        editor.putString("dnd_listkey_creating",databaseReference3.getKey());
        editor.putString("dnd_listtypekey_creating",databaseReference2.getKey());
        editor.apply();

        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("dnd_do_text",Context.MODE_PRIVATE);
        SharedPreferences.Editor sav = sharedPreferences.edit();
        sav.putInt("size",0);
        sav.apply();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.addView(createNewTextView(mEditText.getText().toString()));
                mEditText.setText("");
            }
        });
        TextView textView = new TextView(mcontext);
        textView.setText("New text");

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*android.support.v4.app.Fragment fragment = new DosNDonts_two_s2(mcontext);
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.abcd, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
                mviewpager.setCurrentItem(1);
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    private TextView createNewTextView(String text) {
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("dnd_do_text",Context.MODE_PRIVATE);
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

        DatabaseReference dnddo = data.child("dotext");
        dnddo.child(String.valueOf(sharedPreferences.getInt("size",1) - 1 )).setValue(text);

        return textView;
    }

}
