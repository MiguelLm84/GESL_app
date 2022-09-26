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
import com.example.geslapp.core.clases.ConfigPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login_Local_DB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "users";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PASS = "password";
    private static final String ID_USER = "id_user";

    public Login_Local_DB(@Nullable Context context){
        super(context, TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME+" VARCHAR," +
                ID_USER+ " INTEGER,"+
                PASS + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
       onCreate(db);
    }

    public void setUser(String user, String pass, int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user);
        contentValues.put("password", pass);
        contentValues.put("id_user",id);

        db.insert(TABLE_NAME,null,contentValues);
    }

    public void getDBUser(Context context) {

        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db,1,1);
        String IP = new ConfigPreferences().getIP(context);
        String REC = new ConfigPreferences().getRec(context);
        String PROJECT = new ConfigPreferences().getProject(context);
        StringRequest usersRequest = new StringRequest(Request.Method.POST, "http://"+IP+"/"+PROJECT+"/"+REC+"/usuarios.php",
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("array");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String nombre = jsonObject1.getString("NombreUsuario");
                            String password = jsonObject1.getString("Pass");
                            int id = jsonObject1.getInt("Id");

                            setUser(nombre,password,id);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(usersRequest);
    }

    public void deleteRows() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }

    public void UpdatePassword(String password, String user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("password",password);
        db.update(TABLE_NAME,cv,"name=?", new String[]{user});
        db.close();
    }

    public boolean checkUser(String username, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM users WHERE password = ? and name = ? ",new String[]{password,username});

        if(c.moveToFirst()) {
           String name =  String.valueOf(c.getString(0));
            return true;
        } else return false;
    }

    public int getIdUser(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id_user FROM users WHERE name = ?",new String[]{nombre});
        if(c.moveToFirst()) {
            return c.getInt(0);
        }
        else return -1;
    }
}
