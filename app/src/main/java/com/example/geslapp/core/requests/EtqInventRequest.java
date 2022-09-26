package com.example.geslapp.core.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class EtqInventRequest extends StringRequest {
    public EtqInventRequest(Response.Listener<String> listener, String IP, String REC,String PROJECT) {
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/datos_etq_invent.php", listener, null);

    }
}
