package com.example.geslapp.core.uploads;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.General_invent;

import java.util.HashMap;
import java.util.Map;

public class Upload_General_Invent extends StringRequest {

    private final Map<String,String> params;

    public Upload_General_Invent(Response.Listener<String> listener, Response.ErrorListener errorListener, General_invent general, String IP, String REC,String madeby) {
        super(Method.POST, "http://"+IP+"/gesl/"+REC+"/general_invent.php", listener, errorListener);
        params = new HashMap<>();
        params.put("id_invent",String.valueOf(general.getId_invent()));
        params.put("server_ubi",general.getSERVIDOR_UBI());
        params.put("server_foto1",general.getSERVIDOR_FOTO1());
        params.put("server_foto2",general.getSERVIDOR_FOTO2());
        params.put("poe_ubi",general.getPOE_UBI());
        params.put("poe_foto",general.getPOE_FOTO());
        params.put("cartel_ubi",general.getCARTEL_UBI());
        params.put("cartel_foto",general.getCARTEL_FOTO());
        params.put("id_user",String.valueOf(general.getID_USER()));
        params.put("realizado",madeby);
    }

    public Map<String, String> getParams() {
        return params;
    }







}





