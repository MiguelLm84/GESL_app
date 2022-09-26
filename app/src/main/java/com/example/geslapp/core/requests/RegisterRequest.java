package com.example.geslapp.core.requests;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.ConfigPreferences;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    private static Context context;
    static ConfigPreferences config = new ConfigPreferences();
    private static String IP = config.getIP(context);
    private static String REC = config.getRec(context);
    private static String PROJECT = config.getProject(context);

    private static final String REGISTER_REQUEST_URL = "http://192.180.1.101/AGIntranet/register2.php";
    private Map<String,String> params;
    public RegisterRequest(String username, String password,Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL,listener, null);
        params = new HashMap<>();
        params.put("username" , username);
        params.put("password" , password);
    }

    public Map<String,String> getParams(){
        return params;
    }

    public void setContext(Context context) {
        RegisterRequest.context = context;
    }
}

