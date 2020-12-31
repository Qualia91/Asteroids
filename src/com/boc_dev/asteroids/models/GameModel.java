package com.boc_dev.asteroids.models;

import com.boc_dev.asteroids.models.sprites.*;
import com.boc_dev.asteroids.utils.DynamicState;
import com.boc_dev.asteroids.utils.Utilities;
import com.boc_dev.asteroids.utils.Vector2D;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.*;

public class GameModel {
    private final Random rand = new Random();
    private final CollisionModel collisionModel;
    private final DeathModel deathModel;
    private double shootSpeed;
    private final SimpleBooleanProperty shoot;
    private Vector2D playerMotion;
    private Vector2D playerEffectivePosition = new Vector2D();
    private final LocationLoader locationLoader = new LocationLoader();

    public GameModel() {
        this.shoot = new SimpleBooleanProperty(false);
        this.collisionModel = new CollisionModel();
        this.deathModel = new DeathModel(this);
    }

    public void updateGame(double timeStep) {
        // check if player sprite is alive. if not, dont bother
        if (GameObjectDatabase.getPlayerSprite() != null) {

            // get player sprites orientation
            double rotationAngle = GameObjectDatabase.getPlayerSprite().getRotationAngle();
            // get a motion vector that contains the player acceleration times by timestep,
            // and rotate this by the rotation angle
            double velocity = (GameObjectDatabase.getPlayerSprite().getAcceleration() * timeStep) / 20.0;

            playerMotion = new Vector2D(Utilities.rotateX(0, velocity, Utilities.toRadians(rotationAngle)), Utilities.rotateY(0, velocity, Utilities.toRadians(rotationAngle)));

            GameObjectDatabase.getPlayerSprite().setPlayerMotion(playerMotion);

            playerEffectivePosition = playerEffectivePosition.add(playerMotion.multiply(timeStep));

            naturalDeccleration();

            GameObjectDatabase.getGameObjectList().forEach(gameObject -> {

                gameObject.update();
                DynamicState dynamicState = gameObject.getDynamicState();
                Vector2D position = dynamicState.getPosition();

                if (!(gameObject instanceof PlayerSprite)) {
                    Vector2D motion = dynamicState.getMotion().subtract(playerMotion);
                    dynamicState.setPosition(position.add(motion.multiply(timeStep)));
                }
            });

            collisionModel.run();
            deathModel.run();
            locationLoader.run(playerEffectivePosition);

        }

    }

    public void addAsteroid1() {
        double rotationAngle = 0;
        double velocity = 0.05;
        Vector2D motion = new Vector2D(Utilities.rotateX(0, velocity, (rotationAngle - (Math.PI))), Utilities.rotateY(0, velocity, (rotationAngle - (Math.PI))));
        Vector2D position = new Vector2D(Utilities.rotateX(
                0,
                400,
                rotationAngle),
                Utilities.rotateY(
                        0,
                        400,
                        rotationAngle));
        DynamicState dynamicState = new DynamicState(position, motion);
        AsteroidSprite asteroidSprite = new AsteroidSprite(dynamicState, 10);
    }

    /*public void addAsteroid2() {
        double rotationAngle = Math.PI;
        double velocity = 0;
        Vector2D motion = new Vector2D(Utilities.rotateX(0, velocity, (rotationAngle - (Math.PI/2))), Utilities.rotateY(0, velocity, (rotationAngle - (Math.PI/2))));
        Vector2D position = new Vector2D(Utilities.rotateX(
                -800,
                0,
                rotationAngle),
                Utilities.rotateY(
                        -800,
                        0,
                        rotationAngle));
        DynamicState dynamicState = new DynamicState(position, motion);
        AsteroidSprite asteroidSprite = new AsteroidSprite(dynamicState);
        asteroidSprite.name = "Two";
    }*/

    public Vector2D getPlayerEffectivePosition() {
        return playerEffectivePosition;
    }

    public void addAsteroid(int meanAsteroidSize) {
        double lower = 0.0;
        double upper = 2 * Math.PI;
        double rotationAngle = rand.nextDouble() * (upper - lower) + lower;
        double velocity = rand.nextInt(100) * 0.002;
        Vector2D motion = new Vector2D(Utilities.rotateX(0, velocity, (rotationAngle - (Math.PI/2))), Utilities.rotateY(0, velocity, (rotationAngle - (Math.PI/2))));
        Vector2D position = new Vector2D(Utilities.rotateX(
                        -800,
                        0,
                        rotationAngle),
                Utilities.rotateY(
                        -800,
                        0,
                        rotationAngle));
        DynamicState dynamicState = new DynamicState(position, motion);
        new AsteroidSprite(dynamicState, meanAsteroidSize);
    }

    public void addBullet() {
        double rotationAngle = Utilities.toRadians(GameObjectDatabase.getPlayerSprite().getRotationAngle());
        double velocity = 0.2;
        Vector2D motion = playerMotion.add(new Vector2D(Utilities.rotateX(0, velocity, rotationAngle), Utilities.rotateY(0, velocity, rotationAngle)));
        Vector2D position = new Vector2D(0,0);
        DynamicState dynamicState = new DynamicState(position, motion);
        new BulletSprite(rotationAngle, dynamicState);
    }

    public void addAcceleration(double acceleration) {
        GameObjectDatabase.getPlayerSprite().addAcceleration(acceleration);
    }

    public void naturalDeccleration() {
        if (GameObjectDatabase.getPlayerSprite().getAcceleration() > 0.0) {
            GameObjectDatabase.getPlayerSprite().addAcceleration(-0.002);
        }
        else if (GameObjectDatabase.getPlayerSprite().getAcceleration() < -0.0) {
            GameObjectDatabase.getPlayerSprite().addAcceleration(0.002);
        }
    }

    public void shoot() {
        if (GameObjectDatabase.getPlayerSprite().isCanFire()) {
            shootSpeed++;
            if (this.shootSpeed > 5) {
                addBullet();
                shootSpeed = 0;
            } else {
                this.shoot.set(false);
            }
        }
    }

    public void addSpaceShip() {
        if (GameObjectDatabase.getSpaceShipList().size() < 1) {
            double lower = 0.0;
            double upper = 2 * Math.PI;
            double rotationAngle = rand.nextDouble() * (upper - lower) + lower;
            Vector2D position = new Vector2D(Utilities.rotateX(
                    -800,
                    0,
                    rotationAngle),
                    Utilities.rotateY(
                            -800,
                            0,
                            rotationAngle));
            new SpaceShipSprite(new DynamicState(position, new Vector2D()));
        }
    }

    public LocationLoader getLocationLoader() {
        return locationLoader;
    }

    public void addPowerup() {
        double lower = 0.0;
        double upper = 2 * Math.PI;
        double rotationAngle = rand.nextDouble() * (upper - lower) + lower;
        double velocity = rand.nextInt(100) * 0.002;
        Vector2D motion = new Vector2D(Utilities.rotateX(0, velocity, (rotationAngle - (Math.PI/2))), Utilities.rotateY(0, velocity, (rotationAngle - (Math.PI/2))));
        Vector2D position = new Vector2D(Utilities.rotateX(
                -800,
                0,
                rotationAngle),
                Utilities.rotateY(
                        -800,
                        0,
                        rotationAngle));
        DynamicState dynamicState = new DynamicState(position, motion);
        new PickupSprite(dynamicState, PickupType.HEALTH);
    }
}
