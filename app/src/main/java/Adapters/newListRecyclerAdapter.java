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

import com.awesome.adams.planysmanuals.DosNDonts;
import com.awesome.adams.planysmanuals.Processs;
import com.awesome.adams.planysmanuals.R;
import com.awesome.adams.planysmanuals.Tutorial;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Model.ListObject;
import Model.listbasicinfo;

import static android.content.Context.MODE_PRIVATE;

public class newListRecyclerAdapter extends RecyclerView.Adapter<newListRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<String> newlist;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private static final String TAG = "newListRecyclerAdapter";

    public newListRecyclerAdapter(Context context, List<String> newlist) {
        this.context = context;
        this.newlist = newlist;
    }

    @NonNull
    @Override
    public newListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View neew = LayoutInflater.from(context).inflate(R.layout.articlecard,parent,false);
        return new ViewHolder(neew,context);
    }

    @Override
    public void onBindViewHolder(@NonNull final newListRecyclerAdapter.ViewHolder holder, final int position) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final int[] count = {0};
        final SharedPreferences sharedPreferences = context.getSharedPreferences("article_into",MODE_PRIVATE);
        final String article_key = sharedPreferences.getString("article","null");

        if (position == 0) {
            holder.name.setText("Do's and Dont's");
            databaseReference.child("Contents").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listbasicinfo listbasicinfos = dataSnapshot.child("basicinfo").getValue(listbasicinfo.class);
                    String list_id = listbasicinfos.getList_id();
                    Log.d("nlra list_id",list_id);
                    if (listbasicinfos.getType_id().equals("1") && dataSnapshot.child("basicinfo").child("active").getValue(String.class).equals("true"))
                        databaseReference.child("Lists").child(list_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ListObject object = dataSnapshot.getValue(ListObject.class);
                                if (object.getArticle_id().equals(article_key))
                                    count[0]++;
                                if (count[0] == 0)
                                    holder.num.setText("no lists");
                                else if (count[0] == 1)
                                    holder.num.setText(count[0] + " list");
                                else
                                    holder.num.setText(count[0] + " lists");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
        } else if (position == 1) {
            holder.name.setText("Process");
            databaseReference.child("Contents").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listbasicinfo listbasicinfos = dataSnapshot.child("basicinfo").getValue(listbasicinfo.class);
                    String list_id = listbasicinfos.getList_id();
                    if (listbasicinfos.getType_id().equals("2") && dataSnapshot.child("basicinfo").child("active").getValue(String.class).equals("true"))
                        databaseReference.child("Lists").child(list_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ListObject object = dataSnapshot.getValue(ListObject.class);
                                if (object.getArticle_id().equals(article_key))
                                    count[0]++;
                                if (count[0] == 0)
                                    holder.num.setText("no lists");
                                else if (count[0] == 1)
                                    holder.num.setText(count[0] + " list");
                                else
                                    holder.num.setText(count[0] + " lists");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
        } else if (position == 2) {
            holder.name.setText("Tutorials");
            Log.d(TAG,"1");
            databaseReference.child("Contents").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listbasicinfo listbasicinfos = dataSnapshot.child("basicinfo").getValue(listbasicinfo.class);
                    String list_id = listbasicinfos.getList_id();
                    if (listbasicinfos.getType_id().equals("3") && dataSnapshot.child("basicinfo").child("active").getValue(String.class).equals("true"))
                        databaseReference.child("Lists").child(list_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ListObject object = dataSnapshot.getValue(ListObject.class);
                                if (object.getArticle_id().equals(article_key))
                                    count[0]++;
                                if (count[0] == 0)
                                    holder.num.setText("no lists");
                                else if (count[0] == 1)
                                    holder.num.setText(count[0] + " list");
                                else
                                    holder.num.setText(count[0] + " lists");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Intent intent = new Intent(context,DosNDonts.class);
                    context.startActivity(intent);
                }
                else if (position == 1) {
                    Intent intent = new Intent(context,Processs.class);
                    context.startActivity(intent);
                }
                else if (position == 2) {
                    Intent intent = new Intent(context,Tutorial.class);
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return newlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,num;
        public ViewHolder(View itemView, Context context) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.article_articlecard);
            num = (TextView) itemView.findViewById(R.id.numberlists);
        }
    }
}
