package com.example.app_cotizacion.model;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;

public class Material {
    String materialId, materialImg, materialName, materialPrice, materialStatus;
    boolean isSelected;
    private DocumentSnapshot documentSnapshot;
    MaterialCardView materialCard;
    public Material() {}

    public Material(boolean isSelected, String materialId, String materialImg, String materialName, String materialPrice, String materialStatus, MaterialCardView materialCard, DocumentSnapshot documentSnapshot) {
        this.isSelected = isSelected;
        this.materialId = materialId;
        this.materialImg = materialImg;
        this.materialName = materialName;
        this.materialPrice = materialPrice;
        this.materialStatus = materialStatus;
        this.materialCard = materialCard;
        this.documentSnapshot = documentSnapshot;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public MaterialCardView getMaterialCard() {
        return materialCard;
    }

    public void setMaterialCard(MaterialCardView materialCard) {
        this.materialCard = materialCard;
    }

    public DocumentSnapshot getDocumentSnapshot() {
        return documentSnapshot;
    }

    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
    }
}
