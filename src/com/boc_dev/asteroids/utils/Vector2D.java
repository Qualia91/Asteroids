package com.boc_dev.asteroids.utils;

public class Vector2D {

    public double x;
    public double y;

    public Vector2D() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D vec2d) {
        double a = this.x + vec2d.x;
        double b = this.y + vec2d.y;
        return new Vector2D(a, b);
    }
    public Vector2D add(double number) {
        double a = this.x + number;
        double b = this.y + number;
        return new Vector2D(a, b);
    }
    public Vector2D subtract(Vector2D vec2d) {
        double a = this.x - vec2d.x;
        double b = this.y - vec2d.y;
        return new Vector2D(a, b);
    }
    public Vector2D subtract(double number) {
        double a = this.x + number;
        double b = this.y + number;
        return new Vector2D(a, b);
    }
    public Vector2D multiply(double number) {
        double a = this.x * number;
        double b = this.y * number;
        return new Vector2D(a, b);
    }
    public Vector2D divide(double number) {
        if (number == 0) {
            return new Vector2D(0, 0);
        }

        double a = this.x / number;
        double b = this.y / number;
        return new Vector2D(a, b);
    }
    public double dot(Vector2D vec2d) {
        return (this.x * vec2d.x) + (this.y * vec2d.y);
    }
    public double cross(Vector2D vec2d) {
        return (this.x * vec2d.y) - (vec2d.x * this.y);
    }
    public Vector2D normalise() {
        return this.divide(this.length());
    }
    public double length() {
        return Math.sqrt((this.x * this.x) + (this.y * this.y));
    }
    public Vector2D midpoint(Vector2D vector2D) {
        double x = (vector2D.x + this.x)/2;
        double y = (vector2D.y + this.y)/2;
        return new Vector2D();
    }

    public void set(Vector2D vector2D) {
        this.x = vector2D.x;
        this.y = vector2D.y;
    }
}
