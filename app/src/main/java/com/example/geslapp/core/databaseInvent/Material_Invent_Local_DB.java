package com.example.geslapp.core.databaseInvent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import androidx.annotation.Nullable;
import com.example.geslapp.core.clases.Material_invent;
import java.io.File;


public class Material_Invent_Local_DB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "material_invent";
    private static final String ID_INVENT = "id_invent";
    private static final String ID = "id";

    private static final String CLIP_LISO_UBI = "clip_liso_ubi";
    private static final String CLIP_LISO_RETI = "clip_liso_reti";
    private static final String CLIP_LISO_TIENDA = "clip_liso_tienda";
   // private static final String CLIP_LISO_FOTO = "clip_liso_foto";
   private static final String  CLIP_LISO_ENVIADO= "clip_liso_enviado";

    private static final String CLIP_CRUZ_UBI = "clip_cruz_ubi";
    private static final String CLIP_CRUZ_RETI = "clip_cruz_reti";
    private static final String CLIP_CRUZ_TIENDA = "clip_cruz_tienda";
   //private static final String CLIP_CRUZ_FOTO = "clip_cruz_foto";
   private static final String  CLIP_CRUZ_ENVIADO= "clip_cruz_enviado";

    private static final String PIE_10_UBI = "pie_10_ubi";
    private static final String PIE_10_RETI = "pie_10_reti";
    private static final String PIE_10_TIENDA = "pie_10_tienda";
    //private static final String PIE_10_FOTO = "pie_10_foto";
    private static final String  PIE_10_ENVIADO= "pie_10_enviado";

    private static final String PIE_15_UBI = "pie_15_ubi";
    private static final String PIE_15_RETI = "pie_15_reti";
    private static final String PIE_15_TIENDA = "pie_15_tienda";
    //private static final String PIE_15_FOTO = "pie_15_foto";
    private static final String  PIE_15_ENVIADO= "pie_15_enviado";

    private static final String BASES_UBI = "bases_ubi";
    private static final String BASES_RETI = "bases_reti";
    private static final String BASES_TIENDA = "bases_tienda";
    //private static final String BASES_FOTO = "bases_foto";
    private static final String  BASES_ENVIADO= "bases_enviado";

    private static final String PINCHO_PESC_UBI = "pincho_pesc_ubi";
    private static final String PINCHO_PESC_RETI = "pincho_pesc_reti";
    private static final String PINCHO_PESC_TIENDA = "pincho_pesc_tienda";
    //private static final String PINCHO_PESC_FOTO = "pincho_pesc_foto";
    private static final String  PINCHO_PESC_ENVIADO= "pincho_pesc_enviado";

    private static final String PINCHO_CORTO_UBI = "pincho_corto_ubi";
    private static final String PINCHO_CORTO_RETI = "pincho_corto_reti";
    private static final String PINCHO_CORTO_TIENDA = "pincho_corto_tienda";
    //private static final String PINCHO_CORTO_FOTO = "pincho_corto_foto";
    private static final String  PINCHO_CORTO_ENVIADO= "pincho_corto_enviado";

    private static final String PINCHO_LARGO_UBI = "pincho_largo_ubi";
    private static final String PINCHO_LARGO_RETI = "pincho_largo_reti";
    private static final String PINCHO_LARGO_TIENDA = "pincho_largo_tienda";
    //private static final String PINCHO_LARGO_FOTO = "pincho_largo_foto";
    private static final String  PINCHO_LARGO_ENVIADO= "pincho_largo_enviado";

    private static final String SACA_CLIPS_UBI = "saca_clips_ubi";
    private static final String SACA_CLIPS_RETI = "saca_clips_reti";
    private static final String SACA_CLIPS_TIENDA = "saca_clips_tienda";
    //private static final String SACA_CLIPS_FOTO = "saca_clips_foto";
    private static final String  SACA_CLIPS_ENVIADO= "saca_clips_enviado";

    private static final String PERFILES_UBI = "perfiles_ubi";
    private static final String PERFILES_RETI = "perfiles_reti";
    private static final String PERFILES_TIENDA = "perfiles_tienda";
    //private static final String PERFILES_FOTO = "perfiles_foto";
    private static final String  PERFILES_ENVIADO= "perfiles_enviado";

    private static final String TERM_PERFILES_UBI = "term_perfiles_ubi";
    private static final String TERM_PERFILES_RETI = "term_perfiles_reti";
    private static final String TERM_PERFILES_TIENDA = "term_perfiles_tienda";
    //private static final String TERM_PERFILES_FOTO = "term_perfiles_foto";
    private static final String  TERM_PERFILES_ENVIADO= "term_perfiles_enviado";

    private static final String PINZAS_UBI = "pinzas_ubi";
    private static final String PINZAS_RETI = "pinzas_reti";
    private static final String PINZAS_TIENDA = "pinzas_tienda";
    //private static final String PINZAS_FOTO = "pinzas_foto";
    private static final String  PINZAS_ENVIADO= "pinzas_enviado";

    private static final String ELEVA_PASTIC_UBI = "eleva_plastic_ubi";
    private static final String ELEVA_PASTIC_RETI = "eleva_plastic_reti";
    private static final String ELEVA_PASTIC_TIENDA = "eleva_plastic_tienda";
    //private static final String ELEVA_PASTIC_FOTO = "eleva_plastic_foto";
    private static final String  ELEVA_PASTIC_ENVIADO= "eleva_plastic_enviado";

    private static final String ELEVA_METAL_UBI = "eleva_metal_ubi";
    private static final String ELEVA_METAL_RETI = "eleva_metal_reti";
    private static final String ELEVA_METAL_TIENDA = "eleva_metal_tienda";
    //private static final String ELEVA_METAL_FOTO = "eleva_metal_foto";
    private static final String  ELEVA_METAL_ENVIADO= "eleva_metal_enviado";

    private static final String PINCHO_CRUCETA_CORTO_UBI = "term_perfiles_ubi";
    private static final String PINCHO_CRUCETA_CORT_RETI = "term_perfiles_reti";
    private static final String PINCHO_CRUCETA_CORT_TIENDA = "term_perfiles_tienda";
    //private static final String PINCHO_CRUCETA_CORTO_FOTO = "term_perfiles_foto";
    private static final String  PINCHO_CRUCETA_CORTO_ENVIADO= "term_perfiles_enviado";

    private static final String PINCHO_CRUCETA_LARGO_UBI = "pinzas_ubi";
    private static final String PINCHO_CRUCETA_LARGO_RETI = "pinzas_reti";
    private static final String PINCHO_CRUCETA_LARGO_TIENDA = "pinzas_tienda";
    //private static final String PINCHO_CRUCETA_LARGO_FOTO = "pinzas_foto";
    private static final String  PINCHO_CRUCETA_LARGO_ENVIADO= "pinzas_enviado";

    private static final String SACA_CLIP_UBI = "eleva_plastic_ubi";
    private static final String SACA_CLIP_RETI = "eleva_plastic_reti";
    private static final String SACA_CLIP_TIENDA = "eleva_plastic_tienda";
    //private static final String SACA_CLIP_FOTO = "eleva_plastic_foto";
    private static final String  SACA_CLIP_ENVIADO= "eleva_plastic_enviado";

    private static final String TERMINACION_PERFILES_UBI = "eleva_metal_ubi";
    private static final String TERMINACION_PERFILES_RETI = "eleva_metal_reti";
    private static final String TERMINACION_PERFILES_TIENDA = "eleva_metal_tienda";
    //private static final String TERMINACION_PERFILESL_FOTO = "eleva_metal_foto";
    private static final String  TERMINACION_PERFILES_ENVIADO= "eleva_metal_enviado";

    public Material_Invent_Local_DB(@Nullable Context context){
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ID_INVENT + " INTEGER,"+
                CLIP_LISO_UBI + " VARCHAR,"+
                CLIP_LISO_RETI + " VARCHAR,"+
                CLIP_LISO_TIENDA + " VARCHAR,"+
                //CLIP_LISO_FOTO + " VARCHAR,"+
                CLIP_LISO_ENVIADO + " VARCHAR," +

                //CLIP_CRUZ_FOTO + " VARCHAR,"+
                CLIP_CRUZ_RETI + " VARCHAR,"+
                CLIP_CRUZ_TIENDA + " VARCHAR,"+
                CLIP_CRUZ_UBI + " VARCHAR,"+
                CLIP_CRUZ_ENVIADO + " VARCHAR," +

                //PIE_10_FOTO + " VARCHAR,"+
                PIE_10_RETI + " VARCHAR,"+
                PIE_10_TIENDA + " VARCHAR,"+
                PIE_10_UBI + " VARCHAR,"+
                PIE_10_ENVIADO + " VARCHAR," +

                //PIE_15_FOTO + " VARCHAR,"+
                PIE_15_RETI + " VARCHAR,"+
                PIE_15_TIENDA + " VARCHAR,"+
                PIE_15_UBI + " VARCHAR,"+
                PIE_15_ENVIADO + " VARCHAR," +

                //BASES_FOTO + "  VARCHAR,"+
                BASES_RETI + " VARCHAR,"+
                BASES_TIENDA + " VARCHAR,"+
                BASES_UBI + " VARCHAR,"+
                BASES_ENVIADO + " VARCHAR," +

                //PINCHO_PESC_FOTO + " VARCHAR,"+
                PINCHO_PESC_RETI + " VARCHAR,"+
                PINCHO_PESC_TIENDA + " VARCHAR,"+
                PINCHO_PESC_UBI + " VARCHAR,"+
                PINCHO_PESC_ENVIADO + " VARCHAR," +


                //PINCHO_CORTO_FOTO + " VARCHAR,"+
                PINCHO_CORTO_RETI + " VARCHAR,"+
                PINCHO_CORTO_TIENDA + " VARCHAR,"+
                PINCHO_CORTO_UBI + " VARCHAR,"+
                PINCHO_CORTO_ENVIADO + " VARCHAR," +


                //PINCHO_LARGO_FOTO + " VARCHAR,"+
                PINCHO_LARGO_RETI + " VARCHAR,"+
                PINCHO_LARGO_TIENDA + " VARCHAR,"+
                PINCHO_LARGO_UBI + " VARCHAR,"+
                PINCHO_LARGO_ENVIADO + " VARCHAR," +

                //SACA_CLIPS_FOTO + " VARCHAR,"+
                SACA_CLIPS_RETI + " VARCHAR,"+
                SACA_CLIPS_TIENDA + " VARCHAR,"+
                SACA_CLIPS_UBI + " VARCHAR,"+
                SACA_CLIPS_ENVIADO + " VARCHAR," +

                //PERFILES_FOTO + " VARCHAR,"+
                PERFILES_RETI + " VARCHAR,"+
                PERFILES_TIENDA + " VARCHAR,"+
                PERFILES_UBI + " VARCHAR,"+
                PERFILES_ENVIADO + " VARCHAR," +

                //TERM_PERFILES_FOTO + " VARCHAR,"+
                TERM_PERFILES_RETI + " VARCHAR,"+
                TERM_PERFILES_TIENDA + " VARCHAR,"+
                TERM_PERFILES_UBI + " VARCHAR,"+
                TERM_PERFILES_ENVIADO + " VARCHAR," +

                //PINZAS_FOTO + " VARCHAR,"+
                PINZAS_RETI + " VARCHAR,"+
                PINZAS_TIENDA + " VARCHAR,"+
                PINZAS_UBI + " VARCHAR,"+
                PINZAS_ENVIADO + " VARCHAR," +

                //ELEVA_METAL_FOTO + " VARCHAR,"+
                ELEVA_METAL_RETI + " VARCHAR,"+
                ELEVA_METAL_TIENDA + " VARCHAR,"+
                ELEVA_METAL_UBI + " VARCHAR,"+
                ELEVA_METAL_ENVIADO + " VARCHAR," +

                //ELEVA_PASTIC_FOTO + " VARCHAR,"+
                ELEVA_PASTIC_RETI + " VARCHAR,"+
                ELEVA_PASTIC_TIENDA + " VARCHAR,"+
                ELEVA_PASTIC_UBI + " VARCHAR," +
                ELEVA_PASTIC_ENVIADO + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void UpdateItem(String dbName, int id_invent,String reti, String tienda, String ubi, String enviado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbName+"_reti", reti);
        contentValues.put(dbName+"_tienda",tienda);
        contentValues.put(dbName+"_ubi",ubi);
        contentValues.put(dbName+"_enviado",enviado);
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_invent)});
    }

    public void UpdateSend(String dbName, String enviado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbName+"_enviado",enviado);
        db.update(TABLE_NAME,contentValues,"enviado = ?",new String[]{enviado});
    }

    public void firstInsert(int id_invent) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id_invent FROM material_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});
        if(c.moveToFirst()) {

        }
        else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_invent",id_invent);
            db.insert(TABLE_NAME,null,contentValues);
        }

        //onUpgrade(db,1,1);
    }

    /*public void updateFoto(String itemName, String name,int id_invent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(itemName+"_foto", name);
        db.update(TABLE_NAME,contentValues,"id_invent = ?",new String[]{String.valueOf(id_invent)});
    }

    public Bitmap getFoto(int id_invent, String itemname,Context context) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT "+itemname+"_foto FROM "+ TABLE_NAME +" WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});
        if(c.moveToFirst()) {
            if(c.getString(0) == null || c.getString(0).equals("")) {
                return null;
            }
            File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), c.getString(0));
            Bitmap bmp = BitmapFactory.decodeFile(image.getPath());

            return bmp;

        } else {
            return null;
        }
    }*/

    public Material_invent getAllData(int id_invent, String itenmane, String enviado) {

        Material_invent material_invent;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT "+itenmane+"_ubi, "+itenmane+"_reti, "+itenmane+"_tienda, "+itenmane+"_enviado FROM material_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});
        if(c.moveToFirst()) {
            String ubi = c.getString(0);
            String reti =  c.getString(1);
            String tienda = c.getString(2);
            String send = c.getString(3);
            material_invent = new Material_invent("",itenmane,ubi,reti,tienda, send);
        }
        else return null;
        return material_invent;
    }

    public String getUbi(int id_invent, String dbName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + dbName+"_ubi FROM material_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});
        if(c.moveToFirst()) {
            return c.getString(0);

        } else return "";
    }

    public String getTienda(int id_invent, String dbName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + dbName+"_tienda FROM material_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});
        if(c.moveToFirst()) {
            return c.getString(0);
        }
        else return "";
    }

    public String getReti(int id_invent, String dbName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + dbName+"_reti FROM material_invent WHERE id_invent = ?",new String[]{String.valueOf(id_invent)});
        if(c.moveToFirst()) {
            return c.getString(0);
        }
        else return "";
    }
    public void deleteRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
