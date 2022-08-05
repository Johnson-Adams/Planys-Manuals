package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesome.adams.planysmanuals.R;
import com.awesome.adams.planysmanuals.ShowProcess;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class ProcessFragment extends android.support.v4.app.Fragment {

    int i;
    private Context context;
    private ShowProcess.MyViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TextView step,text;
    private ImageView image;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ProcessFragment(int i, Context context, ShowProcess.MyViewPagerAdapter adapter, ViewPager viewPager) {
        this.i = i;
        this.context = context;
        this.adapter = adapter;
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View page = LayoutInflater.from(context).inflate(R.layout.fragment_show_process,container,false);
        step = (TextView) page.findViewById(R.id.process_step_x);
        text = (TextView) page.findViewById(R.id.process_show_text);
        image = (ImageView) page.findViewById(R.id.process_show_image);
        return page;
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        step.setText("Step " + String.valueOf( i + 1 ));

        SharedPreferences sharedPreferences = context.getSharedPreferences("processs", MODE_PRIVATE);
        String content_key = sharedPreferences.getString("process_in_show_process","null");
        Log.d("content key in cp",content_key);

        databaseReference.child("Contents").child(content_key).child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String point = dataSnapshot.child("text").child(String.valueOf(i)).getValue(String.class);
                Log.d("process frag text",point);
                text.setText(point);
                String imgurl = dataSnapshot.child("image").child(String.valueOf(i)).getValue(String.class);
                Picasso.with(context)
                        .load(imgurl)
                        .error(R.color.actionbar)
                        .into(image);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
