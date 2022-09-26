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
import com.example.geslapp.core.clases.Antenas;
import com.example.geslapp.core.requests.AntenasRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Antenas_Local_DB extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "antenas";
    private static final int DATABASE_VERSION = 1;

    private static final String ID = "id";

    private static final String ID_CENTRO = "id_centro";
    private static final String ID_ANTENA = "id_antena";



    public Antenas_Local_DB(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +TABLE_NAME + " (" +
                ID  + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ID_ANTENA +" INTEGER,"+
                ID_CENTRO + " INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            onCreate(db);
    }

    public void fillDBdata(Context context,String IP, String REC, String PROJECT, int id_centro)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,1,1);
        Response.Listener<String> LISTENER = response -> {

            try {
                JSONObject jsonObject= new JSONObject(response);
                int success = jsonObject.getInt("success");
                String request = jsonObject.getString("message");

                if (success == 1) {
                    //Toast.makeText(context, "Insert realizado exitosamente", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Ha ocurrido un problema, volver a cargar", Toast.LENGTH_SHORT).show();
                }

                    JSONArray jsonArray = jsonObject.getJSONArray("array");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    int id_antena = jsonObject1.getInt("id");
                    int id_centro1 = jsonObject1.getInt("id_centro");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id_antena", id_antena);
                    contentValues.put("id_centro", id_centro1);
                    db.insert(TABLE_NAME, null, contentValues);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        };
        String idcentro = String.valueOf(id_centro);
        //Toast.makeText(context, idcentro, Toast.LENGTH_SHORT).show();
        AntenasRequest antenasRequest = new AntenasRequest(LISTENER,IP,REC,PROJECT,idcentro);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(antenasRequest);



    }

    public ArrayList<Antenas> getAntenas(int id_centro)
    {
        ArrayList<Antenas> listaantenas = new ArrayList<>();
        int i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id, id_antena FROM antenas WHERE id_centro = ?",new String[]{String.valueOf(id_centro)});
        if(c.moveToFirst())
        {
            do{
                int id = c.getInt(0);
                int id_antenas = c.getInt(1);

                listaantenas.add(new Antenas(id,id_antenas));
                i++;
            } while(c.moveToNext());
            return  listaantenas;
        }//end if
        else
        {
            return listaantenas;
        }
    }//end getAntenas


    public void setAntenas()
    {


        SQLiteDatabase db = this.getWritableDatabase();
        //onUpgrade(db,1,1);
        for (int i = 0; i<3;i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", i);
            contentValues.put("nombre", "antenax");
            contentValues.put("id_centro",3271);
            db.insert(TABLE_NAME, null, contentValues);
        }


    }

    public void delteRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
