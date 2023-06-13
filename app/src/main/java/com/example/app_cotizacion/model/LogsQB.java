package com.example.app_cotizacion.model;

import java.util.List;
import java.util.Map;

public class LogsQB {
    private double amount;
    private String img, nameM;
    private double price, totalXmaterial;

    public LogsQB(double amount, String img, String nameM, double price, double totalXmaterial) {
        this.amount = amount;
        this.img = img;
        this.nameM = nameM;
        this.price = price;
        this.totalXmaterial = totalXmaterial;
    }
    public double getAmount() {
        return amount;
    }

    public String getImg() {
        return img;
    }

    public String getNameM() {
        return nameM;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalXmaterial() {
        return totalXmaterial;
    }
}
