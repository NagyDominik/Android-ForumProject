package com.exam.forumproject.GUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.exam.forumproject.R;

public class NewPostActivity extends AppCompatActivity {

    private Button btnPost, btnCamera, btnGallery;
    private ImageView imageView;
    private EditText etTitle, etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        btnPost = findViewById(R.id.btnPost);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGallery);
        imageView = findViewById(R.id.imageView);
        etTitle = findViewById(R.id.etTitle);
        etText = findViewById(R.id.etText);

        setUpListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpListeners() {
        btnPost.setOnClickListener(v -> finish());
        btnCamera.setOnClickListener(v -> {
            onPictureSwitch(new byte[1]);
        });
        btnGallery.setOnClickListener(v -> {

        });
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().equals("")) {

                }
            }
        });
    }

    private void onPictureSwitch(byte[] image) {
        btnGallery.setVisibility(View.GONE);
        btnCamera.setVisibility(View.GONE);
        Bitmap imageBM = BitmapFactory.decodeByteArray(image, 0, image.length);
        imageView.setImageBitmap(imageBM);
        imageView.setBackground(null);
        imageView.setClickable(true);
        imageView.setOnClickListener(v -> {
            imageView.setClickable(false);
            imageView.setBackground(getDrawable(R.drawable.border));
            btnGallery.setVisibility(View.VISIBLE);
            btnCamera.setVisibility(View.VISIBLE);
        });
    }

    private void post() {

    }
}
