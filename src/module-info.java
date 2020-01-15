module com.nick.wood.asteroids.Asteroids {

	requires javafx.graphics;
	requires javafx.fxml;

	opens com.nick.wood.asteroids.controllers to javafx.fxml;

	exports com.nick.wood.asteroids;
}