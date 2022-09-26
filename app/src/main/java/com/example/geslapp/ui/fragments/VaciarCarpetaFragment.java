package com.example.geslapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geslapp.R;

public class VaciarCarpetaFragment extends Fragment {

    public static VaciarCarpetaFragment newInstance() {
        return new VaciarCarpetaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vaciar_carpeta, container, false);
        return root;
    }
}