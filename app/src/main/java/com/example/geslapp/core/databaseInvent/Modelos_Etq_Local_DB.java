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
import com.example.geslapp.core.requests.EstadoInformeRequest;
import com.example.geslapp.core.requests.ModelosEtqRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Modelos_Etq_Local_DB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "modelos_etq";
    private static final int DATABASE_VERSION = 1;

    private static final String ID = "id";
    private static final String MODELO = "modelo";
    private static final String ID_MODELO = "id_modelo";

    public Modelos_Etq_Local_DB(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ID_MODELO + " INTEGER,"+
                MODELO + " VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public String getModelo(int id_modelo) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT modelo FROM modelos_etq WHERE id_modelo = ?",new String[]{String.valueOf(id_modelo)});
        if(c.moveToFirst())
        {
            return c.getString(0);
        }
        else return "";
    }

    public int getIdModelo(String modelo) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id_modelo FROM modelos_etq WHERE modelo = ?",new String[]{modelo});
        if(c.moveToFirst())
        {
            return c.getInt(0);
        }
        else return -1;
    }

    public void fillData(Context context)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,1,1);
        Response.Listener<String> LISTENER = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject= new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    String request = jsonObject.getString("message");

                    if (success == 1) {
                        //Toast.makeText(context, "Insert realizado exitosamente-MODELOS", Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray("array");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            int id = jsonObject1.getInt("id");

                            String modelo = jsonObject1.getString("modelo");
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("id_modelo",id);
                            contentValues.put("modelo",modelo);

                            db.insert(TABLE_NAME, null, contentValues);
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Ha ocurrido un problema, volver a cargar", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String IP = new ConfigPreferences().getIP(context);
        String REC = new ConfigPreferences().getRec(context);
        String PROJECT = new ConfigPreferences().getRec(context);
        ModelosEtqRequest estadoInformeRequest = new ModelosEtqRequest(LISTENER,IP,REC,PROJECT);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(estadoInformeRequest);
    }//END FILLDATA

    public void deleteRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
