package com.example.geslapp.core.requests;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.ConfigPreferences;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginRequest extends StringRequest {

    private Map<String,String> params;
    public LoginRequest(String username, String password, Response.Listener<String> listener,String IP,String REC,String PROJECT, String ip){
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/login.php",listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password",password);
        params.put("ip_user",ip);

    }

    public Map<String,String> getParams(){
        return params;
    }




}

