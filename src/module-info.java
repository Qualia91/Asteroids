module com.boc_dev.asteroids.Asteroids {

	requires javafx.graphics;
	requires javafx.fxml;

	opens com.boc_dev.asteroids.controllers to javafx.fxml;

	exports com.boc_dev.asteroids;
}