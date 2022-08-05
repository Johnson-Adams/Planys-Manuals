package Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.awesome.adams.planysmanuals.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Model.Title;

public class EdittitleRecyclerAdapter extends RecyclerView.Adapter<EdittitleRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<Title> titleList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public EdittitleRecyclerAdapter(Context context, List<Title> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public EdittitleRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aview = LayoutInflater.from(parent.getContext()).inflate(R.layout.edittitlecard,parent,false);
        return new ViewHolder(aview,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final EdittitleRecyclerAdapter.ViewHolder holder, int position) {
        final Title title = titleList.get(position);
        holder.title.setText(title.getName());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Topics").child(title.getKey());
        holder.deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("active").setValue("false");
            }
        });

        holder.saveicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getValue(String.class);
                        String head = holder.title.getText().toString();
                        databaseReference.child("name").setValue(head);
                        Log.d("etra","1");
                        Toast.makeText(context,"Title "+ name + " changed to " + head,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private EditText title;
        private ImageView deleteicon,saveicon;
        public ViewHolder(View itemView, Context context) {
            super(itemView);

            title = (EditText) itemView.findViewById(R.id.edittitlefields);
            deleteicon = (ImageView) itemView.findViewById(R.id.deletetitleiconback);
            saveicon = (ImageView) itemView.findViewById(R.id.savetitleiconback);
        }
    }
}
