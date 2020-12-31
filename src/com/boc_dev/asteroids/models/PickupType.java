package com.boc_dev.asteroids.models;

import javafx.scene.paint.Color;

public enum PickupType {
    HEALTH(Color.ORANGERED),
    FUEL(Color.LIGHTBLUE);

    private final Color color;

    PickupType(Color color) {
        this.color =  color;
    }

    public Color getColor() {
        return color;
    }
}
