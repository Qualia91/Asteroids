package com.boc_dev.asteroids.models;

import com.boc_dev.asteroids.models.sprites.GameObjectDatabase;
import com.boc_dev.asteroids.models.sprites.SpaceShipSprite;
import com.boc_dev.asteroids.utils.Utilities;
import com.boc_dev.asteroids.utils.Vector2D;
import com.boc_dev.asteroids.utils.DynamicState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LocationLoader {
    private final ObservableList<Vector2D> locations;

    public LocationLoader() {
        this.locations = FXCollections.observableArrayList();
        createLocations();
    }

    public ObservableList<Vector2D> getLocations() {
        return locations;
    }

    public void run(Vector2D playerLocation) {
        this.locations.forEach(location -> {
            if (Utilities.initialCollisionCheck(location, playerLocation, 1000, 1000)) {
                if (GameObjectDatabase.getSpaceShipList().size() < 1) {
                    new SpaceShipSprite(new DynamicState(location.subtract(playerLocation), new Vector2D()));
                }
            }
        });
    }

    private void createLocations() {

        locations.add(new Vector2D(3000, 0));

        locations.add(new Vector2D(6000, 0));

        locations.add(new Vector2D(3000, 4000));

        locations.add(new Vector2D(1000, -9000));
    }
}
