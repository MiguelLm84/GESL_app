package com.example.geslapp.core.clases;
//inservible
public class Modelo {
    String nombre;
    String id;
    String tamano;


    public Modelo() {
    }

    public Modelo(String nombre) {
        this.nombre = nombre;
    }

    public Modelo(String nombre, String id, String tamano) {
        this.nombre = nombre;
        this.id = id;
        this.tamano = tamano;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

}
