package com.example.geslapp.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.geslapp.R;

import com.example.geslapp.core.clases.CheckConnection;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Update;
import com.example.geslapp.core.database.Cajas_Local_DB;
import com.example.geslapp.core.database.Login_Local_DB;
import com.example.geslapp.core.databaseInvent.Estado_Informe_Local_DB;
import com.example.geslapp.core.databaseInvent.Modelos_Etq_Local_DB;
import com.example.geslapp.core.databaseInvent.Tipo_Informe_Local_DB;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcesoPorLotesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    Button butcaja, butetq, butinvent, butscanstore, butmenu, butrecogidas, btnDescargaInventarios;
    TextView txtversion, trackingTv, tvCaja, txtuser;
    String tipo;
    ArrayList<String> listaCentros, listaIps, listaDominios, listaIdsCentros,listaCecos,listaModelos,listaIdsModelos,listaTamanos;
    String version;
    ImageView imgcon;
    private final ConfigPreferences config = new ConfigPreferences();
    private static String IP,REC,PROJECT;
    private static boolean con_invent = false;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_procesoporlotes);
        init();
        getArgs();
        //startCheckConnection();
        buttonEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCheckConnection();
    }

    private void buttonEvents() {

        buttonInvent();
        buttonMenu();
        buttonCaja();
        buttonEtiquetas();
        buttonScanStore();
        buttonRecogidas();
        buttonDownloadInvent();
    }

    private void getArgs() {

        listaCentros = getIntent().getStringArrayListExtra("listaCentros");
        listaIdsCentros = getIntent().getStringArrayListExtra("listaIdsCentros");
        listaCecos = getIntent().getStringArrayListExtra("listaCecos");
        //listaIps = (ArrayList<String>) getIntent().getStringArrayListExtra("listaIps");
        //listaDominios = (ArrayList<String>) getIntent().getStringArrayListExtra("listaDominios");
        listaModelos = getIntent().getStringArrayListExtra("listaModelos");
        listaIdsModelos = getIntent().getStringArrayListExtra("listaIdsModelos");
        listaTamanos = getIntent().getStringArrayListExtra("listaTamanos");
        version= getIntent().getStringExtra("version");
        txtversion.setText(version);
    }

    private void init() {

        butcaja = findViewById(R.id.btnCajas);
        butetq = findViewById(R.id.btnEtq);
        butinvent = findViewById(R.id.btnInventario);
        butscanstore = findViewById(R.id.btnEscanearTienda);
        imgcon = findViewById(R.id.imgCacon1);
        trackingTv = findViewById(R.id.tvTracking);
        tvCaja = findViewById(R.id.tvCaja);
        butmenu = findViewById(R.id.butCamenu2);
        txtuser = findViewById(R.id.txtCauser1);
        butrecogidas = findViewById(R.id.butrecogidos);
        btnDescargaInventarios = findViewById(R.id.btnDescargaInventarios);
        txtversion = findViewById(R.id.txtCversion);
        txtuser.setText(getIntent().getStringExtra("username"));

        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());
        PROJECT = config.getProject(getApplicationContext());

        swipeRefreshLayout = findViewById(R.id.swipeRefreshProcesoPorLotes);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void buttonRecogidas() {

        butrecogidas.setOnClickListener(v -> {
            Intent t = new Intent(ProcesoPorLotesActivity.this,RecogidasActivity.class);
            startActivity(t);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
    }

    private void buttonScanStore() {

        butscanstore.setOnClickListener(v -> {
            Intent intent = new Intent(ProcesoPorLotesActivity.this, EscanearTiendaActivity.class);
            intent.putStringArrayListExtra("listaCentros", listaCentros);
            intent.putStringArrayListExtra("listaIdsCentros", listaIdsCentros);
            intent.putStringArrayListExtra("listaCecos", listaCecos);
            intent.putStringArrayListExtra("listaModelos", listaModelos);
            intent.putStringArrayListExtra("listaTamanos", listaTamanos);
            intent.putStringArrayListExtra("listaIdsModelos", listaIdsModelos);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
    }

    private void buttonEtiquetas() {

        butetq.setOnClickListener(view -> {
            Intent t = new Intent(ProcesoPorLotesActivity.this,EtiquetasActivity.class);
            startActivity(t);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
    }

    private void buttonCaja() {

        butcaja.setOnClickListener(view -> {
            tipo = "caja";
            Intent scannercaja = new Intent(ProcesoPorLotesActivity.this,CajasActivity.class);
            scannercaja.putExtra("tipo",tipo);
            startActivity(scannercaja);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
    }

    private void buttonMenu() {

        butmenu.setOnClickListener(v -> showDialog());
    }

    private void buttonInvent() {

        butinvent.setOnClickListener(v -> {

            Intent t = new Intent(ProcesoPorLotesActivity.this, Activity_Dashboard.class);
            t.putStringArrayListExtra("listaCentros", listaCentros);
            t.putStringArrayListExtra("listaIdsCentros", listaIdsCentros);
            t.putStringArrayListExtra("listaCecos", listaCecos);
            t.putExtra("con_invent",con_invent);
            Estado_Informe_Local_DB estado_informe_local_db = new Estado_Informe_Local_DB(getApplicationContext());
            if(con_invent)estado_informe_local_db.fillData(getApplicationContext());
            Tipo_Informe_Local_DB tipo_informe_local_db = new Tipo_Informe_Local_DB(getApplicationContext());
            if(con_invent)tipo_informe_local_db.fillData(getApplicationContext());
            Modelos_Etq_Local_DB modelos_etq_local_db = new Modelos_Etq_Local_DB(getApplicationContext());
            if(con_invent)modelos_etq_local_db.fillData(getApplicationContext());
            t.putExtra("username",config.getUsername(getApplicationContext()));
            startActivity(t);
        });
    }

    private void buttonDownloadInvent() {

        btnDescargaInventarios.setOnClickListener(view -> downLoadInvents());
    }

    private void downLoadInvents() {


    }

    private void startCheckConnection() {

        new Update().cancel(true);
        /*new Handler().postDelayed(() -> {
            new CheckConnection().execute("http://"+IP+"/gesl/"+REC+"/");
        }, 500);*/

        new CheckConnection().execute("http://"+IP+"/"+PROJECT+"/"+REC+"/");
        /*Explode explode = new Explode();
        explode.setDuration(500);*/

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String serverResponse = new CheckConnection().getServerResponse();
        if (serverResponse == null) {
            new CheckConnection().execute("http://"+IP+"/"+PROJECT+"/"+REC+"/");
            /*Explode explode2 = new Explode();
            explode2.setDuration(500);*/
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (serverResponse == null) {
                config.setCon(getApplicationContext(),false);
                int color = Color.parseColor("#737373");
                con_invent = false;

                butetq.setEnabled(false);
                butetq.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

                butscanstore.setEnabled(false);
                butscanstore.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

                butrecogidas.setEnabled(false);
                butrecogidas.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

                butinvent.setEnabled(true);
                butinvent.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

                btnDescargaInventarios.setEnabled(false);
                btnDescargaInventarios.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

            } else {
                config.setLastCon(getApplicationContext());
                int color = Color.parseColor("#000000");
                config.setCon(getApplicationContext(),true);
                new CheckConnection().setServer_response();
                con_invent = true;
                config.setVTables(getApplicationContext());

                butinvent.setEnabled(true);
                butinvent.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

                butetq.setEnabled(true);
                butetq.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

                butscanstore.setEnabled(true);
                butscanstore.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

                butrecogidas.setEnabled(true);
                butrecogidas.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

                btnDescargaInventarios.setEnabled(true);
                btnDescargaInventarios.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);
            }

        } else {
            config.setLastCon(getApplicationContext());
            int color = Color.parseColor("#000000");
            config.setCon(getApplicationContext(),true);
            new CheckConnection().setServer_response();
            con_invent = true;
            config.setVTables(getApplicationContext());

            butinvent.setEnabled(true);
            butinvent.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

            butetq.setEnabled(true);
            butetq.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

            butscanstore.setEnabled(true);
            butscanstore.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

            butrecogidas.setEnabled(true);
            butrecogidas.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);

            btnDescargaInventarios.setEnabled(true);
            btnDescargaInventarios.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);
        }

        if(config.getCon(getApplicationContext())) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.online_2);
            imgcon.setImageBitmap(bitmap);

        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.offline_2);
            imgcon.setImageBitmap(bitmap);
        }
    }

    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet);
        LinearLayout configlayout = dialog.findViewById(R.id.layoutconfig);
        LinearLayout rechargelayout = dialog.findViewById(R.id.layoutrecharge);
        LinearLayout infolayout = dialog.findViewById(R.id.layoutinfo3);
        LinearLayout exitlayout = dialog.findViewById(R.id.layoutexit);
        LinearLayout actulayout = dialog.findViewById(R.id.layoutactu);
        TextView txtvtables = dialog.findViewById(R.id.txtVtablas);
        TextView txtlastupdate = dialog.findViewById(R.id.txtVapp);
        txtlastupdate.setText(config.getLastAppUpdate(getApplicationContext()));
        txtvtables.setText(config.getVTables(getApplicationContext()));

        infolayout.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://asysgon.com");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        });
        actulayout.setOnClickListener(v -> {
            startCheckConnection();
            if(config.getCon(getApplicationContext())) {
                if (checkPermission()) {
                    new CheckConnection().cancel(true);
                    Update atualizaApp = new Update();

                    atualizaApp.setContext(ProcesoPorLotesActivity.this);
                    ConfigPreferences config = new ConfigPreferences();
                    String IP = config.getIP(getApplicationContext());
                    String REC = config.getRec(getApplicationContext());

                    boolean updated = atualizaApp.getUpdated();
                    new Handler().postDelayed(() -> {
                        atualizaApp.execute("http://"+IP+"/gesl/"+REC+"/app-release.apk");
                    }, 2000);

                    /*try {
                        Thread.sleep(2000);
                        startCheckConnection();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

                    if(updated) {
                        config.setLastAppUpdate(getApplicationContext());
                    }
                    txtlastupdate.setText(config.getLastAppUpdate(getApplicationContext()));

                } else {
                    requestPermission();
                }
            } else {
                new CheckConnection().cancel(true);
                Toast.makeText(ProcesoPorLotesActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();
            }
        });
        rechargelayout.setOnClickListener(v -> rechargeTables(txtvtables));

        configlayout.setOnClickListener(v -> showConfigDialog());

        exitlayout.setOnClickListener(v -> {
            Intent t = new Intent(ProcesoPorLotesActivity.this, LoginActivity.class);
            startActivity(t);
            overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
            finish();
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void rechargeTables(TextView tvTables) {

        new Handler().postDelayed(() -> {
            new CheckConnection().execute("http://"+IP+"/"+PROJECT+"/"+REC+"/");
        }, 1000);

        /*new CheckConnection().execute("http://"+IP+"/gesl/"+REC+"/");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        String serverResponse = new CheckConnection().getServerResponse();

        if (serverResponse!= null) {
            new CheckConnection().setServer_response();
            Cajas_Local_DB cajas_local_db = new Cajas_Local_DB(getApplicationContext());
            Login_Local_DB login_local_db = new Login_Local_DB(getApplicationContext());
            cajas_local_db.deleteRows();
            cajas_local_db.getDBCajas(getApplicationContext(),IP,REC,PROJECT);
            login_local_db.deleteRows();
            login_local_db.getDBUser(getApplicationContext());
            config.setVTables(getApplicationContext());
            tvTables.setText(config.getVTables(getApplicationContext()));
            Toast.makeText(getApplicationContext(),"LAS TABLAS HAN SIDO CARGADAS",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(),"NO HAY CONEXION CON EL SERVIDOR",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showConfigDialog() {

        final Dialog dialog = new Dialog(ProcesoPorLotesActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomconfig);
        EditText edtip = dialog.findViewById(R.id.edtip);
        EditText edtrec = dialog.findViewById(R.id.edtrecurso);
        EditText edtproject = dialog.findViewById(R.id.edtproject);
        Button butsave = dialog.findViewById(R.id.butCsave);
        edtip.setText(IP);
        edtrec.setText(REC);
        edtproject.setText(PROJECT);
        TextView txtversion = dialog.findViewById(R.id.txtVconfig);
        txtversion.setText(version + "");

        butsave.setOnClickListener(v -> {
            String ip = edtip.getText().toString();
            String rec = edtrec.getText().toString();
            String project = edtproject.getText().toString();

            if(isValidIPAddress(ip)) {
                IP = ip;
                REC = rec;
                PROJECT = project;
                new CheckConnection().cancel(true);
                new CheckConnection().execute("http://"+ip+"/"+project+"/"+rec+"/");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String serverResponse = new CheckConnection().getServerResponse();

                if (serverResponse == null) {
                    Toast.makeText(ProcesoPorLotesActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();
                }
                else {
                    config.createPreferences(getApplicationContext(),ip,rec,project);
                    Toast.makeText(ProcesoPorLotesActivity.this, "Configuración guardada", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
            else Toast.makeText(ProcesoPorLotesActivity.this, "Datos erróneos", Toast.LENGTH_SHORT).show();

        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }


    public static boolean isValidIPAddress(String ip) {

        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        Pattern p = Pattern.compile(regex);

        if (ip == null) {
            return false;
        }

        Matcher m = p.matcher(ip);

        return m.matches();
    }

    private boolean checkPermission() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    @Override
    public void onRefresh() {

    }
}

