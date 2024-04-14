package controller;

import java.io.*;
import java.net.*;

public class hello {
    private String ipAddress;
    private int port;

    public hello(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public void start() {
        try {
            Socket socket = new Socket(ipAddress, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Gửi dữ liệu tới client khác
            writer.println("Hello, Client!");

            // Đọc dữ liệu từ client khác
            String message = reader.readLine();
            System.out.println("Received message from client: " + message);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String ipAddress = "127.0.0.1"; // Địa chỉ IP của client khác
        int port = 8080; // Cổng của client khác
        hello client = new hello(ipAddress, port);
        client.start();
    }
}
