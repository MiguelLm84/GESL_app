package com.example.geslapp.core.requests;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.ConfigPreferences;

import java.util.HashMap;
import java.util.Map;

public class TrackingRequest extends StringRequest {

    private Map<String, String> params;

    public TrackingRequest(String tracking, Response.Listener<String> listener,String IP, String REC,String PROJECT,String user,String ip) {
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/tracking.php", listener, null);
        params = new HashMap<>();
        params.put("tracking", tracking);
        params.put("user",user);
        params.put("ip",ip);
    }


    public Map<String, String> getParams() {
        return params;
    }


}


