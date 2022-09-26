package com.example.geslapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.geslapp.R;
import com.example.geslapp.core.adapters.RvEtqAdapter;
import com.example.geslapp.core.adapters.RvEtqAdapter.MyViewHolder;
import com.example.geslapp.core.clases.Etqs_invent;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import java.util.ArrayList;


public class Rv_etiquetas extends AppCompatActivity {

    private RecyclerView rvEtq;
    private Button butitems, butback;
    private int id_invent;
    private ConstraintLayout constraintLayoutInventarioMaterial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_invent);

        init();
        getArgsRecyclerViewInventarios();
        buttonItems();
        buttonBack();
    }

    private void init() {

        constraintLayoutInventarioMaterial = findViewById(R.id.constraintLayoutInventarioMaterial);
        butback = findViewById(R.id.butbackmaterial);
        butitems = findViewById(R.id.butToItems);
        rvEtq = findViewById(R.id.rvmaterialinvent);
        rvEtq.setHasFixedSize(true);
    }

    private void getArgsRecyclerViewInventarios() {

        id_invent = getIntent().getIntExtra("id_invent",-1);

        if(String.valueOf(id_invent).isEmpty()) {
            constraintLayoutInventarioMaterial.setVisibility(View.VISIBLE);
            rvEtq.setVisibility(View.INVISIBLE);

        } else {
            rvEtq.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager layoutManagerEtq = new LinearLayoutManager(this);
            rvEtq.setLayoutManager(layoutManagerEtq);

            Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(this);

            ArrayList<Etqs_invent> listaEtqs = new ArrayList<>(etqs_invent_local_db.fillArray(id_invent, Rv_etiquetas.this));
            if(listaEtqs.size() == 0) {
                recreate();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return;
            }
            Adapter<MyViewHolder> rvEtqAdapter = new RvEtqAdapter(Rv_etiquetas.this, id_invent, listaEtqs, Rv_etiquetas.this);
            rvEtq.setAdapter(rvEtqAdapter);
        }
    }

    private void buttonItems() {

        butitems.setOnClickListener(v -> {
            Intent t = new Intent(Rv_etiquetas.this,Rv_items.class);
            t.putExtra("id_invent",id_invent);
            startActivity(t);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void buttonBack() {

        butback.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
