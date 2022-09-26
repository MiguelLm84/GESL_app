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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.camara.CaptureAct;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.requests.RecogidasRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Locale;
import java.util.StringTokenizer;



public class RecogidasActivity extends AppCompatActivity {
    EditText edtcode;
    EditText codigoCaja;
    Button butbarras;

    boolean escritura = false;
    private final ConfigPreferences config = new ConfigPreferences();
    private static String IP,REC,PROJECT;
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
        tituloTv.setText("RECOGIDA");
        TextView opcionTv = findViewById(R.id.txtversion);
        TextView NFCtv = findViewById(R.id.tvNFC);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchNFC = findViewById(R.id.switchNFC);
        TextView txtinfo = findViewById(R.id.txtinfo);
        txtinfo.setText("Pulsa el botton 'ESCANEAR' o escriba el código del RMA");
        edtcode = findViewById(R.id.etCode);
        NFCtv.setVisibility(View.INVISIBLE);
        switchNFC.setVisibility(View.INVISIBLE);
        ImageView btnIntro = findViewById(R.id.btnEnter);
        ImageButton btnScanner = findViewById(R.id.btnEscanear);

        opcionTv.setText("CENTRAL");

        butbarras.setOnClickListener(v -> {
            escritura = !escritura;
            if(escritura) {
                int color = Color.parseColor("#04A800");
                Drawable butdraw = butbarras.getBackground();
                butdraw = DrawableCompat.wrap(butdraw);
                DrawableCompat.setTint(butdraw,color);

            } else {
                int color = Color.parseColor("#535353");
                Drawable butdraw = butbarras.getBackground();
                butdraw = DrawableCompat.wrap(butdraw);
                DrawableCompat.setTint(butdraw,color);
            }
        });

        edtcode.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        trackingCaja(edtcode.getText().toString().toUpperCase(Locale.ROOT));
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        btnScanner.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "scanner", Toast.LENGTH_LONG).show();
            InicioScanner(edtcode);
        });

        btnIntro.setOnClickListener(view -> {
            String text = edtcode.getText().toString();
            String txt2 = text.toUpperCase(Locale.ROOT);
            if(text.equals(""))
            {
                Toast.makeText(getApplicationContext(),"Campo vacío, por favor introduzca el tracking",Toast.LENGTH_SHORT).show();
            }else trackingCaja(txt2);

        });

    }//end onCreate

    private void InicioScanner(EditText et) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    public void trackingCaja(String tracking) {


        @SuppressLint("SetTextI18n") Response.Listener<String> respoListener2 = response2 -> {
            Dialog dialog = new Dialog(RecogidasActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.custom_dialog);

            TextView tvMensaje = dialog.findViewById(R.id.alertMsg);
            ImageView verde = dialog.findViewById(R.id.cajaVerde);
            ImageView rojo = dialog.findViewById(R.id.cajaRoja);
            TextView btScan = dialog.findViewById(R.id.btScan);
            ImageView imgclose = dialog.findViewById(R.id.imgclose);
            if(escritura)btScan.setVisibility(View.INVISIBLE);
            btScan.setOnClickListener(v -> {
                scanCode();
                dialog.dismiss();
            });
            imgclose.setOnClickListener(v -> dialog.dismiss());

            try {

                JSONObject jsonResponse = new JSONObject(response2);
                int success = jsonResponse.getInt("success");
                System.out.println(success);
                System.out.println(response2);
                if (success == 1) {
                    verde.setVisibility(View.VISIBLE);
                    rojo.setVisibility(View.INVISIBLE);
                    btScan.setVisibility(View.INVISIBLE);
                    tvMensaje.setVisibility(View.VISIBLE);

                    tvMensaje.setText("RECOGIDA REGISTRADA EN CENTRAL");

                    dialog.show();

                } else {
                    verde.setVisibility(View.INVISIBLE);
                    rojo.setVisibility(View.VISIBLE);
                    tvMensaje.setVisibility(View.VISIBLE);
                    btScan.setVisibility(View.INVISIBLE);
                    if(!escritura)edtcode.setText("");
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
                    t.start();}

            } catch (JSONException e) {
                verde.setVisibility(View.INVISIBLE);
                rojo.setVisibility(View.VISIBLE);
                tvMensaje.setVisibility(View.VISIBLE);
                btScan.setVisibility(View.INVISIBLE);
                if(!escritura)edtcode.setText("");
                tvMensaje.setText("NO SE HA ENCONTRADO LA CAJA");
                dialog.show();
                Thread t = new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        dialog.dismiss();

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                });
                t.start();
            }
            dialog.show();
        };

        String username = config.getUsername(getApplicationContext());
        String ip = config.getUip(getApplicationContext());

        RecogidasRequest trackingRequest = new RecogidasRequest(respoListener2,tracking, IP, REC, PROJECT, username, ip);

        RequestQueue queue2 = Volley.newRequestQueue(RecogidasActivity.this);
        queue2.add(trackingRequest);
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            StringTokenizer st = new StringTokenizer(result.getContents());
            String response = st.nextToken(";");
            trackingCaja(response);
            Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

}