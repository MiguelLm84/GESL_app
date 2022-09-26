package com.example.geslapp.core.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class ModelosEtqRequest extends StringRequest {
    public ModelosEtqRequest(Response.Listener<String> listener, String IP, String REC, String PROJECT) {
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/datos_modelos_etq.php", listener, null);

    }
}
