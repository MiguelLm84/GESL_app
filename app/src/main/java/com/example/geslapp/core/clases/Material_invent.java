package com.example.geslapp.core.clases;

public class Material_invent {

    private String name;
    private String ubi;
    private String reti;
    private String tienda;
    private String dbName;
    private String enviado;

    public Material_invent(String name,String dbName, String ubi, String reti, String tienda, String enviado) {
        this.name = name;
        this.ubi = ubi;
        this.reti = reti;
        this.tienda = tienda;
        this.dbName = dbName;
        this.enviado = enviado;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUbi() {
        return ubi;
    }

    public void setUbi(String ubi) {
        this.ubi = ubi;
    }

    public String getReti() {
        return reti;
    }

    public void setReti(String reti) {
        this.reti = reti;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
