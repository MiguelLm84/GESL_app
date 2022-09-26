package com.example.geslapp.core.clases;

public class Etiquetas {

    private int tamanho;
    private int ceco;
    private String nomCentro;
    private String tipoEtq;

    public Etiquetas(int tamanho, int ceco, String nomCentro, String tipoEtq) {
        this.tamanho = tamanho;
        this.ceco = ceco;
        this.nomCentro = nomCentro;
        this.tipoEtq = tipoEtq;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public int getCeco() {
        return ceco;
    }

    public void setCeco(int ceco) {
        this.ceco = ceco;
    }

    public String getNomCentro() {
        return nomCentro;
    }

    public void setNomCentro(String nomCentro) {
        this.nomCentro = nomCentro;
    }

    public String getTipoEtq() {
        return tipoEtq;
    }

    public void setTipoEtq(String tipoEtq) {
        this.tipoEtq = tipoEtq;
    }
}
