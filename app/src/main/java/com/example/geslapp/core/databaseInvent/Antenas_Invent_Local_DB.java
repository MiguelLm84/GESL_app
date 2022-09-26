package com.example.geslapp.core.databaseInvent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.geslapp.core.clases.Antenas;
import com.example.geslapp.core.clases.AntenasInvent;
import com.example.geslapp.core.clases.ConfigPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class Antenas_Invent_Local_DB extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "antenas_invent";
    private static final int DATABASE_VERSION = 1;
    private static final String ID = "id";
    private static final String ID_INVENTARIO = "id_inventario";
    private static final String ID_ANTENA = "id_antena";
    private static final String FOTO = "foto";
    private static final String UBICACION = "ubicacion";



    public Antenas_Invent_Local_DB(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ID_INVENTARIO + " INTEGER,"+
                ID_ANTENA + " INTEGER UNIQUE,"+
                FOTO + " VARCHAR,"+
                UBICACION + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void saveAntenas(int id_antena,String ubi,int id_inventario)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ubicacion", ubi);
        contentValues.put("id_inventario", id_inventario);
        contentValues.put("id_antena",id_antena);

        Cursor c = db.rawQuery("SELECT id FROM antenas_invent WHERE id_antena = ?",new String[]{String.valueOf(id_antena)});
        if(c.moveToFirst())
        {
            db.update(TABLE_NAME,contentValues,"id_antena = ? AND id_inventario = ?",new String[]{String.valueOf(id_antena),String.valueOf(id_inventario)});
        }
        else db.insert(TABLE_NAME, null  ,contentValues );


    }

    public void updateFoto(String path,int id_antena,int id_invent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("foto", path);
        db.update(TABLE_NAME, contentValues, "id_antena = ? AND id_inventario = ?",new String[]{String.valueOf(id_antena),String.valueOf(id_invent)});
    }

    public void resetTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,1,1);
    }

    public int getAntenasCount()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM "+ TABLE_NAME,new String[]{});
        if(c.moveToFirst()){
            c.moveToLast();
            return c.getInt(0);
        }
        else return -1;
    }


    public String getFoto(int id_antena,int id_invent) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT foto FROM antenas_invent WHERE id_antena = ? AND id_inventario = ?",new String[]{String.valueOf(id_antena),String.valueOf(id_invent)});
        String foto;
        if(c.moveToFirst())
        {
            foto = c.getString(0);
            if(foto == null) foto = "NOFOTO";
        }
        else
        {
            foto = "NOFOTO";
        }
        return foto;
    }

    public ArrayList<AntenasInvent> getAllAntenas(int id_invent)
    {
        ArrayList<AntenasInvent> listaantenas = new ArrayList<>();
        int i = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM antenas_invent WHERE id_inventario = ?",new String[]{String.valueOf(id_invent)});
        if(c.moveToFirst())
        {
            do{
                int id_antena = c.getInt(2);
                int id_Invent = c.getInt(1);
                String foto = c.getString(3);
                String ubicacion = c.getString(4);

                listaantenas.add(new AntenasInvent(id_Invent,id_antena,foto,ubicacion));
                i++;
            } while(c.moveToNext());
            return  listaantenas;
        }//end if
        else
        {
            return listaantenas;
        }

    }


    public String getUbi(int id_antena, int id_invent) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ubicacion FROM antenas_invent WHERE id_antena = ? AND id_inventario = ?",new String[]{String.valueOf(id_antena),String.valueOf(id_invent)});
        if(c.moveToFirst())
        {
            return c.getString(0);
        }
        else
            return "";
    }

    public void delteRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
