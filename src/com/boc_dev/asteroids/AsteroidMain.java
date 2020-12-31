package com.boc_dev.asteroids;

import com.boc_dev.asteroids.controllers.MainWindowController;
import com.boc_dev.asteroids.models.ApplicationModel;
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
                case LEFT :
                    applicationModel.rotateLeft(true);
                    break;
                case RIGHT :
                    applicationModel.rotateRight(true);
                    break;
                case SPACE :
                    applicationModel.shoot(true);
                    break;
                case Z :
                    applicationModel.accelerate(true);
                    break;
                case X :
                    applicationModel.decelerate(true);
                    break;
            }
        });
        primaryStage.getScene().setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT :
                    applicationModel.rotateLeft(false);
                    break;
                case RIGHT :
                    applicationModel.rotateRight(false);
                    break;
                case SPACE :
                    applicationModel.shoot(false);
                    break;
                case Z :
                    applicationModel.accelerate(false);
                    break;
                case X :
                    applicationModel.decelerate(false);
                    break;
                case R :
                    applicationModel.restart();
                    break;
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
