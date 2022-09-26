package com.example.geslapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import com.example.geslapp.R;
import com.example.geslapp.core.camara.CaptureAct;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.database.Cajas_Local_DB;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.Locale;


public class CajasActivity extends AppCompatActivity {
    EditText et;
    EditText codigoCaja;
    Button butbarras;
    Drawable butdraw;

    boolean escritura = false;
    private final ConfigPreferences config = new ConfigPreferences();
    private static String IP, REC, PROJECT;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prescanner);
        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());
        PROJECT = config.getProject(getApplicationContext());

      //  Toast.makeText(getApplicationContext(),isServerConnected() + "",Toast.LENGTH_SHORT).show();
        butbarras = findViewById(R.id.butbarras);
        TextView tituloTv = findViewById(R.id.tvTitulo);
        tituloTv.setText("CAJAS");
        TextView opcionTv = findViewById(R.id.txtversion);
        TextView NFCtv = findViewById(R.id.tvNFC);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchNFC = findViewById(R.id.switchNFC);
        et= findViewById(R.id.etCode);
        NFCtv.setVisibility(View.INVISIBLE);
        switchNFC.setVisibility(View.INVISIBLE);
        ImageView btnIntro = findViewById(R.id.btnEnter);
        ImageButton btnScanner = findViewById(R.id.btnEscanear);
        et.requestFocus();
        opcionTv.setText("CENTRAL");

        butbarras.setOnClickListener(v -> {
            escritura = !escritura;
            int color;
            if(escritura) {
                color = Color.parseColor("#04A800");

            } else {
                color = Color.parseColor("#535353");
            }
            butdraw = butbarras.getBackground();
            butdraw = DrawableCompat.wrap(butdraw);
            DrawableCompat.setTint(butdraw,color);
        });
       // if(et.isFocusable()) showKeyboard(getApplicationContext());
        et.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        trackingCaja(et.getText().toString().toUpperCase(Locale.ROOT),"texto");
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        btnScanner.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "scanner", Toast.LENGTH_LONG).show();
            InicioScanner(et);
        });

        btnIntro.setOnClickListener(view -> {
            String text = et.getText().toString();
            String txt2 = text.toUpperCase(Locale.ROOT);
            if(text.equals("")) {
                Toast.makeText(getApplicationContext(),"Campo vacío, por favor introduzca el tracking",Toast.LENGTH_SHORT).show();
            } else trackingCaja(txt2,"texto");
        });
    }

    private void InicioScanner(EditText et) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @SuppressLint("SetTextI18n")
    public void trackingCaja(String tracking, String mode){

        System.out.println(tracking);
        Cajas_Local_DB cajasDB = new Cajas_Local_DB(this);
        String nameTrack =  cajasDB.getTracking(tracking);

        Dialog dialog = new Dialog(CajasActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog);

        TextView tvMensaje = dialog.findViewById(R.id.alertMsg);
        ImageView verde = dialog.findViewById(R.id.cajaVerde);
        ImageView rojo = dialog.findViewById(R.id.cajaRoja);
        TextView btScan = dialog.findViewById(R.id.btScan);
        ImageView imgclose = dialog.findViewById(R.id.imgclose);

    if(mode.equals("texto")) {
        if (nameTrack.equals("0")) {
            verde.setVisibility(View.INVISIBLE);
            rojo.setVisibility(View.VISIBLE);
            tvMensaje.setVisibility(View.VISIBLE);
            btScan.setVisibility(View.INVISIBLE);
            if(!escritura)et.setText("");
            tvMensaje.setText("NO SE HA ENCONTRADO LA CAJA");
            dialog.show();

            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    dialog.dismiss();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();

        } else {
            verde.setVisibility(View.VISIBLE);
            rojo.setVisibility(View.INVISIBLE);
            btScan.setVisibility(View.INVISIBLE);
            tvMensaje.setVisibility(View.VISIBLE);

            tvMensaje.setText(nameTrack);

            dialog.show();
        }

    } else {
        if (nameTrack.equals("0")) {
            verde.setVisibility(View.INVISIBLE);
            rojo.setVisibility(View.VISIBLE);
            tvMensaje.setVisibility(View.VISIBLE);
            tvMensaje.setText("NO SE HA ENCONTRADO LA CAJA");
            dialog.show();

            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    dialog.dismiss();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();

        } else {
            verde.setVisibility(View.VISIBLE);
            rojo.setVisibility(View.INVISIBLE);

            tvMensaje.setVisibility(View.VISIBLE);
            tvMensaje.setText(nameTrack);
            dialog.show();
        }
    }

        btScan.setOnClickListener(view -> {
            dialog.dismiss();
            InicioScanner(et);
        });

        imgclose.setOnClickListener(v -> {
            if(!escritura)et.setText("");
            et.requestFocus();
            dialog.dismiss();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String resultado = result.getContents();

        if(resultado != null)trackingCaja(resultado,"scan");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
