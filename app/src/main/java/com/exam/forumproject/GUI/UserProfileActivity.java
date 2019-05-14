package com.exam.forumproject.GUI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exam.forumproject.BE.User;
import com.exam.forumproject.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    CircleImageView profilePicture;
    Button btnOpenGallery;
    Button btnOpenCamera;
    Button btnSave;
    TextView tvUsername;
    Model model;

    private static final int GALLERY_OPEN_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    public static final String TAG = "UserProfileActivity";
    private File cameraFile;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        btnOpenGallery = findViewById(R.id.btnOpenGallery);
        btnSave = findViewById(R.id.btnSaveProfile);
        profilePicture = findViewById(R.id.circleImageView);
        tvUsername = findViewById(R.id.tvUsername);
        model = Model.getInstance(this);
        currentUser = model.getUserById("id");
        tvUsername.setText(currentUser.getUsername());
        Glide.with(this)
            .asDrawable()
            .load(currentUser.getProfilePicUrl())
            .into(profilePicture);
        setUpListeners();
    }

    @Override
    protected void onDestroy() {
        if (cameraFile != null) {
            cameraFile.delete();
        }
        super.onDestroy();
    }

    /**
     * Sets up the onClickListeners for the buttons of the Activity
     */
    private void setUpListeners() {
        btnSave.setOnClickListener(v -> {
            Bitmap tempBitmap = null;
            if (profilePicture.getDrawable() != null) {
                tempBitmap = ((BitmapDrawable) profilePicture.getDrawable()).getBitmap();
            }
            model.updateProfilePicture(tempBitmap);
        });
        btnOpenCamera.setOnClickListener(v -> openCamera());
        btnOpenGallery.setOnClickListener(v -> openGallery());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == GALLERY_OPEN_REQUEST_CODE) {
                if (data != null && data.getData() != null) {
                    Uri pic = data.getData(); //Media URI (content://)
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(pic, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]); //Should be 0, because the query returns only one column
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    profilePicture.setImageURI(Uri.parse(picturePath));
                }
            }
            if (requestCode == CAMERA_REQUEST_CODE) {
                byte[] returnValue = data.getByteArrayExtra("data");
                Bitmap bmp = BitmapFactory.decodeByteArray(returnValue, 0, returnValue.length);
                profilePicture.setImageBitmap(bmp);
            }
        }
    }

    /**
     * Opens the Gallery app for choosing image from the device.
     */
    private void openGallery() {
        if (model.checkPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png", "image/gif"};
            pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            startActivityForResult(pickIntent, GALLERY_OPEN_REQUEST_CODE);
        }
    }

    /**
     * Opens the Camera app for image capture.
     */
    private void openCamera() {
        if (model.checkPermissions(this, Manifest.permission.CAMERA)) {
            Intent cameraIntent = new Intent(this, CustomCameraActivity.class);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                cameraFile = null;
                try {
                    cameraFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.i(TAG, "IOException");
                }
                // Continue only if the File was successfully created
                if (cameraFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, "com.exam.forumproject.fileprovider", cameraFile));
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            }
        }
    }

    /**
     * Creates the image file in the apps folder.
     *
     * @return The created file.
     * @throws IOException When the file creation failed.
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //App only folder
        return File.createTempFile(
            imageFileName,   // prefix
            ".jpg",   // suffix
            storageDir      // directory
        );

    }

}
