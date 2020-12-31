package com.boc_dev.asteroids.models.sprites;

import com.boc_dev.asteroids.models.PickupType;
import com.boc_dev.asteroids.utils.DynamicState;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class PickupSprite extends GameObject {

    private final StackPane sprite = new StackPane();
    private final Circle firstCircle = new Circle(5);
    private final Circle secondCircle = new Circle(5);
    private final int radius = 10;
    private final PickupType pickupType;
    private double rotationSpeed = 10.0;


    public PickupSprite(DynamicState dynamicState, PickupType pickupType) {
        super(dynamicState);
        this.pickupType = pickupType;

        firstCircle.setFill(pickupType.getColor());
        firstCircle.setScaleX(2);

        secondCircle.setFill(pickupType.getColor());
        secondCircle.setScaleX(2);

        sprite.getChildren().addAll(firstCircle, secondCircle);


    }

    public PickupType getPickupType() {
        return pickupType;
    }

    @Override
    public void update() {
        rotationSpeed += 10;
        if (rotationSpeed > 360) {
            rotationSpeed = 0;
        }
        firstCircle.rotateProperty().set(rotationSpeed);
        secondCircle.rotateProperty().set(-rotationSpeed);
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public Node getSprite() {
        return sprite;
    }
}
