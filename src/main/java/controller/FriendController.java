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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FriendController {

    public ImageView frient_avatar;
    public Label friend_name;


    public static List<User> getUser() throws SQLException {
        List<User> datalist = new ArrayList<>();

        String friendQuery = "SELECT name, avatar FROM user WHERE name != ?";

        Connection connect = JDBCUtil.getConnection();
        PreparedStatement prepare = connect.prepareStatement(friendQuery);
        prepare.setString(1, LoginController.nameAccount);
        ResultSet result = prepare.executeQuery();

        while (result.next()) {
            User user = new User();

        user.setname(result.getString("name"));

        user.setimage(result.getString("avatar"));

        datalist.add(user);
        }
    return datalist;
    }

    public void setData(User user) {
        friend_name.setText(user.getname());
        frient_avatar.setImage(urlToImage(user.getimage()));
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


    public void connectSocketclick(MouseEvent mouseEvent) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(12345);
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received message from client: " + message);
                }

                in.close();
                clientSocket.close();
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 12345);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send message to server
                out.println("Hello ");

                out.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}

