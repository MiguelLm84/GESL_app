package com.example.geslapp.core.clases;

import android.widget.EditText;

public class Etqs_invent {
    private int ID;
    private int POS;
    private int ID_INVENT;
    private String MODELO_ETQ;
    private String RETIRADAS_CAJAS;
    private String RETIRADAS_ETQ;
    private String TIENDA_CAJAS;
    private String TIENDA_ETQ;
    private String FOTO_ETQ;
    private String ASIG_CAJAS_GESL;
    private String ASIG_CAJAS;
    private String ASIG_ETQ;
    private String DESASIG_CAJAS_GESL;
    private String DESASIG_CAJAS;
    private String DESASIG_ETQ;

    public Etqs_invent(int ID, int ID_INVENT, String MODELO_ETQ, int POS, String RETIRADAS_CAJAS, String RETIRADAS_ETQ, String TIENDA_CAJAS, String TIENDA_ETQ, String FOTO_ETQ, String ASIG_CAJAS_GESL, String ASIG_CAJAS, String ASIG_ETQ, String DESASIG_CAJAS_GESL, String DESASIG_CAJAS, String DESASIG_ETQ) {
     this.ID = ID;
     this.POS = POS;
     this.ID_INVENT = ID_INVENT;
     this.MODELO_ETQ = MODELO_ETQ;
     this.RETIRADAS_CAJAS = RETIRADAS_CAJAS;
     this.RETIRADAS_ETQ = RETIRADAS_ETQ;
     this.TIENDA_CAJAS = TIENDA_CAJAS;
     this.TIENDA_ETQ = TIENDA_ETQ;
     this.FOTO_ETQ = FOTO_ETQ;
     this.ASIG_CAJAS_GESL = ASIG_CAJAS_GESL;
     this.ASIG_CAJAS = ASIG_CAJAS;
     this.ASIG_ETQ = ASIG_ETQ;
     this.DESASIG_CAJAS_GESL = DESASIG_CAJAS_GESL;
     this.DESASIG_CAJAS = DESASIG_CAJAS;
     this.DESASIG_ETQ = DESASIG_ETQ;
    }
    public Etqs_invent(){}

    public int getID() {
        return ID;
    }

    public  void setID(int ID) {
        this.ID = ID;
    }

    public  int getPOS() {
        return POS;
    }

    public  void setPOS(int POS) {
        this.POS = POS;
    }

    public  int getIdInvent() {
        return ID_INVENT;
    }

    public  void setIdInvent(int idInvent) {
        ID_INVENT = idInvent;
    }

    public  String getModeloEtq() {
        return MODELO_ETQ;
    }

    public  void setModeloEtq(String modeloEtq) {
        MODELO_ETQ = modeloEtq;
    }

    public  String getRetiradasCajas() {
        if (RETIRADAS_CAJAS == null) RETIRADAS_CAJAS = "";
        return RETIRADAS_CAJAS;
    }

    public  void setRetiradasCajas(String retiradasCajas) {
        RETIRADAS_CAJAS = retiradasCajas;
    }

    public  String getRetiradasEtq() {
        if(RETIRADAS_ETQ == null) RETIRADAS_ETQ = "";
        return RETIRADAS_ETQ;
    }

    public  void setRetiradasEtq(String retiradasEtq) {
        RETIRADAS_ETQ = retiradasEtq;
    }

    public  String getTiendaCajas() {
        if(TIENDA_CAJAS == null) TIENDA_CAJAS = "";
        return TIENDA_CAJAS;
    }

    public  void setTiendaCajas(String tiendaCajas) {
        TIENDA_CAJAS = tiendaCajas;
    }

    public  String getTiendaEtq() {
        if(TIENDA_ETQ == null) TIENDA_ETQ = "";
        return TIENDA_ETQ;
    }

    public  void setTiendaEtq(String tiendaEtq) {
        TIENDA_ETQ = tiendaEtq;
    }

    public  String getFotoEtq() {
        if(FOTO_ETQ == null) FOTO_ETQ = "";
        return FOTO_ETQ;
    }

    public  void setFotoEtq(String fotoEtq) {
        FOTO_ETQ = fotoEtq;
    }

    public  String getAsigCajasGesl() {
        if(ASIG_CAJAS_GESL == null) ASIG_CAJAS_GESL = "";
        return ASIG_CAJAS_GESL;
    }

    public  void setAsigCajasGesl(String asigCajasGesl) {
        ASIG_CAJAS_GESL = asigCajasGesl;
    }

    public  String getAsigCajas() {
        if(ASIG_CAJAS == null) ASIG_CAJAS = "";
        return ASIG_CAJAS;
    }

    public  void setAsigCajas(String asigCajas) {
        ASIG_CAJAS = asigCajas;
    }

    public  String getAsigEtq() {
        if(ASIG_ETQ == null) ASIG_ETQ = "";
        return ASIG_ETQ;
    }

    public  void setAsigEtq(String asigEtq) {
        ASIG_ETQ = asigEtq;
    }

    public  String getDesasigCajasGesl() {
        if(DESASIG_CAJAS_GESL ==null) DESASIG_CAJAS_GESL = "";
        return DESASIG_CAJAS_GESL;
    }

    public  void setDesasigCajasGesl(String desasigCajasGesl) {
        DESASIG_CAJAS_GESL = desasigCajasGesl;
    }

    public  String getDesasigCajas() {
        if(DESASIG_CAJAS == null) DESASIG_CAJAS = "";
        return DESASIG_CAJAS;
    }

    public  void setDesasigCajas(String desasigCajas) {
        DESASIG_CAJAS = desasigCajas;
    }

    public  String getDesasigEtq() {
        if(DESASIG_ETQ == null) DESASIG_ETQ = "";
        return DESASIG_ETQ;
    }

    public  void setDesasigEtq(String desasigEtq) {
        DESASIG_ETQ = desasigEtq;
    }
}
