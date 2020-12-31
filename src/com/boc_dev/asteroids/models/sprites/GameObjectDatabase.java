package com.boc_dev.asteroids.models.sprites;

import java.util.ArrayList;
import java.util.List;

public class GameObjectDatabase {

    static private final List<AsteroidSprite> asteroidList = new ArrayList<>();
    static private final List<BulletSprite> bulletList = new ArrayList<>();
    static private final List<ExplosionSprite> explosionList = new ArrayList<>();
    static private final List<PickupSprite> pickupList = new ArrayList<>();
    static private final List<SpaceShipSprite> spaceShipList = new ArrayList<>();
    static private PlayerSprite playerSprite = new PlayerSprite();

    static public List<AsteroidSprite> getAsteroidList() {
        return asteroidList;
    }

    static public List<BulletSprite> getBulletList() {
        return bulletList;
    }

    public static List<ExplosionSprite> getExplosionList() {
        return explosionList;
    }

    public static List<PickupSprite> getPickupList() {
        return pickupList;
    }

    public static List<SpaceShipSprite> getSpaceShipList() {
        return spaceShipList;
    }

    static public PlayerSprite getPlayerSprite() {
        return playerSprite;
    }

    static public List<GameObject> getGameObjectList() {
        List<GameObject> gameObjectList = new ArrayList<>();
        gameObjectList.addAll(asteroidList);
        gameObjectList.addAll(explosionList);
        gameObjectList.addAll(bulletList);
        gameObjectList.addAll(pickupList);
        gameObjectList.addAll(spaceShipList);
        gameObjectList.add(playerSprite);
        return gameObjectList;
    }

    static public void addGameObject(GameObject gameObject) {
        if (gameObject instanceof AsteroidSprite) {
            if (asteroidList.size() < 20) {
                asteroidList.add((AsteroidSprite) gameObject);
            }
        }
        else if (gameObject instanceof BulletSprite) {
            bulletList.add((BulletSprite)gameObject);
        }
        else if (gameObject instanceof  PlayerSprite) {
            playerSprite = (PlayerSprite) gameObject;
        }
        else if (gameObject instanceof  ExplosionSprite) {
            explosionList.add((ExplosionSprite) gameObject);
        }
        else if (gameObject instanceof  PickupSprite) {
            pickupList.add((PickupSprite) gameObject);
        }
        else if (gameObject instanceof  SpaceShipSprite) {
            spaceShipList.add((SpaceShipSprite) gameObject);
        }
    }

    static  public void removeGameObject(GameObject gameObject) {
        if (gameObject instanceof AsteroidSprite) {
            asteroidList.remove(gameObject);
        }
        else if (gameObject instanceof BulletSprite) {
            bulletList.remove(gameObject);
        }
        else if (gameObject instanceof  PlayerSprite) {
            playerSprite = null;
        }
        else if (gameObject instanceof  ExplosionSprite) {
            explosionList.remove(gameObject);
        }
        else if (gameObject instanceof  PickupSprite) {
            pickupList.remove(gameObject);
        }
        else if (gameObject instanceof  SpaceShipSprite) {
            spaceShipList.remove(gameObject);
        }
    }

    public static void restart() {
        playerSprite.reset();
        asteroidList.clear();
        bulletList.clear();
        explosionList.clear();
        pickupList.clear();
        spaceShipList.clear();
    }
}
