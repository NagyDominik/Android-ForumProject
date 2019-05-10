package com.exam.forumproject.GUI;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.exam.forumproject.R;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

public class CustomCameraActivity extends AppCompatActivity {

    public static String TAG = "CustomCameraActivity: ";
    TextureView textureView;
    FloatingActionButton btnCapturePhoto;
    Model model;
    private static int CAMERA_REQUEST_CODE = 4;

    CameraDevice cameraDevice;
    CameraManager cameraManager;
    TextureView.SurfaceTextureListener surfaceTextureListener;
    private Size previewSize;
    String cameraId;
    CameraCaptureSession cameraCaptureSession;

    int cameraFacing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);

        textureView =findViewById(R.id.cameraTexture);
        findViewById(R.id.btn_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();



                onTakePhotoButtonClicked(resultIntent);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        model = Model.getInstance(this);
        setUp();

    }

    public void setUp(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraFacing = CameraCharacteristics.LENS_FACING_FRONT;

        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                setUpCamera();
                openCamera();

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };

    }
    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            CustomCameraActivity.this.cameraDevice = cameraDevice;
            createPreviewSession();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            CustomCameraActivity.this.cameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            cameraDevice.close();
            CustomCameraActivity.this.cameraDevice = null;
        }
    };
    private void setUpCamera() {
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics =
                        cameraManager.getCameraCharacteristics(cameraId);
                Log.d(TAG, "Looking for front camera, current cameraId=" + cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                        cameraFacing) {
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    this.cameraId = cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {

                cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
                Log.d(TAG, "Camera with id= " + cameraId + " open.");

            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    HandlerThread backgroundThread;
    Handler backgroundHandler;

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    @Override
    protected void onResume() {
        super.onResume();
        openBackgroundThread();
        if (textureView.isAvailable()) {
            setUpCamera();
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeCamera();
        closeBackgroundThread();
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            final CaptureRequest.Builder captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                CaptureRequest captureRequest = captureRequestBuilder.build();
                                CustomCameraActivity.this.cameraCaptureSession = cameraCaptureSession;
                                CustomCameraActivity.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
                                        null, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public void onTakePhotoButtonClicked(Intent intent) {
        Bitmap bitmap = null;
        try {

            bitmap = textureView.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bytes = stream.toByteArray();
            intent.putExtra("data", bytes);

            Log.d(TAG, "Photo taken");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
