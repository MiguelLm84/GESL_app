package com.example.geslapp.core.camara;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.example.geslapp.R;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.UploadImage;
import com.example.geslapp.core.databaseInvent.Antenas_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Fotos_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.General_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Material_Invent_Local_DB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

public class CamaraX extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewview;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    FloatingActionButton butphoto;
    private ImageCapture imageCapture;
    TextureView textureView;
    private String mode,itemName;
    private int id_invent;
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result)
            {
                startCamera();
            }
            else
            {
                Toast.makeText(CamaraX.this, "No hay permisos de cámara", Toast.LENGTH_SHORT).show();
            }

        }
    });



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        butphoto = findViewById(R.id.buttakephoto);
        previewview = findViewById(R.id.previewView);
        mode = getIntent().getStringExtra("mode");

        id_invent = getIntent().getIntExtra("id_invent",0);
        if(ContextCompat.checkSelfPermission(CamaraX.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        }
        else
        {
            startCamera();
        }

    }

    private void startCamera() {
        Size resolutionSize = new Size(1280,720);
        ListenableFuture listenableFuture = ProcessCameraProvider.getInstance(this);
        listenableFuture.addListener(() ->
        {
         try {
             ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) listenableFuture.get();
             Preview preview = new Preview.Builder().setTargetResolution(resolutionSize).build();
             @SuppressLint("RestrictedApi") ImageCapture imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).setJpegQuality(95).setDefaultResolution(resolutionSize).build();
             CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
             processCameraProvider.unbindAll();
             Camera camara = processCameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,preview,imageCapture);
             butphoto.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     captureImage(imageCapture);
                 }
             });
             preview.setSurfaceProvider(previewview.getSurfaceProvider());
         } catch (Exception e) {
             e.printStackTrace();
         }

        },ContextCompat.getMainExecutor(this));



    }

    private void captureImage(ImageCapture imageCapture) {
        String filename = System.currentTimeMillis()+".png";
        File image = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),filename);
        ImageCapture.OutputFileOptions fileOptions = new ImageCapture.OutputFileOptions.Builder(image).build();

        imageCapture.takePicture(fileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        General_Invent_Local_DB general_invent_local_db = new General_Invent_Local_DB(getApplicationContext());
                        Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(getApplicationContext());
                        Fotos_Invent_Local_DB fotos_invent_local_db = new Fotos_Invent_Local_DB(getApplicationContext());

                        switch (mode) {
                            case "antenas":
                                int id_antena = getIntent().getIntExtra("id_antena",0);
                                Antenas_Invent_Local_DB antenas_invent_local_db = new Antenas_Invent_Local_DB(getApplicationContext());
                                antenas_invent_local_db.updateFoto(image.getName(),id_antena,id_invent);

                                fotos_invent_local_db.savefoto(id_invent,image.getName());

                                break;

                            case "fotoserver1" :


                                general_invent_local_db.updateFotoserver1(id_invent,image.getName());
                                fotos_invent_local_db.savefoto(id_invent,image.getName());


                                break;
                            case "fotoserver2" :


                                general_invent_local_db.updateFotoserver2(id_invent,image.getName());
                                fotos_invent_local_db.savefoto(id_invent,image.getName());


                                break;
                            case "fotopoe" :


                                general_invent_local_db.updateFotoPoe(id_invent,image.getName());
                                fotos_invent_local_db.savefoto(id_invent,image.getName());


                                break;
                            case "fotogood" :


                                general_invent_local_db.updateFotoGood(id_invent,image.getName());
                                fotos_invent_local_db.savefoto(id_invent,image.getName());


                                break;
                            case"etqs":
                                int position = getIntent().getIntExtra("position",-1);
                                etqs_invent_local_db.updateFoto(position,image.getName());
                                fotos_invent_local_db.savefoto(id_invent,image.getName());

                                break;
                            case "items":
                                itemName = getIntent().getStringExtra("itemName");
                                Material_Invent_Local_DB material_invent_local_db = new Material_Invent_Local_DB(getApplicationContext());
                                //material_invent_local_db.updateFoto(itemName, image.getName(),id_invent);
                                fotos_invent_local_db.savefoto(id_invent,image.getName());
                                break;

                        }


                        finish();
                    }
                });


            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CamaraX.this, "No se ha podido guardar la foto", Toast.LENGTH_SHORT).show();

                    }
                });
               startCamera();
            }
        });
    }









}

