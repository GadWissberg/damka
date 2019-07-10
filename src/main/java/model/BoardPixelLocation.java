package model;

public class BoardPixelLocation {
    private double x;
    private double y;

    public BoardPixelLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
