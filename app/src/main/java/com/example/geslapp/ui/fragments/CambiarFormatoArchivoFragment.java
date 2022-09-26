package com.example.geslapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geslapp.R;

public class CambiarFormatoArchivoFragment extends Fragment {

    public static CambiarFormatoArchivoFragment newInstance() {
        return new CambiarFormatoArchivoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cambiar_formato_archivo, container, false);

        return root;
    }
}