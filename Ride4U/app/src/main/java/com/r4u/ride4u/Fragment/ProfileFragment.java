package com.r4u.ride4u.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.r4u.ride4u.AdminActivities.Operations;
//import com.r4u.ride4u.UserActivities.ChangeAdress;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.R;
import com.r4u.ride4u.UserActivities.Payment;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    FirebaseUser firebaseUser;
    private final FirebaseAuth authProfile = FirebaseAuth.getInstance();
    //    private String user_password;
    TextView profileName;
    TextView profileEmail;
    TextView profileAddress;
    Button adminBtn;


    DatabaseReference databaseReference;
    String storagepath = "Users_Profile_Cover_image/";
    ProgressDialog pd;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    Uri imageuri;
    String profileOrCoverPhoto;

    // This function creates the screen using the data pulled from the functions below.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initProfile(view);
        setAdminBtn(view);
        setupChangePassBtn(view);
        return view;
    }

    // setting the admin options button
    // if the user viewing the profile page is an admin he'll see an admin options button
    private void setAdminBtn(View view) {
        adminBtn = view.findViewById(R.id.admin_options);
        if (!Login.user.getIsAdmin()) {
            adminBtn.setVisibility(View.INVISIBLE);
        } else {
            adminBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), Operations.class));
                }
            });
        }
    }


    ProgressDialog loadingBar;
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Password");
        LinearLayout linearLayout=new LinearLayout(this.getContext());
        final EditText emailet= new EditText(this.getContext());

        // write the email using which you registered
        emailet.setHint("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginChange(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void beginChange(String email) {
        loadingBar=new ProgressDialog(this.getContext());
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadingBar.dismiss();
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(ProfileFragment.this.getContext(),"Password change link has been sent",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ProfileFragment.this.getContext(),"Error Occurred",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(ProfileFragment.this.getContext(),"Error Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupChangePassBtn(View view) {
        final TextView changePass = view.findViewById(R.id.change_pass_text);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });
    }

    private void setupPaymentBtn(View view) {
        final ImageView paymentBtn = view.findViewById(R.id.imageView3);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Payment.class));
            }
        });
    }
//
//    private void setupProfilePicture(View view) {
//        ImageView profilePic = view.findViewById(R.id.profilePicture);
//        profilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }
//    // Initialize the profile's name and email acoording to the set database.
//
//    // checking storage permission ,if given then we can add something in our storage
//    private Boolean checkStoragePermission() {
//        boolean result = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//        return result;
//    }
//
//    // requesting for storage permission
//    private void requestStoragePermission() {
//        requestPermissions(storagePermission, STORAGE_REQUEST);
//    }
//
//    // checking camera permission ,if given then we can click image using our camera
//    private Boolean checkCameraPermission() {
//        boolean result = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
//        boolean result1 = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
//        return result && result1;
//    }
//
//    // requesting for camera permission if not given
//    private void requestCameraPermission() {
//        requestPermissions(cameraPermission, CAMERA_REQUEST);
//    }
//
//    // Here we are showing image pic dialog where we will select
//    // and image either from camera or gallery
//    private void showImagePicDialog() {
//        String options[] = {"Camera", "Gallery"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
//        builder.setTitle("Pick Image From");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // if access is not given then we will request for permission
//                if (which == 0) {
//                    if (!checkCameraPermission()) {
//                        requestCameraPermission();
//                    } else {
//                        pickFromCamera();
//                    }
//                } else if (which == 1) {
//                    if (!checkStoragePermission()) {
//                        requestStoragePermission();
//                    } else {
//                        pickFromGallery();
//                    }
//                }
//            }
//        });
//        builder.create().show();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
//                imageuri = data.getData();
//                uploadProfileCoverPhoto(imageuri);
//            }
//            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
//                uploadProfileCoverPhoto(imageuri);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case CAMERA_REQUEST: {
//                if (grantResults.length > 0) {
//                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (camera_accepted && writeStorageaccepted) {
//                        pickFromCamera();
//                    } else {
//                        Toast.makeText(this.getContext(), "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//            break;
//            case STORAGE_REQUEST: {
//                if (grantResults.length > 0) {
//                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (writeStorageaccepted) {
//                        pickFromGallery();
//                    } else {
//                        Toast.makeText(this.getContext(), "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//            break;
//        }
//    }
//
//    // Here we will click a photo and then go to startactivityforresult for updating data
//    private void pickFromCamera() {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
//        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
//        imageuri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
//        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
//    }
//
//    // We will select an image from gallery
//    private void pickFromGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
//    }
//
//    // We will upload the image from here.
//    private void uploadProfileCoverPhoto(final Uri uri) {
//        pd.show();
//
//        // We are taking the filepath as storagepath + firebaseauth.getUid()+".png"
//        String filepathname = storagepath + "" + profileOrCoverPhoto + "_" + firebaseUser.getUid();
//        StorageReference storageReference1 = storageReference.child(filepathname);
//        storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!uriTask.isSuccessful()) ;
//
//                // We will get the url of our image using uritask
//                final Uri downloadUri = uriTask.getResult();
//                if (uriTask.isSuccessful()) {
//
//                    // updating our image url into the realtime database
//                    HashMap<String, Object> hashMap = new HashMap<>();
//                    hashMap.put(profileOrCoverPhoto, downloadUri.toString());
//                    databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            pd.dismiss();
//                            Toast.makeText(ProfileFragment.this.getContext() "Updated", Toast.LENGTH_LONG).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            pd.dismiss();
//                            Toast.makeText(ProfileFragment.this.getContext(), "Error Updating ", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } else {
//                    pd.dismiss();
//                    Toast.makeText(ProfileFragment.this.getContext(), "Error", Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                pd.dismiss();
//                Toast.makeText(ProfileFragment.this.getContext(), "Error", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    private void initProfile(View view) {
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profileName.setText(Login.user.getFullName());
        profileEmail.setText(Login.user.getEmail());
    }


}