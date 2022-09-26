package com.example.geslapp.core.uploads;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.Etqs_invent;
import com.example.geslapp.core.clases.General_invent;
import com.example.geslapp.core.databaseInvent.Modelos_Etq_Local_DB;

import java.util.HashMap;
import java.util.Map;

public class Upload_Etqs_Invent extends StringRequest {
    private Map<String,String> params;
    public Upload_Etqs_Invent(Response.Listener<String> listener, Etqs_invent etqs, String IP, String REC, int id_invent, Context context) {
        super(Method.POST, "http://"+IP+"/gesl/"+REC+"/etqs_invent.php", listener, null);
        params = new HashMap<>();
        params.put("id_invent",String.valueOf(id_invent));
        Modelos_Etq_Local_DB modelos_etq_local_db = new Modelos_Etq_Local_DB(context);
        int id_modelo = modelos_etq_local_db.getIdModelo(etqs.getModeloEtq());
        params.put("id_modelo",String.valueOf(id_modelo));
        params.put("caja_retirada", etqs.getRetiradasCajas());
        params.put("caja_tienda",etqs.getTiendaCajas());
        params.put("etq_retirada",etqs.getRetiradasEtq());
        params.put("etq_tienda",etqs.getTiendaEtq());
        params.put("asignadas_cajas",etqs.getAsigCajas());
        params.put("asignadas_etq",etqs.getAsigEtq());
        params.put("asignadas_ncajas",etqs.getAsigCajasGesl());
        params.put("desasignadas_cajas",etqs.getDesasigCajas());
        params.put("desasignadas_etq",etqs.getDesasigEtq());
        params.put("desasignadas_ncajas",etqs.getDesasigCajasGesl());
        params.put("foto_etq", etqs.getFotoEtq());


    }
    public Map<String, String> getParams() {
        return params;
    }
}
