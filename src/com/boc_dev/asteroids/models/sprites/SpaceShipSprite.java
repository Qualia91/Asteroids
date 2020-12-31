package com.boc_dev.asteroids.models.sprites;

import com.boc_dev.asteroids.utils.DynamicState;
import com.boc_dev.asteroids.utils.Utilities;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;

public class SpaceShipSprite extends GameObject {

    private final Pane sprite = new Pane();
    private final int size = 300;
    private final Circle mainHull = new Circle(size - 100);
    private final Polyline shield;
    private double rotationSpeed = 0;

    public SpaceShipSprite(DynamicState dynamicState) {
        super(dynamicState);

        mainHull.setFill(Color.WHITESMOKE);
        double[] shieldPoints = new double[36];
        double angleDiff = Math.PI / 8;
        for (int index = 0; index < 18; index++) {
            shieldPoints[index * 2] = Utilities.rotateX(size, 0, angleDiff*index);
            shieldPoints[(index * 2) + 1] = Utilities.rotateY(size, 0, angleDiff*index);
        }
        shield = new Polyline(shieldPoints);
        shield.setStroke(Color.LIGHTCORAL);
        sprite.getChildren().add(mainHull);
        sprite.getChildren().add(shield);
    }

    @Override
    public void update() {
        mainHull.setRotate(rotationSpeed);
        shield.setRotate(-rotationSpeed);
        rotationSpeed += 0.1;
        if (rotationSpeed > 360) {
            rotationSpeed = 0.1;
        }
    }

    @Override
    public int getRadius() {
        return size * 5;
    }

    @Override
    public Node getSprite() {
        return sprite;
    }

    @Override
    public void collisionDetected() {

    }
}
