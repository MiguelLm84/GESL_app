package com.example.geslapp.core.uploads;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.geslapp.core.clases.General_invent;
import com.example.geslapp.core.clases.Material_invent;

import java.util.HashMap;
import java.util.Map;

public class Upload_material_invent extends StringRequest {

    private Map<String,String> params;

    public Upload_material_invent(Response.Listener<String> listener, Material_invent material, String IP, String REC, int id_invent,String made) {
        super(Method.POST, "http://"+IP+"/gesl/"+REC+"/material_invent.php", listener, null);
        params = new HashMap<>();
        String itemname = material.getDbName();
        params.put("id_invent",String.valueOf(id_invent));
        params.put("itemname",itemname);
        String ubi = material.getUbi();
        if(ubi == null) ubi = "";
        String reti = material.getReti();
        if(reti == null) reti = "";
        //String foto = material.getFoto();
        //if(foto == null) foto = "";
        String tienda = material.getTienda();
        if(tienda == null) tienda = "";
        params.put(itemname+"_ubi",ubi);
        params.put(itemname+"_reti", reti);
        params.put(itemname+"_tienda", tienda);
        //params.put(itemname+"_foto", foto);
        params.put("madeby",made);
    }

    public Map<String, String> getParams() {
        return params;
    }
}
