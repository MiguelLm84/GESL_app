package com.example.geslapp.ui;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.example.geslapp.R;
import com.example.geslapp.core.adapters.RvInformesAdapter;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Informes;
import com.example.geslapp.core.databaseInvent.Antenas_Local_DB;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import java.util.ArrayList;


public class Activity_Dashboard extends AppCompatActivity {

    ConstraintLayout constarintLayoutLottie, constraintLayoutLottieLupa, constraintLayoutNoHayInformes;
    Spinner centros;
    private RecyclerView rvdash;
    private Adapter rvadapter;
    private RecyclerView.LayoutManager layoutManager;
    private String centro;
    ArrayList<String> listaCentros, listaidCentros, listaCecos;
    int ceco;
    Button butfind;
    private boolean con_invent;
    private static String username;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);

        con_invent = getIntent().getBooleanExtra("con_invent",false);
        username = getIntent().getStringExtra("username");
        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(getApplicationContext());
        if(con_invent)inventario_local_db.fillInventario(getApplicationContext(),username);

        listaCentros =  getIntent().getStringArrayListExtra("listaCentros");
        listaidCentros =  getIntent().getStringArrayListExtra("listaIdsCentros");
        listaCecos = getIntent().getStringArrayListExtra("listaCecos");
        centros = findViewById(R.id.spincentros1);
        rvdash = findViewById(R.id.rvinformes);
        butfind = findViewById(R.id.butfind);
        constarintLayoutLottie = findViewById(R.id.constarinLayoutLottie);
        constraintLayoutLottieLupa = findViewById(R.id.constraintLayoutLottieLupa);
        constraintLayoutNoHayInformes = findViewById(R.id.constraintLayoutNoHayInformes);

        Antenas_Local_DB antenas_local_db = new Antenas_Local_DB(getApplicationContext());
        ConfigPreferences config = new ConfigPreferences();

        constarintLayoutLottie.setVisibility(View.VISIBLE);
        constraintLayoutLottieLupa.setVisibility(View.GONE);

        butfind.setOnClickListener(v -> {

            layoutManager = new LinearLayoutManager(getApplicationContext());
            rvdash.setLayoutManager(layoutManager);
            ArrayList<Informes> centroSelectec = inventario_local_db.centroselected(ceco,username);
            rvadapter = new RvInformesAdapter(getApplicationContext(),username,centro,centroSelectec,ceco,Activity_Dashboard.this,con_invent);
            constraintLayoutNoHayInformes.setVisibility(View.GONE);
            constarintLayoutLottie.setVisibility(View.GONE);
            constraintLayoutLottieLupa.setVisibility(View.GONE);

            if(centroSelectec == null || centroSelectec.isEmpty()){
                constraintLayoutNoHayInformes.setVisibility(View.VISIBLE);
                rvdash.setVisibility(View.INVISIBLE);

            } else {
                constraintLayoutNoHayInformes.setVisibility(View.GONE);
                rvdash.setVisibility(View.VISIBLE);
                rvdash.setAdapter(rvadapter);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Activity_Dashboard.this, android.R.layout.simple_spinner_dropdown_item, listaCentros) {

            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                TextView tv = ((TextView) v);

                tv.setTextColor(getResources().getColor(R.color.blue_20, getTheme()));
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv.setSingleLine();
                tv.setEllipsize(TextUtils.TruncateAt.END);
                tv.setTextSize(16);

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }
        };

        constarintLayoutLottie.setVisibility(View.VISIBLE);
        constraintLayoutLottieLupa.setVisibility(View.GONE);
        centros.setAdapter(adapter);

        centros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String ip = config.getIP(getApplicationContext());
                String rec = config.getRec(getApplicationContext());
                String project = config.getProject(getApplicationContext());

                centro = centros.getSelectedItem().toString();
                ceco = Integer.parseInt(listaidCentros.get(position));
                if(con_invent) antenas_local_db.fillDBdata(Activity_Dashboard.this,ip,rec,project,ceco);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
