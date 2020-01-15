package com.nick.wood.asteroids.models;

import com.nick.wood.asteroids.models.sprites.GameObjectDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class ApplicationModel {
    private final SimpleStringProperty gameName;
    private final GameModel gameModel;
    private DrawingModel drawingModel;
    private final SimpleDoubleProperty screenMiddleX;
    private final SimpleDoubleProperty screenMiddleY;
    private AnchorPane mainCanvas;
    private boolean rotateLeft;
    private boolean rotateRight;
    private boolean shoot;
    private boolean accelerate;
    private boolean decelerate;
    private String points;

    private final Timeline timeline = new Timeline();
    private final Random rand = new Random();

    public ApplicationModel(String gameName, Stage primaryStage) {
        this.screenMiddleX = new SimpleDoubleProperty(400);
        this.screenMiddleY = new SimpleDoubleProperty(800);
        this.gameName = new SimpleStringProperty(gameName);
        this.gameModel = new GameModel();
        points = "Points: 0";

        setupBindings();
    }

    public void run() {

        this.drawingModel = new DrawingModel(this);

        KeyFrame f = new KeyFrame(Duration.millis(20),
                ae -> {
                    double rotateAngle = 0;
                    double acceleration = 0;

                    if (GameObjectDatabase.getPlayerSprite().playerHealthProperty().get() > 0) {

                            if (rotateLeft) rotateAngle -= 5.0;
                            if (rotateRight) rotateAngle += 5.0;
                            if (accelerate) acceleration += 0.01;
                            if (decelerate) acceleration -= 0.01;

                            GameObjectDatabase.getPlayerSprite().addRotation(rotateAngle);
                            gameModel.addAcceleration(acceleration);
                            gameModel.naturalDeccleration();

                            /*if (shoot)*/ gameModel.shoot();

                            if (rand.nextInt(1000) < 30) {

                                gameModel.addAsteroid(rand.nextInt(25) + 25);

                            }
                            /*if (GameObjectDatabase.getPickupList().size() < 1) {
                                gameModel.addPowerup();
                            }*/
                            /*if (GameObjectDatabase.getAsteroidList().size() < 1) {
                                gameModel.addAsteroid1();
                            }*/

                        }
                    gameModel.updateGame(20);
                    drawingModel.drawObjects();

        });
        timeline.getKeyFrames().add(f);
        timeline.setCycleCount(-1);
        timeline.play();
    }

    private void setupBindings() {
        if (GameObjectDatabase.getPlayerSprite() != null) {
            GameObjectDatabase.getPlayerSprite().pointsProperty().addListener((observable, oldValue, newValue) -> points = "Points: " + newValue);
        }
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void rotateRight(boolean bool) {
        rotateLeft = bool;
    }

    public void rotateLeft(boolean bool) {
        rotateRight = bool;
    }

    public void shoot(boolean bool) {
        shoot = bool;
    }

    public SimpleStringProperty gameNameProperty() {
        return gameName;
    }

    public double getScreenMiddleX() {
        return screenMiddleX.get();
    }

    public SimpleDoubleProperty screenMiddleXProperty() {
        return screenMiddleX;
    }

    public SimpleDoubleProperty screenMiddleYProperty() {
        return screenMiddleY;
    }

    public void setMainCanvas(AnchorPane mainCanvas) {
        this.mainCanvas = mainCanvas;
    }

    public AnchorPane getMainCanvas() {
        return mainCanvas;
    }

    public double getScreenMiddleY() {
        return screenMiddleY.get();
    }

    public void accelerate(boolean b) {
        this.accelerate = b;
    }

    public void decelerate(boolean b) {
        this.decelerate = b;
    }

    public String getPoints() {
        return points;
    }

    public void restart() {
        GameObjectDatabase.restart();
    }
}
