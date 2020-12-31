package com.boc_dev.asteroids.models.sprites;

import com.boc_dev.asteroids.utils.Vector2D;
import com.boc_dev.asteroids.utils.DynamicState;
import com.boc_dev.asteroids.utils.Utilities;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Random;

public class AsteroidSprite extends GameObject {

    private final Polygon asteroidSprite;

    private final int meanAsteroidSize;

    private SimpleDoubleProperty rotationAngle = new SimpleDoubleProperty(0);
    private final double rotationSpeed;

    private final Random rand = new Random();

    private Vector2D motionToUpdate;

    private boolean invulnerable = false;
    private int invulnerableCounter = 0;

    public AsteroidSprite(DynamicState dynamicState, int meanAsteroidSize) {
        super(dynamicState);

        int arraySize = 20;

        double[] xCoord;
        double[] yCoord;

        xCoord = new double[arraySize];
        yCoord = new double[arraySize];

        this.meanAsteroidSize = meanAsteroidSize;

        motionToUpdate = new Vector2D();

        double rotationAngleSection = (Math.PI * 2) / (arraySize - 1);
        double localRotationAngle = 0;
        rotationSpeed = ((double)rand.nextInt(20))/10;

        for (int index = 0; index < arraySize - 1; index++) {
            double x = rand.nextInt(meanAsteroidSize / 4) + (3.0*(meanAsteroidSize / 4.0));
            localRotationAngle += rotationAngleSection;
            xCoord[index] = Utilities.rotateX(x, 0, localRotationAngle);
            yCoord[index] = Utilities.rotateY(x, 0, localRotationAngle);
        }
        xCoord[arraySize - 1] = xCoord[0];
        yCoord[arraySize - 1] = yCoord[0];

        int size = xCoord.length;

        double[] coords = new double[size * 2];
        for (int index = 0; index < size; index++) {
            coords[index * 2] = xCoord[index];
            coords[(index * 2) + 1] = yCoord[index];
        }

        asteroidSprite = new Polygon(coords);
        asteroidSprite.setFill(Color.BLACK);

        asteroidSprite.rotateProperty().bind(rotationAngle);
    }

    public void asteriodCollisionDetected(Vector2D motionToTwo) {
        motionToUpdate = motionToUpdate.add(motionToTwo);
    }

    private void updateMotion() {
        if (invulnerableCounter > 60) {
            invulnerable = false;
        }
        invulnerableCounter++;
        getDynamicState().setMotion(getDynamicState().getMotion().add(motionToUpdate));
        rotationAngle.set(rotationAngle.get() + rotationSpeed);
        motionToUpdate = new Vector2D();
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    @Override
    public void update() {
        updateMotion();
    }

    @Override
    public int getRadius() {
        return meanAsteroidSize;
    }

    @Override
    public Polygon getSprite() {
        return asteroidSprite;
    }
}
