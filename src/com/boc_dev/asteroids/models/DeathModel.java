package com.boc_dev.asteroids.models;

import com.boc_dev.asteroids.models.sprites.*;
import com.boc_dev.asteroids.utils.DynamicState;
import com.boc_dev.asteroids.utils.Utilities;
import com.boc_dev.asteroids.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeathModel {
    private final GameModel gameModel;
    private final Random random = new Random();

    public DeathModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void run() {
        for (GameObject object : GameObjectDatabase.getGameObjectList()) {
            boolean remove = false;

            // Check if it has dead set
            if (object.deadProperty().getValue()) {
                remove = true;
            }

            // Check if it is out of bounds
            if (isOutOfBounds(object.getDynamicState().getPosition())) {
                remove = true;
            }

            if (remove) {
                if (object instanceof AsteroidSprite) {
                    AsteroidSprite asteroidSprite = (AsteroidSprite) object;
                    int parentMeanSize = asteroidSprite.getRadius();
                    if (parentMeanSize > 25) {
                        int runningSize = 0;
                        List<Integer> pieceSizes = new ArrayList<>();
                        // calculate sizes
                        while (runningSize < parentMeanSize) {
                            int pieceSize = random.nextInt(parentMeanSize / 2) + parentMeanSize / 2;
                            pieceSizes.add(pieceSize);
                            runningSize += pieceSize;
                        }
                        // calculate directions
                        double directionRotationDifference = (2 * Math.PI) / pieceSizes.size();
                        // create asteroids
                        double velocity = random.nextInt(100) * 0.005;
                        for (int pieceIndex = 0; pieceIndex < pieceSizes.size(); pieceIndex++) {
                            double rotationAngle = (Math.PI / 2) + (directionRotationDifference * pieceIndex);
                            // add on motion of parent asteroid
                            Vector2D fullMotion = new Vector2D(Utilities.rotateX(velocity, 0, rotationAngle), Utilities.rotateY(velocity, 0, rotationAngle)).add(asteroidSprite.getDynamicState().getMotion().divide(2));
                            Vector2D position = asteroidSprite.getDynamicState().getPosition();
                            DynamicState dynamicState = new DynamicState(position, fullMotion);
                            AsteroidSprite newAsteroidSprite = new AsteroidSprite(dynamicState, pieceSizes.get(pieceIndex));
                            newAsteroidSprite.setInvulnerable(true);
                        }
                    }
                    new ExplosionSprite(object.getDynamicState());
                    // decide whether asteroid should drop pickup
                    if (random.nextInt(100) < 10) {
                        PickupType pickupType = PickupType.FUEL;
                        if (random.nextInt(100) < 40) pickupType = PickupType.HEALTH;
                        DynamicState dynamicState = object.getDynamicState();
                        dynamicState.setMotion(dynamicState.getMotion().divide(4));
                        new PickupSprite(dynamicState, pickupType);
                    }
                }
                GameObjectDatabase.removeGameObject(object);
            }
        }
    }

    private boolean isOutOfBounds(Vector2D position) {
        boolean toLeft = position.x < -900;
        boolean toRight = position.x > 900;
        boolean toUp = position.y < -900;
        boolean toDown = position.y > 900;
        return (toLeft || toRight || toUp || toDown);
    }
}
