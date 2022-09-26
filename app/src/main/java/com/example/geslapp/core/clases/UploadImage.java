package com.example.geslapp.core.clases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.core.databaseInvent.Fotos_Invent_Local_DB;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class UploadImage {

    public static final String TAG = "Upload Image Apache";
    private static String url1;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private  boolean once = false;
    private static int contUp = 1;

    public void doFileUpload(final String url, final Bitmap bmp, String filename,Context context1,DialogCargar dialog, int cont){

        once =false;
        url1 = url;
        context = context1;
        RealizarPost(bmp,filename, dialog,cont);
       // Toast.makeText(context, "Subiendo imagen....", Toast.LENGTH_SHORT).show();

    }

    public String convertBitmapToString(Bitmap bmp){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        return Base64.encodeBytes(byte_arr);
    }
    public void RealizarPost(Bitmap bmp,String filename, DialogCargar dialog, int cont) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, url1,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                       // Toast.makeText(context, "Imagen "+filename+" guardada en el servidor", Toast.LENGTH_SHORT).show();
                        Fotos_Invent_Local_DB fotos_invent_local_db = new Fotos_Invent_Local_DB(context);
                        fotos_invent_local_db.deleteFoto(filename);

                        if(contUp>=cont){

                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                Toast.makeText(context, "Imágenes guardadas en el servidor", Toast.LENGTH_SHORT).show();
                                dialog.dismissDialog();
                                contUp  = 0;

                            },2000);
                        }
                        contUp++;
                    }

                }, error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Imagen NO guardada en el servidor", Toast.LENGTH_SHORT).show();
                    dialog.dismissDialog();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                if(!once) {
                    once = true;
                    Map<String, String> params = new HashMap<>();

                    params.put("filename", filename);
                    params.put("image", convertBitmapToString(bmp));
                    return params;
                }
                return null;
            }
        };

        Volley.newRequestQueue(context).add(postRequest);
    }

    public int getContUp()
    {
        return contUp;
    }
}
