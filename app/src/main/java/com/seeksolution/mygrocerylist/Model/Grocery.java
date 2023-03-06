package com.seeksolution.mygrocerylist.Model;

public class Grocery {
    private String name;
    private String quantity;
    private String dateItemAdded;
    private int id;

    public Grocery() {
    }

    public Grocery(String name, String quantity, String dateItemAdded, int id) {
        this.name = name;
        this.quantity = quantity;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public void setId(int id) {
        this.id = id;
    }
}
