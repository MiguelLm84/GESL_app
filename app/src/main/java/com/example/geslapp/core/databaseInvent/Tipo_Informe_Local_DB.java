package com.example.geslapp.core.databaseInvent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.requests.InventarioRequest;
import com.example.geslapp.core.requests.TipoInformeRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Tipo_Informe_Local_DB extends SQLiteOpenHelper {

    private final static String TABLE_NAME = "tipo_informe";
    private final static int DATABASE_VERSION = 1;
    private final static String ID ="id";
    private final static String NOMBRE = "nombre";
    private final static String VALOR = "valor";

    public Tipo_Informe_Local_DB(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                NOMBRE + " VARCHAR,"+
                VALOR + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void fillData(Context context) {

        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,1,1);
        Response.Listener<String> LISTENER = response -> {

            try {
                JSONObject jsonObject= new JSONObject(response);
                int success = jsonObject.getInt("success");
                String request = jsonObject.getString("message");

                if (success == 1) {
                    //Toast.makeText(context, "Insert realizado exitosamente-TIPO", Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = jsonObject.getJSONArray("array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id = jsonObject1.getInt("id");
                        int valor = jsonObject1.getInt("valor");
                        String nombre = jsonObject1.getString("nombre");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("id",id);
                        contentValues.put("valor",valor);
                        contentValues.put("nombre",nombre);
                        db.insert(TABLE_NAME, null, contentValues);
                    }

                } else {
                    Toast.makeText(context, "Ha ocurrido un problema, volver a cargar", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        String IP = new ConfigPreferences().getIP(context);
        String REC = new ConfigPreferences().getRec(context);
        String PROJECT = new ConfigPreferences().getProject(context);
        TipoInformeRequest tipoInformeRequest = new TipoInformeRequest(LISTENER,IP,REC,PROJECT);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(tipoInformeRequest);
    }//END FILLDATA

    public String getTipoInforme(int id_tipo) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT nombre FROM tipo_informe WHERE valor = ?", new String[]{String.valueOf(id_tipo)});

        if(c.moveToFirst()) {
            return c.getString(0);
        }else return null;
    }

    public void deleteRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
