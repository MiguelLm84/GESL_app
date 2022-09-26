package com.example.geslapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.camara.EscanerContinuoAct;
import com.example.geslapp.core.clases.Centro;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.requests.EansRequest;
import com.example.geslapp.core.clases.Modelo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class EscanearTiendaActivity extends AppCompatActivity {
    Spinner centros, modelosetq;
    //String centro_position, modelo_position;
    Button btComenzar;
    ArrayList<String> listaCentros, listaIdsCentros, listaCecos, listaModelos, listaTamanos, listaIdsModelos;
    String code = "Scanning Code";
    Centro c = new Centro();
    Modelo m = new Modelo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaneartienda);
        centros = findViewById(R.id.spinerCentro);
        modelosetq = findViewById(R.id.spinerModeloEtq);
        btComenzar = findViewById(R.id.btnComenzar);

        listaCentros = getIntent().getStringArrayListExtra("listaCentros");
        listaIdsCentros = getIntent().getStringArrayListExtra("listaIdsCentros");
        listaCecos = getIntent().getStringArrayListExtra("listaCecos");
        listaModelos = getIntent().getStringArrayListExtra("listaModelos");
        listaIdsModelos = getIntent().getStringArrayListExtra("listaIdsModelos");
        listaTamanos = getIntent().getStringArrayListExtra("listaTamanos");


        centros.setAdapter(new ArrayAdapter<String>(EscanearTiendaActivity.this, android.R.layout.simple_spinner_dropdown_item, listaCentros) {

            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                TextView tv = ((TextView) v);
                tv.setTextColor(getResources().getColor(R.color.blue_20, getTheme()));
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv.setSingleLine();
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setTextSize(22);
                return v;
            }
        });

        modelosetq.setAdapter(new ArrayAdapter<String>(EscanearTiendaActivity.this, android.R.layout.simple_spinner_dropdown_item, listaModelos) {

            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                TextView tv = ((TextView) v);
                tv.setTextColor(getResources().getColor(R.color.blue_20, getTheme()));
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv.setSingleLine();
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setTextSize(22);
                return v;
            }
        });

        centros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String nombre = centros.getSelectedItem().toString();
                System.out.println(nombre);
                c.setNombre(nombre);
                String idcentro = String.valueOf(listaIdsCentros.get(position));
                System.out.println(idcentro);
                c.setId(idcentro);
                String ceco = String.valueOf(listaCecos.get(position));
                System.out.println(ceco);
                c.setCeco(ceco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modelosetq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String nombre = modelosetq.getSelectedItem().toString();
                System.out.println(nombre);
                m.setNombre(nombre);
                String idmodelos = listaIdsModelos.get(position);
                System.out.println(idmodelos);
                m.setId(idmodelos);
                String tamano =listaTamanos.get(position);
                System.out.println(tamano);
                m.setTamano(tamano);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       // btComenzar.setOnClickListener(view -> scanCode());

        btComenzar.setOnClickListener(view -> goToScannerTiendaActivity());
    }

    private void goToScannerTiendaActivity() {

        Intent i = new Intent(this, ScannerTiendaActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void scanCode() {

        IntentIntegrator integrator2 = new IntentIntegrator(this);
        integrator2.setCaptureActivity(EscanerContinuoAct.class);
        integrator2.setOrientationLocked(false);
        integrator2.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator2.setPrompt(code);
        integrator2.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String resultado = result.getContents();
        System.out.println(resultado);
        System.out.println(c.getId()+"       a");
        System.out.println(m.getNombre());
        if (resultado != null) {
            System.out.println(resultado);
            code = resultado;
            Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

            Response.Listener<String> respoListener = response -> {
                try {
                    System.out.println(response);
                    JSONObject jsonResponse = new JSONObject(response);
                    int success = jsonResponse.getInt("success");
                    System.out.println(resultado+"    a");
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(),"Insert realizado exitosamente", Toast.LENGTH_SHORT).show();
                        System.out.println(resultado+"    b");

                    } else {
                        System.out.println(resultado+"    c");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(resultado+"    d");
                }
            };

            ConfigPreferences config = new ConfigPreferences();
            String REC = config.getRec(getApplicationContext());
            String IP = config.getIP(getApplicationContext());
            String PROJECT = config.getProject(getApplicationContext());
            String[] udata = new String[2];
            udata[0] = config.getUsername(getApplicationContext());
            udata[1] = config.getUip(getApplicationContext());
            EansRequest eansRequest = new EansRequest(c.getId(),m.getTamano(), resultado, respoListener,IP,REC,PROJECT,udata[0],udata[1]);

            RequestQueue queue = Volley.newRequestQueue(EscanearTiendaActivity.this);
            queue.add(eansRequest);

            scanCode();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
