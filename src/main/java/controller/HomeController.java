package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.JDBCUtil;
import model.User;
import model.data;
import view.Home;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static controller.LoginController.encodePassword;
import static view.Home.stageHomePage;

public class HomeController {
    public static Home homepage;
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

    public ScrollPane friend_scene;


    public void onchat(MouseEvent mouseEvent) throws Exception {
        scene_chat.setVisible(true);
        scene_account.setVisible(false);
        scene_group.setVisible(false);
        scene_people.setVisible(false);
        scene_setting.setVisible(false);
        FriendScene();
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

    public void onpeople(MouseEvent mouseEvent) throws Exception {
        scene_chat.setVisible(false);
        scene_account.setVisible(false);
        scene_group.setVisible(false);
        scene_people.setVisible(true);
        scene_setting.setVisible(false);


    }

    public void FriendScene() throws Exception {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/friend.fxml"));

                List<User> modelProducts = getfriend();

                TilePane tilePane = new TilePane();
                tilePane.setPrefColumns(1);  // Đặt số cột
                tilePane.setHgap(10);  // Đặt khoảng cách ngang giữa các nút
                tilePane.setVgap(20);  // Đặt khoảng cách dọc giữa các nút

                for (User productView : modelProducts) {
                    // Tải một phiên bản mới của view FXML và lấy controller của nó
                    Node productNode = loader.load();
                    FriendController productController = loader.getController();

                    // Thiết lập dữ liệu cho controller
                    productController.setData(productView);

                    // Thêm node đã tải vào TilePane
                    tilePane.getChildren().add(productNode);

                    // Tạo một loader mới để sử dụng cho sản phẩm tiếp theo
                    loader = new FXMLLoader(getClass().getResource("/view/friend.fxml"));
                }

                ScrollPane scrollPane = new ScrollPane(tilePane);
                friend_scene.setContent(scrollPane);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public List<User> getfriend() throws SQLException {
        FriendController controllerProduct = new FriendController();
        return controllerProduct.getUser();
    }
    public void setHomePage(Home home) {
        this.homepage = home;
    }

}
