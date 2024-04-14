package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.JDBCUtil;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendController {

    public ImageView frient_avatar;
    public Label friend_name;
    public Button friend_addfriend;
    User user = new User();
    public List<User> getUser() throws SQLException {
        List<User> datalist = new ArrayList<>();

        String friendQuery = "SELECT name, avatar FROM user WHERE name != ?";
        Connection connect = JDBCUtil.getConnection();
        PreparedStatement prepare = connect.prepareStatement(friendQuery);
        prepare.setString(1, LoginController.nameAccount);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
        user.setname(result.getString("name"));
            System.out.println(result.getString("name"));
        user.setimage(result.getString("avatar"));
            System.out.println(result.getString("avatar"));
        }
    return datalist;
    }
    public void setData(User user){
        friend_name.setText(user.getname());
        frient_avatar.setImage(urlToImage(user.getimage()));
    }
    public static Image urlToImage(String imageUrl) {
        try {
            Image image = new Image(imageUrl);
            return image;
        } catch (Exception e) {
            // Xử lý nếu không thể tạo được Image từ URL
            e.printStackTrace();
            return null;
        }
    }
}
