package controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server{


    public static void main(String[] args) {
        final int PORT = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server đã khởi động và đang chờ kết nối...");

                Socket clientSocket = serverSocket.accept();
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected");
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                        String username = in.readLine();
                        ClientHandler clientHandler = new ClientHandler(username,in, out, clients);
                    }
                    catch (Exception e) {
                        socket.close();
                        e.printStackTrace();
                    }
                }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
