package com.nick.wood.asteroids.utils;

public class DynamicState {
    private Vector2D position;
    private Vector2D motion;

    public DynamicState(Vector2D position, Vector2D motion) {
        this.position = position;
        this.motion = motion;
    }

    public DynamicState() {
        this.position = new Vector2D();
        this.motion = new Vector2D();
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public Vector2D getMotion() {
        return motion;
    }

    public void setMotion(Vector2D motion) {
        this.motion = motion;
    }
}
