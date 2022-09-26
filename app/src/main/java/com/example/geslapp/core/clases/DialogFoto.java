package com.example.geslapp.core.clases;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.geslapp.R;


public class DialogFoto {

    Activity activity;
    Bitmap bitmap;
    String text;

    public DialogFoto(Activity activity, Bitmap bitmap,String text) {

        this.activity = activity;
        this.bitmap = bitmap;
        this.text = text;
    }

    public void startDialog() {

       final Dialog dialog = new Dialog(activity);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       dialog.setCancelable(true);
       dialog.setContentView(R.layout.dialog_fotos);

        ImageView imgfoto = dialog.findViewById(R.id.imgDF);
        TextView txttext = dialog.findViewById(R.id.txtDF);
        Button butclose = dialog.findViewById(R.id.butDFclose);
        butclose.setOnClickListener(v -> dialog.dismiss());
        imgfoto.setImageBitmap(bitmap);
        txttext.setText(text);
        dialog.show();
    }
}