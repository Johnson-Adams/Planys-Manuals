package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesome.adams.planysmanuals.CreateProcess;
import com.awesome.adams.planysmanuals.Processs;
import com.awesome.adams.planysmanuals.R;
import com.awesome.adams.planysmanuals.ShowProcess;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Model.listbasicinfo;

import static android.content.Context.MODE_PRIVATE;

public class ProcessRecyclerAdapter extends RecyclerView.Adapter<ProcessRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<listbasicinfo> listbasicinfos;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ProcessRecyclerAdapter(Context context, List<listbasicinfo> listbasicinfos) {
        this.context = context;
        this.listbasicinfos = listbasicinfos;
    }

    @NonNull
    @Override
    public ProcessRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rview  = LayoutInflater.from(context).inflate(R.layout.mainlistcard,parent,false);
        return new ViewHolder(rview,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProcessRecyclerAdapter.ViewHolder holder, int position) {
        listbasicinfo list = listbasicinfos.get(position);
        final String content_key = list.getKey();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Log.d("pra size", String.valueOf(getItemCount()));

        SharedPreferences sharedPreferences = context.getSharedPreferences("processs", MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPreferences.edit();

                databaseReference.child("Contents").child(content_key).child("basicinfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        holder.name.setText(name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                databaseReference.child("Contents").child(content_key).child("data").child("text").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String frag_count = String.valueOf(dataSnapshot.getChildrenCount());
                        holder.number.setText(frag_count);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("process_in_show_process",content_key);
                editor.apply();
                Intent intent = new Intent(context, ShowProcess.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listbasicinfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,number;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.process_name);
            number = (TextView) itemView.findViewById(R.id.processnumber);
        }
    }


}
