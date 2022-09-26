package com.example.geslapp.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.geslapp.R;

public class RutaFragment extends Fragment {

    public static RutaFragment newInstance() {
        return new RutaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ruta, container, false);

        return root;
    }
}