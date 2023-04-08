package com.example.app_cotizacion.model;

public class Material {
    String materialImg, materialName, materialPrice, materialStatus;
    public Material() {}

    public Material(String materialImg, String materialName, String materialPrice, String materialStatus) {
        this.materialImg = materialImg;
        this.materialName = materialName;
        this.materialPrice = materialPrice;
        this.materialStatus = materialStatus;
    }

    public String getMaterialImg() {
        return materialImg;
    }

    public void setMaterialImg(String materialImg) {
        this.materialImg = materialImg;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(String materialPrice) {
        this.materialPrice = materialPrice;
    }

    public String getMaterialStatus() {
        return materialStatus;
    }

    public void setMaterialStatus(String materialStatus) {
        this.materialStatus = materialStatus;
    }
}
