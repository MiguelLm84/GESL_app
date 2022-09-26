
package com.example.geslapp.core.requests;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.ConfigPreferences;

import java.util.HashMap;
import java.util.Map;

public class EtqContenidoApiRequest extends StringRequest {

    private Map<String,String> params;
    public EtqContenidoApiRequest(String media, String ip_centro, String domain_centro, Response.Listener<String> listener,String IP,String REC,String PROJECT,String user,String uip){
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/etq_cont_api.php",listener, null);
        params = new HashMap<>();
        params.put("media", media);
        params.put("ip_centro", ip_centro);
        params.put("domain_centro", domain_centro);
        params.put("user",user);
        params.put("ip",uip);
    }

    public Map<String,String> getParams(){
        return params;
    }


}

