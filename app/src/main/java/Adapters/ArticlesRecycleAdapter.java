package Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.awesome.adams.planysmanuals.Lists;
import com.awesome.adams.planysmanuals.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Model.Article;
import Model.ListObject;

import static android.content.Context.MODE_PRIVATE;

public class ArticlesRecycleAdapter extends RecyclerView.Adapter<ArticlesRecycleAdapter.ViewHolder>{
    private Context context;
    private List<Article> articleList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ArticlesRecycleAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticlesRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articlecard,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticlesRecycleAdapter.ViewHolder holder, int position) {
        final Article article = articleList.get(position);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final int i[] = {0};
        databaseReference.child("Lists").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ListObject listObject = dataSnapshot.getValue(ListObject.class);
                if (listObject.getArticle_id().equals(article.getKey()) && listObject.getActive().equals("true"))
                    i[0]++;
                holder.number.setText(String.valueOf(i[0]+" lists"));
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
        holder.article.setText(article.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences sharedPreferences = context.getSharedPreferences("article_into",MODE_PRIVATE);
                SharedPreferences.Editor into = sharedPreferences.edit();
                into.putString("article",article.getKey());
                into.apply();
                Intent intent = new Intent(context, Lists.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView article,number;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            article = (TextView) itemView.findViewById(R.id.article_articlecard);
            number = (TextView) itemView.findViewById(R.id.numberlists);
        }
    }
}
