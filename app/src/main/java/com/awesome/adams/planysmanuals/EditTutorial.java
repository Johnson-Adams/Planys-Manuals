package com.awesome.adams.planysmanuals;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditTutorial extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText head,desc;
    private ImageView photo,cam,gallery;
    private Button savechanges;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Dialog dialog;
    int GALLERY_DO_CAM_CODE = 1,GALLERY_DO_GRANERY_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tutorial);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        head = (EditText) findViewById(R.id.edit_tut_head);
        photo = (ImageView) findViewById(R.id.edit_tut_image);
        desc = (EditText) findViewById(R.id.edit_tut_desc);
        savechanges = (Button) findViewById(R.id.edit_tut_save);

        final SharedPreferences sharedPreferences = getSharedPreferences("Tutorial", Context.MODE_PRIVATE);
        final String key = sharedPreferences.getString("key_selected","null");

        databaseReference.child("Contents").child(key).child("data").child("image").child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                Picasso.with(EditTutorial.this)
                        .load(url)
                        .into(photo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Contents").child(key).child("data").child("text").child("0").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                desc.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("Contents").child(key).child("basicinfo").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String heads = dataSnapshot.getValue(String.class);
                head.setText(heads);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Contents").child(key).child("basicinfo").child("name").setValue(head.getText().toString());
                databaseReference.child("Contents").child(key).child("data").child("text").child("0").setValue(desc.getText().toString());
                databaseReference.child("Contents").child(key).child("data").child("image").child("0").setValue(sharedPreferences.getString("image","null"));
                Toast.makeText(EditTutorial.this,"Changes Applied in "+head.getText().toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditTutorial.this,Tutorial.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void CustomAlertDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("Tutorial",MODE_PRIVATE);
        final SharedPreferences.Editor save = sharedPreferences.edit();
        dialog = new Dialog(    EditTutorial.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dosndonts_one_dialog);
        dialog.setTitle("FETCH FROM ...");
        gallery = (ImageView) dialog.findViewById(R.id.file_dosndonts_one);
        cam = (ImageView) dialog.findViewById(R.id.camera_dosndonts_one);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,GALLERY_DO_CAM_CODE);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_DO_GRANERY_CODE);
            }
        });
        dialog.show();
        save.apply();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences sharedPreferences = getSharedPreferences("Tutorial",MODE_PRIVATE);
        final SharedPreferences.Editor saver = sharedPreferences.edit();
        final Uri[] imguri = new Uri[1];

        if ( requestCode == GALLERY_DO_CAM_CODE && resultCode == RESULT_OK) {
            Bitmap photos = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(photos);
            Uri uri = data.getData();
            StorageReference filepath = storageReference.child("MBlog_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imguri[0] = taskSnapshot.getDownloadUrl();
                    saver.putString("image",String.valueOf(imguri[0]));
                    saver.apply();
                }
            });

        }

        else if ( requestCode == GALLERY_DO_GRANERY_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Picasso.with(getBaseContext()).load(uri).into(photo);
            StorageReference filepath = storageReference.child("MBlog_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imguri[0] = taskSnapshot.getDownloadUrl();
                    saver.putString("image",String.valueOf(imguri[0]));
                    saver.apply();
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditTutorial.this,Tutorial.class);
        startActivity(intent);
        finish();
    }
}
