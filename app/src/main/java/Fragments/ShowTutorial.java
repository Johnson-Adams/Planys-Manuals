package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.adams.planysmanuals.EditDosNDonts;
import com.awesome.adams.planysmanuals.EditTutorial;
import com.awesome.adams.planysmanuals.Lists;
import com.awesome.adams.planysmanuals.Login;
import com.awesome.adams.planysmanuals.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class ShowTutorial extends android.support.v4.app.Fragment {

    private Context context;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference data,databaseReference;
    private TextView head;
    private ImageView photo;
    private TextView content;
    private String content_key;
    private Button prev;
    private ViewPager v;
    private FirebaseAuth mauth;

    public ShowTutorial(Context context,String content_key,ViewPager v) {
        this.context = context;
        this.content_key = content_key;
        this.v = v;

    }

    public ShowTutorial(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_show_tut,container,false);
        head = (TextView) view.findViewById(R.id.showtut_head);
        photo = (ImageView) view.findViewById(R.id.showtut_photo);
        content = (TextView) view.findViewById(R.id.showtut_content);
        prev = (Button) view.findViewById(R.id.prev_tut_button);
        SharedPreferences sharedPreferences = context.getSharedPreferences("Tutorial",Context.MODE_PRIVATE);
        Log.d("showTut", String.valueOf(1));

        String context_key = sharedPreferences.getString("key_selected","null");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Contents").child(context_key).child("basicinfo").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String h = dataSnapshot.getValue(String.class);
                head.setText(h);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Contents").child(context_key).child("data").child("image").child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                //url = "https://firebasestorage.googleapis.com/v0/b/planysmanuals.appspot.com/o?name=MBlog_images%2F172694&uploadType=resumable&upload_id=AEnB2UosW68ap7qCFfhCZopuXMCIe9vsxhd4e8YLsom1v0Iuh-n2KjBm1Nn_XEiTLqPZIs_w9vz_KP2XUmTP42qHoa9Eo0uk8MEUh-Johvzdtkxy0rpAF7s&upload_protocol=resumable";
                //url = "https://firebasestorage.googleapis.com/v0/b/planysmanuals.appspot.com/o/MBlog_images%2Fimage%3A170891?alt=media&token=c64903ea-4000-4882-9f43-8e6a5d85c8df";
                //Glide.with(context).load(url).into(photo);
                Log.d("url",url);
                Picasso.with(context)
                        .load(url)
                        .into(photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("Contents").child(context_key).child("data").child("text").child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String h = dataSnapshot.getValue(String.class);
                content.setText(h);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abc();

            }
        });
        return view;
    }

    public void abc() {
        v.setCurrentItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            /*case R.id.addlistmenu:
                Intent intent = new Intent(Lists.this,CreateList.class);
                startActivity(intent);
                finish();
                break;*/

            case R.id.edit_dnd:
                mauth = FirebaseAuth.getInstance();
                data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("admin").getValue(String.class).equals("true")) {
                            Intent intent = new Intent(context, EditTutorial.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(context,"Get Admin Status to edit",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case R.id.delete_dnd:
                data.child("Userlist").child(mauth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("admin").getValue(String.class).equals("true")) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences("Tutorial",Context.MODE_PRIVATE);
                            databaseReference.child(sharedPreferences.getString("key_selected","null")).child("basicinfo").child("active").setValue("false");
                            Intent intent1 = new Intent(context, Lists.class);
                            startActivity(intent1);
                        }
                        else
                            Toast.makeText(context,"Get Admin Status to edit",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case R.id.logoutfromdosndonts:
                Intent intent2 = new Intent(context,Login.class);
                SharedPreferences prefs = context.getSharedPreferences("rem_user",MODE_PRIVATE);
                SharedPreferences.Editor changer = prefs.edit();
                changer.putString("rem_useremail",null);
                changer.putString("rem_pass",null);
                changer.apply();
                startActivity(intent2);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.dondontsmenu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
