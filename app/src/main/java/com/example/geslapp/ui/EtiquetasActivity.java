package com.example.geslapp.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.camara.CaptureAct;
import com.example.geslapp.core.clases.ConfigPreferences;
import com.example.geslapp.core.clases.Etiqueta;
import com.example.geslapp.core.requests.EtqRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

public class EtiquetasActivity extends AppCompatActivity {
    public static final String MIME_TEXT_PLAIN = "text/plain";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    final static String TAG = "nfc_test";
    ImageView butenter;
    ImageButton butscan;
    Parcelable[] etiqueta;
    EditText edtcode;
    IntentFilter writeTagFilters[];

    private static boolean switch0=false;
    TextView NFCtv, txttitulo;
    TextView TextView2;
    Switch switchNFC;
    private static String IP,REC,PROJECT;
    ConfigPreferences config = new ConfigPreferences();
    Button butbarras;
    private boolean escritura = false;


    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint({"UnspecifiedImmutableFlag", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescanner);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        inits();
        butscan = findViewById(R.id.btnEscanear);
        butenter = findViewById(R.id.btnEnter);
        txttitulo = findViewById(R.id.tvTitulo);
        TextView Titulo = findViewById(R.id.tvTitulo);
        TextView opcionTv = findViewById(R.id.txtversion);
        NFCtv = findViewById(R.id.tvNFC);
        TextView2 = findViewById(R.id.txtinfo);
        switchNFC = findViewById(R.id.switchNFC);
        edtcode = findViewById(R.id.etCode);
        edtcode.requestFocus();
        IP = config.getIP(getApplicationContext());
        REC = config.getRec(getApplicationContext());
        PROJECT = config.getProject(getApplicationContext());
        butbarras = findViewById(R.id.butbarras);

        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities", Toast.LENGTH_SHORT).show();
            switchNFC.setVisibility(View.INVISIBLE);

        } else {
            NFCtv.setVisibility(View.VISIBLE);
            switchNFC.setVisibility(View.VISIBLE);
            NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
            NfcAdapter adapter = manager.getDefaultAdapter();

            if (adapter != null && adapter.isEnabled()) {
                switch0 = true;
                switchNFC.setChecked(switch0);

            } else {

                switchNFC.setChecked(false);
            }
        }

        edtcode.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        enterscan(edtcode.getText().toString().toUpperCase(Locale.ROOT));
                        return true;
                    default:
                        break;
                }
            }
            return false;
        });

        String etiqueta = getIntent().getStringExtra("Nombre");
        String opcion = getIntent().getStringExtra("Opcion");

        ImageButton btnScanner = findViewById(R.id.btnEscanear);

        Titulo.setText("ETIQUETAS");
        opcionTv.setText("CENTRAL");
        //pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this,0,new Intent(
                            this,
                            this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this,0,new Intent(
                            this,
                            this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        btnScanner.setOnClickListener(view -> scanCode());

        switchNFC.setOnClickListener(v -> {

            switch0=(!switch0);
            if(switch0) {
                NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
                NfcAdapter adapter = manager.getDefaultAdapter();
                if (adapter != null && adapter.isEnabled()) {

                } else {
                    Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                    startActivity(intent);
                }

            } else{
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
            }
        });

        butenter.setOnClickListener(v -> enterscan(edtcode.getText().toString().toUpperCase(Locale.ROOT)));

        butbarras.setOnClickListener(v -> {
            escritura = !escritura;
            int color;
            if(escritura) {
                color = Color.parseColor("#04A800");

            } else {
                color = Color.parseColor("#535353");
            }
            Drawable butdraw = butbarras.getBackground();
            butdraw = DrawableCompat.wrap(butdraw);
            DrawableCompat.setTint(butdraw,color);
        });
    }

    private void inits() {
    }

    private void enterscan(String etcode) {

        Response.Listener<String> respoListener2 = response2 -> {
            System.out.println(response2);
            String error = "ETIQUETA VACÍA";
            Dialog dialog = new Dialog(EtiquetasActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.etq_dialog);

            LinearLayout layoutMsg = dialog.findViewById(R.id.layoutMsg);

            TextView mediaTv = dialog.findViewById(R.id.mediatv);
            mediaTv.setText(etcode);
            TextView cajaTv = dialog.findViewById(R.id.tvPrecioProducto);
            TextView centroTv = dialog.findViewById(R.id.txtstatus);
            TextView itemTv = dialog.findViewById(R.id.tv_nomProducto);
            TextView idTv = dialog.findViewById(R.id.tvCodigoProducto);

            TextView errorMsg = dialog.findViewById(R.id.error);

            ImageView imgEtq = dialog.findViewById(R.id.imgEtq);
            ImageView tachar = dialog.findViewById(R.id.tachar);

            /*TextView btScan = dialog.findViewById(R.id.btScan);

            btScan.setOnClickListener(view -> {
                dialog.dismiss();
                scanCode();
            });*/

            ImageView close = dialog.findViewById(R.id.imgclose);
            close.setOnClickListener(view -> {
                if(!escritura) edtcode.setText("");
                dialog.dismiss();
            });

            try {
                JSONObject jsonResponse = new JSONObject(response2);
                int success = jsonResponse.getInt("success");
                System.out.println(success);
                System.out.println(response2);

                if (success == 1) {
                    System.out.println(success);
                    String caja = jsonResponse.getString("caja");
                    System.out.println(caja);
                    String centro = jsonResponse.getString("centro");
                    System.out.println(centro);
                    String item = jsonResponse.getString("item");
                    System.out.println(item);
                    String id = String.valueOf(jsonResponse.getInt("id"));
                    System.out.println(item);

                    //Dialog
                    tachar.setVisibility(View.INVISIBLE);
                    cajaTv.setText(caja);
                    centroTv.setText(centro);
                    itemTv.setText(item);
                    idTv.setText(id);
                    imgEtq.getLayoutParams().width = 800;
                    imgEtq.getLayoutParams().height = 400;
                    imgEtq.setAdjustViewBounds(true);
                        /*String mensaje=e.getCaja()+"\n"+e.getCentro()+"\n"+e.getItem()+"\n"+e.getId();
                        System.out.println(mensaje);*/
                } else {
                    layoutMsg.setVisibility(View.GONE);
                    errorMsg.setText(error);
                    Toast.makeText(getApplicationContext(), "No se ha encontrado la etiqueta", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                layoutMsg.setVisibility(View.GONE);
                errorMsg.setText(error);
            }
            dialog.show();
        };

        String username = config.getUsername(getApplicationContext());
        String ip = config.getUip(getApplicationContext());

        EtqRequest trackingRequest = new EtqRequest(etcode, respoListener2,IP,REC,PROJECT,username,ip);

        RequestQueue queue2 = Volley.newRequestQueue(EtiquetasActivity.this);
        queue2.add(trackingRequest);
    }

    public void resolveIntent(Intent intent){

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            byte[] payload = detectTagData(tag).getBytes();
            System.out.println("Payload ----->" + Arrays.toString(payload));
        }
    }

    private String detectTagData(Tag tag) {

        StringBuilder sb = new StringBuilder();
        sb.append("NFC ID (dec): ").append('\n');
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareUltralight.class.getName())) {
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                LecturaEtq(mifareUlTag);
            }
        }
        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }
        Log.v("test",sb.toString());
        return sb.toString();
    }

    public String LecturaEtq(MifareUltralight mifareUlTag) {

        try {
            mifareUlTag.connect();
            byte[] payload = mifareUlTag.readPages(4);
            return new String(payload, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            Log.e(TAG, "IOException while reading MifareUltralight message...", e);
        } finally {
            if (mifareUlTag != null) {
                try {
                    mifareUlTag.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Error closing tag...", e);
                }
            }
        }
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
        setIntent(intent);

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            etiqueta = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        assert nfcAdapter != null;
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();

        if (adapter != null && adapter.isEnabled()) {
            switch0 = true;
            switchNFC.setChecked(switch0);

        } else {
            switchNFC.setChecked(false);
        }

        nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
        Log.i(TAG, "onResume");
        setDisplayText ( "onResume " + getIntent().getAction());
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processNFCData(getIntent());
        }
    }

    private void processNFCData( Intent inputIntent ) {

        Log.i(TAG, "processNFCData");
        Parcelable[] rawMessages = inputIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (rawMessages != null && rawMessages.length > 0) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];

            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }

            Log.i(TAG, "message size = " + messages.length);
            TextView veiw = findViewById(R.id.viewdata);

            if(veiw != null) {
                // only one message sent during the beam
                NdefMessage msg = (NdefMessage) rawMessages[0];
                // record 0 contains the MIME type, record 1 is the AAR, if present
                String base = new String(msg.getRecords()[0].getPayload());
                String str = String.format(Locale.getDefault(), "Message entries=%d. Base message is %s", rawMessages.length, base);
                //veiw.setText("Content: "+base);
                //NFCtv.setText(str);

                String[] churro = base.split("/");
                String codigoetq = churro[1];
                Toast.makeText(this, codigoetq+"", Toast.LENGTH_LONG).show();
                edtcode.setText(codigoetq);
                enterscan(codigoetq);
            }
        }
    }

    private void setDisplayText ( String text ) {

        TextView veiw = findViewById(R.id.viewdata);
        veiw.setText(text);
    }

    private void obtenerEtiqueta(IntentResult result) {

        Etiqueta e = new Etiqueta();
        e.setMedia(result.getContents());

        edtcode.setText(e.getMedia());
        final String tracking = edtcode.getText().toString().trim().toUpperCase(Locale.ROOT);
        System.out.println(tracking);
        if (result.getContents() != null) {
            Response.Listener<String> respoListener2 = response2 -> {
                System.out.println(response2);
                String error = "ETIQUETA VACÍA";
                Dialog dialog = new Dialog(EtiquetasActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.etq_dialog);

                LinearLayout layoutMsg = dialog.findViewById(R.id.layoutMsg);

                TextView mediaTv = dialog.findViewById(R.id.mediatv);
                mediaTv.setText(e.getMedia());
                TextView cajaTv = dialog.findViewById(R.id.tvPrecioProducto);
                TextView centroTv = dialog.findViewById(R.id.txtstatus);
                TextView itemTv = dialog.findViewById(R.id.tv_nomProducto);
                TextView idTv = dialog.findViewById(R.id.tvCodigoProducto);
                TextView errorMsg = dialog.findViewById(R.id.error);
                ImageView imgEtq = dialog.findViewById(R.id.imgEtq);
                ImageView tachar = dialog.findViewById(R.id.tachar);
                TextView btScan = dialog.findViewById(R.id.btScan);

                /*if(){
                    btScan.setOnClickListener(view -> {
                        dialog.dismiss();
                        scanCode();
                    });
                }*/

                ImageView close = dialog.findViewById(R.id.imgclose);
                close.setOnClickListener(view -> dialog.dismiss());

                try {
                    JSONObject jsonResponse = new JSONObject(response2);
                    int success = jsonResponse.getInt("success");
                    System.out.println(success);
                    System.out.println(response2);

                    if (success == 1) {
                        System.out.println(success);
                        String caja = jsonResponse.getString("caja");
                        System.out.println(caja);
                        String centro = jsonResponse.getString("centro");
                        System.out.println(centro);
                        String item = jsonResponse.getString("item");
                        System.out.println(item);
                        String id = String.valueOf(jsonResponse.getInt("id"));
                        System.out.println(item);
                        e.setCaja(caja);
                        e.setCentro(centro);
                        e.setItem(item);
                        e.setId(id);

                        //Dialog
                        tachar.setVisibility(View.INVISIBLE);
                        cajaTv.setText(e.getCaja());
                        centroTv.setText(e.getCentro());
                        itemTv.setText(e.getItem());
                        idTv.setText(e.getId());
                        imgEtq.getLayoutParams().width = 800;
                        imgEtq.getLayoutParams().height = 400;
                        imgEtq.setAdjustViewBounds(true);
                        /*String mensaje=e.getCaja()+"\n"+e.getCentro()+"\n"+e.getItem()+"\n"+e.getId();
                        System.out.println(mensaje);*/
                    } else {
                        layoutMsg.setVisibility(View.GONE);
                        errorMsg.setText(error);
                        Toast.makeText(getApplicationContext(), "No se ha encontrado la etiqueta", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e1) {
                    layoutMsg.setVisibility(View.GONE);
                    errorMsg.setText(error);
                }
                dialog.show();
            };
            String username = config.getUsername(getApplicationContext());
            String ip = config.getUip(getApplicationContext());
            EtqRequest trackingRequest = new EtqRequest(e.getMedia(), respoListener2,IP,REC,PROJECT,username,ip);

            RequestQueue queue2 = Volley.newRequestQueue(EtiquetasActivity.this);
            queue2.add(trackingRequest);
        }
    }

    private void scanCode() {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
                obtenerEtiqueta(result);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}


