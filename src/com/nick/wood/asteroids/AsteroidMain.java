package com.nick.wood.asteroids;

import com.nick.wood.asteroids.controllers.MainWindowController;
import com.nick.wood.asteroids.models.ApplicationModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class AsteroidMain extends Application {

    ApplicationModel applicationModel;

    @Override
    public void start(Stage primaryStage) {
        applicationModel = new ApplicationModel("Asteroids", primaryStage);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/MainWindowView.fxml"));
        fxmlLoader.setController(new MainWindowController(applicationModel));

        try {
            AnchorPane root = fxmlLoader.load();
            Scene scene = new Scene(root, 1020, 800);


            applicationModel.screenMiddleXProperty().set(1020/2.0);
            applicationModel.screenMiddleYProperty().set(800/2.0);

            scene.widthProperty().addListener((observable, oldValue, newValue) -> {
                root.prefWidthProperty().setValue(newValue);
                applicationModel.screenMiddleXProperty().set(newValue.doubleValue()/2.0);
            });
            scene.heightProperty().addListener((observable, oldValue, newValue) -> {
                root.prefHeightProperty().setValue(newValue);
                applicationModel.screenMiddleYProperty().set(newValue.doubleValue()/2.0);
            });


            primaryStage.setScene(scene);
            primaryStage.titleProperty().bindBidirectional(applicationModel.gameNameProperty());
            primaryStage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        applicationModel.run();

        primaryStage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT -> applicationModel.rotateLeft(true);
                case RIGHT -> applicationModel.rotateRight(true);
                case SPACE -> applicationModel.shoot(true);
                case Z -> applicationModel.accelerate(true);
                case X -> applicationModel.decelerate(true);
            }
        });
        primaryStage.getScene().setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT -> applicationModel.rotateLeft(false);
                case RIGHT -> applicationModel.rotateRight(false);
                case SPACE -> applicationModel.shoot(false);
                case Z -> applicationModel.accelerate(false);
                case X -> applicationModel.decelerate(false);
                case R -> applicationModel.restart();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
