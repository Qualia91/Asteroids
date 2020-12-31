package com.boc_dev.asteroids.models.sprites;

import com.boc_dev.asteroids.utils.Utilities;
import com.boc_dev.asteroids.utils.Vector2D;
import com.boc_dev.asteroids.utils.DynamicState;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

public class ExplosionSprite extends GameObject {

    private final AnchorPane sprite;

    private final Random rand = new Random();

    private final double[] speedsArray;

    private final double[] directionsArray;

    private final Circle[] explosionParts;

    private int updateNumber = 0;

    public ExplosionSprite(DynamicState dynamicState) {
        super(dynamicState);

        sprite = new AnchorPane();

        // get a random number of parts the explosion should have (5 - 10)
        int numberOfParts = rand.nextInt(5) + 5;

        // Get an array or random speeds the parts should go at
        speedsArray = new double[numberOfParts];

        // Get a random number of initial rotations the explosive part should head out at
        directionsArray = new double[numberOfParts];

        explosionParts = new Circle[numberOfParts];

        for (int partIndex = 0; partIndex < numberOfParts; partIndex++) {
            // create explosive part
            explosionParts[partIndex] = new Circle(1);
            explosionParts[partIndex].setFill(Color.BLACK);

            // get this speed
            speedsArray[partIndex] = ((double)rand.nextInt(180) + 20) / 100000;

            // get this initial direction
            directionsArray[partIndex] = Utilities.toRadians(rand.nextInt(350));

            // add the explosion part to the sprite
            sprite.getChildren().add(explosionParts[partIndex]);
        }
    }

    private void updateMotion() {
        // loop through explosion parts
        for (int partIndex = 0; partIndex < explosionParts.length; partIndex++) {

            // for each part, translate the part by the speed vector times by a time, rotated by the rotation
            Vector2D nonRotated = new Vector2D(speedsArray[partIndex] * updateNumber, 0);
            Vector2D translation = new Vector2D(Utilities.rotateX(nonRotated.x,nonRotated.y, directionsArray[partIndex]), Utilities.rotateY(nonRotated.x,nonRotated.y, directionsArray[partIndex]));
            explosionParts[partIndex].setTranslateX(translation.x * 1000);
            explosionParts[partIndex].setTranslateY(translation.y * 1000);
        }
        updateNumber++;
        if (updateNumber > 25) {
            deadProperty().set(true);
        }
    }

    @Override
    public void update() {
        updateMotion();
    }

    @Override
    public int getRadius() {
        return 0;
    }

    @Override
    public AnchorPane getSprite() {
        return sprite;
    }
}
