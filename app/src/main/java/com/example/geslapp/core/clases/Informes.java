package com.example.geslapp.core.clases;

import java.util.Date;

public class Informes {

    private int id;
    private String fecha_apertura;
    private String fecha_cierre;
    private String user;
    private String tipo_informe;
    private String estado_informe;

    public Informes(int id, String fecha_apertura, String fecha_cierre, String tipo_informe,String user,String estado_informe) {
        this.id = id;
        this.user = user;
        this.fecha_apertura = fecha_apertura;
        this.fecha_cierre = fecha_cierre;
        this.estado_informe = estado_informe;
        this.tipo_informe = tipo_informe;
    }

    public Informes(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFecha_apertura() {
        return fecha_apertura;
    }

    public void setFecha_apertura(String fecha_apertura) {
        this.fecha_apertura = fecha_apertura;
    }

    public String getFecha_cierre() {
        return fecha_cierre;
    }

    public void setFecha_cierre(String fecha_cierre) {
        this.fecha_cierre = fecha_cierre;
    }


    public String getTipo_informe() {
        return tipo_informe;
    }

    public void setTipo_informe(String tipo_informe) {
        this.tipo_informe = tipo_informe;
    }

    public String getEstado_informe() {
        return estado_informe;
    }

    public void setEstado_informe(String estado_informe) {
        this.estado_informe = estado_informe;
    }
}
