package com.example.geslapp.core.clases;

public class AntenasInvent {
  private int ID;
  private int ID_INVENTARIO;
  private int ID_ANTENA;
  private String FOTO;
  private String UBICACION;

    public AntenasInvent(int ID_INVENTARIO, int ID_ANTENA, String FOTO, String UBICACION) {
        this.ID_INVENTARIO = ID_INVENTARIO;
        this.ID_ANTENA = ID_ANTENA;
        this.FOTO = FOTO;
        this.UBICACION = UBICACION;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID_INVENTARIO() {
        return ID_INVENTARIO;
    }

    public void setID_INVENTARIO(int ID_INVENTARIO) {
        this.ID_INVENTARIO = ID_INVENTARIO;
    }

    public int getID_ANTENA() {
        return ID_ANTENA;
    }

    public void setID_ANTENA(int ID_ANTENA) {
        this.ID_ANTENA = ID_ANTENA;
    }

    public String getFOTO() {
        return FOTO;
    }

    public void setFOTO(String FOTO) {
        this.FOTO = FOTO;
    }

    public String getUBICACION() {
        return UBICACION;
    }

    public void setUBICACION(String UBICACION) {
        this.UBICACION = UBICACION;
    }
}
