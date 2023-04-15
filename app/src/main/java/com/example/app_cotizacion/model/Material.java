package com.example.app_cotizacion.model;


public class Material {
    String materialId,  materialImg, materialName, materialPrice, materialStatus;
    boolean isSelected;
    long materialAmount;
    public Material() {}

    public Material(boolean isSelected, String materialId, long materialAmount, String materialImg, String materialName, String materialPrice, String materialStatus) {
        this.isSelected = isSelected;
        this.materialId = materialId;
        this.materialAmount = materialAmount;
        this.materialImg = materialImg;
        this.materialName = materialName;
        this.materialPrice = materialPrice;
        this.materialStatus = materialStatus;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public long getMaterialAmount() {
        return materialAmount;
    }

    public void setMaterialAmount(long materialAmount) {
        this.materialAmount = materialAmount;
    }
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
