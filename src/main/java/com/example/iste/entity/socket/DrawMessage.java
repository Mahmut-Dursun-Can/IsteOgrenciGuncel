package com.example.iste.entity.socket;


public class DrawMessage {
    private double x;
    private double y;
    private String type; // "begin", "draw", "end"

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
