package com.boc_dev.asteroids.models;

import com.boc_dev.asteroids.models.sprites.GameObjectDatabase;
import com.boc_dev.asteroids.utils.Vector2D;
import com.boc_dev.asteroids.models.sprites.GameObject;
import com.boc_dev.asteroids.utils.DynamicState;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class DrawingModel {
    private final ApplicationModel applicationModel;
    private final Node background;
    private final Node playerHealthBar;
    private final Node playerFuelBar;
    private final Node playerPoints;
    private Pane miniMap;

    public DrawingModel(ApplicationModel applicationModel) {

        this.applicationModel = applicationModel;

        this.background = getBackground();

        this.playerHealthBar = getPlayerHealth();

        this.playerFuelBar = getPlayerFuel();

        this.playerPoints = getPlayerPoints();

        this.miniMap = getMiniMap();
    }

    public void drawObjects()
    {

        applicationModel.getMainCanvas().getChildren().clear();
        miniMap.getChildren().clear();

        this.miniMap = getMiniMap();

        double xDiff = -100.0;
        double yDiff = -100.0;
        if (GameObjectDatabase.getPlayerSprite() != null) {
            xDiff += -GameObjectDatabase.getPlayerSprite().getPlayerMotion().x * 50.0;
            yDiff += -GameObjectDatabase.getPlayerSprite().getPlayerMotion().y * 50.0;
        }
        background.translateXProperty().set(xDiff);
        background.translateYProperty().set(yDiff);

        applicationModel.getMainCanvas().getChildren().add(background);

        GameObjectDatabase.getGameObjectList().forEach(this::draw);

        applicationModel.getMainCanvas().getChildren().add(playerPoints);

        applicationModel.getMainCanvas().getChildren().add(playerHealthBar);

        applicationModel.getMainCanvas().getChildren().add(playerFuelBar);

        applicationModel.getMainCanvas().getChildren().add(miniMap);

        applicationModel.getGameModel().getLocationLoader().getLocations().forEach(this::drawInMap);
    }

    private void draw(GameObject gameObject) {
        DynamicState dynamicState = gameObject.getDynamicState();
        Node sprite = gameObject.getSprite();
        sprite.setTranslateX(dynamicState.getPosition().x + applicationModel.getScreenMiddleX());
        sprite.setTranslateY(dynamicState.getPosition().y + applicationModel.getScreenMiddleY());
        applicationModel.getMainCanvas().getChildren().add(sprite);
    }

    private void drawInMap(Vector2D locationPosition) {
        Circle mapSprite = new Circle(2);
        mapSprite.setFill(Color.BLACK);
        Vector2D difference = locationPosition.subtract(applicationModel.getGameModel().getPlayerEffectivePosition());
        mapSprite.setCenterX(difference.x / 100);
        mapSprite.setCenterY(difference.y / 100);
        miniMap.getChildren().add(mapSprite);
    }

    private Node getBackground() {
        Pane pane = new Pane();
        Image image = new Image("File:src/com/boc_dev/asteroids/assets/background.jpg");
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().setValue(applicationModel.getScreenMiddleX()*4);
        imageView.fitHeightProperty().setValue(applicationModel.getScreenMiddleY()*4);
        pane.getChildren().add(imageView);
        return pane;
    }

    private Text getPlayerPoints() {
        // Draw points
        Text text = new Text();
        text.textProperty().bind(GameObjectDatabase.getPlayerSprite().pointsProperty().asString());
        text.setX(10.0);
        text.setY(20.0);
        text.setStroke(Color.WHITE);
        return text;
    }

    private Node getPlayerHealth() {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(10);
        rectangle.setX(10);
        rectangle.setY((applicationModel.getScreenMiddleY() * 2) - 20);
        applicationModel.screenMiddleYProperty().addListener((observable, oldValue, newValue) -> rectangle.setY((newValue.doubleValue() * 2) - 20));
        rectangle.setFill(Color.RED);
        rectangle.widthProperty().bind(GameObjectDatabase.getPlayerSprite().playerHealthProperty());
        return rectangle;
    }

    private Node getPlayerFuel() {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(5);
        rectangle.setX(10);
        rectangle.setY((applicationModel.getScreenMiddleY() * 2) - 10);
        applicationModel.screenMiddleYProperty().addListener((observable, oldValue, newValue) -> rectangle.setY((newValue.doubleValue() * 2) - 20));
        rectangle.setFill(Color.BLUE);
        rectangle.widthProperty().bind(GameObjectDatabase.getPlayerSprite().playerFuelProperty());
        return rectangle;
    }



    private Pane getMiniMap() {
        Pane miniMapPane = new Pane();
        int radius = 80;
        Circle map = new Circle(radius);
        miniMapPane.setTranslateY(radius + 10);
        miniMapPane.setTranslateX((applicationModel.screenMiddleXProperty().get() * 2) - radius - 10);
        applicationModel.screenMiddleXProperty().addListener((observable, oldValue, newValue) ->
                miniMapPane.setTranslateX((newValue.doubleValue() * 2) - radius - 10));
        map.setFill(Color.GHOSTWHITE);

        Circle playerInMap = new Circle(1);
        playerInMap.setFill(Color.BLACK);
        miniMapPane.getChildren().add(map);
        miniMapPane.getChildren().add(playerInMap);
        return miniMapPane;
    }
}
