package com.r4u.ride4u.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.r4u.ride4u.AdminActivities.Operations;
import com.r4u.ride4u.UserActivities.Chat;
import com.r4u.ride4u.UserActivities.Login;
import com.r4u.ride4u.R;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private final FirebaseAuth authProfile = FirebaseAuth.getInstance();
    TextView profileName;
    TextView profileEmail;

    // This function creates the screen using the data pulled from the functions below.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initProfile(view);
        setAdminBtn(view);
        setupChangePassBtn(view);
        setProfilePic(view);
        setupNotificationBtn(view);
        return view;
    }

    // setting the admin options button
    // if the user viewing the profile page is an admin he'll see an admin options button
    private void setAdminBtn(View view) {
        ImageView adminBtn = view.findViewById(R.id.admin_options);
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

    private void setProfilePic(View view) {
        ImageView profilePic = view.findViewById(R.id.profilePicture);
        final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Uri selectedImage = result.getData().getData();
                            try {
                                Bitmap original = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                                int size = Math.min(original.getWidth(), original.getHeight());
                                int x = (original.getWidth() - size) / 20;
                                int y = (original.getHeight() - size) / 20;
                                // Create a square bitmap from the selected image
                                Bitmap squaredBitmap = Bitmap.createBitmap(original, x, y, size, size);
                                // Create an empty bitmap with the same width and height as the square bitmap
                                Bitmap output = Bitmap.createBitmap(squaredBitmap.getWidth(), squaredBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                                // Create a canvas and paint object
                                Canvas canvas = new Canvas(output);
                                Paint paint = new Paint();
                                paint.setAntiAlias(true);
                                // Create a shader from the square bitmap
                                Shader shader = new BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                                paint.setShader(shader);
                                // Draw a circle on the canvas using the paint object
                                float r = size / 2f;
                                canvas.drawCircle(r, r, r, paint);
                                // Set the output bitmap as the source of the ImageView
                                profilePic.setImageBitmap(output);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Choose from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Choose from Gallery")) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            galleryLauncher.launch(galleryIntent);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
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
        final ImageView changePass = view.findViewById(R.id.change_password);
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });
    }


    private void initProfile(View view) {
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profileName.setText(Login.user.getFullName());
        profileEmail.setText(Login.user.getEmail());
    }

    private void setupNotificationBtn(View view) {
        ImageView notificationsBtn = view.findViewById(R.id.notfication);
        notificationsBtn.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Chat.class));
        });
    }
}