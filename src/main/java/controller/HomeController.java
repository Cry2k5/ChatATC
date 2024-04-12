package controller;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class HomeController {
    public ImageView menu_chat;
    public ImageView menu_group;
    public ImageView menu_account;
    public ImageView menu_setting;
    public ImageView menu_people;
    public ImageView menu_logout;
    public AnchorPane scene_chat;
    public AnchorPane scene_group;
    public AnchorPane scene_people;
    public AnchorPane scene_setting;
    public AnchorPane scene_account;


    public void onchat(MouseEvent mouseEvent) {
        scene_chat.setVisible(true);
        scene_account.setVisible(false);
        scene_group.setVisible(false);
        scene_people.setVisible(false);
        scene_setting.setVisible(false);
    }

    public void ongroup(MouseEvent mouseEvent) {
        scene_chat.setVisible(false);
        scene_account.setVisible(false);
        scene_group.setVisible(true);
        scene_people.setVisible(false);
        scene_setting.setVisible(false);
    }

    public void onaccount(MouseEvent mouseEvent) {
        scene_chat.setVisible(false);
        scene_account.setVisible(true);
        scene_group.setVisible(false);
        scene_people.setVisible(false);
        scene_setting.setVisible(false);
    }

    public void logout(MouseEvent mouseEvent) {

    }

    public void onsetting(MouseEvent mouseEvent) {
        scene_chat.setVisible(false);
        scene_account.setVisible(false);
        scene_group.setVisible(false);
        scene_people.setVisible(false);
        scene_setting.setVisible(true);
    }

    public void onpeople(MouseEvent mouseEvent) {
        scene_chat.setVisible(false);
        scene_account.setVisible(false);
        scene_group.setVisible(false);
        scene_people.setVisible(true);
        scene_setting.setVisible(false);
    }
}
