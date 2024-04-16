package controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private static final int PORT = 12345;
    private ArrayList<ObjectOutputStream> clientOutputStreams = new ArrayList<>();

    public Server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Đang chờ kết nối..........");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Đã kết nối với client " + clientSocket.getInetAddress());

            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            clientOutputStreams.add(outputStream);

            // Xử lý kết nối từ client trong một luồng riêng biệt
            Thread thread = new Thread(() -> {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    while (true) {
                        HashMap<String, String> receivedData = (HashMap<String, String>) objectInputStream.readObject();
                        System.out.println("Dữ liệu nhận được từ client: " + receivedData);

                        // Gửi tin nhắn đến tất cả các client khác
                        sendToOtherClients(receivedData, outputStream);
                    }
                } catch (IOException | ClassNotFoundException e) {
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

    private void sendToOtherClients(HashMap<String, String> message, ObjectOutputStream senderStream) {
        for (ObjectOutputStream outputStream : clientOutputStreams) {
            if (outputStream != senderStream) {
                try {
                    outputStream.writeObject(message);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }
}

