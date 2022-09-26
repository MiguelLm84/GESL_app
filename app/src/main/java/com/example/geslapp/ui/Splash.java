package com.example.geslapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.transition.Explode;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.geslapp.BuildConfig;
import com.example.geslapp.R;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Splash extends AppCompatActivity {

    TextView tvVersion;
    ImageView ivLogoGesl, ivLogoAsysgon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);

        init();
        getVersion();
        animSplash();
        apkFindUpdate();
        goToLogin();
    }

    private void init() {

        tvVersion = findViewById(R.id.tvVersion);
        ivLogoGesl = findViewById(R.id.ivLogoGesl);
        ivLogoAsysgon = findViewById(R.id.ivLogoAsysgon);
    }

    private void apkFindUpdate() {

        String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File file = new File(PATH);

        File outputFile = new File(file, "geslapp.apk");
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    private void animSplash() {

        animLogoSplash();
        Explode explode = new Explode();
        explode.setDuration(2500);
        getWindow().setExitTransition(explode);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //Método para ir al LoginActivity
    private void goToLogin() {

        new Handler().postDelayed(() -> {
            Intent i = new Intent(Splash.this, LoginActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }, 7500);
    }

    //Método para mostrar por pantalla la versión y la fecha de la APK.
    @SuppressLint("SetTextI18n")
    public void getVersion(){

        animTextVersionSplashIn();
        String versionName = BuildConfig.VERSION_NAME;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String fechaDeActualizacion = sdf.format(new Date(BuildConfig.BUILD_DATE));
        String fecha = fechaDeActualizacion.replaceAll("/", "");
        tvVersion.setText("Gesl App  v" + versionName + "\n" + fecha);
    }

    //Método para la animación del logo de la app y splash.
    public void animLogoSplash(){

        Animation anim1 = AnimationUtils.loadAnimation(this,R.anim.fade_in_2);
        ivLogoGesl.setAnimation(anim1);
        //ivLogoAsysgon.setAnimation(anim1);
    }

    //Método para la animación del texto referente a las versiones.
    public void animTextVersionSplashIn() {

        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        tvVersion.setAnimation(anim2);
    }
}
