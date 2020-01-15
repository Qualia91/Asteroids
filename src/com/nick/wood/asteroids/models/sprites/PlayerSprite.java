package com.nick.wood.asteroids.models.sprites;

import com.nick.wood.asteroids.models.PickupType;
import com.nick.wood.asteroids.utils.DynamicState;
import com.nick.wood.asteroids.utils.Utilities;
import com.nick.wood.asteroids.utils.Vector2D;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.List;

public class PlayerSprite extends GameObject {

    private final Polygon playerSprite;

    private final SimpleDoubleProperty rotationAngle = new SimpleDoubleProperty(0);

    private final int spriteRadius = 20;

    private final SimpleIntegerProperty points;

    private Vector2D playerMotion;

    private final SimpleDoubleProperty playerHealth = new SimpleDoubleProperty(1000);

    private final SimpleDoubleProperty playerFuel = new SimpleDoubleProperty(500);

    private final SimpleBooleanProperty canFire = new SimpleBooleanProperty(true);

    private double rotationIncrease = 0;
    private boolean needsToRotate = false;

    private double acceleration = 0;
    private boolean accelerationIncrease = false;

    private int numberOfRotationMovementsNeeded;

    private boolean targetingATarget = false;

    private PickupSprite pickupTarget = null;

    private final int rotationSpeed = 2;

    public PlayerSprite() {
        super(new DynamicState());

        playerMotion = new Vector2D();

        points = new SimpleIntegerProperty(0);

        double[] intialXCoord = new double[5];
        double[] intialYCoord = new double[5];

        intialXCoord[0] = 0;
        intialXCoord[1] = spriteRadius/2.0;
        intialXCoord[2] = 0;
        intialXCoord[3] = -spriteRadius/2.0;
        intialXCoord[4] = 0;

        intialYCoord[0] = spriteRadius/2.0;
        intialYCoord[1] = -spriteRadius/2.0;
        intialYCoord[2] = -spriteRadius/4.0;
        intialYCoord[3] = -spriteRadius/2.0;
        intialYCoord[4] = spriteRadius/2.0;

        int size = intialYCoord.length;

        double[] playerCoords = new double[size * 2];
        for (int index = 0; index < size; index++) {
            playerCoords[index * 2] = intialXCoord[index];
            playerCoords[(index * 2) + 1] = intialYCoord[index];
        }

        playerSprite = new Polygon(playerCoords);
        playerSprite.setFill(Color.BLACK);

        playerSprite.rotateProperty().bind(rotationAngle);
    }

    public Vector2D getPlayerMotion() {
        return playerMotion;
    }

    public void setPlayerMotion(Vector2D playerMotion) {
        this.playerMotion = playerMotion;
    }

    public void addRotation(double rotationAngle) {
        this.rotationAngle.set(this.rotationAngle.get() + rotationAngle);
    }

    public double getRotationAngle() {
        return this.rotationAngle.get();
    }

    public SimpleIntegerProperty pointsProperty() {
        return points;
    }

    public void addPoints(int points) {
        this.points.setValue(this.points.get() + points);
    }

    public SimpleDoubleProperty playerHealthProperty() {
        return playerHealth;
    }

    public SimpleDoubleProperty playerFuelProperty() {
        return playerFuel;
    }

    public boolean isCanFire() {
        return canFire.get();
    }

    public SimpleBooleanProperty canFireProperty() {
        return canFire;
    }

    public void addPickup(PickupSprite pickupSprite) {
        if (pickupSprite.getPickupType() == PickupType.HEALTH) {
            playerHealth.set(playerHealth.get() + 100);
            if (playerHealth.get() > 1000) playerHealth.set(1000);
        } else if (pickupSprite.getPickupType() == PickupType.FUEL) {
            playerFuel.set(playerFuel.get() + 50);
            if (playerFuel.get() > 500) playerFuel.set(500);
        }
    }

    public void reset() {
        rotationAngle.set(0);

        points.set(0);

        playerMotion.set(new Vector2D());

        playerHealth.set(1000);

        playerFuel.set(500);
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void addAcceleration(double amount) {
        playerFuel.set(GameObjectDatabase.getPlayerSprite().playerFuelProperty().get() - (amount * 10));
        if (!(playerFuel.get() < 0)) {
            acceleration += amount;
            if (acceleration > 0.15) {
                acceleration = 0.15;
            } else if (acceleration < -0.15) {
                acceleration = -0.15;
            }
        }
    }

    private void accelerate() {
        accelerationIncrease = true;
    }

    private void rotateToward(PickupSprite pickupTarget) {
        rotateTowardsAngle((int)Math.toDegrees(Utilities.getAngleTo(getDynamicState().getPosition(), pickupTarget.getDynamicState().getPosition(), Math.toRadians(rotationAngle.get()))));
    }

    private void rotateTowardsAngle(int angle) {
        System.out.println("Angle: " + angle);
        needsToRotate = true;
        if (-rotationSpeed < angle && angle < rotationSpeed) {
            needsToRotate = false;
            rotationIncrease = 0;
        }
        else if (angle < rotationSpeed) {
            rotationIncrease = rotationSpeed;
            numberOfRotationMovementsNeeded = angle / rotationSpeed;
        }
        else {
            rotationIncrease = +rotationSpeed;
            numberOfRotationMovementsNeeded = angle / rotationSpeed;
        }
    }

    @Override
    public void update() {
        if (needsToRotate) {
            addRotation(rotationIncrease);
            numberOfRotationMovementsNeeded--;
            if (numberOfRotationMovementsNeeded == 0) {
                needsToRotate = false;
            }
        }
        if (accelerationIncrease) {
            addAcceleration(0.01);
        }
    }

    @Override
    public int getRadius() {
        return spriteRadius;
    }

    @Override
    public Polygon getSprite() {
        return playerSprite;
    }

    @Override
    public void collisionDetected() {
        if (playerHealth.get() > 0) {
            playerHealth.set(playerHealth.get() - 50);
        }
    }


    /** Behaviour tree stuff **/
    @Override
    public boolean isSafe() {
        // Loop through all asteroids
        List<AsteroidSprite> asteroidList = GameObjectDatabase.getAsteroidList();
        for (AsteroidSprite asteroid : asteroidList) {
            // if asteroid is within 500 get velocity vector
            if (Utilities.initialCollisionCheck(getDynamicState().getPosition(),
                    asteroid.getDynamicState().getPosition(),
                    500,
                    asteroid.getRadius())) {
                // if velocity vector is in direction of player bubble, it is not safe
                if (Utilities.lineItersectsCircleInCenter(asteroid.getDynamicState().getMotion(), asteroid.getDynamicState().getPosition(), spriteRadius)) {
                    return false;
                }
            }
        }
        accelerationIncrease = false;
        return true;
    }


    /** Powerups **/
    @Override
    public boolean isPowerupAquired() {
        return (pickupTarget != null);
    }

    @Override
    public boolean isPowerupInRange() {
        // Loop through all pickups
        List<PickupSprite> pickupSprites = GameObjectDatabase.getPickupList();
        for (PickupSprite pickup : pickupSprites) {
            // if pickup is within 500 return true
            if (Utilities.initialCollisionCheck(getDynamicState().getPosition(),
                    pickup.getDynamicState().getPosition(),
                    500,
                    pickup.getRadius())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void aquirePowerup(){
        // Loop through all pickups
        double closestDistance = Double.MAX_VALUE;
        List<PickupSprite> pickupSprites = GameObjectDatabase.getPickupList();
        for (PickupSprite pickup : pickupSprites) {
            // get how far each pickup is from player. If it is closer than the last one, set
            // closest equal to it anf distance == to distance
            double thisDistance = Utilities.getDistanceBetween(pickup.getDynamicState().getPosition(), getDynamicState().getPosition());
            if (thisDistance < closestDistance) {
                pickupTarget = pickup;
                closestDistance = thisDistance;
            }
        }

    }

    @Override
    public boolean needsTargetedPowerup() {
        if (pickupTarget.getPickupType() == PickupType.HEALTH) {
            return playerHealth.get() < 1000;
        } else {
            return playerFuel.get() < 500;
        }
    }

    @Override
    public void moveToPowerup() {
        System.out.println("moveToPowerup");
        rotateToward(pickupTarget);
        accelerate();
    }

    @Override
    public void moveToSafety() {
        // cast ray outward from player. if it doesn't collide with anything, accelerate
        // Loop through all asteroids
        int returnAngle = 0;
        for (int angle = 0; angle < 180; angle+=rotationSpeed) {
            boolean rotationNeeded = false;
            List<AsteroidSprite> asteroidList = GameObjectDatabase.getAsteroidList();
            for (AsteroidSprite asteroid : asteroidList) {
                returnAngle = angle;
                // check if +ve angle works
                if (Utilities.inFoV(getDynamicState().getPosition(), asteroid.getDynamicState().getPosition(), Utilities.toRadians(returnAngle + getRotationAngle()), Utilities.toRadians(20), 1000)) {
                    // if it intersects, rotate and try again
                    rotationNeeded = true;
                    break;
                }
                returnAngle = -angle;
                // check if -ve angle works
                if (Utilities.inFoV(getDynamicState().getPosition(), asteroid.getDynamicState().getPosition(), Utilities.toRadians(returnAngle + getRotationAngle()), Utilities.toRadians(20), 1000)) {
                    // if it intersects, rotate and try again
                    rotationNeeded = true;
                    break;
                }
                rotationNeeded = false;
            }
            if (!rotationNeeded) {
                rotateTowardsAngle(returnAngle);
                break;
            }
        }
        // then accelerate
        accelerate();
    }

}
