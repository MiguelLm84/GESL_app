package com.example.geslapp.core.uploads;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.AntenasInvent;
import com.example.geslapp.core.clases.General_invent;

import java.util.HashMap;
import java.util.Map;

public class Upload_Antenas_Invent extends StringRequest {
    private Map<String,String> params;

    public Upload_Antenas_Invent(Response.Listener<String> listener, AntenasInvent antenas, String IP, String REC) {
        super(Method.POST, "http://"+IP+"/gesl/"+REC+"/antenas_invent.php", listener, null);
        params = new HashMap<>();
        params.put("id_invent",String.valueOf(antenas.getID_INVENTARIO()));
        params.put("id_antena",String.valueOf(antenas.getID_ANTENA()));
        params.put("ubicacion",antenas.getUBICACION());
        String foto  = antenas.getFOTO();
        if(foto ==null) foto = "";
        params.put("foto",foto);


    }
    public Map<String, String> getParams() {
        return params;
    }
}
