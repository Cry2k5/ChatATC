package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.*;

import java.io.IOException;

public class Home extends Application {
    public static Stage stageHomePage;
    public Scene sceneHome;

    @Override
    public void start(Stage stageHomePage) throws IOException {
        this.stageHomePage = stageHomePage;

        FXMLLoader loaderhomepage = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent fxmlhome = loaderhomepage.load();

        HomeController controllerhome = loaderhomepage.getController();

        sceneHome = new Scene(fxmlhome, 1280, 700);
        sceneHome.getStylesheets().add(getClass().getResource("home.css").toExternalForm());

        controllerhome.setHomePage(this);
        stageHomePage.centerOnScreen();
        stageHomePage.setTitle("Home");
        stageHomePage.setScene(sceneHome);

        stageHomePage.show();
    }

}