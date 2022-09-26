package com.example.geslapp.core.clases;

public class CajaLocal {
    private String nombre;
    private String tracking;
    private int id;

    public CajaLocal(){}
    public CajaLocal(String nombre, String tracking, int id)
    {
        this.tracking = tracking;
        this.nombre = nombre;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
