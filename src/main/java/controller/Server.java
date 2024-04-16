package controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private static final int PORT = 12345;

    public Server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Đang chờ kết nối..........");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Đã kết nối với client " + clientSocket.getInetAddress());

            // Xử lý kết nối từ client trong một luồng riêng biệt
            Thread thread = new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                    // Đọc dữ liệu từ client
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    HashMap<String, String> receivedData = (HashMap<String, String>) objectInputStream.readObject();
                    System.out.println("Dữ liệu nhận được từ client: " + receivedData);

                    // Xử lý dữ liệu (ở đây, bạn có thể thêm logic xử lý dữ liệu nhận được)

                    // Tạo HashMap để gửi lại cho client
                    HashMap<String, String> responseData = new HashMap<>();
                    responseData.put("response", "Dữ liệu đã được xử lý thành công!");

                    // Gửi lại dữ liệu cho client
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.writeObject(responseData);
                    objectOutputStream.flush();
                    System.out.println("Đã gửi dữ liệu trả lại cho client");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}
