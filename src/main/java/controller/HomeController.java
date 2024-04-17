package controller;

import javafx.application.Platform;
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

    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12345;
    public VBox messenger_scene1;
    public ImageView avatarAccount;
    public Label nameAccount;
    public Label nameFriend;
    public ImageView avatarFriend;
    public Label nameFriend1;
    public ImageView avatarFriend1;
    public AnchorPane informationfriend;
    public AnchorPane chatscene;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ScrollPane friend_scene;
    public VBox messenger_scene;

    public TextArea messenger;

    public HomeController() throws IOException {
        connect();
        listenToServer();
    }


    public void onchat(MouseEvent mouseEvent) throws Exception {
        scene_chat.setVisible(true);
        scene_account.setVisible(false);
        scene_group.setVisible(false);
        scene_people.setVisible(false);
        scene_setting.setVisible(false);
        FriendScene();
        nameAccount.setText(LoginController.nameAccount);
        avatarAccount.setImage(FriendController.urlToImage(LoginController.urlAvatar)) ;

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
        sendMessage(FriendController.name, messenger.getText());
    }

public  void setavatarandnamefriend(){
        nameFriend.setText(FriendController.name);
        avatarFriend.setImage(FriendController.frientavatar);
    nameFriend1.setText(FriendController.name);
    avatarFriend1.setImage(FriendController.frientavatar);
    chatscene.setVisible(true);
}
        public void connect() {
            try {
                // Kết nối tới server
                socket = new Socket(SERVER_ADDRESS, PORT);
                System.out.println("Đã kết nối tới server");

                // Khởi tạo outputStream và inputStream trong các luồng riêng biệt
                Thread outputThread = new Thread(() -> {
                    try {
                        outputStream = new ObjectOutputStream(socket.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                outputThread.start();

                Thread inputThread = new Thread(() -> {
                    try {
                        inputStream = new ObjectInputStream(socket.getInputStream());
                        listenToServer(); // Bắt đầu lắng nghe dữ liệu từ server sau khi khởi tạo inputStream
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                inputThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public void sendMessage(String recipient, String message) {
        try {
            // Tạo HashMap chứa thông tin tin nhắn
            HashMap<String, String> messageMap = new HashMap<>();
            messageMap.put("recipient", recipient);
            messageMap.put("message", message);

            // Gửi HashMap tới server
            outputStream.writeObject(messageMap);
            outputStream.flush();
            System.out.println("Đã gửi tin nhắn tới server: " + messageMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenToServer() {
        Thread listenThread = new Thread(() -> {
            try {
                while (true) {
                    // Nhận HashMap từ server
                    HashMap<String, String> messageMap = (HashMap<String, String>) inputStream.readObject();

                    // Xử lý tin nhắn nhận được từ server
                    String recipient = messageMap.get("recipient");
                    if (recipient != null &&recipient.equals(LoginController.nameAccount)) {
                        // Hiển thị tin nhắn cho người dùng
                        String message = messageMap.get("message");
                        System.out.println("Nhận được tin nhắn từ server: " + message);
                        recivice(messageMap.get("message"));
                    }
                    System.out.println(LoginController.nameAccount);
                    System.out.println("hashmap from listen:"+ messageMap);

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        listenThread.start();
    }
    public void recivice(String messengerr) throws Exception {
        Platform.runLater(() -> {
            // Tạo một label để đại diện cho ô tin nhắn mới
            Label newMessage = new Label(messengerr);

            // Thiết lập căn lề và giao diện của label
            newMessage.setPadding(new Insets(10));
            messenger_scene1.setSpacing(20);
            messenger_scene1.setPadding(new Insets(5));
            newMessage.setBackground(new Background(new BackgroundFill(Color.GRAY, new CornerRadii(20), null)));
            newMessage.setTextFill(Color.WHITE); // Thiết lập màu chữ là màu trắng
            newMessage.setFont(Font.font(15)); // Thiết lập font là cỡ chữ 14
            messenger_scene1.setAlignment(Pos.TOP_LEFT);

            // Đặt id để có thể tìm kiếm trong VBox
            newMessage.setId("message1");
            VBox messengerScene = (VBox) scene_chat.lookup("#messenger_scene1");
            // Thêm label vào VBox
            messengerScene.getChildren().add(newMessage);
        });
    }


    public void exitclick(MouseEvent mouseEvent) {
        chatscene.setVisible(false);
        informationfriend.setVisible(false);
    }

    public void informationclick(MouseEvent mouseEvent) {
        informationfriend.setVisible(true);
    }
}