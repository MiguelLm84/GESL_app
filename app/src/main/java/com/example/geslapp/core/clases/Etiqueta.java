package com.example.geslapp.core.clases;

public class Etiqueta {

    String media;
    String caja;
    String centro;
    String item;
    String id;

    public Etiqueta(String media) {
        this.media = media;
    }

    public Etiqueta(String media, String caja, String centro, String item, String id) {
        this.media = media;
        this.caja = caja;
        this.centro = centro;
        this.item = item;
        this.id = id;
    }
    public Etiqueta(){}

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
