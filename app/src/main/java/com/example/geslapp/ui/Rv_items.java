package com.example.geslapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.adapters.RvItemAdapter;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Etqs_invent;
import com.example.geslapp.core.clases.Material_invent;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Fotos_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import com.example.geslapp.core.databaseInvent.Material_Invent_Local_DB;
import com.example.geslapp.core.uploads.Upload_Etqs_Invent;
import com.example.geslapp.core.uploads.Upload_material_invent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Rv_items extends AppCompatActivity {

    Button butupload,butback, btnCancel, btnSend;
    TextView tvInventario;
    private static int id_invent;
    private static String enviado;
    EditText edtmade;
    String fileName = "";
    static String linea = "";
    int num = 1;
    //ArrayList<String> listFilesInventories = new ArrayList<>();
    ConfigPreferences config = new ConfigPreferences();
    Inventario_Local_DB inventario_local_db1 = new Inventario_Local_DB(this);
    Material_Invent_Local_DB material_invent_local_db = new Material_Invent_Local_DB(this);
    ArrayList<Material_invent> listaMaterial = fillMaterial();

    //static File carpetInventarios = new File(Environment.getExternalStorageDirectory(), "Inventarios_Material");
    static File carpetInventarios = new File(Environment.getExternalStorageDirectory(), "Inventarios_Material");

    static File file = new File(carpetInventarios, "Inventario_material" + ".txt");
    static String direction = file.getPath();
    boolean isFileCreated = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_items);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para leer.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para leer y escribir!");
        }

        id_invent = getIntent().getIntExtra("id_invent",-1);
        RecyclerView rvitem = findViewById(R.id.rvItems);
        rvitem.setHasFixedSize(true);
        //ArrayList<Material_invent> listaMaterial = fillMaterial();
        material_invent_local_db.firstInsert(id_invent);
        RecyclerView.LayoutManager layoutManageritem = new LinearLayoutManager(getApplicationContext());
        rvitem.setLayoutManager(layoutManageritem);
        RecyclerView.Adapter<RvItemAdapter.MyViewHolder> rvadapteritem = new RvItemAdapter(getApplicationContext(), listaMaterial, id_invent, Rv_items.this);
        edtmade = findViewById(R.id.edtmadeitems);
        rvitem.setAdapter(rvadapteritem);
        butupload = findViewById(R.id.butUploadItems);
        checkData(id_invent);
        Inventario_Local_DB inventario_local_db =new Inventario_Local_DB(getApplicationContext());
        edtmade.setText(inventario_local_db.getMade(id_invent));
        butback = findViewById(R.id.butbackitems);

        butback.setOnClickListener(v -> onBackPressed());

        butupload.setOnClickListener(v -> enviarInventarioMaterial());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDialogErrorSend(isFileCreated);
    }

    private void createFileSegunVersionApiAndroid(ArrayList<Material_invent> listaSendItems) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            createFileNewVersionAndroid(this, listaSendItems);

        } else {
            createFile(listaSendItems);
        }
        isFileCreated = true;
    }

    private void createFile(ArrayList<Material_invent> listSendItems) {

        /*String direction = Environment.getExternalStorageDirectory() + "Inventario_material.txt";
        File file = new File(direction);

        FileOutputStream fileOutputStream = null;
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        try {
            if (!carpetInventarios.exists()) {
                carpetInventarios.mkdir();
            }
            try {
                file.createNewFile();
                writeFileOnInternalStorage(listSendItems);
                //writeFile(listSendItems);

            } catch (Exception ex) {
                Log.e("ERROR STATUS FILE", "Error statusFile" + ex.getMessage());
            }
        } catch (Exception e) {
            Log.e("ERROR STATUS FILE", "Error statusFile" + e.getMessage());
        }
    }

    private void createFileNewVersionAndroid(Context context, ArrayList<Material_invent> listSendItems){

        /*String direction = context.getExternalFilesDir(null) + "Inventario_material.txt";
        File file = new File(direction);

        FileOutputStream fileOutputStream = null;
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("ERROR STATUS FILE", "Error statusFile" + e.getMessage());
            }
        }*/

        File carpeta_inventarios = new File(getApplicationContext().getExternalFilesDir(null), "Inventarios");

        try {
            if (!carpeta_inventarios.exists()) {
                carpeta_inventarios.mkdir();
            }
            try {
                //listFilesInventories.add(file.getName());
                if(file.exists()){
                    File newFile = new File(carpeta_inventarios, "Inventario_material" + "_" + num + ".txt");
                    //listFilesInventories.add(newFile.getName());
                    newFile.createNewFile();
                    writeFileOnInternalStorage(listSendItems);
                    //writeFile(listSendItems);
                    num++;
                }
                file.createNewFile();
                writeFileOnInternalStorage(listSendItems);
                //writeFile(listSendItems);

            } catch (Exception ex) {
                Log.e("ERROR STATUS FILE", "Error statusFile" + ex.getMessage());
            }
        } catch (Exception e) {
            Log.e("ERROR STATUS FILE", "Error statusFile" + e.getMessage());
        }
    }

    /*public static void writeFile(ArrayList<Material_invent> listSendItems){

        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(direction, true));
            for(int i = 0; i < listSendItems.size(); i++) {
                linea = listSendItems.get(i).getDbName();
                linea = listSendItems.get(i).getName();
                //linea = listSendItems.get(i).getFoto();
                linea = listSendItems.get(i).getReti();
                linea = listSendItems.get(i).getTienda();
                linea = listSendItems.get(i).getUbi();
                br.write(linea);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void writeFileOnInternalStorage(ArrayList<Material_invent> listSendItems){

        File dir = new File(getApplicationContext().getFilesDir(), "Inventarios");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File file2 = new File(dir, "Inventario_material.csv");
            FileWriter writer = new FileWriter(file2);
            for(int i = 0; i < listSendItems.size(); i++) {
                linea = listSendItems.get(i).getDbName();
                linea = listSendItems.get(i).getName();
                //linea = listSendItems.get(i).getFoto();
                linea = listSendItems.get(i).getReti();
                linea = listSendItems.get(i).getTienda();
                linea = listSendItems.get(i).getUbi();
                writer.append(linea);
                writer.flush();
            }
            writer.close();
            isFileCreated = true;

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showDialogErrorSend(boolean isFileCreated) {

        if(isFileCreated) {
            dialogErrorSend();
        }
    }

    private void dialogErrorSend() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        final View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_renvio_inventario, null);
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();
        initDialogErrorSend(dialogLayout);
        tvInventario.setText(file.getName());
        //getListInventMaterial(tvInventario);
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSend.setOnClickListener(v -> enviarInventarioMaterial());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*private void getListInventMaterial(TextView tvInventario){

      for(int i = 0; i < listFilesInventories.size(); i++) {
          if(listFilesInventories.get(i) != null){
              tvInventario.setText(listFilesInventories.get(i));
          }
      }
    }*/

    private void initDialogErrorSend(View dialogLayout) {

        btnSend = dialogLayout.findViewById(R.id.btnSend);
        btnCancel = dialogLayout.findViewById(R.id.btn_cancel);
        tvInventario = dialogLayout.findViewById(R.id.tvInventario);
    }

    private void enviarInventarioMaterial(){

        //ConfigPreferences config = new ConfigPreferences();
        String IP = config.getIP(getApplicationContext());
        String REC = config.getRec(getApplicationContext());

        //Inventario_Local_DB inventario_local_db1 = new Inventario_Local_DB(getApplicationContext());
        inventario_local_db1.updateState(id_invent);
        String made = edtmade.getText().toString();

        ArrayList<Material_invent> listaSendItems = new ArrayList<>();

        for(int i=0;i<listaMaterial.size();i++) {
            String itemname = listaMaterial.get(i).getDbName();
            Material_invent material_invent = material_invent_local_db.getAllData(id_invent,itemname, enviado);
            listaSendItems.add(material_invent);
        }

        for(int i = 0; i<listaSendItems.size();i++) {

            String itemName = listaSendItems.get(i).getDbName();
            Response.Listener<String> respoListener = response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int success = jsonResponse.getInt("success");
                    String request = jsonResponse.getString("message");

                    if (success == 1) {
                        //TODO HACER LLAMADA SQLITE
                        material_invent_local_db.UpdateSend (itemName,"1");
                        Toast.makeText(getApplicationContext(),"Insert realizado exitosamente ", Toast.LENGTH_SHORT).show();

                    } else {
                        material_invent_local_db.UpdateSend (itemName,"0");
                        Toast.makeText(getApplicationContext(),"Se ha producido un error, volver a intentar", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),request, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    material_invent_local_db.UpdateSend (itemName,"0");
                    Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar", Toast.LENGTH_SHORT).show();
                }
            };

            Upload_material_invent upload_material_invent = new Upload_material_invent(respoListener,listaSendItems.get(i),IP,REC,id_invent,made);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(upload_material_invent);

        }//END FOR

        //ETIQUETAS-------------------------------------------------------------------------
        Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(getApplicationContext());
        ArrayList<Etqs_invent> listaEtqs = new ArrayList<>();
        listaEtqs.addAll(etqs_invent_local_db.fillArray(id_invent, Rv_items.this));

        for(int i = 0; i<listaEtqs.size();i++) {

            Response.Listener<String> respoListener = response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int success = jsonResponse.getInt("success");
                    String request = jsonResponse.getString("message");

                    if (success == 1) {
                        Toast.makeText(getApplicationContext(),"Insert realizado exitosamente-ETQS " +request, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar-ETQ" +response, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),request, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar-ETQ" +response, Toast.LENGTH_SHORT).show();
                }
            };
            Upload_Etqs_Invent upload_etqs_invent = new Upload_Etqs_Invent(respoListener,listaEtqs.get(i),IP,REC,id_invent,getApplicationContext());
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(upload_etqs_invent);

        }//END FOR

        //Fotos_Invent_Local_DB fotos_invent_local_db = new Fotos_Invent_Local_DB(getApplicationContext());
        //fotos_invent_local_db.getAllFotos(id_invent,getApplicationContext(),Rv_items.this);
        //Toast.makeText(Rv_items.this, "Se han subido los datos", Toast.LENGTH_SHORT).show();
    }

    private void checkData(int id_invent) {
        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(getApplicationContext());
        String estado = inventario_local_db.getState(id_invent);
        if(estado.equals("Abierto")) {

            butupload.setEnabled(false);
        }
    }

    public ArrayList<Material_invent> fillMaterial() {

        ArrayList<Material_invent> listaMaterial = new ArrayList<>();
        Material_invent clip_liso = new Material_invent("Clip liso para perfiles","clip_liso","","","", "0");
        listaMaterial.add(clip_liso);
        Material_invent clip_cruz = new Material_invent("Clip cruceta/mariposa","clip_cruz","","","","0");
        listaMaterial.add(clip_cruz);
        Material_invent pie_10 = new Material_invent("Pie 10 cm","pie_10","","","","0");
        listaMaterial.add(pie_10);
        Material_invent pie_15 = new Material_invent("Pie 15cm","pie_15","","","", "0");
        listaMaterial.add(pie_15);
        Material_invent bases = new Material_invent("Bases imantadas","bases","","","", "0");
        listaMaterial.add(bases);
        Material_invent pincho_pesc = new Material_invent("Pinchos pescadería","pincho_pesc","","","", "0");
        listaMaterial.add(pincho_pesc);
        Material_invent pincho_largo = new Material_invent("Pinchos de cruceta largos","pincho_largo","","","", "0");
        listaMaterial.add(pincho_largo);
        Material_invent pincho_corto = new Material_invent("Pinchos de cruceta cortos","pincho_corto","","","", "0");
        listaMaterial.add(pincho_corto);
        Material_invent saca_clips = new Material_invent("Saca clips","saca_clips","","","", "0");
        listaMaterial.add(saca_clips);
        Material_invent perfiles = new Material_invent("Perfiles","perfiles","","","", "0");
        listaMaterial.add(perfiles);
        Material_invent term_perfiles = new Material_invent("Terminación perfiles","term_perfiles","","","", "0");
        listaMaterial.add(term_perfiles);
        Material_invent pinzas = new Material_invent("Pinzas","pinzas","","","", "0");
        listaMaterial.add(pinzas);
        Material_invent eleva_metal = new Material_invent("Elevadores de metal","eleva_metal","","","", "0");
        listaMaterial.add(eleva_metal);
        Material_invent eleva_plastic = new Material_invent("Elevadores plásticos","eleva_plastic","","","", "0");
        listaMaterial.add(eleva_plastic);
        return listaMaterial;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
