package controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.JDBCUtil;
import model.User;
import model.data;
import view.Home;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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

    public  ScrollPane friend_scene;
    public VBox messenger_scene;

    public TextArea messenger;

    public HomeController() throws IOException {
    }


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
            FXMLLoader loader = new FXMLLoader(HomeController.class.getResource("/view/friend.fxml"));

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
                loader = new FXMLLoader(HomeController.class.getResource("/view/friend.fxml"));
            }

            ScrollPane scrollPane = new ScrollPane(tilePane);
            friend_scene.setContent(scrollPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<User> getfriend() throws SQLException {
        FriendController controllerProduct = new FriendController();
        return controllerProduct.getUser();
    }

    public void setHomePage(Home home) {
        this.homepage = home;
    }

    public void send(MouseEvent mouseEvent) throws Exception {
        // Tạo một label để đại diện cho ô tin nhắn mới
        Label newMessage = new Label(messenger.getText());

        // Thiết lập căn lề và giao diện của label
        newMessage.setPadding(new Insets(10));
        messenger_scene.setSpacing(20);
        messenger_scene.setPadding(new Insets(5));
        newMessage.setBackground(new Background(new BackgroundFill(Color.AQUA, new CornerRadii(20), null)));
        newMessage.setTextFill(Color.WHITE); // Thiết lập màu chữ là màu trắng
        newMessage.setFont(Font.font(15)); // Thiết lập font là cỡ chữ 14
        messenger_scene.setAlignment(Pos.TOP_RIGHT);


        // Đặt id để có thể tìm kiếm trong VBox
        newMessage.setId("message");
        VBox messengerScene = (VBox) scene_chat.lookup("#messenger_scene");
        // Thêm label vào VBox
        messengerScene.getChildren().add(newMessage);

        // Gửi tin nhắn đến server
        sendMessage(messenger.getText());
    }


    //CLIENT------------------------------------------------------
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;

    public class Client {
        public Socket socket = new Socket(SERVER_ADDRESS, PORT);
        private BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        private  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        public Client() throws Exception {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());


            new Thread(() -> {
                while (true) {
                    try {
                        String message = reader.readLine();
                        if (message != null) {
                            System.out.println(message);
                        } else {
                            System.out.println("Server disconnected");
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            System.out.println("Enter your username: ");
            String username = null;
            out.writeBytes(username + "\n");
            while (true) {
                System.out.println("Enter username to send");
                String usernameToSend = null;
                System.out.println("Enter message to send");
                String message = null;
                out.writeBytes(usernameToSend + "\n");
                out.writeBytes(username+": "+message + "\n");
                out.flush();
            }
        }
    }




    class ClientHandler extends Thread {
        private String username;
        private BufferedReader in;
        private DataOutputStream out;
        private HashMap<String,ClientHandler> clients;
        public ClientHandler(String username, BufferedReader in, DataOutputStream out, HashMap<String,ClientHandler> clients) {
            this.in = in;
            this.out = out;
            this.clients = clients;
            clients.put(username,this);
            start();
        }

        public void send(String message) {
            try {
                out.writeBytes(message + "\n");
                out.flush(); // Ensure data is sent immediately
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendTo(String username, String message) {
            ClientHandler client = clients.get(username);
            if (client != null) {
                client.send(message);
            }
        }

        public void run() {
            while (true) {
                try {
                    String username = in.readLine();
                    String message = in.readLine();
                    if (message == null) { // Client disconnected
                        break;
                    }
                    System.out.println(message);
                    sendTo(username,message);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


//    public void sendMessage(String message) {
//        try {
//            writer.write(message);
//            writer.newLine();
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void receiveMessage() throws IOException {
        Label receiveMessage = new Label(reader.readLine());
        System.out.println(reader.readLine());

        // Thiết lập căn lề và giao diện của label
        receiveMessage.setPadding(new Insets(10));
        messenger_scene.setSpacing(20);
        messenger_scene.setPadding(new Insets(5));
        receiveMessage.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(20), null)));
        receiveMessage.setTextFill(Color.WHITE); // Thiết lập màu chữ là màu trắng
        receiveMessage.setFont(Font.font(15)); // Thiết lập font là cỡ chữ 14
        messenger_scene.setAlignment(Pos.TOP_LEFT);


        // Đặt id để có thể tìm kiếm trong VBox
        receiveMessage.setId("message");
        VBox messengerScene = (VBox) scene_chat.lookup("#messenger_scene");
        // Thêm label vào VBox
        messengerScene.getChildren().add(receiveMessage);
    }



//        public void disconnect() {
//            try {
//                if (socket != null) {
//                    socket.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

}
