package com.example.geslapp.core.requests;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.ConfigPreferences;

import java.util.HashMap;
import java.util.Map;

public class EtqRequest extends StringRequest {

    private Map<String, String> params;

    public EtqRequest(String media, Response.Listener<String> listener,String IP, String REC,String PROJECT,String user,String ip) {
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/etq.php", listener, null);
        params = new HashMap<>();
        params.put("media", media);
    }

    public Map<String, String> getParams() {
        return params;
    }




}
