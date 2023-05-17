package com.example.app_cotizacion.model;


public class Material {
    boolean isSelected;
    String materialImg, materialName, materialStatus;
    double materialPrice;

    public Material(String materialImg, String materialName, double materialPrice, String materialStatus) {
        this.isSelected = false;
        this.materialImg = materialImg;
        this.materialName = materialName;
        this.materialPrice = materialPrice;
        this.materialStatus = materialStatus;
    }

    public String getMaterialImg() {
        return materialImg;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getMaterialStatus() {
        return materialStatus;
    }

    public double getMaterialPrice() {
        return materialPrice;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
