package com.exam.forumproject.GUI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.exam.forumproject.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPostActivity extends AppCompatActivity {

    private Button btnPost, btnCamera, btnGallery;
    private ImageView imageView;
    private EditText etTitle, etText;
    private FrameLayout frameLayout;

    private static final int GALLERY_OPEN_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    public static final String TAG = "Forumproject NewPost";
    private File cameraFile;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        model = Model.getInstance(this);

        btnPost = findViewById(R.id.btnPost);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        imageView = findViewById(R.id.imageView);
        etTitle = findViewById(R.id.etTitle);
        etText = findViewById(R.id.etText);
        frameLayout = findViewById(R.id.frameLayout);

        setUpListeners();
    }

    @Override
    protected void onDestroy() {
        cameraFile.delete();
        super.onDestroy();
    }

    @Override
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

                    onPictureSwitch(Uri.parse(picturePath));
                }
            }
            if (requestCode == CAMERA_REQUEST_CODE) {
                onPictureSwitch(Uri.fromFile(cameraFile));
            }
        }
    }

    private void setUpListeners() {
        btnPost.setOnClickListener(v -> finish());
        btnCamera.setOnClickListener(v -> openCamera());
        btnGallery.setOnClickListener(v -> openGallery());
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equals("")) {
                    btnCamera.setEnabled(false);
                    btnGallery.setEnabled(false);
                    imageView.setImageBitmap(null);
                    frameLayout.setForeground(null);
                }
                if (s != null && s.toString().equals("")) {
                    btnCamera.setEnabled(true);
                    btnGallery.setEnabled(true);
                }
            }
        });
    }

    private void onPictureSwitch(Uri image) {
        btnGallery.setVisibility(View.GONE);
        btnCamera.setVisibility(View.GONE);
        etText.setEnabled(false);
        frameLayout.setForeground(null);

        imageView.setImageURI(image);
        imageView.setBackground(null);
        imageView.setClickable(true);

        imageView.setOnClickListener(v -> {
            etText.setEnabled(true);
            imageView.setClickable(false);
            frameLayout.setForeground(getDrawable(R.drawable.overlay));
            btnGallery.setVisibility(View.VISIBLE);
            btnCamera.setVisibility(View.VISIBLE);
        });
    }

    private void openGallery() {
        if (model.checkPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setType("image/*");
            String[] mimeTypes = {"image/jpeg", "image/png", "image/gif"};
            pickIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

            startActivityForResult(pickIntent, GALLERY_OPEN_REQUEST_CODE);
        }
    }

    private void openCamera() {
        if (model.checkPermissions(this, Manifest.permission.CAMERA)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //App only folder
        return File.createTempFile(
            imageFileName,  // prefix
            ".jpg",   // suffix
            storageDir      // directory
        );

    }

    private void post() {
        cameraFile.delete(); //Important temp file cleanup!!
    }
}
