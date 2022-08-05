package Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesome.adams.planysmanuals.CreateProcess;
import com.awesome.adams.planysmanuals.CreateTut;
import com.awesome.adams.planysmanuals.DosNDonts_one;
import com.awesome.adams.planysmanuals.Lists;
import com.awesome.adams.planysmanuals.Manifest;
import com.awesome.adams.planysmanuals.Processs;
import com.awesome.adams.planysmanuals.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
//import static com.google.android.gms.flags.FlagSource.G;

@SuppressLint("ValidFragment")
public class CreateProcessFragment extends android.support.v4.app.Fragment {

    private String Tag = "abc";
    private ImageView image,file,cam;
    private TextView stephead;
    private EditText point;
    private Button del,save;
    private Context context;
    private ViewPager viewPager;
    int i,GALLERY_DO_CAM_CODE = 1,GALLERY_DO_FILE_CODE = 2;
    private CreateProcess.MyViewPagerAdapter adapter;
    private Dialog dialog;
    int n = 1;
    private Button plus;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,data;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private int REQUEST_EXTERNAL_STORAGE_RESULT = 1,MY_REQUEST__CODE = 2;
    private ProgressDialog progressDialog;

    public CreateProcessFragment(int i, Context context, CreateProcess.MyViewPagerAdapter adapter, ViewPager viewPager) {
        this.i = i;
        Log.d("cpf","1");
        this.context = context;
        this.adapter = adapter;
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View page = inflater.inflate(R.layout.fragment_process,container,false);
        image = (ImageView) page.findViewById(R.id.addimageprocess);
        point = (EditText) page.findViewById(R.id.addpointsprocess);
        save = (Button) page.findViewById(R.id.savecreateprocess);
        del = (Button) page.findViewById(R.id.deletestepcreateprocess);
        stephead = (TextView) page.findViewById(R.id.processstepno);

        return page;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("i", String.valueOf(i));
        firebaseDatabase = FirebaseDatabase.getInstance();

        final SharedPreferences sharedPreferences = context.getSharedPreferences("process", MODE_PRIVATE);
        final SharedPreferences s = context.getSharedPreferences("processs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final String contextinto = sharedPreferences.getString("process_in_process","null");
        data = firebaseDatabase.getReference();
        databaseReference = firebaseDatabase.getReference().child("Contents").child(contextinto);

        databaseReference.child("basicinfo").child("list_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String list_id = dataSnapshot.getValue(String.class);
                editor.putString("list_into_create_process",list_id);
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        data.child("Lists").child(sharedPreferences.getString("list_into_create_process","null")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String type_id = dataSnapshot.child("type_id").getValue(String.class);
                editor.putString("listtype_into_create_process",type_id);
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAlertDialog();
            }
        });

        Log.d("pos", String.valueOf(viewPager.getCurrentItem()));
        Log.d("posi", String.valueOf(i));

        databaseReference.child("data").child("image").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uristring = dataSnapshot.getValue(String.class);
                if (uristring != null) {
                    Picasso.with(context)
                            .load(uristring)
                            .into(image);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("data").child("text").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                point.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        stephead.setText("Step "+ Integer.valueOf(i+1));

        if (s.getString("size","1").equals("0")) {
            Intent intent = new Intent(context,Process.class);
            startActivity(intent);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = point.getText().toString();
                databaseReference.child("data").child("text").child(String.valueOf(i)).setValue(text);
                databaseReference.child("basicinfo").child("active").setValue("true");
                databaseReference.child("data").child("size").setValue(String.valueOf(i+1));
                data.child("Lists").child(sharedPreferences.getString("list_into_create_process","null")).child("active").setValue("true");
                data.child("List_type").child(sharedPreferences.getString("listtype_into_create_process","null")).child("active").setValue("true");
                Toast.makeText(context,"Process Created",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context,Processs.class);
                startActivity(intent);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cpf", "2");
                final List<String> strings = new ArrayList<>();
                final List<String> images = new ArrayList<>();
                adapter.RemoveFragmentPage(i, viewPager, adapter);
                adapter.notifyDataSetChanged();
                if (i == 0) {
                    viewPager.setCurrentItem(0);
                }
                viewPager.setCurrentItem(i - 1);
                //databaseReference.child("data").child("text").child(String.valueOf(i)).setValue(null);

                    databaseReference.child("data").child("text").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                //Log.d(String.valueOf(finalA), dataSnapshot.getValue(String.class));
                            //GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                            Log.d("children", String.valueOf(dataSnapshot.getChildrenCount()));
                            for (int a = 0 ; a < dataSnapshot.getChildrenCount() + 1 ; a++ ) {
                                if (a != i){
                                    strings.add(dataSnapshot.child(String.valueOf(a)).getValue(String.class));
                                    //Log.d("cpf ",dataSnapshot.child(String.valueOf(a)).getValue(String.class));
                                }
                            }
                            databaseReference.child("data").child("text").child(String.valueOf(Integer.valueOf(s.getString("size","1")))).setValue(null);

                            Log.d("cpf size",s.getString("size","1"));

                            for (int a = i ; a < Integer.valueOf(s.getString("size", "1")) ; a++) {
                                databaseReference.child("data").child("text").child(String.valueOf(a)).setValue(strings.get(a));
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                databaseReference.child("data").child("image").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Log.d(String.valueOf(finalA), dataSnapshot.getValue(String.class));
                        //GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                        Log.d("children foe image", String.valueOf(dataSnapshot.getChildrenCount()));
                        for (int a = 0 ; a < dataSnapshot.getChildrenCount() + 1 ; a++ ) {
                            if (a != i){
                                images.add(dataSnapshot.child(String.valueOf(a)).getValue(String.class));
                                //Log.d("cpf ",dataSnapshot.child(String.valueOf(a)).getValue(String.class));
                            }
                        }
                        databaseReference.child("data").child("image").child(String.valueOf(Integer.valueOf(s.getString("size","1")))).setValue(null);

                        Log.d("cpf size image",s.getString("size","1"));

                        for (int a = i ; a < Integer.valueOf(s.getString("size", "1")) ; a++) {
                            databaseReference.child("data").child("image").child(String.valueOf(a)).setValue(images.get(a));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    /*public void takephoto(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (shouldShowRequestPermissionRationale(Manifest.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(context,"External Storage Permission required to Save Image",Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_RESULT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE_RESULT)

    }*/

    public void CustomAlertDialog() {

        SharedPreferences sharedPreferences = context.getSharedPreferences("process",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.process_addimg_dialog);
        dialog.setTitle("FETCH FROM ...");
        file = (ImageView) dialog.findViewById(R.id.file_createprocess);
        cam = (ImageView) dialog.findViewById(R.id.camera_createprocess);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*editor.putInt("imgselect",i);
                editor.apply();*/

                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
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
                /*editor.putInt("imgselect",i);
                editor.apply();*/
                Intent intentGalley = new Intent(Intent.ACTION_PICK);
                intentGalley.setType("image/*");
                startActivityForResult(intentGalley,GALLERY_DO_FILE_CODE);
            }
        });
        dialog.show();

    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_DO_CAM_CODE && resultCode == RESULT_OK ) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
        }

        else if (requestCode == GALLERY_DO_FILE_CODE && resultCode ==RESULT_OK ) {
            Uri uri = data.getData();
            Picasso.with(context).load(uri).into(image);
        }
    }*/

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
                Toast.makeText(context,"Please grant camera permission for this app",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uriPhoto;
        SharedPreferences sharedPreferences = context.getSharedPreferences("process", MODE_PRIVATE);
        String contextinto = sharedPreferences.getString("processinprocess","null");

        if (requestCode == GALLERY_DO_FILE_CODE  && resultCode == RESULT_OK ) {
            uriPhoto = data.getData();
            StorageReference path = storageReference.child("Process").child(contextinto).child(String.valueOf(i));
            path.putFile(uriPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    databaseReference.child("data").child("image").child(String.valueOf(i)).setValue(String.valueOf(downloadurl));
                }
            });

        }
        else if (requestCode == GALLERY_DO_CAM_CODE && resultCode == RESULT_OK) {
            uriPhoto = data.getData();
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
            StorageReference path = storageReference.child("Process").child(contextinto).child(String.valueOf(i));
            path.putFile(uriPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    databaseReference.child("data").child("image").child(String.valueOf(i)).setValue(String.valueOf(downloadurl));
                }
            });

            FirebaseStorage storage;

            StorageReference storageRef,imageRef;

            //accessing the firebase storage
            storage = FirebaseStorage.getInstance();
            //creates a storage reference
            storageRef = storage.getReference();

            imageRef = storageRef.child("images/"+ uriPhoto.getLastPathSegment());

            UploadTask uploadTask;

            uploadTask = imageRef.putFile(data.getData());

            progressDialog = new ProgressDialog(context);
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
                    Toast.makeText(context,"Error in uploading!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(context,"Upload successful",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //showing the uploaded image in ImageView using the download url
                    databaseReference.child("data").child("image").child(String.valueOf(i)).setValue(String.valueOf(downloadUrl));
                }
            });

        }



    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            if (!isVisibleToUser) {
                String text = point.getText().toString();
                databaseReference.child("data").child("text").child(String.valueOf(i)).setValue(text);
            }
        }
    }
}
