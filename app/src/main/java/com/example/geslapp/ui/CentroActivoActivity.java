package com.example.geslapp.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.geslapp.R;
import com.example.geslapp.core.clases.CheckConnection;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Update;
import com.example.geslapp.core.database.Cajas_Local_DB;
import com.example.geslapp.core.database.Login_Local_DB;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CentroActivoActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    Button botonInventario, botonEtq, botonEtqEstado, butmenu;
    TextView versionTv, trackingTv, tvCaja, txtser;
    String version, centro, ip, dominio;
    ImageView imgcon;
    private String IP, REC, PROJECT;
    private final ConfigPreferences config = new ConfigPreferences();
    private String serverResponse;
    private SwipeRefreshLayout swipeRefreshLayout;
    int ruben;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centroactivo);

        botonInventario = findViewById(R.id.btnInventarioInfo);
        botonEtq = findViewById(R.id.btnEtqInfo);
        botonEtqEstado = findViewById(R.id.btnEtqEstado);
        versionTv = findViewById(R.id.txtCversion);
        version= getIntent().getStringExtra("version");
        versionTv.setText(version);
        trackingTv = findViewById(R.id.tvTracking);
        tvCaja = findViewById(R.id.tvCaja);
        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());
        PROJECT = config.getProject(getApplicationContext());
        imgcon = findViewById(R.id.imgCacon1);
        butmenu = findViewById(R.id.butCamenu2);
        txtser = findViewById(R.id.txtCauser1);
        txtser.setText(getIntent().getStringExtra("username"));
        centro= getIntent().getStringExtra("centro");
        ip=  getIntent().getStringExtra("ip");
        dominio= getIntent().getStringExtra("dominio");

        startCheckConnection();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshCentroActivo);
        swipeRefreshLayout.setOnRefreshListener(this);

        botonInventario.setOnClickListener(view -> Toast.makeText(getApplicationContext(), botonInventario.getText()+" No Disponible", Toast.LENGTH_LONG).show());
        botonEtq.setOnClickListener(view -> {
            Intent intent = new Intent(CentroActivoActivity.this, EtiquetasCentros.class);
            intent.putExtra("centro", centro);
            intent.putExtra("ip", ip);
            intent.putExtra("dominio", dominio);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);


        });

        botonEtqEstado.setOnClickListener(view -> {
           Intent t = new Intent(CentroActivoActivity.this,EstadoEtq.class);
           t.putExtra("centro",centro);
           t.putExtra("ip",ip);
           t.putExtra("dominio",dominio);
           startActivity(t);
           overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        });
        butmenu.setOnClickListener(v -> showDialog());
    }

    private void showDialog() {

        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());
        PROJECT = config.getProject(getApplicationContext());
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
        txtvtables.setText(config.getVTables(getApplicationContext()));

        actulayout.setOnClickListener(v -> {
            startCheckConnection();
            if(config.getCon(getApplicationContext())) {

                if (checkPermission()) {
                    new CheckConnection().cancel(true);
                    Update atualizaApp = new Update();
                    atualizaApp.setContext(CentroActivoActivity.this);
                    ConfigPreferences config = new ConfigPreferences();
                    String IP = config.getIP(getApplicationContext());
                    String REC = config.getRec(getApplicationContext());
                    atualizaApp.execute("http://"+IP+"/gesl/"+REC+"/app-release.apk");
                    boolean updated = atualizaApp.getUpdated();
                    try {
                        Thread.sleep(2000);
                        startCheckConnection();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(updated) {
                        config.setLastAppUpdate(getApplicationContext());
                    }
                    txtlastupdate.setText(config.getLastAppUpdate(getApplicationContext()));

                } else {
                    requestPermission();
                }

            } else {
                new CheckConnection().cancel(true);
                Toast.makeText(CentroActivoActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();
            }

        });
        infolayout.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://asysgon.com");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        });
        rechargelayout.setOnClickListener(v -> {

            new CheckConnection().execute("http://"+IP+"/"+PROJECT+"/"+REC+"/");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            serverResponse = new CheckConnection().getServerResponse();
            if (serverResponse == null) {
                new CheckConnection().execute("http://"+IP+"/"+PROJECT+"/"+REC+"/");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
               ruben = 0;
            } else {
               ruben = 1;
            }

            serverResponse = new CheckConnection().getServerResponse();
            if (serverResponse!= null || ruben == 1) {
                new CheckConnection().setServer_response();
                Cajas_Local_DB cajas_local_db = new Cajas_Local_DB(getApplicationContext());
                Login_Local_DB login_local_db = new Login_Local_DB(getApplicationContext());
                cajas_local_db.deleteRows();
                cajas_local_db.getDBCajas(getApplicationContext(),IP, REC, PROJECT);
                login_local_db.deleteRows();
                login_local_db.getDBUser(getApplicationContext());
                config.setVTables(getApplicationContext());
                txtvtables.setText(config.getVTables(getApplicationContext()));
                Toast.makeText(getApplicationContext(),"LAS TABLAS HAN SIDO CARGADAS",Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(),"NO HAY CONEXION CON EL SERVIDOR",Toast.LENGTH_SHORT).show();
            }
        });
        configlayout.setOnClickListener(v -> showConfigDialog());
        exitlayout.setOnClickListener(v -> {
            Intent t = new Intent(CentroActivoActivity.this, LoginActivity.class);
            startActivity(t);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

    }

    private void showConfigDialog() {

        final Dialog dialog = new Dialog(CentroActivoActivity.this);
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
        txtversion.setText(version);

        butsave.setOnClickListener(v -> {
            String ip = edtip.getText().toString();
            String rec = edtrec.getText().toString();
            String project = edtproject.getText().toString();

            if(isValidIPAddress(ip)) {
                IP = ip;
                REC = rec;
                PROJECT = project;
                new CheckConnection().cancel(true);
                new CheckConnection().execute("http://"+ip+"/"+PROJECT+"/"+rec+"/");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String serverResponse = new CheckConnection().getServerResponse();

                if (serverResponse == null) {

                    Toast.makeText(CentroActivoActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();

                } else {
                    config.createPreferences(getApplicationContext(),ip,rec,project);
                    Toast.makeText(CentroActivoActivity.this, "Configuración guardada", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
            else Toast.makeText(CentroActivoActivity.this, "Datos erróneos", Toast.LENGTH_SHORT).show();
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


    private void startCheckConnection() {

        new Update().cancel(true);
        new CheckConnection().execute("http://"+IP+"/"+PROJECT+"/"+REC+"/");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        serverResponse = new CheckConnection().getServerResponse();
        if (serverResponse == null) {
            int color = Color.parseColor("#737373");
            config.setCon(getApplicationContext(),false);
            botonEtq.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);
            botonEtqEstado.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);
            botonInventario.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);
            botonEtq.setEnabled(false);
            botonInventario.setEnabled(false);
            botonEtqEstado.setEnabled(false);

        } else {
            int colorBlack = Color.parseColor("#FF000000");
            botonEtq.getBackground().setColorFilter(colorBlack, PorterDuff.Mode.ADD);
            botonEtqEstado.getBackground().setColorFilter(colorBlack, PorterDuff.Mode.ADD);
            botonInventario.getBackground().setColorFilter(colorBlack, PorterDuff.Mode.ADD);
            botonEtq.setEnabled(true);
            botonInventario.setEnabled(true);
            botonEtqEstado.setEnabled(true);
            config.setLastCon(getApplicationContext());
            config.setCon(getApplicationContext(),true);
            new CheckConnection().setServer_response();
            config.setVTables(getApplicationContext());
        }

        if(config.getCon(getApplicationContext())) {

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.online_2);
            imgcon.setImageBitmap(bitmap);

        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.offline_2);
            imgcon.setImageBitmap(bitmap);
        }
    }
    protected void onResume() {
        super.onResume();
        startCheckConnection();
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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    @Override
    public void onRefresh() {

        startCheckConnection();
        swipeRefreshLayout.setRefreshing(false);
    }
}