package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesome.adams.planysmanuals.Articles;
import com.awesome.adams.planysmanuals.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Model.Title;

import static android.content.Context.MODE_PRIVATE;

public class TitleRecyclerAdapter extends RecyclerView.Adapter<TitleRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Title> titleList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public TitleRecyclerAdapter(Context context, List<Title> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public TitleRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aview = LayoutInflater.from(parent.getContext()).inflate(R.layout.titlecard,parent,false);
        return new ViewHolder(aview,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final TitleRecyclerAdapter.ViewHolder holder, int position) {

        final Title titleinstance = titleList.get(position);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final int[] i = {0};

        databaseReference.child("Article").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("topic_id").getValue(String.class).equals(titleinstance.getKey()))
                    i[0]++;
                holder.numberarticles.setText(String.valueOf(i[0]+" articles"));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.title.setText(titleinstance.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("title adapter", String.valueOf(1));
                Intent intent = new Intent(context, Articles.class);
                SharedPreferences sharedPreferences = context.getSharedPreferences("title_into",MODE_PRIVATE);
                SharedPreferences.Editor titlechoosed = sharedPreferences.edit();
                titlechoosed.putString("title",titleinstance.getKey());
                titlechoosed.apply();
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title,numberarticles;
        public ViewHolder(View itemView, Context context) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title_titlecard);
            numberarticles = (TextView) itemView.findViewById(R.id.numberarticles);

        }
    }
}
