package com.example.app_cotizacion.model;

import com.google.android.material.card.MaterialCardView;

public class Material {
    String materialImg, materialName, materialPrice, materialStatus;
    MaterialCardView materialCard;
    public Material() {}

    public Material(String materialImg, String materialName, String materialPrice, String materialStatus, MaterialCardView materialCard) {
        this.materialImg = materialImg;
        this.materialName = materialName;
        this.materialPrice = materialPrice;
        this.materialStatus = materialStatus;
        this.materialCard = materialCard;
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

    public MaterialCardView getMaterialCard() {
        return materialCard;
    }

    public void setMaterialCard(MaterialCardView materialCard) {
        this.materialCard = materialCard;
    }
}
