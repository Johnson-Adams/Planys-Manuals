package com.awesome.adams.planysmanuals;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DosNDonts_one extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    private Dialog dialog;
    private ImageView file,cam;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference,databaseReference2,databaseReference3;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private int  GALLERY_DO_CAM_CODE = 1,GALLERY_DO_FILE_CODE = 2,GALLERY_DONT_CAM_CODE = 3,GALLERY_DONT_FILE_CODE = 4,MY_REQUEST__CODE=5;
    int i = 0;
    private Uri uri;
    private ImageView doimg,dontimg;
    private EditText desc1,desc2;
    private Button add;
    private ProgressDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dos_ndonts_one);

        final SharedPreferences sharedPreferences = getSharedPreferences("DND_IMAGE",MODE_PRIVATE);
        final SharedPreferences sharedPreferences3 = getSharedPreferences("count_list",MODE_PRIVATE);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("article_into",MODE_PRIVATE);
        final SharedPreferences sharedPreferences4 = getSharedPreferences("dnd_size",MODE_PRIVATE);
        final SharedPreferences.Editor adder = sharedPreferences4.edit();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Contents");
        storageReference = firebaseStorage.getReference();

        databaseReference2 = firebaseDatabase.getReference().child("List_type");
        databaseReference3 = firebaseDatabase.getReference().child("Lists");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        doimg = (ImageView) findViewById(R.id.dos_image_add);
        dontimg = (ImageView) findViewById(R.id.donts_image_add);
        doimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog();
            }
        });
        dontimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog2();
            }
        });

        desc1 = (EditText) findViewById(R.id.dos_image_desc_add);
        desc2 = (EditText) findViewById(R.id.donts_image_desc_add);
        add = (Button) findViewById(R.id.dosndonts_save);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(desc1.getText().toString().trim()) && TextUtils.isEmpty(desc2.getText().toString().trim())) {

                    Toast.makeText(DosNDonts_one.this,"Please dont make empty list",Toast.LENGTH_SHORT).show();

                }

                DatabaseReference news = databaseReference.push();
                databaseReference2 = databaseReference2.push();
                databaseReference3 = databaseReference3.push();

                DatabaseReference basic = news.child("basicinfo");
                Map<String,String> adddo = new HashMap<>();
                adddo.put("type_id","1");
                adddo.put("list_id",databaseReference3.getKey());
                adddo.put("key",news.getKey());
                adddo.put("active","true");
                adddo.put("created_at", String.valueOf(System.currentTimeMillis()));
                adddo.put("updated_at", String.valueOf(System.currentTimeMillis()));
                adddo.put("user_id",user.getUid());
                adddo.put("name","Do's and Dont's");
                basic.setValue(adddo);

                final DatabaseReference data = news.child("data");

                Map<String,String> adddata = new HashMap<>();

                adddata.put("type","image");
                data.setValue(adddata);
                DatabaseReference imagedb = data.child("image");
                Map<String,String> image = new HashMap<>();
                //Log.d("url",sharedPreferences.getString("doimguri",null));
                image.put("0", sharedPreferences.getString("doimgurl","null"));
                Log.d("uri 0",sharedPreferences.getString("doimgurl","null"));
                image.put("1", sharedPreferences.getString("dontimgurll","null"));
                imagedb.setValue(image);

                DatabaseReference textdb = data.child("text");
                Map<String,String> text = new HashMap<>();
                textdb.setValue(text);

                Map<String,String> listtypes = new HashMap<>();
                listtypes.put("count_id",String.valueOf(sharedPreferences3.getInt("noofart",0)));
                listtypes.put("active","true");
                listtypes.put("created_at",String.valueOf(System.currentTimeMillis()));
                listtypes.put("updated_at",String.valueOf(System.currentTimeMillis()));
                listtypes.put("key",databaseReference2.getKey());
                listtypes.put("user_id",user.getUid());
                databaseReference2.setValue(listtypes);

                Map<String,String> addlist = new HashMap<>();
                addlist.put("count_id",String.valueOf(sharedPreferences3.getInt("noofart",0)));
                addlist.put("type_id",databaseReference2.getKey());
                addlist.put("type","1");
                addlist.put("article_id",sharedPreferences2.getString("article","s"));
                addlist.put("active","true");
                addlist.put("user_id",user.getUid());
                addlist.put("created_at",String.valueOf(System.currentTimeMillis()));
                addlist.put("updated_at",String.valueOf(System.currentTimeMillis()));
                addlist.put("name","dos and donts");
                addlist.put("key",databaseReference3.getKey());
                databaseReference3.setValue(addlist);

                DatabaseReference dnddo = data.child("dotext").child("0");
                dnddo.setValue(desc1.getText().toString().trim());

                DatabaseReference dnddont = data.child("donttext").child("0");
                dnddont.setValue(desc2.getText().toString().trim());

                Toast.makeText(DosNDonts_one.this,"Content Added",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DosNDonts_one.this,Lists.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_REQUEST__CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,GALLERY_DO_CAM_CODE);
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
                Toast.makeText(DosNDonts_one.this,"Please grant camera permission for this app",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void CustomAlertDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("DND_IMAGE",MODE_PRIVATE);
        final SharedPreferences.Editor save = sharedPreferences.edit();
        dialog = new Dialog(DosNDonts_one.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dosndonts_one_dialog);
        dialog.setTitle("FETCH FROM ...");
        file = (ImageView) dialog.findViewById(R.id.file_dosndonts_one);
        cam = (ImageView) dialog.findViewById(R.id.camera_dosndonts_one);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(DosNDonts_one.this,Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                MY_REQUEST__CODE);
                    else {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,GALLERY_DO_CAM_CODE);
                    }
                }
                else {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,GALLERY_DO_CAM_CODE);
                }
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
        dialog = new Dialog(DosNDonts_one.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dosndonts_one_dialog);
        dialog.setTitle("FETCH FROM ...");
        file = (ImageView) dialog.findViewById(R.id.file_dosndonts_one);
        cam = (ImageView) dialog.findViewById(R.id.camera_dosndonts_one);

        cam.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                if (ActivityCompat.checkSelfPermission(DosNDonts_one.this,Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                MY_REQUEST__CODE);
                    else {
                        Intent intent1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent1,GALLERY_DONT_CAM_CODE);
                    }
                }
                else {
                    Intent intent2 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2,GALLERY_DONT_CAM_CODE);
                }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = getSharedPreferences("DND_IMAGE",MODE_PRIVATE);
        final SharedPreferences.Editor saver = sharedPreferences.edit();
        final Uri[] imguri = new Uri[1];

        if ( requestCode == GALLERY_DO_CAM_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            doimg.setImageBitmap(photo);
            uri = data.getData();

            FirebaseStorage storage;

            StorageReference storageRef,imageRef;

            //accessing the firebase storage
            storage = FirebaseStorage.getInstance();
            //creates a storage reference
            storageRef = storage.getReference();

            imageRef = storageRef.child("images/"+ uri.getLastPathSegment());

            UploadTask uploadTask;

            uploadTask = imageRef.putFile(data.getData());

            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //sets and increments value of progressbar
                    progressDialog.incrementProgressBy((int) progress);
                }
            });
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(DosNDonts_one.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(DosNDonts_one.this,"Upload successful",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //showing the uploaded image in ImageView using the download url
                    saver.putString("doimgurl",String.valueOf(downloadUrl));
                    saver.apply();
                }
            });

        }

        else if ( requestCode == GALLERY_DONT_CAM_CODE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            dontimg.setImageBitmap(photo);
            uri = data.getData();

            FirebaseStorage storage;

            StorageReference storageRef,imageRef;

            //accessing the firebase storage
            storage = FirebaseStorage.getInstance();
            //creates a storage reference
            storageRef = storage.getReference();

            imageRef = storageRef.child("images/"+ uri.getLastPathSegment());

            UploadTask uploadTask;

            uploadTask = imageRef.putFile(data.getData());

            progressDialog = new ProgressDialog(this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //sets and increments value of progressbar
                    progressDialog.incrementProgressBy((int) progress);
                }
            });
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(DosNDonts_one.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(DosNDonts_one.this,"Upload successful",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //showing the uploaded image in ImageView using the download url
                    saver.putString("image",String.valueOf(downloadUrl));
                    saver.apply();
                }
            });

        }

        else if ( requestCode == GALLERY_DO_FILE_CODE && resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.with(DosNDonts_one.this).load(uri).into(doimg);
            StorageReference filepath = storageReference.child("MBlog_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imguri[0] = taskSnapshot.getDownloadUrl();
                    saver.putString("doimgurl",String.valueOf(imguri[0]));
                    Log.d("DND_one",String.valueOf(imguri[0]));
                    saver.apply();
                }
            });

        }

        else if ( requestCode == GALLERY_DONT_FILE_CODE && resultCode == RESULT_OK) {
            uri = data.getData();
            Picasso.with(DosNDonts_one.this).load(uri).into(dontimg);
            StorageReference filepath = storageReference.child("MBlog_images").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("dnd_one","1");
                    imguri[0] = taskSnapshot.getDownloadUrl();
                    saver.putString("dontimgurll",String.valueOf(imguri[0]));
                    saver.apply();
                }
            });

        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPreferences = getSharedPreferences("DND_IMAGE", MODE_PRIVATE);
        final SharedPreferences.Editor deleter = sharedPreferences.edit();
        deleter.remove("doimguri");
        deleter.remove("dontimguri");
        deleter.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DosNDonts_one.this,DosNDonts.class);
        startActivity(intent);
        finish();
    }
}
