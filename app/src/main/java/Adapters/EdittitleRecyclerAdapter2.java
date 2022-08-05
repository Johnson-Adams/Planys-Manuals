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

import com.awesome.adams.planysmanuals.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Model.Title;

public class EdittitleRecyclerAdapter2 extends RecyclerView.Adapter<EdittitleRecyclerAdapter2.ViewHolder>{

    private Context context;
    private List<Title> titleList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public EdittitleRecyclerAdapter2(Context context, List<Title> titleList) {
        this.context = context;
        this.titleList = titleList;
    }

    @NonNull
    @Override
    public EdittitleRecyclerAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View aview = LayoutInflater.from(parent.getContext()).inflate(R.layout.edittitlecard,parent,false);
        return new ViewHolder(aview,context);
    }

    @Override
    public void onBindViewHolder(@NonNull EdittitleRecyclerAdapter2.ViewHolder holder, int position) {
        Title title = titleList.get(position);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Topics").child(title.getKey());
        String name = holder.title.getText().toString();
        Log.d("name",name);

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private EditText title;
        public ViewHolder(View itemView, Context context) {
            super(itemView);

            title = (EditText) itemView.findViewById(R.id.edittitlefields);
        }
    }
}
