package com.example.geslapp.core.clases;

public class Caja {
    private String tracking;
    private String nombre;

    public Caja(String tracking) {
        this.tracking = tracking;
    }

    public Caja(String tracking, String nombre) {
        this.tracking = tracking;
        this.nombre = nombre;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
