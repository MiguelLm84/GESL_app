package com.example.geslapp.core.clases;

public class Antenas {
    private int id;
    private String nombre;
    private int id_centro;
    private int id_antenas;


    public Antenas(int id, int id_antenas){this.id = id; this.id_antenas = id_antenas;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId_centro() {
        return id_centro;
    }

    public void setId_centro(int id_centro) {
        this.id_centro = id_centro;
    }

    public int getId_antenas() {
        return id_antenas;
    }

    public void setId_antenas(int id_antenas) {
        this.id_antenas = id_antenas;
    }


}
