package com.example.geslapp.core.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geslapp.R;
import com.example.geslapp.core.clases.BarcodePreferences;
import java.util.ArrayList;


public class RvAdapterListaEtqTienda extends RecyclerView.Adapter<RvAdapterListaEtqTienda.MyViewHolder> {

    private final Context context;
    private final ArrayList<String> listaEtqsCodigos;
    private Button btnRemove, btnCancel;
    private TextView tvBarcodeRemove;
    BarcodePreferences barcodePreferences;
    private Drawable drawable;

    public RvAdapterListaEtqTienda(ArrayList<String> listaEtqsCodigos, Context context) {

        this.listaEtqsCodigos = listaEtqsCodigos;
        this.context = context;

        barcodePreferences = new BarcodePreferences();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_codes,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvCodeEtq.setText(listaEtqsCodigos.get(position));
        holder.btn_remove.setOnClickListener(v -> showDialog(position));

        changeColorItems(holder, position);

        /*holder.tamanho.setText(listaEtqsEscaneadas.get(position).getTamanho());
        holder.ceco.setText(listaEtqsEscaneadas.get(position).getCeco());
        holder.nomCentro.setText(listaEtqsEscaneadas.get(position).getNomCentro());
        holder.tipoEtq.setText(listaEtqsEscaneadas.get(position).getTipoEtq());

        holder.btn_option.setOnClickListener(v -> {

        });*/
    }



    @SuppressLint("NotifyDataSetChanged")
    private void showDialog(int position) {

        /*this.listaEtqsEscaneadas.remove(position);
        notifyDataSetChanged();*/

        //int newPosition = holder.getAdapterPosition();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_eliminar, null);
        builder.setView(dialogLayout);
        final AlertDialog dialog = builder.create();
        initDialogRemove(dialogLayout);
        tvBarcodeRemove.setText(listaEtqsCodigos.get(position));
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnRemove.setOnClickListener(v -> removeItem(position, dialog));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void changeColorItems(@NonNull MyViewHolder holder, int position) {

        ColorStateList csl = AppCompatResources.getColorStateList(context, R.color.black);
        ColorStateList csl2 = AppCompatResources.getColorStateList(context, R.color.yellow);
        ColorStateList csl3 = AppCompatResources.getColorStateList(context, R.color.re);

        if (position == 1) {
            holder.cardViewItem.setBackgroundTintList(csl2);
            ImageViewCompat.setImageTintList(holder.iv_iconBarcode, csl);
            ImageViewCompat.setImageTintList(holder.btn_remove, csl);

        } else if (position == 2) {
            holder.cardViewItem.setBackgroundTintList(csl3);
            Toast.makeText(context, "CAJA COMPLETA", Toast.LENGTH_LONG).show();
            //TODO: Activar visibilidad booton 'Guardar Caja'.

        } else {
            holder.cardViewItem.setBackgroundTintList(csl);
        }
    }

    //Método para mapear los componentes del dialogo eliminar.
    private void initDialogRemove(View dialogLayout) {

        btnRemove = dialogLayout.findViewById(R.id.btn_eliminar_dialog);
        btnCancel = dialogLayout.findViewById(R.id.btn_cancelar_dialog);
        tvBarcodeRemove = dialogLayout.findViewById(R.id.tvBarcodeRemove);
    }

    private void removeItem(int position, final AlertDialog dialog) {

        listaEtqsCodigos.remove(position);
        barcodePreferences.removeBarcodePref(context, listaEtqsCodigos.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listaEtqsCodigos.size());
        dialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return listaEtqsCodigos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        /*TextView tamanho, ceco, nomCentro, tipoEtq;
        ImageButton btn_option;*/

        TextView tvCodeEtq;
        ImageButton btn_remove;
        CardView cardViewItem;
        ImageView iv_iconBarcode;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCodeEtq = itemView.findViewById(R.id.tvCodeEtq);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            iv_iconBarcode = itemView.findViewById(R.id.iv_iconBarcode);
            cardViewItem = itemView.findViewById(R.id.cardViewItem);

            /*tamanho = itemView.findViewById(R.id.tvNumTamanho);
            ceco = itemView.findViewById(R.id.tvNumCeco);
            nomCentro = itemView.findViewById(R.id.tvNomCentro);
            tipoEtq = itemView.findViewById(R.id.tvCodigoTipo);
            btn_option = itemView.findViewById(R.id.btn_option);*/
        }
    }
}
