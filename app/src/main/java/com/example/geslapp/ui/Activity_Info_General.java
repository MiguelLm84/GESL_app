package com.example.geslapp.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.adapters.RvAntenasAdapter;
import com.example.geslapp.core.camara.CamaraX;
import com.example.geslapp.core.clases.AntenasInvent;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.DialogFoto;
import com.example.geslapp.core.clases.General_invent;
import com.example.geslapp.core.databaseInvent.Antenas_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Antenas_Local_DB;
import com.example.geslapp.core.databaseInvent.Fotos_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.General_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import com.example.geslapp.core.uploads.Upload_Antenas_Invent;
import com.example.geslapp.core.uploads.Upload_General_Invent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Info_General extends AppCompatActivity {
    private RecyclerView rvantenas;
    private RecyclerView.Adapter rvadapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText edtserver, edtpoe, edtgood,edtmade;
    Button butfotoserver, butfotopoe, butfotogood, butsave, butupdate, butimgserv, butimgpoe, butimgcart, butcloseimg,butserverfoto2;
    ImageView imgfoto;
    private static int contfoto = 0;
    private static int contservimg = 0;

    int ceco;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_general);
        Antenas_Local_DB antenas_local_db = new Antenas_Local_DB(getApplicationContext());
        General_Invent_Local_DB general_invent_local_db = new General_Invent_Local_DB(getApplicationContext());
       // general_invent_local_db.resetTable();

        edtserver = findViewById(R.id.edtubiserver);
        edtpoe = findViewById(R.id.edtubipoe);
        edtgood = findViewById(R.id.edtubigood);
        butfotogood = findViewById(R.id.butfotogood);
        butfotopoe = findViewById(R.id.butfotopoe);
        butfotoserver = findViewById(R.id.butfotoserver);
        butsave = findViewById(R.id.butUploadItems);
        butupdate = findViewById(R.id.butupdategeneral);
        butimgcart = findViewById(R.id.butCarImg);
        butimgpoe = findViewById(R.id.butPoeImg);
        butimgserv = findViewById(R.id.butservimg);
        edtmade = findViewById(R.id.edtmadeGeneral);


        butserverfoto2 = findViewById(R.id.butfotoserver2);

        ConfigPreferences config = new ConfigPreferences();
        String IP = config.getIP(getApplicationContext());
        String REC = config.getRec(getApplicationContext());

        int id_inventario = getIntent().getIntExtra("id_invent",0);
        general_invent_local_db.firsInsert(id_inventario);
        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(getApplicationContext());
        edtmade.setText(inventario_local_db.getMade(id_inventario));
        edtserver.setText(general_invent_local_db.getServUbi(id_inventario));
        edtpoe.setText(general_invent_local_db.getPoeUbi(id_inventario));
        edtgood.setText(general_invent_local_db.getGoodUbi(id_inventario));


        Antenas_Invent_Local_DB antenas_invent_local_db = new Antenas_Invent_Local_DB(getApplicationContext());
        //antenas_invent_local_db.resetTable();
        boolean open = false;
        ceco = getIntent().getIntExtra("ceco",0);
        rvantenas = findViewById(R.id.rvAntenas);
        rvantenas.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        rvantenas.setLayoutManager(layoutManager);
        if(checkData(id_inventario)) open = true;
        rvadapter = new RvAntenasAdapter(getApplicationContext(),antenas_local_db.getAntenas(ceco),id_inventario,open);
        rvantenas.setAdapter(rvadapter);
        if(open == true) {
            butsave.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dialogverde)));
            butsave.setEnabled(false);
        }

        butupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventario_local_db.updateState(id_inventario);
                String madeby = edtmade.getText().toString();
                General_invent general_invent = general_invent_local_db.getAllData(getApplicationContext(),id_inventario);
                Response.Listener<String> respoListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            int success = jsonResponse.getInt("success");
                            String request = jsonResponse.getString("message");

                            if (success == 1) {
                                //Toast.makeText(getApplicationContext(),"Insert realizado exitosamente ", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar" +response, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(),request, Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar", Toast.LENGTH_SHORT).show();

                        }

                    }
                };
                Upload_General_Invent upload_general_invent = new Upload_General_Invent(respoListener,null,general_invent,IP,REC,madeby);
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(upload_general_invent);
                Toast.makeText(Activity_Info_General.this, "Se han subido los datos", Toast.LENGTH_SHORT).show();

                mandarAntenas(id_inventario);
                uploadFotos(id_inventario);


            }
        });

        butfotoserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_Info_General.this, "Foto server 1", Toast.LENGTH_SHORT).show();
                    Intent t = new Intent(Activity_Info_General.this, CamaraX.class);
                    t.putExtra("mode", "fotoserver1");
                    t.putExtra("id_invent", id_inventario);
                    startActivity(t);
                butfotoserver.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dialogverde)));


            }
        });

        butserverfoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_Info_General.this, "Foto server 2", Toast.LENGTH_SHORT).show();
                Intent t = new Intent(Activity_Info_General.this, CamaraX.class);
                t.putExtra("mode", "fotoserver2");
                t.putExtra("id_invent", id_inventario);
                contfoto = 0;
                startActivity(t);
                butserverfoto2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dialogverde)));

            }
        });
        butfotopoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Activity_Info_General.this, CamaraX.class);
                t.putExtra("mode", "fotopoe");
                t.putExtra("id_invent", id_inventario);
                startActivity(t);
                butfotopoe.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dialogverde)));

            }
        });

        butfotogood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(Activity_Info_General.this, CamaraX.class);
                t.putExtra("mode", "fotogood");
                t.putExtra("id_invent", id_inventario);
                startActivity(t);
                butfotogood.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dialogverde)));

            }
        });

        butsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverubi = edtserver.getText().toString();
                String poeubi = edtpoe.getText().toString();
                String goodubi = edtgood.getText().toString();
                int itemcount = rvadapter.getItemCount();
                int saves = antenas_invent_local_db.getAntenasCount();
                butupdate.setVisibility(View.VISIBLE);


                    butupdate.setVisibility(View.VISIBLE);
                    Toast.makeText(Activity_Info_General.this, "Se han guardado los datos", Toast.LENGTH_SHORT).show();

                    general_invent_local_db.updateUbi(id_inventario,serverubi,poeubi,goodubi);
                    butsave.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.dialogverde)));

            }
        });

        butimgserv.setOnClickListener(v -> {
            if(contservimg == 0) {
                Bitmap bitmap1 = general_invent_local_db.getServFoto(id_inventario,getApplicationContext());
                if(bitmap1 !=null) {
                    DialogFoto dialogFoto = new DialogFoto(Activity_Info_General.this,bitmap1,"Foto server 1");
                    dialogFoto.startDialog();
                    contservimg++;
                }
                else Toast.makeText(Activity_Info_General.this, "No hay foto para mostrar", Toast.LENGTH_SHORT).show();

            } else {
                Bitmap bitmap2 = general_invent_local_db.getServFoto2(id_inventario,getApplicationContext());
                if(bitmap2 != null) {
                    DialogFoto dialogFoto = new DialogFoto(Activity_Info_General.this,bitmap2,"Foto server 2");
                    dialogFoto.startDialog();
                    contservimg = 0;
                } else {
                    contservimg = 0;
                    Toast.makeText(Activity_Info_General.this, "No hay foto para mostrar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        butimgpoe.setOnClickListener(v -> {
            Bitmap bitmap1 = general_invent_local_db.getPoeFoto(id_inventario,getApplicationContext());
            if(bitmap1!= null) {
                DialogFoto dialogFoto = new DialogFoto(Activity_Info_General.this,bitmap1,"Foto switch POE");
                dialogFoto.startDialog();
            }
            else Toast.makeText(Activity_Info_General.this, "No hay foto para mostrar", Toast.LENGTH_SHORT).show();

        });
        butimgcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap1 = general_invent_local_db.getGoodFoto(id_inventario,getApplicationContext());
                if(bitmap1 != null) {
                    DialogFoto dialogFoto = new DialogFoto(Activity_Info_General.this,bitmap1,"Foto cartel");
                    dialogFoto.startDialog();
                }else Toast.makeText(Activity_Info_General.this, "No hay foto para mostrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkData(int id_inventario) {
        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(getApplicationContext());
       String estado =  inventario_local_db.getState(id_inventario);
       if(estado.equals("Abierto")) {
           General_Invent_Local_DB general_invent_local_db = new General_Invent_Local_DB(getApplicationContext());
           General_invent invent = general_invent_local_db.getOpenData(getApplicationContext(),id_inventario);
           edtserver.setText(invent.getSERVIDOR_UBI());
           edtpoe.setText(invent.getPOE_UBI());
           edtgood.setText(invent.getCARTEL_UBI());
           butfotogood.setEnabled(false);
           butfotopoe.setEnabled(false);
           butfotoserver.setEnabled(false);
           butserverfoto2.setEnabled(false);
           edtgood.setEnabled(false);
           edtpoe.setEnabled(false);
           edtserver.setEnabled(false);
           return true;
       }
        else return false;
    }

    private void mandarAntenas(int id_inventario) {
        Antenas_Invent_Local_DB antenas_invent_local_db = new Antenas_Invent_Local_DB(getApplicationContext());
        ArrayList<AntenasInvent> listaAntenas = new ArrayList<>();
        listaAntenas = antenas_invent_local_db.getAllAntenas(id_inventario);

        for(int i = 0; i<listaAntenas.size();i++) {

            Response.Listener<String> respoListener2 = response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int success = jsonResponse.getInt("success");
                    String request = jsonResponse.getString("message");

                    if (success == 1) {
                        //Toast.makeText(getApplicationContext(),"Insert realizado exitosamente-ANTENAS"+id_inventario + request, Toast.LENGTH_SHORT).show();


                    } else {
                       Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),request, Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Ha currido un problema, volver a intentar" +response, Toast.LENGTH_SHORT).show();

                }

            };
        ConfigPreferences config = new ConfigPreferences();
        String ip = config.getIP(getApplicationContext());
        String rec = config.getRec(getApplicationContext());
        AntenasInvent antenasInvent = listaAntenas.get(i);
            //Toast.makeText(this, listaAntenas.get(i).getUBICACION(), Toast.LENGTH_SHORT).show();
        Upload_Antenas_Invent upload_antenas_invent = new Upload_Antenas_Invent(respoListener2, antenasInvent, ip, rec);
        RequestQueue requestQueue1 = Volley.newRequestQueue(Activity_Info_General.this);
        requestQueue1.add(upload_antenas_invent);



    }
    }

    private void uploadFotos(int id_inventario) {


        Handler handler = new Handler();
        if(handler.post(new Runnable() {
            @Override
            public void run() {
                Fotos_Invent_Local_DB fotos_invent_local_db = new Fotos_Invent_Local_DB(getApplicationContext());
                fotos_invent_local_db.getAllFotos(id_inventario,getApplicationContext(),Activity_Info_General.this);

            }
        }));

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}
