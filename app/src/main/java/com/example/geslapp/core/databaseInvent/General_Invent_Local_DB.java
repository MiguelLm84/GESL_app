package com.example.geslapp.core.databaseInvent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import androidx.annotation.Nullable;

import com.example.geslapp.core.clases.General_invent;

import java.io.File;

public class General_Invent_Local_DB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String ID = "id";
    public static final String TABLE_NAME = "general_invent";
    private static final String ID_INVENT = "id_invent";
    private static final String SERVIDOR_UBI = "servidor_ubi";
    private static final String SERVIDOR_FOTO1 = "servidor_foto1";
    private static final String SERVIDOR_FOTO2 = "servidor_foto2";
    private static final String POE_UBI = "poe_ubi";
    private static final String POE_FOTO = "poe_foto";
    private static final String CARTEL_UBI = "good_ubi";
    private static final String CARTEL_FOTO="good_foto";

    public General_Invent_Local_DB(@Nullable Context context) {
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ID_INVENT  + " INTEGER UNIQUE,"+
                SERVIDOR_UBI + " VARCHAR,"+
                SERVIDOR_FOTO1 + " VARCHAR,"+
                SERVIDOR_FOTO2 + " VARCHAR,"+
                POE_FOTO + " BLOB,"+
                POE_UBI + " VARCHAR,"+
                CARTEL_FOTO + " VARCHAR,"+
                CARTEL_UBI + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }


    public void saveData(String servubi, String poeubi, String goodubi, int id_invent) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("servidor_ubi",servubi);
        contentValues.put("poe_ubi",poeubi);
        contentValues.put("good_ubi",goodubi);
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_invent)});

    }

    public void firsInsert(int id_inventario) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_invent",id_inventario);

        Cursor c = db.rawQuery("SELECT id_invent FROM general_invent WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
       if(c.moveToFirst()) {

       } else {
           db.insert(TABLE_NAME, null, contentValues);
       }
    }

    public void updateFotoserver1(int id_inventario, String path) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("servidor_foto1",path);
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_inventario)});
    }

    public void updateFotoserver2(int id_inventario, String path) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("servidor_foto2",path);
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_inventario)});
    }
    public void updateFotoPoe(int id_inventario, String path) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("poe_foto",path);
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_inventario)});
    }
    public void updateFotoGood(int id_inventario, String path) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("good_foto",path);
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_inventario)});
    }
    public void updateUbi(int id_inventario, String ubiserver, String ubipoe, String ubigood) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("servidor_ubi",ubiserver);
        contentValues.put("poe_ubi",ubipoe);
        contentValues.put("good_ubi",ubigood);
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_inventario)});
    }

    public void resetTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,1,1);
    }

    public General_invent getAllData(Context context,int id_invent1) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM general_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent1)});
        int    id_invent;
        String servidor_ubi;
        String servidor_foto1;
        String servidor_foto2;
        String poe_ubi;
        String poe_foto;
        String cartel_ubi ;
        String cartel_foto;

        if(c.moveToFirst()) {
            do {
                 id_invent = c.getInt(1);
                 servidor_ubi = c.getString(2);
                 servidor_foto1 = c.getString(3);
                 servidor_foto2 = c.getString(4);
                 poe_foto = c.getString(5);
                 poe_ubi = c.getString(6);
                 cartel_foto = c.getString(7);
                 cartel_ubi =c.getString(8);


            }while (c.moveToNext());

            if(servidor_foto1 == null) servidor_foto1 = "";
            if(servidor_foto2 == null) servidor_foto2 = "";
            if(poe_foto == null) poe_foto = "";
            if(cartel_foto == null) cartel_foto = "";
            if(servidor_ubi == null) servidor_ubi = "";
            if(poe_ubi == null) poe_ubi="";
            if(cartel_ubi == null) cartel_ubi="";

            Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(context);
            int id_user = inventario_local_db.getIdUserByIdInvent(id_invent);

            return new General_invent(id_invent,
            servidor_ubi,
            servidor_foto1,
            servidor_foto2,
            poe_ubi,
            poe_foto,
            cartel_ubi,
            cartel_foto, id_user);
        }

        return null;
    }

    public Bitmap getServFoto(int id_inventario,Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT servidor_foto1 FROM general_invent WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst())
        {
            if(c.getString(0) == null || c.getString(0).equals(""))
            {
                return null;
            }
            File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), c.getString(0));
            Bitmap bmp = BitmapFactory.decodeFile(image.getPath());

            return bmp;
        }
        else
        {
            return null;
        }

    }

    public Bitmap getServFoto2(int id_inventario, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT servidor_foto2 FROM general_invent WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst())
        {
            if(c.getString(0) == null || c.getString(0).equals(""))
            {
                return null;
            }
            File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), c.getString(0));
            Bitmap bmp = BitmapFactory.decodeFile(image.getPath());

            return bmp;
        }
        else
        {
            return null;
        }
    }

    public Bitmap getPoeFoto(int id_inventario, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT poe_foto FROM general_invent WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst())
        {
            if(c.getString(0) == null || c.getString(0).equals(""))
            {
                return null;
            }
            File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), c.getString(0));
            Bitmap bmp = BitmapFactory.decodeFile(image.getPath());

            return bmp;
        }
        else
        {
            return null;
        }
    }
    public Bitmap getGoodFoto(int id_inventario, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT good_foto FROM general_invent WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst())
        {
            if(c.getString(0) == null || c.getString(0).equals(""))
            {
                return null;
            }
            File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), c.getString(0));
            Bitmap bmp = BitmapFactory.decodeFile(image.getPath());

            return bmp;
        }
        else
        {
            return null;
        }
    }

    public General_invent getOpenData(Context context,int id_invent1) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM general_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent1)});
        int    id_invent;
        String servidor_ubi;
        String servidor_foto1;
        String servidor_foto2;
        String poe_ubi;
        String poe_foto;
        String cartel_ubi ;
        String cartel_foto;
        if(c.moveToFirst())
        {
            do {
                id_invent = c.getInt(1);
                servidor_ubi = c.getString(2);
                servidor_foto1 = c.getString(3);
                servidor_foto2 = c.getString(4);
                poe_foto = c.getString(5);
                poe_ubi = c.getString(6);
                cartel_foto = c.getString(7);
                cartel_ubi =c.getString(8);


            }while (c.moveToNext());

            if(servidor_foto1 == null) servidor_foto1 = "";
            if(servidor_foto2 == null) servidor_foto2 = "";
            if(poe_foto == null) poe_foto = "";
            if(cartel_foto == null) cartel_foto = "";

            Inventario_Local_DB inventario_local_db = new Inventario_Local_DB(context);
            int id_user = inventario_local_db.getIdUserByIdInvent(id_invent);

            return new General_invent(id_invent,
                    servidor_ubi,
                    servidor_foto1,
                    servidor_foto2,
                    poe_ubi,
                    poe_foto,
                    cartel_ubi,
                    cartel_foto, id_user);
        }

        return null;
    }

    public boolean getOpen( int id_invent) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM general_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});
        if(c.moveToFirst())
        {
            return true;
        }
        else return false;

    }

    public String getServUbi(int id_inventario) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT servidor_ubi FROM "+TABLE_NAME+" WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst())
        {
            return c.getString(0);
        }
        else return "";
    }

    public String getPoeUbi(int id_inventario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT poe_ubi FROM "+TABLE_NAME+" WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst())
        {
            return c.getString(0);
        }
        else return "";
    }

    public String getGoodUbi(int id_inventario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT good_ubi FROM "+TABLE_NAME+" WHERE id_invent = ?",new String[]{String.valueOf(id_inventario)});
        if(c.moveToFirst())
        {
            return c.getString(0);
        }
        else return "";
    }

    public void deleteRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}


