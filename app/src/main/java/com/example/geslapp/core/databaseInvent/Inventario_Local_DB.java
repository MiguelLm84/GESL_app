package com.example.geslapp.core.databaseInvent;

import android.annotation.SuppressLint;
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
import com.example.geslapp.core.clases.Informes;
import com.example.geslapp.core.clases.Inventario;
import com.example.geslapp.core.database.Login_Local_DB;
import com.example.geslapp.core.requests.AntenasRequest;
import com.example.geslapp.core.requests.InventarioRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Inventario_Local_DB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "inventario";
    private static final String ID_INVENT = "id_invent";
    private static final String ID = "id";
    private static final String STATE = "estado";
    private static final String FECHA_APERTURA = "fecha_apertura";
    private static final String ID_USER_GESTOR = "id_user_gestor";
    private static final String ID_CENTRO = "id_centro";
    private static final String FECHA_CIERRE = "fecha_cierre";
    private static final String TIPO_INFORME = "tipo_informe";
    private static final String ID_USER = "id_user";
    private static final String FECHA_MADE = "fecha_made";
    private static final String POSITION = "position";
    private static final String MADEBY = "madeby";

    public Inventario_Local_DB(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" CREATE TABLE "+ TABLE_NAME + " ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ID_INVENT + " INTENGER,"+
                STATE +" VARCHAR,"+
                FECHA_APERTURA + " DATE,"+
                FECHA_CIERRE + " DATE," +
                FECHA_MADE + " DATE,"+
                ID_CENTRO + " INTEGER,"+
                ID_USER_GESTOR + " INTEGER,"+
                TIPO_INFORME + " VARCHAR,"+
                ID_USER + " INTEGER," +
                MADEBY + " VARCHAR,"+
                POSITION + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public int getIdInvent(int position,int id_centro) {

        SQLiteDatabase db = this.getWritableDatabase();

        @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT id_invent FROM inventario WHERE position = ? AND id_centro = ?",new String[]{String.valueOf(position),String.valueOf(id_centro)});
        if (c.moveToNext()) {
            return c.getInt(0);

        } else return 0;

    }

    public void getInvent(Context context) {

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> listaInventarios = new ArrayList<>();

        @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT * FROM inventario", null);

        if (c != null && c.getCount() != 0) {
            c.moveToFirst();
           do {
               listaInventarios.add(String.valueOf(new Inventario(
                       c.getInt(1),
                       c.getInt(2),
                       c.getString(3),
                       new Date(c.getString(4)),
                       new Date(c.getString(5)),
                       new Date(c.getString(6)),
                       c.getInt(7),
                       c.getInt(8),
                       c.getString(9),
                       c.getInt(10),
                       c.getString(11),
                       c.getInt(12)
               )));

           } while(c.moveToNext());

        } else {
            Toast.makeText(context, "No hay registros", Toast.LENGTH_SHORT).show();
        }
    }

    public void setInventarios() {

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime());

        onUpgrade(db,1,1);
        for (int i = 0; i<3;i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_INVENT,1);
            contentValues.put("tipo_informe", "general");
            contentValues.put("fecha_apertura", date);
            contentValues.put("estado", "pendiente");
            contentValues.put("id_centro",5);
            db.insert(TABLE_NAME, null, contentValues);
        }
        for (int i = 0; i<3;i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID_INVENT,1);
            contentValues.put("tipo_informe", "material");
            contentValues.put("fecha_apertura", date);
            contentValues.put("estado", "pendiente");
            contentValues.put("id_centro",5);
            db.insert(TABLE_NAME, null, contentValues);
        }
    }

    public ArrayList<Informes> centroselected(int id_centro, String user) {

        ArrayList<Informes> listainformes = new ArrayList<>();
        int i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery("SELECT fecha_apertura, fecha_cierre, tipo_informe, estado FROM inventario WHERE id_centro = ?", new String[]{String.valueOf(id_centro)});

        if (c.moveToFirst()) {
            do {
                String fecha_apertura = c.getString(0);
                String fecha_cierre = c.getString(1);
                String tipo_informe = c.getString(2);
                String estado = c.getString(3);

                listainformes.add(new Informes(i, fecha_apertura, fecha_cierre, tipo_informe, user, estado));
                i++;
            } while (c.moveToNext());
        }
        return listainformes;
    }

    public void fillInventario(Context context, String username) {

        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,1,1);
        final int[] lasIDCentro = {0};
        final int[] position = {0};

        Response.Listener<String> LISTENER = response -> {

            try {
                JSONObject jsonObject= new JSONObject(response);
                int success = jsonObject.getInt("success");
                String request = jsonObject.getString("message");

                if (success == 1) {
                   // Toast.makeText(context, "Insert realizado exitosamente1", Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = jsonObject.getJSONArray("array");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        int id = jsonObject1.getInt("id");
                        int id_centro = jsonObject1.getInt("id_centro");

                        if(id_centro != lasIDCentro[0]) {
                            lasIDCentro[0] = id_centro;
                            position[0] = 0;
                        }
                        int id_user_gestor = jsonObject1.getInt("id_user_gestor");
                        int id_tipo = jsonObject1.getInt("id_tipo");
                        int id_estado = jsonObject1.getInt("id_estado");
                        String fecha_apertura = jsonObject1.getString("fecha_apertura");
                        String fecha_cierrre = jsonObject1.getString("fecha_apertura");
                        String madeby = jsonObject1.getString("realizado");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("id_invent", id);
                        contentValues.put("id_centro", id_centro);
                        contentValues.put("id_user_gestor", id_user_gestor);
                        int idUser = getIdUser(username,context);
                        String estado_informe = getEstadoInforme(id_estado,context);
                        contentValues.put("estado",estado_informe);
                        String tipo_informe = getTipoInforme(id_tipo,context);
                        contentValues.put("tipo_informe",tipo_informe);
                        contentValues.put("id_user", idUser);
                        contentValues.put("fecha_apertura", fecha_apertura);
                        contentValues.put("fecha_cierre", fecha_cierrre);
                        contentValues.put("position",position[0]);
                        contentValues.put("madeby",madeby);
                        db.insert(TABLE_NAME, null, contentValues);
                        position[0]++;
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
        InventarioRequest inventarioRequest = new InventarioRequest(LISTENER,IP,REC,PROJECT);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(inventarioRequest);
    }

    private int getIdUser(String nombre,Context context) {

        Login_Local_DB login_local_db = new Login_Local_DB(context);
        return login_local_db.getIdUser(nombre);
    }

    private String getTipoInforme(int id_tipo, Context context) {

        Tipo_Informe_Local_DB tipo_informe_local_db = new Tipo_Informe_Local_DB(context);
        return tipo_informe_local_db.getTipoInforme(id_tipo);
    }

    private String getEstadoInforme(int valor,Context context) {

        Estado_Informe_Local_DB estado_informe_local_db = new Estado_Informe_Local_DB(context);
        return estado_informe_local_db.getEstadoInforme(valor);
    }

    public int getIdUserByIdInvent(int id_invent) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id_user FROM inventario WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});

        if(c.moveToFirst()) {
            return c.getInt(0);
        } else return -1;
    }

    public String getState(int id_inventario) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT estado FROM inventario WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst()) {
            return c.getString(0);

        } else return "";
    }

    public String getMade(int id_inventario) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT madeby FROM inventario WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst()) {
            return c.getString(0);

        } else return "";
    }

    public void updateState(int id_invent) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("estado","Abierto");
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_invent)});
    }

    public void deleteRows() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
