package com.example.geslapp.core.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AntenasRequest extends StringRequest {
    private Map<String,String> params;
    public AntenasRequest(Response.Listener<String> listener,String IP, String REC,String PROJECT, String id_centro) {
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/antenas.php", listener, null);
        params = new HashMap<>();
        params.put("id_centro",id_centro);
    }
    public Map<String, String> getParams() {
        return params;
    }
}
