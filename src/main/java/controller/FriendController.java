package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.JDBCUtil;
import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendController {



    public static String name;
    public ImageView friends_avatar;
    public  Label friends_name;


    public List<User> getUser() throws SQLException {
        List<User> datalist = new ArrayList<>();

        String friendQuery = "SELECT name, avatar FROM user WHERE name != ?";

        Connection connect = JDBCUtil.getConnection();
        PreparedStatement prepare = connect.prepareStatement(friendQuery);
        prepare.setString(1, LoginController.nameAccount);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            User user = new User();

        user.setname(result.getString("name"));
        name = result.getString("name");
            System.out.println(name);

        user.setimage(result.getString("avatar"));

        datalist.add(user);
        }
    return datalist;
    }
    public User user1;

    public void setData(User user) {
        friends_name.setText(user.getname());
        friends_avatar.setImage(urlToImage(user.getimage()));
       user1 = user;
    }

    public static Image urlToImage(String imagePath) {
        try {
            File file = new File(imagePath);
            String localUrl = file.toURI().toURL().toString();
            Image image = new Image(localUrl);
            return image;
        } catch (Exception e) {
            // Xử lý nếu không thể tạo được Image từ URL
            e.printStackTrace();
            return null;
        }
    }

    public static Image frientavatar;
    public  void connectSocketclick(MouseEvent mouseEvent) throws IOException {
        name = friends_name.getText();
        System.out.println(name);
        frientavatar = urlToImage(user1.getimage());
        HomeController homeController = new HomeController();
        homeController.setavatarandnamefriend(); // Gọi phương thức setavatarandnamefriend() để khởi tạo nameFriend và avatarFriend
        homeController.nameFriend.setText(name);
        homeController.avatarFriend.setImage(frientavatar);
        homeController.nameFriend1.setText(name);
        homeController.avatarFriend1.setImage(frientavatar);
        homeController.chatscene.setVisible(true);
    }


}

