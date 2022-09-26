package com.example.geslapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.camara.CaptureAct;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Etiqueta;
import com.example.geslapp.core.requests.EtqContenidoApiRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Locale;


public class EtqContenidoActivity extends AppCompatActivity {

    String media,centro, ip, dominio;
    private static String IP,REC,PROJECT;
    ConfigPreferences config = new ConfigPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescanner);
        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());
        PROJECT = config.getProject(getApplicationContext());
        TextView tituloTv = (TextView) findViewById(R.id.tvTitulo);
        centro = (String)getIntent().getStringExtra("centro");
        tituloTv.setText(centro);

        EditText et= findViewById(R.id.etCode);

        ImageView btEnter= findViewById(R.id.btnEnter);
        ImageButton botonEscanear = findViewById(R.id.btnEscanear);


        ip=(String) getIntent().getStringExtra("ip").trim();
        dominio=(String) getIntent().getStringExtra("dominio").trim();


        botonEscanear.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "scanner", Toast.LENGTH_LONG).show();
            scanCode();
        });

        btEnter.setOnClickListener(view -> {
            media = et.getText().toString().trim().toUpperCase(Locale.ROOT);
            Toast.makeText(getApplicationContext(), "click", Toast.LENGTH_LONG).show();
            Response.Listener<String> respoListener = response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    System.out.println(response);
                    int success = jsonResponse.getInt("success");
                    System.out.println(success);
                    if (success == 1) {
                        String id = jsonResponse.getString("id_item");
                        System.out.println(id);
                        String item = jsonResponse.getString("name");
                        System.out.println(item);
                        String precio = jsonResponse.getString("price");
                        System.out.println(precio);

                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };

            System.out.println(media);
            System.out.println(ip);
            System.out.println(dominio);
            String username = config.getUsername(getApplicationContext());
            String ip = config.getUip(getApplicationContext());

            EtqContenidoApiRequest etqContentRequest = new EtqContenidoApiRequest(media,ip, dominio,respoListener,IP,REC,PROJECT,username,ip);

            //EtqContenidoApiRequest etqContentRequest = new EtqContenidoApiRequest("BC0177B2","10.1.135.210", "laboratorio.3626", respoListener);
            RequestQueue queue = Volley.newRequestQueue(EtqContenidoActivity.this);
            queue.add(etqContentRequest);

        });
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
            Etiqueta e= new Etiqueta(result.getContents());
            media= result.getContents().trim();

            Response.Listener<String> respoListener = response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    System.out.println(response);
                    int success = jsonResponse.getInt("success");
                    System.out.println(success);
                    if (success == 1) {
                        String id = jsonResponse.getString("id_item");
                        System.out.println(id);
                        String item = jsonResponse.getString("name");
                        System.out.println(item);
                        String precio = jsonResponse.getString("price");
                        System.out.println(precio);

                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            };

            System.out.println(e.getMedia());
            System.out.println(ip);
            System.out.println(dominio);
            String username = config.getUsername(getApplicationContext());
            String ip = config.getUip(getApplicationContext());

            EtqContenidoApiRequest etqContentRequest = new EtqContenidoApiRequest(media,ip,dominio,respoListener,IP,REC,PROJECT,username,ip);

            //EtqContenidoApiRequest etqContentRequest = new EtqContenidoApiRequest("BC0177B2","10.1.135.210", "laboratorio.3626", respoListener);
            RequestQueue queue = Volley.newRequestQueue(EtqContenidoActivity.this);
            queue.add(etqContentRequest);

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
    }
}
