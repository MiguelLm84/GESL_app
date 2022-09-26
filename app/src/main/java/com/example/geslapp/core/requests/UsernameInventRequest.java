package com.example.geslapp.core.requests;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class UsernameInventRequest  extends StringRequest {

    private final Map<String,String> params;

    public UsernameInventRequest(String username, Response.Listener<String> listener,String IP,String REC,String PROJECT){
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/usernameInvent.php",listener, null);
        params = new HashMap<>();
        params.put("username", username);
    }

    public Map<String,String> getParams(){
        return params;
    }
}
