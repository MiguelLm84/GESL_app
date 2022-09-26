package com.example.geslapp.core.clases;

import java.util.Date;

public class Inventario {

    private int id;
    private int idInvent;
    private String estado;
    private Date fechaApertura;
    private Date fechaCierre;
    private Date fechaMade;
    private int idCentro;
    private int idUserGestor;
    private String tipoInforme;
    private int idUser;
    private String MadeBy;
    private int position;

    public Inventario(int id, int idInvent, String estado, Date fechaApertura, Date fechaCierre,
                      Date fechaMade, int idCentro, int idUserGestor, String tipoInforme,
                      int idUser, String madeBy, int position) {
        this.id = id;
        this.idInvent = idInvent;
        this.estado = estado;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.fechaMade = fechaMade;
        this.idCentro = idCentro;
        this.idUserGestor = idUserGestor;
        this.tipoInforme = tipoInforme;
        this.idUser = idUser;
        MadeBy = madeBy;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdInvent() {
        return idInvent;
    }

    public void setIdInvent(int idInvent) {
        this.idInvent = idInvent;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Date getFechaMade() {
        return fechaMade;
    }

    public void setFechaMade(Date fechaMade) {
        this.fechaMade = fechaMade;
    }

    public int getIdCentro() {
        return idCentro;
    }

    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public int getIdUserGestor() {
        return idUserGestor;
    }

    public void setIdUserGestor(int idUserGestor) {
        this.idUserGestor = idUserGestor;
    }

    public String getTipoInforme() {
        return tipoInforme;
    }

    public void setTipoInforme(String tipoInforme) {
        this.tipoInforme = tipoInforme;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getMadeBy() {
        return MadeBy;
    }

    public void setMadeBy(String madeBy) {
        MadeBy = madeBy;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
