package com.example.geslapp.core.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class TipoInformeRequest extends StringRequest {
    public TipoInformeRequest(Response.Listener<String> listener,String IP,String REC,String PROJECT) {
        super(Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/tipo_inventarios.php", listener, null);
    }
}
