package com.example.geslapp.core.requests;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.ConfigPreferences;

import java.util.HashMap;
import java.util.Map;

public class EtqEstadoApiRequest extends StringRequest {



    private Map<String,String> params;
    public EtqEstadoApiRequest(String media, String ip_centro, String domain_centro, Response.Listener<String> listener,String IP,String REC,String PROJECT, String username,String ip){
        super(Method.POST,  "http://"+IP+"/"+PROJECT+"/"+REC+"/etqapi.php",listener, null);
        params = new HashMap<>();
        params.put("media", media);
        params.put("ip_centro", ip_centro);
        params.put("domain_centro", domain_centro);
        params.put("username",username);
        params.put("user_ip",ip);
    }

    public Map<String,String> getParams(){
        return params;
    }

}

