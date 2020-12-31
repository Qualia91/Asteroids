package com.boc_dev.asteroids.controllers;

import com.boc_dev.asteroids.models.ApplicationModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{
    //FXML
    @FXML
    private AnchorPane mainCanvas;

    // Model
    private final ApplicationModel applicationModel;

    public MainWindowController(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applicationModel.setMainCanvas(mainCanvas);
    }
}
