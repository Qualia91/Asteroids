package com.boc_dev.asteroids.models;

import com.boc_dev.asteroids.models.sprites.*;
import com.boc_dev.asteroids.utils.Utilities;
import com.boc_dev.asteroids.utils.Vector2D;

public class CollisionModel {

    public void run() {
        GameObjectDatabase.getPlayerSprite().canFireProperty().set(true);
        GameObjectDatabase.getGameObjectList().forEach(gameObject -> {
            // Check for collisions of game objects with game objects
            GameObjectDatabase.getGameObjectList().forEach(secondGameObject -> {
                if (!gameObject.equals(secondGameObject)) {
                    // dont check collisions of explosions
                    if (!(gameObject instanceof ExplosionSprite || secondGameObject instanceof ExplosionSprite)) {
                        // check if it is player and bullet or bullet and bullet collision. If so, dont check collisions
                        if (!((gameObject instanceof BulletSprite && secondGameObject instanceof PlayerSprite ||
                                gameObject instanceof PlayerSprite && secondGameObject instanceof BulletSprite))) {
                            if (!(gameObject instanceof BulletSprite && secondGameObject instanceof BulletSprite)) {
                                // check if asteroid and bullet collision. if so, if asteroid is invulnerable, dont collide
                                boolean invulnerableAsteroid = false;
                                if (gameObject instanceof BulletSprite && secondGameObject instanceof AsteroidSprite) {
                                    if (((AsteroidSprite)secondGameObject).isInvulnerable()) {
                                        invulnerableAsteroid = true;
                                    }
                                }
                                if (gameObject instanceof AsteroidSprite && secondGameObject instanceof BulletSprite) {
                                    if (((AsteroidSprite)gameObject).isInvulnerable()) {
                                        invulnerableAsteroid = true;
                                    }
                                }
                                if (!invulnerableAsteroid) {
                                    int collisionCheck = Utilities.checkCollision(
                                            gameObject.getDynamicState().getPosition(),
                                            gameObject.getRadius(),
                                            secondGameObject.getDynamicState().getPosition(),
                                            secondGameObject.getRadius());
                                    if (collisionCheck == 1) {
                                        collision(gameObject, secondGameObject);
                                    } else if (collisionCheck == 2) {
                                        if (gameObject instanceof AsteroidSprite) {
                                            gameObject.deadProperty().set(true);
                                        } else if (secondGameObject instanceof AsteroidSprite) {
                                            secondGameObject.deadProperty().set(true);
                                        } else if (gameObject instanceof PlayerSprite) {
                                            ((PlayerSprite) gameObject).canFireProperty().set(false);
                                        } else if (secondGameObject instanceof PlayerSprite) {
                                            ((PlayerSprite) secondGameObject).canFireProperty().set(false);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        });

    }

    private Vector2D changeInVelocity(GameObject one, GameObject two) {
        double collisionPointX = ((one.getDynamicState().getPosition().x * two.getRadius()) + (two.getDynamicState().getPosition().x * one.getRadius())) / (one.getRadius() + two.getRadius());
        double collisionPointY = ((one.getDynamicState().getPosition().y * two.getRadius()) + (two.getDynamicState().getPosition().y * one.getRadius())) / (one.getRadius() + two.getRadius());
        Vector2D collisionPoint = new Vector2D(collisionPointX, collisionPointY);
        double collisiondist = Math.sqrt(Math.pow(two.getDynamicState().getPosition().x - collisionPoint.x, 2) + Math.pow(two.getDynamicState().getPosition().y - collisionPoint.y, 2));
        double n_x = (two.getDynamicState().getPosition().x - collisionPoint.x) / collisiondist;
        double n_y = (two.getDynamicState().getPosition().y - collisionPoint.y) / collisiondist;
        double p = ((one.getDynamicState().getMotion().x * n_x + one.getDynamicState().getMotion().y * n_y) - (two.getDynamicState().getMotion().x * n_x + two.getDynamicState().getMotion().y * n_y)) /
                (one.getRadius() + two.getRadius());
        double w_x = p * one.getRadius() * n_x + p * two.getRadius() * n_x;
        double w_y = p * one.getRadius() * n_y + p * two.getRadius() * n_y;
        return new Vector2D(-w_x, -w_y);
    }

    private void collision(GameObject gameObject, GameObject secondGameObject) {
        // Player collision
        if (gameObject instanceof PlayerSprite && secondGameObject instanceof AsteroidSprite ||
                gameObject instanceof AsteroidSprite && secondGameObject instanceof PlayerSprite) {
            gameObject.collisionDetected();
        }
        // Asteroid collision, do bouncing
        else if (gameObject instanceof AsteroidSprite && secondGameObject instanceof AsteroidSprite) {
            // Only doing half collision and then the next time the loops hit on same collisions will be other size
            // This means the collision detected will have to change a motion that only gets added to dynamic state on game update
            Vector2D change = changeInVelocity(gameObject, secondGameObject);
            ((AsteroidSprite) gameObject).asteriodCollisionDetected(change);
        }
        // bullet asteroid collision, destroy
        else if (gameObject instanceof BulletSprite && secondGameObject instanceof AsteroidSprite ||
                    gameObject instanceof AsteroidSprite && secondGameObject instanceof BulletSprite) {
            gameObject.collisionDetected();
            secondGameObject.collisionDetected();
            GameObjectDatabase.getPlayerSprite().addPoints(gameObject.getRadius() + secondGameObject.getRadius());
        }
        // player and pickup collision
        else if (gameObject instanceof PlayerSprite && secondGameObject instanceof PickupSprite ||
                gameObject instanceof PickupSprite && secondGameObject instanceof PlayerSprite) {
            PickupSprite pickupSprite;
            if (gameObject instanceof PickupSprite) {
                pickupSprite = (PickupSprite) gameObject;
                gameObject.collisionDetected();
            }
            else {
                pickupSprite = (PickupSprite) secondGameObject;
                secondGameObject.collisionDetected();
            }
            GameObjectDatabase.getPlayerSprite().addPickup(pickupSprite);
        }
        // spaceship and asteroid collision
        else if ((gameObject instanceof SpaceShipSprite && secondGameObject instanceof AsteroidSprite)
            || (secondGameObject instanceof SpaceShipSprite && gameObject instanceof AsteroidSprite)) {
            if (gameObject instanceof AsteroidSprite) {
                Vector2D change = changeInVelocity(gameObject, secondGameObject);
                ((AsteroidSprite) gameObject).asteriodCollisionDetected(change);
            } else {
                Vector2D change = changeInVelocity(secondGameObject, gameObject);
                ((AsteroidSprite) secondGameObject).asteriodCollisionDetected(change);
            }
        }

    }


}