package com.example.geslapp.core.clases;

public class General_invent {
    private int id_invent;
    private String SERVIDOR_UBI;
    private String SERVIDOR_FOTO1;
    private String SERVIDOR_FOTO2;
    private String POE_UBI;
    private String POE_FOTO;
    private String CARTEL_UBI;
    private String CARTEL_FOTO;
    private int ID_USER;

    public General_invent(int id_invent, String SERVIDOR_UBI, String SERVIDOR_FOTO1, String SERVIDOR_FOTO2, String POE_UBI, String POE_FOTO, String CARTEL_UBI, String CARTEL_FOTO, int ID_USER) {
        this.id_invent = id_invent;
        this.SERVIDOR_UBI = SERVIDOR_UBI;
        this.SERVIDOR_FOTO1 = SERVIDOR_FOTO1;
        this.SERVIDOR_FOTO2 = SERVIDOR_FOTO2;
        this.POE_UBI = POE_UBI;
        this.POE_FOTO = POE_FOTO;
        this.CARTEL_UBI = CARTEL_UBI;
        this.CARTEL_FOTO = CARTEL_FOTO;
        this.ID_USER = ID_USER;
    }


    public int getId_invent() {
        return id_invent;
    }

    public void setId_invent(int id_invent) {
        this.id_invent = id_invent;
    }

    public String getSERVIDOR_UBI() {
        return SERVIDOR_UBI;
    }

    public void setSERVIDOR_UBI(String SERVIDOR_UBI) {
        this.SERVIDOR_UBI = SERVIDOR_UBI;
    }

    public String getSERVIDOR_FOTO1() {
        return SERVIDOR_FOTO1;
    }

    public void setSERVIDOR_FOTO1(String SERVIDOR_FOTO1) {
        this.SERVIDOR_FOTO1 = SERVIDOR_FOTO1;
    }

    public String getSERVIDOR_FOTO2() {
        return SERVIDOR_FOTO2;
    }

    public void setSERVIDOR_FOTO2(String SERVIDOR_FOTO2) {
        this.SERVIDOR_FOTO2 = SERVIDOR_FOTO2;
    }

    public String getPOE_UBI() {
        return POE_UBI;
    }

    public void setPOE_UBI(String POE_UBI) {
        this.POE_UBI = POE_UBI;
    }

    public String getPOE_FOTO() {
        return POE_FOTO;
    }

    public void setPOE_FOTO(String POE_FOTO) {
        this.POE_FOTO = POE_FOTO;
    }

    public String getCARTEL_UBI() {
        return CARTEL_UBI;
    }

    public void setCARTEL_UBI(String CARTEL_UBI) {
        this.CARTEL_UBI = CARTEL_UBI;
    }

    public String getCARTEL_FOTO() {
        return CARTEL_FOTO;
    }

    public void setCARTEL_FOTO(String CARTEL_FOTO) {
        this.CARTEL_FOTO = CARTEL_FOTO;
    }

    public int getID_USER() {
        return ID_USER;
    }

    public void setID_USER(int ID_USER) {
        this.ID_USER = ID_USER;
    }
}
