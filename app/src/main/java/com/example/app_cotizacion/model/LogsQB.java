package com.example.app_cotizacion.model;

import java.util.List;
import java.util.Map;

public class LogsQB {
    private List<Map<String, Object>> logs;
    private double amount, price, totalXmaterial;
    private String name, total;

    public LogsQB(List<Map<String, Object>> logs, double amount, double price, double totalXmaterial, String name, String total) {
        this.logs = logs;
        this.amount = amount;
        this.price = price;
        this.totalXmaterial = totalXmaterial;
        this.name = name;
        this.total = total;
    }

    public List<Map<String, Object>> getLogs() {
        return logs;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalXmaterial() {
        return totalXmaterial;
    }

    public String getName() {
        return name;
    }

    public String getTotal() {
        return total;
    }
}
