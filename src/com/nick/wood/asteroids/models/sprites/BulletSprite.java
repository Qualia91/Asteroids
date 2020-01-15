package com.nick.wood.asteroids.models.sprites;

import com.nick.wood.asteroids.utils.DynamicState;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


public class BulletSprite extends GameObject {

    private final Polygon bulletSprite;

    private final int bulletRadius = 4;

    private double rotationAngle;

    public BulletSprite(double rotationAngle, DynamicState dynamicState) {
        super(dynamicState);

        double[] intialXCoord = new double[5];
        double[] intialYCoord = new double[5];

        intialXCoord[0] = -bulletRadius/2.0;
        intialXCoord[1] = bulletRadius/2.0;
        intialXCoord[2] = bulletRadius/2.0;
        intialXCoord[3] = -bulletRadius/2.0;
        intialXCoord[4] = -bulletRadius/2.0;

        intialYCoord[0] = bulletRadius/2.0;
        intialYCoord[1] = bulletRadius/2.0;
        intialYCoord[2] = -bulletRadius/2.0;
        intialYCoord[3] = -bulletRadius/2.0;
        intialYCoord[4] = bulletRadius/2.0;

        int size = intialYCoord.length;

        double[] coords = new double[size * 2];
        for (int index = 0; index < size; index++) {
            coords[index * 2] = intialXCoord[index];
            coords[(index * 2) + 1] = intialYCoord[index];
        }

        bulletSprite = new Polygon(coords);
        bulletSprite.setFill(Color.LIGHTGOLDENRODYELLOW);
        this.rotationAngle = rotationAngle;
    }

    public void rotate() {
        bulletSprite.setRotate(rotationAngle);
    }

    @Override
    public void update() {
        rotate();
    }

    @Override
    public Polygon getSprite() {
        return bulletSprite;
    }

    @Override
    public int getRadius() {
        return bulletRadius * 2;
    }
}
