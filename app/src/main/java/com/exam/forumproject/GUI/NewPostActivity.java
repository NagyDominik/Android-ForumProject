package com.exam.forumproject.GUI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.exam.forumproject.R;

public class NewPostActivity extends AppCompatActivity {

    private Button btnPost, btnCamera, btnGallery;
    private ImageView imageView;
    private EditText etTitle, etText;
    private FrameLayout frameLayout;

    private static final int GALLERY_OPEN_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
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

                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    onPictureSwitch(bitmap);
                }
            }
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null && data.getExtras() != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    onPictureSwitch(bitmap);
                }
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

    private void onPictureSwitch(Bitmap image) {
        btnGallery.setVisibility(View.GONE);
        btnCamera.setVisibility(View.GONE);
        etText.setEnabled(false);
        frameLayout.setForeground(null);

        imageView.setImageBitmap(image);
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
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private void post() {

    }
}
