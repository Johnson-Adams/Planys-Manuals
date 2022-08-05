package com.awesome.adams.planysmanuals;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

import static com.awesome.adams.planysmanuals.R.color.actionbar;

public class EditDosNDonts extends AppCompatActivity {

    private ImageView do_img,dont_img,file,cam;
    private EditText do_text,dont_text;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference  databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Button savechanges;
    private Dialog dialog;
    private int  GALLERY_DO_CAM_CODE = 1,GALLERY_DO_FILE_CODE = 2,GALLERY_DONT_CAM_CODE = 3,GALLERY_DONT_FILE_CODE = 4;
    int i = 0;
    private Uri uri;
    private LinearLayout dolayout,dontlayout;
    private ViewPager v;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dos_ndonts);

        final SharedPreferences sharedPreferences = getSharedPreferences("dndfile", MODE_PRIVATE);
        final String key = sharedPreferences.getString("keytoedit",null);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("DND_IMAGE",MODE_PRIVATE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        do_img = (ImageView) findViewById(R.id.edit_dnd_do_img);
        dont_img = (ImageView) findViewById(R.id.edit_dnd_dont_img);
        savechanges = (Button) findViewById(R.id.edit_dnd_update);
        do_text = (EditText) findViewById(R.id.edit_dnd_do_text);
        dont_text = (EditText) findViewById(R.id.edit_dnd_dont_text);


        databaseReference.child("Contents").child(key).child("data").child("type").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals("image")) {
                    databaseReference.child("Contents").child(key).child("data").child("image").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String dourl = dataSnapshot.child("0").getValue(String.class);
                            String donturl = dataSnapshot.child("1").getValue(String.class);
                            Picasso.with(EditDosNDonts.this)
                                    .load(dourl)
                                    .into(do_img);
                            Picasso.with(EditDosNDonts.this)
                                    .load(donturl)
                                    .into(dont_img);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    databaseReference.child("Contents").child(key).child("data").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            do_text.setText(dataSnapshot.child("dotext").child("0").getValue(String.class));
                            dont_text.setText(dataSnapshot.child("donttext").child("0").getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        do_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog();
            }
        });
        dont_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog2();
            }
        });


        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Contents").child(key).child("data").child("dotext").child("0").setValue(do_text.getText().toString());
                databaseReference.child("Contents").child(key).child("data").child("donttext").child("0").setValue(dont_text.getText().toString());

                databaseReference.child("Contents").child(key).child("data").child("image").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String douri = dataSnapshot.child("0").getValue(String.class);
                        String donturi = dataSnapshot.child("1").getValue(String.class);
                        databaseReference.child("Contents").child(key).child("data").child("image").child("0").setValue(sharedPreferences2.getString("doimguri",douri));
                        databaseReference.child("Contents").child(key).child("data").child("image").child("1").setValue(sharedPreferences2.getString("dontimguri",donturi));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseReference.child("Contents").child(key).child("data").child("dotext").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseReference.child("Contents").child(key).child("basicinfo").child("updated_at").setValue(String.valueOf(java.lang.System.currentTimeMillis()));

                Intent intent = new Intent(EditDosNDonts.this,DosNDonts.class);
                startActivity(intent);
                finish();

            }
        });


    }

    public void CustomAlertDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("DND_IMAGE",MODE_PRIVATE);
        final SharedPreferences.Editor save = sharedPreferences.edit();
        dialog = new Dialog(EditDosNDonts.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dosndonts_one_dialog);
        dialog.setTitle("FETCH FROM ...");
        file = (ImageView) dialog.findViewById(R.id.file_dosndonts_one);
        cam = (ImageView) dialog.findViewById(R.id.camera_dosndonts_one);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,GALLERY_DO_CAM_CODE);
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_DO_FILE_CODE);
            }
        });
        dialog.show();
        save.apply();

    }

    public void CustomAlertDialog2() {
        SharedPreferences sharedPreferences = getSharedPreferences("DND_IMAGE",MODE_PRIVATE);
        final SharedPreferences.Editor save = sharedPreferences.edit();
        dialog = new Dialog(EditDosNDonts.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dosndonts_one_dialog);
        dialog.setTitle("FETCH FROM ...");
        file = (ImageView) dialog.findViewById(R.id.file_dosndonts_one);
        cam = (ImageView) dialog.findViewById(R.id.camera_dosndonts_one);

        cam.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, GALLERY_DONT_CAM_CODE);

            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_DONT_FILE_CODE);
            }
        });
        dialog.show();
        save.apply();

    }

    public class edittexts extends android.support.v7.widget.AppCompatEditText {

        private EditText editText = new EditText(EditDosNDonts.this);

        public edittexts(Context context) {
            super(context);
        }

        @SuppressLint("ResourceAsColor")
        private EditText createNewEdittext(String text,int a) {
            final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            editText.setLayoutParams(lparams);
            if (a == 0)
                editText.setText(text);
            editText.setTextColor(R.color.black);
            editText.setTextSize(20);

            editText.setPadding(8,8,8,8);
            return editText;
        }

        public EditText getEditText() {
            return editText;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = getSharedPreferences("DND_IMAGE",MODE_PRIVATE);
        final SharedPreferences.Editor saver = sharedPreferences.edit();
        final Uri[] imguri = new Uri[1];

        if ( requestCode == GALLERY_DO_CAM_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            do_img.setImageBitmap(photo);
            uri = data.getData();
            StorageReference filepath = storageReference.child("MBlog_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imguri[0] = taskSnapshot.getDownloadUrl();
                    saver.putString("doimguri",String.valueOf(imguri[0]));
                    saver.apply();
                }
            });

        }

        else if ( requestCode == GALLERY_DONT_CAM_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            dont_img.setImageBitmap(photo);
            uri = data.getData();
            StorageReference filepath = storageReference.child("MBlog_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imguri[0] = taskSnapshot.getDownloadUrl();
                    saver.putString("dontimguri",String.valueOf(imguri[0]));
                    saver.apply();
                }
            });

        }

        else if ( requestCode == GALLERY_DO_FILE_CODE && resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.with(EditDosNDonts.this).load(uri).into(do_img);
            StorageReference filepath = storageReference.child("MBlog_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("DND_one","1");
                    imguri[0] = taskSnapshot.getDownloadUrl();
                    saver.putString("doimguri",String.valueOf(imguri[0]));
                    saver.apply();
                }
            });

        }

        else if ( requestCode == GALLERY_DONT_FILE_CODE && resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.with(EditDosNDonts.this).load(uri).into(dont_img);
            StorageReference filepath = storageReference.child("MBlog_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("dnd_one","1");
                    imguri[0] = taskSnapshot.getDownloadUrl();
                    saver.putString("dontimguri",String.valueOf(imguri[0]));
                    saver.apply();
                }
            });

        }
        }

    @SuppressLint("ResourceAsColor")
    private EditText createNewEdittext(String text,int a) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final EditText editText = new EditText(this);
        editText.setLayoutParams(lparams);
        if (a == 0)
            editText.setText(text);
        editText.setTextColor(R.color.black);
        editText.setTextSize(20);

        editText.setPadding(8,8,8,8);
        return editText;
    }

    @SuppressLint("ResourceAsColor")
    private EditText createNewEdittext2(String text,int a) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final EditText editText = new EditText(this);
        editText.setLayoutParams(lparams);
        if (a == 0)
            editText.setText(text);
        editText.setTextColor(R.color.black);
        editText.setTextSize(20);
        editText.setPadding(8,8,8,8);
        return editText;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("DND_IMAGE",MODE_PRIVATE);
        final SharedPreferences.Editor saver = sharedPreferences.edit();
        saver.remove("doimguri");
        saver.remove("dontimguri");
        saver.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditDosNDonts.this,DosNDonts.class);
        startActivity(intent);
        finish();
    }
}
