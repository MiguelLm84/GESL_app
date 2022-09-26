package com.example.geslapp.core.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EansRequest extends StringRequest {

    private final Map<String,String> params;

    public EansRequest(String id, String modelo, String eans, Response.Listener<String> listener,String IP,String REC,String PROJECT,String Uip,String user){
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/escaneartienda.php",listener, null);
        params = new HashMap<>();
        params.put("id_centro", id);
        params.put("modelo", modelo);
        params.put("ean", eans);
        params.put("user",user);
        params.put("ip",Uip);
    }

    public Map<String,String> getParams(){
        return params;
    }


}

