package com.example.geslapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.geslapp.R;
import com.example.geslapp.core.requests.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistroActivity extends AppCompatActivity {
    EditText txtUser, txtPassword;
    Button btnRegistrar;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtUser = findViewById(R.id.txtUser);
        txtPassword = findViewById(R.id.txtPassword);

        btnRegistrar = findViewById(R.id.btnregistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (txtUser.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Campo Nombre vacío", Toast.LENGTH_LONG).show();
                } else {
                    if (txtPassword.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Campo password vacío", Toast.LENGTH_LONG).show();
                    }
                    else{


                    }
                }

                final String username = txtUser.getText().toString().trim();
                final String password = txtPassword.getText().toString().trim();

                if (username.length() == 0 || password.length() == 0) {

                }else{
                    Response.Listener<String> respoListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                              int success = jsonResponse.getInt("success");

                                if (success == 1) {
                                    Intent intent5 = new Intent(RegistroActivity.this, LoginActivity.class);
                                    startActivity(intent5);
                                    Toast.makeText(getApplicationContext(),
                                            "Registro completo...", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "El usuario ya existe...", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                                    builder.setMessage("error en registro")
                                            .setNegativeButton("Reintentar", null)
                                            .create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(username, password, respoListener);
                    RequestQueue queue = Volley.newRequestQueue(RegistroActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }

}


