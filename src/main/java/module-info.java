module ChatATC{
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires java.sql;
	requires javafx.base;
    requires mysql.connector.j;
    requires java.mail;



    opens view to javafx.graphics, javafx.fxml, javafx.controls;
	opens controller to javafx.controls, javafx.fxml, javafx.graphics;

}
