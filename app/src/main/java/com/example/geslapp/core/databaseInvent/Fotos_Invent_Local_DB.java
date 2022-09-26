package com.example.geslapp.core.databaseInvent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.DialogCargar;
import com.example.geslapp.core.clases.UploadImage;

import java.io.File;

public class Fotos_Invent_Local_DB extends SQLiteOpenHelper {

    private final static String TABLE_NAME = "fotos";
    private final static int DATABASE_VERSION = 1;

    private final static String ID = "id";
    private final static String NOMBRE = "nombre";
    private final static String ID_INVENT = "id_invent";
    public Fotos_Invent_Local_DB(@Nullable Context context){
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                NOMBRE + " VARCHAR,"+
                ID_INVENT + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void savefoto(int id_invent, String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_invent",id_invent);
        contentValues.put("nombre",name);
        db.insert(TABLE_NAME,null,contentValues);
    }

    public void getAllFotos(int id_invent, Context context, Activity activity) {

         int cont = 0;
        DialogCargar dialog = new DialogCargar(activity);
        dialog.startDialog();
        UploadImage uploadImage = new UploadImage();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT nombre FROM fotos WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});

        String nombre;
        if(c.moveToFirst()) {
            do{
                cont++;
                ConfigPreferences configPreferences = new ConfigPreferences();
                String IP = configPreferences.getIP(context);
                String REC = configPreferences.getRec(context);

                nombre = c.getString(0);

                Handler handler = new Handler();

                String finalNombre = nombre;
                final int finalCont = cont;
                handler.postDelayed(() -> {

                    File foto = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), finalNombre);
                    Bitmap bmp = BitmapFactory.decodeFile(foto.getPath());
                    uploadImage.doFileUpload("http://"+IP+"/gesl/"+REC+"/image_post.php",bmp, finalNombre,context, dialog, finalCont);

                },1000);

            } while (c.moveToNext());

        } else {
            Toast.makeText(context, "No hay fotos del inventario para subir", Toast.LENGTH_SHORT).show();
            dialog.dismissDialog();
        }
    }

    public void deleteFoto(String filename) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"nombre = ?",new String[]{filename});
    }

    public void deleteRows() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
