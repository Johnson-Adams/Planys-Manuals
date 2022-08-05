package com.awesome.adams.planysmanuals;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static com.awesome.adams.planysmanuals.DosNDonts_one.ALLOW_KEY;
import static com.awesome.adams.planysmanuals.DosNDonts_one.MY_PERMISSIONS_REQUEST_CAMERA;

public class CreateTut extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference2,databaseReference3;
    private Dialog dialog;
    private ImageView addimage;
    private ImageView gallery,cam;
    int GALLERY_DO_CAM_CODE = 1,GALLERY_DO_GRANERY_CODE = 2;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private EditText head,descr;
    private Button submit;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private static int MY_REQUEST__CODE = 3;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tut);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Contents");
        databaseReference2 = firebaseDatabase.getReference().child("List_type");
        databaseReference3 = firebaseDatabase.getReference().child("Lists");


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        submit = (Button) findViewById(R.id.submit_new_tut);

        addimage = (ImageView) findViewById(R.id.tut_new_photo);

        head = (EditText) findViewById(R.id.tut_new_head);
        descr = (EditText) findViewById(R.id.tut_new_content);

        final SharedPreferences sharedPreferences = getSharedPreferences("Tutorial",MODE_PRIVATE);
        final SharedPreferences sharedPreferences3 = getSharedPreferences("count_list",MODE_PRIVATE);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("article_into",MODE_PRIVATE);

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference news = databaseReference.push();
                databaseReference2 = databaseReference2.push();
                databaseReference3 = databaseReference3.push();

                DatabaseReference basic = news.child("basicinfo");
                Map<String,String> adddo = new HashMap<>();
                adddo.put("type_id","3");
                adddo.put("list_id",databaseReference3.getKey());
                adddo.put("key",news.getKey());
                adddo.put("active","true");
                adddo.put("created_at", String.valueOf(System.currentTimeMillis()));
                adddo.put("updated_at", String.valueOf(System.currentTimeMillis()));
                adddo.put("user_id",user.getUid());
                adddo.put("name",head.getText().toString().trim());
                basic.setValue(adddo);

                DatabaseReference data = news.child("data");

                Map<String,String> adddata = new HashMap<>();
                adddata.put("type","image");
                data.setValue(adddata);

                DatabaseReference imagedb = data.child("image");
                Map<String,String> image = new HashMap<>();
                image.put("0", String.valueOf(sharedPreferences.getString("image",null)));
                imagedb.setValue(image);

                DatabaseReference textdb = data.child("text");
                Map<String,String> text = new HashMap<>();
                text.put("0",descr.getText().toString());
                textdb.setValue(text);

                DatabaseReference dnddo = data.child("dotext");
                Map<String,String> nt = new HashMap<>();
                dnddo.setValue(nt);

                DatabaseReference dnddont = data.child("donttext");
                Map<String,String> nt1 = new HashMap<>();
                dnddont.setValue(nt1);

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
                addlist.put("article_id",sharedPreferences2.getString("article","s"));
                addlist.put("active","true");
                addlist.put("type","3");
                addlist.put("user_id",user.getUid());
                addlist.put("created_at",String.valueOf(System.currentTimeMillis()));
                addlist.put("updated_at",String.valueOf(System.currentTimeMillis()));
                addlist.put("name","tutorial");
                addlist.put("key",databaseReference3.getKey());
                databaseReference3.setValue(addlist);

                Toast.makeText(CreateTut.this,"Your Tutorial Added",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CreateTut.this,Lists.class);
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
                Toast.makeText(CreateTut.this,"Please grant camera permission for this app",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void CustomAlertDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("Tutorial",MODE_PRIVATE);
        final SharedPreferences.Editor save = sharedPreferences.edit();
        dialog = new Dialog(    CreateTut.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dosndonts_one_dialog);
        dialog.setTitle("FETCH FROM ...");
        gallery = (ImageView) dialog.findViewById(R.id.file_dosndonts_one);
        cam = (ImageView) dialog.findViewById(R.id.camera_dosndonts_one);

        cam.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (getFromPref(this, ALLOW_KEY)) {
                        showSettingsAlert();
                    } else if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.CAMERA)

                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                android.Manifest.permission.CAMERA)) {
                            showAlert();
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    }
                } else {
                    openCamera();
                }*/

                if (ActivityCompat.checkSelfPermission(CreateTut.this,Manifest.permission.CAMERA)
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

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            addimage.setImageBitmap(photo);
            Uri uri = data.getData();

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
                    Toast.makeText(CreateTut.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(CreateTut.this,"Upload successful",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //showing the uploaded image in ImageView using the download url
                    saver.putString("image",String.valueOf(downloadUrl));
                    saver.apply();
                }
            });


        }

        else if ( requestCode == GALLERY_DO_GRANERY_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Picasso.with(getBaseContext()).load(uri).into(addimage);
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
        Intent intent = new Intent(CreateTut.this,Tutorial.class);
        startActivity(intent);
        finish();
    }
}
