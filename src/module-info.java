module ChatTCPSocket {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.sql;
	requires javafx.base;

	
	
	opens main to javafx.graphics, javafx.fxml, javafx.controls;
	opens controller to javafx.controls, javafx.fxml, javafx.graphics;

}
