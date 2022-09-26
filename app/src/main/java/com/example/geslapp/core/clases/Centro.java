package com.example.geslapp.core.clases;
//inservible
public class Centro {
    String nombre;
    String id;
    String ceco;

    public Centro() {

    }

    public Centro(String nombre) {
        this.nombre = nombre;
    }

    public Centro(String nombre, String id, String ceco, int position) {
        this.nombre = nombre;
        this.id = id;
        this.ceco = ceco;
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

    public String getCeco() {
        return ceco;
    }

    public void setCeco(String ceco) {
        this.ceco = ceco;
    }

}
