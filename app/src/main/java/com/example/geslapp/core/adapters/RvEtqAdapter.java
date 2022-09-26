package com.example.geslapp.core.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geslapp.R;
import com.example.geslapp.core.camara.CamaraX;
import com.example.geslapp.core.clases.CheckConnection;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.DialogFoto;
import com.example.geslapp.core.clases.Etqs_invent;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import com.example.geslapp.ui.ActivityCarga;
import com.example.geslapp.ui.LoginActivity;
import com.example.geslapp.ui.MenuActivity;

import java.util.ArrayList;

public class RvEtqAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<RvEtqAdapter.MyViewHolder>{

    Context context;
    private String currentphotopath;
    private static boolean saved = false;
    private final int id_invent;
    private final Activity activity;
    private final ArrayList<Etqs_invent> listaEtqs;
    private Bitmap bitmap1;

    private static String IP,REC,PROJECT;
    private final ConfigPreferences config = new ConfigPreferences();
    CheckConnection checkConnection = new CheckConnection();
    String serverResponse;

    public RvEtqAdapter(Context context, int id_invent, ArrayList<Etqs_invent> listaEtqs, Activity activity) {

        this.context = context;
        this.id_invent = id_invent;
        this.activity = activity;
        this.listaEtqs = listaEtqs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_etqs,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final String[] retircajas = new String[1];
        final String[] retiretq = new String[1];
        final String[] ticajas = new String[1];
        final String[] tietq = new String[1];
        final String[] asiggesl = new String[1];
        final String[] asigcajas = new String[1];
        final String[] asigetq = new String[1];
        final String[] desasigcajas = new String[1];
        final String[] desasigets = new String[1];
        final String[] desasiggesl = new String[1];

        Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(context);
        holder.txtmodelo.setText(listaEtqs.get(position).getModeloEtq());
        holder.edtretircajas.setText(listaEtqs.get(position).getRetiradasCajas());
        holder.edtretireqt.setText(listaEtqs.get(position).getRetiradasEtq());
        holder.edttiendacajas.setText(listaEtqs.get(position).getTiendaCajas());
        holder.edttiendaetq.setText(listaEtqs.get(position).getTiendaEtq());
        holder.edtasigcajas.setText(listaEtqs.get(position).getAsigCajas());
        holder.edtasigetq.setText(listaEtqs.get(position).getAsigEtq());
        holder.edtasiggesl.setText(listaEtqs.get(position).getAsigCajasGesl());
        holder.edtdesasiggesl.setText(listaEtqs.get(position).getDesasigCajasGesl());
        holder.edtdesasigcajas.setText(listaEtqs.get(position).getDesasigCajas());
        holder.edtdesasigetq.setText(listaEtqs.get(position).getDesasigEtq());

        /*bitmap1 = etqs_invent_local_db.getFoto(id_invent,context,position);
        if(bitmap1 !=null) {
            holder.butimg.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
        }

        holder.butfoto.setOnClickListener(v -> {

            Intent t = new Intent(context, CamaraX.class);
            t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            t.putExtra("mode","etqs");
            t.putExtra("id_invent",id_invent);
            t.putExtra("position",position);
            context.startActivity(t);
            //holder.butimg.setPressed(true);
        });*/

        holder.butsave.setOnClickListener(v -> {

            retircajas[0] = holder.edtretircajas.getText().toString();
            retiretq[0] = holder.edtretireqt.getText().toString();
            ticajas[0] = holder.edttiendacajas.getText().toString();
            tietq[0] = holder.edttiendaetq.getText().toString();
            asigcajas[0] = holder.edtasigcajas.getText().toString();
            asigetq[0] = holder.edtasigetq.getText().toString();
            asiggesl[0] = holder.edtasiggesl.getText().toString();
            desasiggesl[0] = holder.edtdesasiggesl.getText().toString();
            desasigcajas[0] = holder.edtdesasigcajas.getText().toString();
            desasigets[0] = holder.edtdesasigetq.getText().toString();

            Toast.makeText(context, "Datos guardados", Toast.LENGTH_SHORT).show();
            etqs_invent_local_db.saveData(id_invent,retircajas[0], retiretq[0], ticajas[0], tietq[0],
                    asiggesl[0], asigcajas[0], asigetq[0], desasigcajas[0], desasigets[0], desasiggesl[0],position);
            saved = true;
            holder.butsave.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
            /*bitmap1 = etqs_invent_local_db.getFoto(id_invent,context,position);
            if(bitmap1 !=null) {
                holder.butimg.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
            }*/
        });

        /*holder.butimg.setOnClickListener(v -> {

            Bitmap bitmap1 = etqs_invent_local_db.getFoto(id_invent,context,position);
            if(bitmap1 !=null) {
                holder.butimg.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
                DialogFoto dialogFoto = new DialogFoto(activity,bitmap1,listaEtqs.get(position).getModeloEtq());
                dialogFoto.startDialog();
            }
            else Toast.makeText(context, "No hay foto para mostrar", Toast.LENGTH_SHORT).show();
        });*/
    }

    @SuppressLint("ResourceAsColor")
    private void checkData(int id_invent, MyViewHolder holder) {

        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(context);
        String estado = inventario_local_db.getState(id_invent);

        if(estado.equals("Abierto")) {
            //holder.butfoto.setEnabled(false);
            holder.edtdesasiggesl.setEnabled(false);
            holder.edtdesasigcajas.setEnabled(false);
            holder.edtdesasigetq.setEnabled(false);
            holder.edtasigetq.setEnabled(false);
            holder.edtasiggesl.setEnabled(false);
            holder.edtretircajas.setEnabled(false);
            holder.edtasigcajas.setEnabled(false);
            holder.edtretireqt.setEnabled(false);
            holder.edttiendacajas.setEnabled(false);
            holder.edttiendaetq.setEnabled(false);
            holder.butsave.setEnabled(false);
            holder.edttiendacajas.setTextColor(R.color.black);
            holder.butsave.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
        }
    }

    @Override
    public int getItemCount() {
        return listaEtqs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

    //aqui referenciamos los items de la vista
        EditText edtretircajas,edtretireqt,edttiendacajas,edttiendaetq,edtasiggesl,edtasigcajas,edtasigetq,edtdesasiggesl,edtdesasigcajas,edtdesasigetq;
        //Button butfoto, butimg;
        Button butsave;
        TextView txtmodelo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            edtretircajas = itemView.findViewById(R.id.edtretcajas);
            edtretireqt = itemView.findViewById(R.id.edtretetq);
            edttiendacajas = itemView.findViewById(R.id.edtticajas);
            edttiendaetq = itemView.findViewById(R.id.edtubiitem);
            edtasigcajas = itemView.findViewById(R.id.edtasigcajas);
            edtasiggesl = itemView.findViewById(R.id.edtasiggesl);
            edtasigetq = itemView.findViewById(R.id.edtasigetq);
            edtdesasigcajas = itemView.findViewById(R.id.edtdesasigcajas);
            edtdesasigetq = itemView.findViewById(R.id.edtdesasigetq);
            edtdesasiggesl = itemView.findViewById(R.id.edtdesasiggesl);
            //butfoto = itemView.findViewById(R.id.butcardcamara);
            butsave = itemView.findViewById(R.id.butetqsave);
            //butimg = itemView.findViewById(R.id.butcardimage);
            txtmodelo = itemView.findViewById(R.id.txtmodeloetq);
        }
    }
}
