package com.example.app_cotizacion.model;

public class LogContainer {
    private String nameC;
    private String totalC;

    public LogContainer(String nameC, String totalC) {
        this.nameC = nameC;
        this.totalC = totalC;
    }

    public String getNameC() {
        return nameC;
    }

    public String getTotalC() {
        return totalC;
    }
}
