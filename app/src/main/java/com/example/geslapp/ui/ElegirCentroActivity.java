package com.example.geslapp.ui;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ElegirCentroActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final  int PERMISSION_REQUEST_CODE = 200;
    Spinner centros;
    Button btComenzar, butmenu;
    ArrayList<String> listaCentros, listaIps, listaDominios;
    String version;
    ImageView imgcon;
    String username;
    TextView txtuser, txtversion;
    String serverResponse;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String IP, REC, PROJECT;
    private final ConfigPreferences config = new ConfigPreferences();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegircentro);

        centros = findViewById(R.id.spinerCentro2);
        btComenzar = findViewById(R.id.btnComenzar2);
        txtuser = findViewById(R.id.txtCauser1);
        listaCentros =  getIntent().getStringArrayListExtra("listaCentros");
        listaIps =  getIntent().getStringArrayListExtra("listaIps");
        listaDominios = getIntent().getStringArrayListExtra("listaDominios");
        version= getIntent().getStringExtra("version");
        butmenu = findViewById(R.id.butCamenu2);
        username = getIntent().getStringExtra("username");
        txtuser.setText(username);
        txtversion = findViewById(R.id.txtCversion);
        txtversion.setText(version);
        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());
        PROJECT = config.getProject(getApplicationContext());
        Intent intent = new Intent(ElegirCentroActivity.this, CentroActivoActivity.class);
        intent.putExtra("version", version);
        imgcon = findViewById(R.id.imgCacon1);

        startCheckConnection();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshCentro);
        swipeRefreshLayout.setOnRefreshListener(this);

        centros.setAdapter(new ArrayAdapter<String>(ElegirCentroActivity.this, android.R.layout.simple_spinner_dropdown_item, listaCentros) {

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
                intent.putExtra("centro", nombre);

                String ip = String.valueOf(listaIps.get(position));
                System.out.println(ip);
                intent.putExtra("ip", ip);

                String dominio = String.valueOf(listaDominios.get(position));
                System.out.println(dominio);
                intent.putExtra("dominio", dominio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btComenzar.setOnClickListener(view -> {

            new CheckConnection().cancel(true);
            new CheckConnection().execute("http://"+IP+"/"+PROJECT+"/"+REC+"/");
            /*Explode explode = new Explode();
            explode.setDuration(1000);*/

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String serverResponse = new CheckConnection().getServerResponse();

            if (serverResponse!= null) {
                intent.putExtra("username",username);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            } else {
                Toast.makeText(getApplicationContext(),"NO HAY CONEXIÓN CON EL SERVIDOR", Toast.LENGTH_SHORT).show();
            }
        });

        butmenu.setOnClickListener(v -> showdialog());
    }

    private void showdialog() {

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
        TextView txtvtables = dialog.findViewById(R.id.txtVtablas);
        LinearLayout actulayout = dialog.findViewById(R.id.layoutactu);
        TextView txtlastupdate = dialog.findViewById(R.id.txtVapp);
        txtvtables.setText(config.getVTables(getApplicationContext()));

        actulayout.setOnClickListener(v -> {
            startCheckConnection();
            if(config.getCon(getApplicationContext())) {

                if (checkPermission()) {
                    new CheckConnection().cancel(true);
                    Update atualizaApp = new Update();
                    atualizaApp.setContext(ElegirCentroActivity.this);
                    atualizaApp.execute("http://192.180.1.50/gesl/app/app-release.apk");
                    boolean updated = atualizaApp.getUpdated();
                    startCheckConnection();
                   /*Explode explode = new Explode();
                    explode.setDuration(1000);*/
                    try {
                        Thread.sleep(1000);
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
                }//end else

            } else {
                new CheckConnection().cancel(true);
                Toast.makeText(ElegirCentroActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();
            }

        });
        infolayout.setOnClickListener(v -> {

            Uri uri = Uri.parse("http://asysgon.com");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);
        });
        rechargelayout.setOnClickListener(v -> {

            new CheckConnection().execute("http://"+IP+"/"+PROJECT+"/"+REC+"/");
            /*Explode explode = new Explode();
            explode.setDuration(1000);*/

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            serverResponse = new CheckConnection().getServerResponse();
            if (serverResponse!= null) {
                new CheckConnection().setServer_response();
                Cajas_Local_DB cajas_local_db = new Cajas_Local_DB(getApplicationContext());
                Login_Local_DB login_local_db = new Login_Local_DB(getApplicationContext());
                cajas_local_db.deleteRows();
                cajas_local_db.getDBCajas(getApplicationContext(),IP,REC,PROJECT);
                login_local_db.deleteRows();
                login_local_db.getDBUser(getApplicationContext());
                config.setVTables(getApplicationContext());
                txtvtables.setText(config.getVTables(getApplicationContext()));
                Toast.makeText(getApplicationContext(),"LAS TABLAS HAN SIDO CARGADAS",Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(),"NO HAY CONEXION CON EL SERVIDOR",Toast.LENGTH_SHORT).show();
            }
        });
        configlayout.setOnClickListener(v -> showconfingdialog());
        exitlayout.setOnClickListener(v -> {
            Intent t = new Intent(ElegirCentroActivity.this, LoginActivity.class);
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
    private void showconfingdialog() {

        final Dialog dialog = new Dialog(ElegirCentroActivity.this);
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
                /*Explode explode = new Explode();
                explode.setDuration(1000);*/

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                serverResponse = new CheckConnection().getServerResponse();
                if (serverResponse == null) {
                    Toast.makeText(ElegirCentroActivity.this, "No hay conexión con el host", Toast.LENGTH_SHORT).show();
                }
                else {
                    config.createPreferences(getApplicationContext(),ip,rec,project);
                    Toast.makeText(ElegirCentroActivity.this, "Configuración guardada", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
            else Toast.makeText(ElegirCentroActivity.this, "Datos erróneos", Toast.LENGTH_SHORT).show();

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
        /*Explode explode = new Explode();
        explode.setDuration(1000);*/

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        serverResponse = new CheckConnection().getServerResponse();
        if (serverResponse == null) {
            config.setCon(this,false);

        } else {
            config.setLastCon(getApplicationContext());
            config.setCon(this,true);
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