package com.nick.wood.asteroids.models.sprites;

import com.nick.wood.asteroids.utils.DynamicState;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;

public abstract class GameObject {

    private final DynamicState dynamicState;
    private final SimpleBooleanProperty dead;

    GameObject(DynamicState dynamicState) {
        this.dynamicState = dynamicState;
        this.dead = new SimpleBooleanProperty(false);
        GameObjectDatabase.addGameObject(this);
    }
    public void update() {

    }
    public abstract Node getSprite();
    public abstract int getRadius();
    public DynamicState getDynamicState() {return dynamicState;}
    public SimpleBooleanProperty deadProperty() { return dead; }
    public void collisionDetected() { dead.set(true); }


    /** Behaviour tree stuff
     *
     */
    public boolean isSafe() {
        return true;
    }
    public void moveToSafety() {
        System.out.println("moveToSafety");
    }

    public boolean isTargetAquired() {
        return true;
    }
    public boolean isTargetInRange() {
        return true;
    }
    public void aquireTarget() {
        System.out.println("Target Aquired");
    }
    public void shootTarget() {
        System.out.println("shootTarget");
    }

    public boolean isPowerupAquired() {
        return true;
    }
    public boolean isPowerupInRange() {
        return true;
    }
    public void aquirePowerup(){
        System.out.println("Powerup Aquired");
    }
    public boolean needsTargetedPowerup() {
        return true;
    }
    public void moveToPowerup() {
        System.out.println("moveToPowerup");
    }
}
