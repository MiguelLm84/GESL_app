package com.example.geslapp.core.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Informes;
import com.example.geslapp.core.databaseInvent.Etqs_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.General_Invent_Local_DB;
import com.example.geslapp.core.databaseInvent.Inventario_Local_DB;
import com.example.geslapp.core.requests.UsernameInventRequest;
import com.example.geslapp.ui.Activity_Info_General;
import com.example.geslapp.ui.Rv_etiquetas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class RvInformesAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<RvInformesAdapter.MyViewHolder>{

    Context context;
    ArrayList<Informes> listainformes;
    ArrayList<String> listUsers;
    Activity activity;
    boolean con_invent;
    String centro, username;
    int ceco;
    Etqs_Invent_Local_DB etqs_invent_local_db;

    ConfigPreferences config = new ConfigPreferences();

    public RvInformesAdapter(Context context,String username,String centro,ArrayList<Informes> listainformes,int ceco,Activity activity,boolean con_invent) {
        this.context = context;
        this.username = username;
        this.centro = centro;
        this.listainformes = listainformes;
        this.ceco = ceco;
        this.activity = activity;
        this.con_invent = con_invent;

        listUsers = new ArrayList<>();
        etqs_invent_local_db = new Etqs_Invent_Local_DB(context);
    }

    @NonNull
    @Override
    public RvInformesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_infor,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(context);
        holder.txtMade.setText(listainformes.get(position).getUser());
        String tipo = listainformes.get(position).getTipo_informe();

        int id_invent = inventario_local_db.getIdInvent(position,ceco);
        holder.txtIdInvent.setText(String.valueOf(id_invent));

        if(tipo.equals("Material")) {
            holder.img.setImageResource(R.drawable.material);

        } else {
            holder.img.setImageResource(R.drawable.general);
        }
        String estado = listainformes.get(position).getEstado_informe();

        if(estado.equals("Abierto") || estado.equals("Cerrado")) {
            holder.butEdit.setBackgroundResource(R.drawable.viewicon);
        } else {
            holder.butEdit.setBackgroundResource(R.drawable.ic_edit);
        }

        holder.txtFecha.setText(listainformes.get(position).getFecha_apertura());
        holder.txtCentro.setText(centro);

        holder.butEdit.setOnClickListener(v -> {

            //int id_invent = inventario_local_db.getIdInvent(position,ceco);

            if(tipo.equals("General")) {
                if(estado.equals("Abierto")) {
                    General_Invent_Local_DB general_invent_local_db = new General_Invent_Local_DB(context);
                    boolean openBy = general_invent_local_db.getOpen(id_invent);

                    if (!openBy)
                        Toast.makeText(context, "Este inventario ha sido abierto por otro usuario", Toast.LENGTH_SHORT).show();
                    else {
                        Intent t = new Intent(context, Activity_Info_General.class);
                        t.putExtra("id_invent", id_invent);
                        t.putExtra("ceco", ceco);
                        t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(t);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                } else {
                    Intent t = new Intent(context, Activity_Info_General.class);
                    t.putExtra("id_invent", id_invent);
                    t.putExtra("ceco", ceco);
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(t);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }

            } else {
                //Etqs_Invent_Local_DB etqs_invent_local_db = new Etqs_Invent_Local_DB(context);

                if(estado.equals("Abierto")) {
                    getUsernameInvent(id_invent);

                    /*boolean openBy = etqs_invent_local_db.getOpen(id_invent);

                    if (!openBy)
                        Toast.makeText(context, "Este inventario ha sido abierto por otro usuario", Toast.LENGTH_SHORT).show();
                    else {
                        if(con_invent)etqs_invent_local_db.fillServerData(context);
                        Intent t = new Intent(context, Rv_etiquetas.class);
                        t.putExtra("id_invent", id_invent);
                        t.putExtra("ceco", ceco);
                        t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(t);
                        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }*/

                } else {
                    if(con_invent)etqs_invent_local_db.fillServerData(context);
                    Intent t = new Intent(context, Rv_etiquetas.class);
                    t.putExtra("id_invent", id_invent);
                    t.putExtra("ceco", ceco);
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(t);
                    activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            }
        });
    }

    private void getUsernameInvent(int id_invent) {

        String IP = config.getIP(context);
        String REC  = config.getRec(context);
        String PROJECT  = config.getProject(context);

        Response.Listener<String> respoListener = response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                int success = jsonResponse.getInt("success");

                if(success == 1) {
                    new StringRequest(Request.Method.GET, "http://" + IP + "/"+PROJECT+"/" + REC + "/usernameInvent.php", response1 -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String id = String.valueOf(jsonObject.getInt("id"));
                            getUsername(id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "error json", Toast.LENGTH_SHORT).show();
                        }
                    }, Throwable::printStackTrace);
                } else {
                    Toast.makeText(context, "No se han recibido datos", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
            }
        };
        UsernameInventRequest usernameRequest = new UsernameInventRequest(username,respoListener,IP,REC,PROJECT);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(usernameRequest);

        /*StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://" + IP + "/"+ PROJECT+"/" + REC + "/usernameInvent.php",
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if(success.equals(username)) {
                            Toast.makeText(context, "Este inventario ha sido abierto por otro usuario", Toast.LENGTH_SHORT).show();

                        } else {
                            if(con_invent)etqs_invent_local_db.fillServerData(context);
                            Intent t = new Intent(context, Rv_etiquetas.class);
                            t.putExtra("id_invent", id_invent);
                            t.putExtra("ceco", ceco);
                            t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(t);
                            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "error json", Toast.LENGTH_SHORT).show();
                    }

                }, error -> Toast.makeText(context, "error", Toast.LENGTH_SHORT).show());

        UsernameInventRequest usernameRequest = new UsernameInventRequest(username,respoListener,IP,REC);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(usernameInventRequest);*/
    }

    private void getUsername(String id) {

        if(id != null || id.isEmpty()) {
            listUsers.add(id);
        } else {
            Toast.makeText(context, "Este inventario ha sido abierto por otro usuario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return listainformes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //Aquí referenciamos los items de la vista
        TextView txtCentro, txtFecha, txtMade, txtIdInvent;
        Button butEdit;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCentro = itemView.findViewById(R.id.txtcardcentro);
            txtFecha = itemView.findViewById(R.id.txtcardfecha);
            txtMade = itemView.findViewById(R.id.txtcardmade);
            butEdit = itemView.findViewById(R.id.butcardedit);
            img = itemView.findViewById(R.id.imgcardtipo);
            txtIdInvent = itemView.findViewById(R.id.txtidinvent);
        }
    }
}
