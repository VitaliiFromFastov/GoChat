package com.chatappbyvitaliimoroz.gochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.name;

public class SettingsActivity extends AppCompatActivity {

    private static final int PHOTO_PICKER_RC = 123;
    private DatabaseReference mReference;
    private FirebaseUser mUser;
    private FirebaseStorage mStorage;
    private StorageReference mStorageReference;

    private TextView mUserNameTv;
    private TextView mStatusTv;
    private CircleImageView mAvatarCiv;
    private Button mChangeImage;
    private Button mChangeStatus;
    private ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mUserNameTv=(TextView)findViewById(R.id.settings_activity_username_tv);
        mStatusTv = (TextView)findViewById(R.id.settings_activity_user_status_tv);
        mAvatarCiv = (CircleImageView)findViewById(R.id.settings_activity_avatar_iv);
        mChangeImage = (Button)findViewById(R.id.settings_activity_change_image_btn);
        mChangeStatus = (Button)findViewById(R.id.settings_activity_change_status_btn);

        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,StatusActivity.class);
                intent.putExtra("status",mStatusTv.getText().toString());
                startActivity(intent);
            }
        });

        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {


                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
startActivityForResult(Intent.createChooser(intent,getString(R.string.settings_activity_photo_picker_title)),PHOTO_PICKER_RC);

            }
        });

        mStorage= FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference().child("images");


        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if (mUser!=null) {
            String uid = mUser.getUid();
            mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        }

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("name").getValue()!=null
                        && dataSnapshot.child("image").getValue() !=null
                        && dataSnapshot.child("status").getValue()!=null
                        && dataSnapshot.child("thumb").getValue()!=null) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    String thumb = dataSnapshot.child("thumb").getValue().toString();


                    mUserNameTv.setText(name);
                    mStatusTv.setText(status);
                    Picasso.with(SettingsActivity.this).load(image).into(mAvatarCiv);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PHOTO_PICKER_RC && resultCode==RESULT_OK){
            Uri imageUri = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialog= new ProgressDialog(SettingsActivity.this);
                mProgressDialog.setTitle(getString(R.string.settings_activity_progress_dialog_title));
                mProgressDialog.setMessage(getString(R.string.settings_activity_progress_dialog_message));
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();
                StorageReference storageReference = mStorageReference.child(resultUri.getLastPathSegment());
                storageReference.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()){

                            @SuppressWarnings("VisibleForTests") Uri uri = task.getResult().getDownloadUrl();
                            if (uri!=null){
                                String downloadUrl = uri.toString();

                                mReference.child("image").setValue(downloadUrl);

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }

                        }else {
                            Toast.makeText(SettingsActivity.this,"failed",Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();

                        }
                    }
                });


            }
        }

    }
}
