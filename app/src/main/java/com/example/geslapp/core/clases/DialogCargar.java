package com.example.geslapp.core.clases;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.geslapp.R;

public class DialogCargar {
    Activity activity;
    AlertDialog alertDialog;

    public DialogCargar(Activity activity)
    {
        this.activity = activity;
    }

    public void startDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_cargar,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();


    }

    public void dismissDialog()
    {
        alertDialog.dismiss();
    }

}
