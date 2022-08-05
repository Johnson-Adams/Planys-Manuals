package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.awesome.adams.planysmanuals.CreateTut;
import com.awesome.adams.planysmanuals.R;
import com.awesome.adams.planysmanuals.Tutorial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Fragments.ShowTutorial;
import Model.ListObject;
import Model.listbasicinfo;

public class TutorialRecyclerAdapter extends RecyclerView.Adapter<TutorialRecyclerAdapter.ViewHolder> {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<listbasicinfo> list;
    private Context context;
    private ViewPager v;
    private Tutorial.MyViewPagerAdapter adapter;
    private FirebaseAuth mauth;

    public TutorialRecyclerAdapter(List<listbasicinfo> list, Context context,ViewPager v, Tutorial.MyViewPagerAdapter adapter) {
        this.list = list;
        this.context = context;
        this.v = v;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rview = LayoutInflater.from(context).inflate(R.layout.mainlistcard,parent,false);
        return new ViewHolder(rview,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final listbasicinfo info = list.get(position);

        holder.head.setText(info.getName());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mauth = FirebaseAuth.getInstance();

        databaseReference.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("admin").equals("false"))
                    holder.plus.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SharedPreferences sharedPreferences = context.getSharedPreferences("Tutorial", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("key_selected", info.getKey());
                editor.apply();*/
                kjkbb(info.getKey());

            }
        });

    }

    public void kjkbb(String content_key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Tutorial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key_selected",content_key);
        editor.apply();
        Log.d("TutRecAdr", String.valueOf(1));
        ShowTutorial showTutorial = new ShowTutorial(context,content_key,v);
        if (adapter.getCount() == 2) {
            adapter.RemoveFragmentPage(new ShowTutorial(context));
        }
        adapter.AddFragmentPage(showTutorial);
        v.setAdapter(adapter);
        v.setCurrentItem(1);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView head;
        private Button plus;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            plus = (Button) itemView.findViewById(R.id.tut_plus);
            head = (TextView) itemView.findViewById(R.id.process_name);
        }
    }
}
