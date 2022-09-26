package com.example.geslapp.core.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geslapp.R;
import com.example.geslapp.core.camara.CamaraX;
import com.example.geslapp.core.clases.DialogFoto;
import com.example.geslapp.core.clases.Material_invent;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import com.example.geslapp.core.databaseInvent.Material_Invent_Local_DB;



import java.util.ArrayList;


public class RvItemAdapter extends  androidx.recyclerview.widget.RecyclerView.Adapter<RvItemAdapter.MyViewHolder> {
    Context context;
    ArrayList<Material_invent> listaMaterial;
    int id_invent;
    Activity activity;

    public RvItemAdapter(Context context, ArrayList<Material_invent> listaMaterial, int id_invent, Activity activity) {
        this.context = context;
        this.listaMaterial = listaMaterial;
        this.id_invent = id_invent;
        this.activity = activity;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_items,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        checkData(id_invent,holder);
        holder.txtname.setText(listaMaterial.get(position).getName());
        Material_Invent_Local_DB material_invent_local_db = new Material_Invent_Local_DB(context);
        holder.edtubi.setText(material_invent_local_db.getUbi(id_invent,listaMaterial.get(position).getDbName()));
        holder.edttienda.setText(material_invent_local_db.getTienda(id_invent,listaMaterial.get(position).getDbName()));
        holder.edtretir.setText(material_invent_local_db.getReti(id_invent,listaMaterial.get(position).getDbName()));

        String itemDBname = listaMaterial.get(position).getDbName();
        //Material_Invent_Local_DB material_invent_local_db12 = new Material_Invent_Local_DB(context);
        /*Bitmap bitmap1 =  material_invent_local_db.getFoto(id_invent,itemDBname,context);

        if(bitmap1 != null) {
            holder.butview.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
        }*/

        holder.butsave.setOnClickListener(v -> {
            String ubi = holder.edtubi.getText().toString();
            String reti = holder.edtretir.getText().toString();
            String tienda = holder.edttienda.getText().toString();
            String dbName = listaMaterial.get(position).getDbName();
            Material_Invent_Local_DB material_invent_local_db1 = new Material_Invent_Local_DB(context);
            material_invent_local_db1.UpdateItem(dbName,id_invent,reti,tienda,ubi,"0");
            Toast.makeText(context, "Datos guardados", Toast.LENGTH_SHORT).show();

           /* Bitmap bitmap =  material_invent_local_db.getFoto(id_invent,dbName,context);

            if(bitmap != null) {
                holder.butview.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
            }*/
            holder.butsave.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
        });

        /*holder.butfoto.setOnClickListener(v -> {

            String itemName = listaMaterial.get(position).getDbName();
            Intent t = new Intent(context, CamaraX.class);
            t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            t.putExtra("mode","items");
            t.putExtra("itemName",itemName);
            t.putExtra("id_invent",id_invent);
            context.startActivity(t);
        });*/

        /*holder.butview.setOnClickListener(v -> {

            String itemname = listaMaterial.get(position).getDbName();
            //Material_Invent_Local_DB material_invent_local_db12 = new Material_Invent_Local_DB(context);
            Bitmap bitmap =  material_invent_local_db.getFoto(id_invent,itemname,context);

            if(bitmap != null) {
                DialogFoto dialogFoto = new DialogFoto(activity,bitmap,listaMaterial.get(position).getName());
                dialogFoto.startDialog();
            } else Toast.makeText(activity, "No hay foto para mostrar", Toast.LENGTH_SHORT).show();
        });*/
    }

    @Override
    public int getItemCount() {
        return listaMaterial.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //aqui referenciamos los items de la vista
        TextView txtname;
        Button butfoto, butview, butsave;
        EditText edtubi, edtretir, edttienda;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtItemname);
            //butfoto = itemView.findViewById(R.id.butcardcamaraItem);
            //butview = itemView.findViewById(R.id.butcardimageItem);
            butsave = itemView.findViewById(R.id.butcardsaveItem);
            edtretir = itemView.findViewById(R.id.edtretiritem);
            edttienda = itemView.findViewById(R.id.edttiendaitem);
            edtubi = itemView.findViewById(R.id.edtubiitem);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void checkData(int id_invent, MyViewHolder holder) {

        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(context);
        String estado = inventario_local_db.getState(id_invent);

        if(estado.equals("Abierto")) {
            holder.edtretir.setEnabled(false);
            holder.edtubi.setEnabled(false);
            holder.edttienda.setEnabled(false);
            holder.butsave.setEnabled(false);
            //holder.butfoto.setEnabled(false);
            holder.butsave.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
            //holder.butview.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dialogverde)));
        }
    }
}
