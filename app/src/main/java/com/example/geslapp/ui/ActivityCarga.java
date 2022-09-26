package com.example.geslapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.database.Cajas_Local_DB;
import com.example.geslapp.core.database.Login_Local_DB;
import com.example.geslapp.core.databaseInvent.Antenas_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Antenas_Local_DB;
import com.example.geslapp.core.databaseInvent.Estado_Informe_Local_DB;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Fotos_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.General_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import com.example.geslapp.core.databaseInvent.Material_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Modelos_Etq_Local_DB;
import com.example.geslapp.core.databaseInvent.Tipo_Informe_Local_DB;
import com.example.geslapp.core.requests.LoginRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;


public class ActivityCarga extends AppCompatActivity {

    ArrayList<String> listaCentros = new ArrayList<>();
    ArrayList<String> listaIdsCentros = new ArrayList<>();
    ArrayList<String> listaCecos = new ArrayList<>();
    ArrayList<String> listaIps= new ArrayList<>();
    ArrayList<String> listaDominios = new ArrayList<>();

    ArrayList<String> listaModelos = new ArrayList<>();
    ArrayList<String> listaTamanos = new ArrayList<>();
    ArrayList<String> listaIdsModelos= new ArrayList<>();
    //ProgressBar pb = findViewById(R.id.pbCarga);

    private static String version;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_carga);
        getAllData();
    }

    private void getAllData() {

        ConfigPreferences config = new ConfigPreferences();
        String IP = config.getIP(getApplicationContext());
        String REC = config.getRec(getApplicationContext());
        String PROJECT = config.getProject(getApplicationContext());
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        Intent intent = new Intent(ActivityCarga.this, MenuActivity.class);
        final Boolean[] t = {false,false,false};

        Response.Listener<String> respoListener = response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                int success = jsonResponse.getInt("success");

                if (success == 1) {
                    StringRequest modeloRequest = new StringRequest(Request.Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/modeloetq.php",
                            response1 -> {
                                try {
                                    JSONObject jsonObject = new JSONObject(response1);
                                    JSONArray jsonArray = jsonObject.getJSONArray("array");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        String modelo = jsonObject1.getString("modelo");
                                        String tamano = jsonObject1.getString("tamaño");
                                        String id = String.valueOf(jsonObject1.getInt("id"));
                                        if(modelo != null || modelo.isEmpty()){
                                            listaModelos.add(modelo);
                                            System.out.println(modelo);
                                        } else {
                                            Toast.makeText(this, modelo, Toast.LENGTH_SHORT).show();
                                        }
                                        listaIdsModelos.add(id);
                                        System.out.println(id);
                                        listaTamanos.add(tamano);
                                        System.out.println(tamano);

                                    }
                                    String listaN = Arrays.toString(listaModelos.toArray());
                                    System.out.println(listaN);
                                    String listaId = Arrays.toString(listaIdsModelos.toArray());
                                    System.out.println(listaId);
                                    intent.putStringArrayListExtra("listaModelos", listaModelos);
                                    intent.putStringArrayListExtra("listaTamanos", listaTamanos);
                                    intent.putStringArrayListExtra("listaIdsModelos", listaIdsModelos);
                                    //Toast.makeText(getApplicationContext(), "Lista modelos" + listaN, Toast.LENGTH_LONG).show();
                                    //Toast.makeText(getApplicationContext(), "Lista ids" + listaId, Toast.LENGTH_LONG).show();
                                    t[1] = true;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, Throwable::printStackTrace);
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//stringRequest.setRetryPolicy(policy);
                    RequestQueue requestQueue3 = Volley.newRequestQueue(ActivityCarga.this);
                    requestQueue3.add(modeloRequest);

                    StringRequest versiongesl = new StringRequest(Request.Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/version.php",
                            response13 -> {
                                try {
                                    JSONObject jsonResponse1 = new JSONObject(response13);
                                    String v = jsonResponse1.getString("version");
                                    version = "GESL V." + v;
                                    intent.putExtra("version", version);
                                    config.setVApp(getApplicationContext(),version);
                                    //versionTv.setText(version);
                                    // Toast.makeText(getApplicationContext(), "version " + version, Toast.LENGTH_LONG).show();

                                    t[2] = true;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, Throwable::printStackTrace);
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(policy);
                    RequestQueue requestQueue2 = Volley.newRequestQueue(ActivityCarga.this);
                    requestQueue2.add(versiongesl);


                    StringRequest centroRequest = new StringRequest(Request.Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/centros.php",
                            response12 -> {
                                try {
                                    JSONObject jsonObject = new JSONObject(response12);
                                    JSONArray jsonArray = jsonObject.getJSONArray("array");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        String nombre = jsonObject1.getString("nombre");
                                        String id = String.valueOf(jsonObject1.getInt("id"));
                                        String ceco = String.valueOf(jsonObject1.getInt("ceco"));
                                        String ip = jsonObject1.getString("ip");
                                        String dominio = jsonObject1.getString("domain");
                                        listaCentros.add(nombre);
                                        System.out.println(nombre);
                                        listaIdsCentros.add(id);
                                        System.out.println(id);
                                        listaCecos.add(ceco);
                                        System.out.println(ceco);
                                        listaIps.add(ip);
                                        System.out.println(ip);
                                        listaDominios.add(dominio);
                                        System.out.println(dominio);
                                    }

                                    String listaN = Arrays.toString(listaCentros.toArray());
                                    System.out.println(listaN);
                                    String listaId = Arrays.toString(listaIdsCentros.toArray());
                                    System.out.println(listaId);

                                    int ruben = 0;
                                    intent.putStringArrayListExtra("listaCentros", listaCentros);
                                    if(!listaCentros.isEmpty()){
                                        ruben = 1;
                                    }
                                    intent.putStringArrayListExtra("listaIdsCentros", listaIdsCentros);
                                    intent.putStringArrayListExtra("listaCecos", listaCecos);
                                    intent.putStringArrayListExtra("listaIps", listaIps);
                                    intent.putStringArrayListExtra("listaDominios", listaDominios);
                                    intent.putExtra("password", password);
                                    intent.putExtra("username", username);
                                    t[0] = true;

                                    listaCentros = null;
                                    listaIdsCentros = null;
                                    listaCecos = null;
                                    listaIps = null;
                                    listaDominios = null;
                                    listaModelos = null;
                                    listaTamanos = null;
                                    listaIdsModelos = null;

                                    config.setCon(getApplicationContext(),true);
                                    config.setLastCon(getApplicationContext());

                                    /*Handler handler = new Handler();
                                    handler.postDelayed(() -> {
                                        if(t[0] && t[2] && t[1]) {
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            finish();
                                        }
                                    },500);*/

                                    if(ruben == 1){
                                        Handler handler = new Handler();
                                        handler.postDelayed(() -> {
                                            if(t[0] && t[2] && t[1]) {
                                                startActivity(intent);
                                                finish();
                                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            }
                                        },3500);

                                        /*if(t[0] && t[2] && t[1]) {
                                            startActivity(intent);
                                            finish();
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        }*/

                                    } else {
                                        Toast.makeText(this, "No se han podido cargar los datos del servidor", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, Throwable::printStackTrace);
                    RequestQueue requestQueue = Volley.newRequestQueue(ActivityCarga.this);
                    requestQueue.add(centroRequest);


                    //  Login_Local_DB logindb = new Login_Local_DB(getApplicationContext());
                    //4 logindb.setUser(username,password);

                    Toast.makeText(getApplicationContext(), "Logeo correcto...", Toast.LENGTH_LONG).show();

                } else if (success == 2) {
                    Login_Local_DB login_local_db = new Login_Local_DB(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "El usuario no esta en la BD", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCarga.this);
                    builder.setMessage("El usuario no tiene permisos")
                            .setNegativeButton("Reintentar", null)
                            .create().show();
                    Cajas_Local_DB cajasdb = new Cajas_Local_DB(getApplicationContext());
                    cajasdb.deleteRows();
                    login_local_db.deleteRows();
                    Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(getApplicationContext());
                    inventario_local_db.deleteRows();

                    Material_Invent_Local_DB material_invent_local_db = new Material_Invent_Local_DB(getApplicationContext());
                    material_invent_local_db.deleteRows();

                    Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(getApplicationContext());
                    etqs_invent_local_db.deleteRows();

                    General_Invent_Local_DB general_invent_local_db = new General_Invent_Local_DB(getApplicationContext());
                    general_invent_local_db.deleteRows();

                    Antenas_Invent_Local_DB antenas_invent_local_db = new Antenas_Invent_Local_DB(getApplicationContext());
                    antenas_invent_local_db.delteRows();

                    Antenas_Local_DB antenas_local_db = new Antenas_Local_DB(getApplicationContext());
                    antenas_local_db.delteRows();

                    Estado_Informe_Local_DB estado_informe_local_db = new Estado_Informe_Local_DB(getApplicationContext());
                    estado_informe_local_db.deleteRows();

                    Fotos_Invent_Local_DB fotos_invent_local_db = new Fotos_Invent_Local_DB(getApplicationContext());
                    fotos_invent_local_db.deleteRows();

                    Modelos_Etq_Local_DB modelos_etq_local_db = new Modelos_Etq_Local_DB(getApplicationContext());
                    modelos_etq_local_db.deleteRows();

                    Tipo_Informe_Local_DB tipo_informe_local_db = new Tipo_Informe_Local_DB(getApplicationContext());
                    tipo_informe_local_db.deleteRows();


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCarga.this);
                    builder.setMessage("Credenciales incorrectas")
                            .setNegativeButton("Reintentar", null)
                            .create().show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "NO SE HA ESTABLECIDO LA CONEXION CON LA BASE DE DATOS", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        };
        config.setUserip(getApplicationContext(),username);
        String ip = config.getUip(getApplicationContext());


        LoginRequest loginrequest = new LoginRequest(username, password, respoListener,IP,REC,PROJECT,ip);

        RequestQueue queue = Volley.newRequestQueue(ActivityCarga.this);
        queue.add(loginrequest);
    }
}
