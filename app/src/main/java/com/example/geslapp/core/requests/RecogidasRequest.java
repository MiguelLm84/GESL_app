package com.example.geslapp.core.requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RecogidasRequest extends StringRequest {

    private Map<String,String> params;
    public RecogidasRequest(Response.Listener<String> listener,String media,String IP,String REC,String PROJECT, String username,String ip) {
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/updateAviso.php", listener,null);
        params = new HashMap<>();
        params.put("id",media);
        params.put("username",username);
        params.put("user_ip",ip);
    }

    public Map<String,String> getParams(){
        return params;
    }
}
