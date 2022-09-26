package com.example.geslapp.core.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Cajas_Local_DB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "caja";
    public static final String ID_CENTROS = "id_centro";
    public static final String TRACKING = "tracking";
    public static final String NAME = "name";

    public Cajas_Local_DB(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("DROP TABLE '" +TABLE_NAME+"'");
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " ("
        +ID_CENTROS+" INTEGER," +
        NAME + " VARCHAR," + TRACKING + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
    }

    public void setCaja(String nombre, String tracking, int idCentro) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_centro",idCentro);
        contentValues.put("name",nombre);
        contentValues.put("tracking",tracking);
        db.insert(TABLE_NAME,null,contentValues);

    }

    public void getDBCajas(Context context, String IP, String REC, String PROJECT) {

        StringRequest cajasRequest = new StringRequest(Request.Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/cajas.php",
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("array");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String nombre = jsonObject1.getString("nombre");
                            int id_centros = jsonObject1.getInt("id_centro");
                            String tracking = jsonObject1.getString("tracking");
                            setCaja(nombre,tracking,id_centros);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(cajasRequest);
    }

    public String getTracking(String tracking) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM caja WHERE tracking = ?",new String[]{tracking});
       if(c.moveToFirst()) {
           return String.valueOf(c.getString(0));

       } else return "0";

    }
    public void deleteRows() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
