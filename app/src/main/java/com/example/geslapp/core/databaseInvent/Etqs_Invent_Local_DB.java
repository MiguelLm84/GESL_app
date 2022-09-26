package com.example.geslapp.core.databaseInvent;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.transition.Explode;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Etqs_invent;
import com.example.geslapp.core.requests.EtqInventRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


public class Etqs_Invent_Local_DB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "etqs_invent";
    private static final int DATABASE_VERSION = 1;
    private static final String ID = "id";
    private static final String POS = "position";
    private static final String ID_INVENT = "id_invent";
    private static final String MODELO_ETQ = "modelo_etq";
    private static final String RETIRADAS_CAJAS = "retiradas_cajas";
    private static final String RETIRADAS_ETQ = "retiradas_etq";
    private static final String TIENDA_CAJAS = "tienda_cajas";
    private static final String TIENDA_ETQ = "tienda_etq";
    private static final String FOTO_ETQ = "foto_etq";
    private static final String ASIG_CAJAS_GESL = "asig_cajas_gesl";
    private static final String ASIG_CAJAS = "asig_cajas";
    private static final String ASIG_ETQ = "asig_etq";

    private static final String DESASIG_CAJAS_GESL = "desasig_cajas_gesl";
    private static final String DESASIG_CAJAS = "desasig_cajas";
    private static final String DESASIG_ETQ = "desasig_etq";
    private static final int[] pos = {0};
    private static int lastId = 0;

    public Etqs_Invent_Local_DB(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ID_INVENT + " INTEGER,"+
                MODELO_ETQ + " VARCHAR,"+
                POS + " INTEGER,"+
                RETIRADAS_CAJAS + " VARCHAR,"+
                RETIRADAS_ETQ + " VARCHAR,"+
                TIENDA_CAJAS + " VARCHAR,"+
                TIENDA_ETQ + " VARCHAR,"+
                FOTO_ETQ + " VARCHAR,"+
                ASIG_CAJAS + " VARCHAR,"+
                ASIG_CAJAS_GESL + " VARCHAR,"+
                ASIG_ETQ + " VARCHAR,"+
                DESASIG_CAJAS +" VARCHAR,"+
                DESASIG_CAJAS_GESL + " VARHCAR,"+
                DESASIG_ETQ + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void saveData(int id_invent,String retircajas, String retiretq, String ticajas,
                         String tietq, String asiggesl, String asigcajas, String asigetq,
                         String desasigcajas, String desasigets, String desasiggesl,int position) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("retiradas_cajas",retircajas);
        contentValues.put("retiradas_etq",retiretq);
        contentValues.put("tienda_cajas",ticajas);
        contentValues.put("tienda_etq",tietq);
        contentValues.put("asig_cajas",asigcajas);
        contentValues.put("asig_etq",asigetq);
        contentValues.put("asig_cajas_gesl",asiggesl);
        contentValues.put("desasig_cajas",desasigcajas);
        contentValues.put("desasig_etq",desasigets);
        contentValues.put("desasig_cajas_gesl",desasiggesl);

        db.update(TABLE_NAME,contentValues,"id_invent = ? AND position = ?",new String[]{String.valueOf(id_invent),String.valueOf(position)});
    }

    public void updateFoto(int position, String name) {
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("foto_etq",name);
        db.update(TABLE_NAME,contentValues,"position = ?",new String[]{String.valueOf(position)});
    }

    public int getEtqsCount() {

        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT id FROM "+ TABLE_NAME,new String[]{});
        if(c.moveToFirst()){
            c.moveToLast();
            return c.getInt(0);
        }
        else return -1;
    }

    public Bitmap getFoto(int id_invent, Context context, int pos) {

        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT foto_etq FROM etqs_invent WHERE id_invent = ? AND position = ?",new String[]{String.valueOf(id_invent),String.valueOf(pos)});

        if(c.moveToFirst()) {
            if(c.getString(0) == null || c.getString(0).equals("")) {
                return null;
            }
            File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), c.getString(0));

            return BitmapFactory.decodeFile(image.getPath());

        } else {
            return null;
        }
    }

    public void fillServerData(Context context) {

        //onUpgrade(db,1,1);
        pos[0] = 0;
        Response.Listener<String> LISTENER = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int success = jsonObject.getInt("success");
                String request = jsonObject.getString("message");

                if(success == 1) {
                   data(jsonObject, context);

                } else {
                    Toast.makeText(context, request, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        String IP = new ConfigPreferences().getIP(context);
        String REC = new ConfigPreferences().getRec(context);
        String PROJECT = new ConfigPreferences().getProject(context);
        EtqInventRequest estadoInformeRequest = new EtqInventRequest(LISTENER,IP,REC,PROJECT);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(estadoInformeRequest);
    }

    private void data(JSONObject jsonObject, Context context) {

        final boolean[] insert = {false};
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();

        //Toast.makeText(context, "Insert realizado exitosamente-ETQS", Toast.LENGTH_SHORT).show();
        JSONArray jsonArray;
        try {
            jsonArray = jsonObject.getJSONArray("array");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int id = jsonObject1.getInt("id");
                int id_invent = jsonObject1.getInt("id_inventario");

                if(lastId!=id_invent){
                    lastId = id_invent;
                    pos[0] = 0;
                }

                int id_modelo = jsonObject1.getInt("id_modelo");

                ContentValues contentValues = new ContentValues();
                contentValues.put("id",id);
                contentValues.put("id_invent",id_invent);
                contentValues.put("position",pos[0]);

                Modelos_Etq_Local_DB modelos_etq_local_db = new Modelos_Etq_Local_DB(context);
                String modelo = modelos_etq_local_db.getModelo(id_modelo);
                contentValues.put("modelo_etq",modelo);
                @SuppressLint("Recycle") Cursor c = db1.rawQuery("SELECT id FROM etqs_invent WHERE id_invent = ? AND position = ?",new String[]{String.valueOf(id_invent),String.valueOf(pos[0])});

                insert[0] = !c.moveToFirst();

                if(insert[0]) {
                    db.insert(TABLE_NAME, null, contentValues);
                    //Toast.makeText(context, "Datos insert", Toast.LENGTH_SHORT).show();

                } else {
                    db.update(TABLE_NAME,contentValues,"id_invent = ? AND position = ?",new String[]{String.valueOf(id_invent),String.valueOf(pos[0])});
                    //Toast.makeText(context, "datos updated", Toast.LENGTH_SHORT).show();
                }
                pos[0]++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Etqs_invent> fillArray(int id_invent, Context context){

        String idInvent = String.valueOf(id_invent);
        ArrayList<Etqs_invent> listaEtqs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM etqs_invent WHERE id_invent = ? " +
                "ORDER BY position",new String[]{idInvent});

        if(c != null && c.getCount() != 0) {
            if(c.moveToFirst()) {
                do {
                    listaEtqs.add(new Etqs_invent(c.getInt(0),c.getInt(1),
                            c.getString(2),c.getInt(3), c.getString(4),
                            c.getString(5),c.getString(6),c.getString(7),
                            c.getString(8),c.getString(10),c.getString(9),
                            c.getString(11),c.getString(13),c.getString(12),
                            c.getString(14)));

                } while(c.moveToNext());
            }

        } else {
            Log.e("ERROR INVENTARIOS MATERIAL", "No se han encontrado inventarios. Cursor: " + listaEtqs);
        }
        db.close();
        Objects.requireNonNull(c).close();

    return listaEtqs;
}

    public void firsInsert(int id_inventario) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_invent",id_inventario);
        db.insert(TABLE_NAME,null,contentValues);
    }

    public boolean getOpen(int id_invent) {

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT * FROM etqs_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});

        return c.moveToFirst();
    }

    public void deleteRows() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
