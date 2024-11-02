/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package echoapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author alexb
 */
public class EchoClient {

    private static String HOST = "localhost";
    private static int PORT = 8080;

    public static void main(String args[]) {
        System.out.println("Client started");
        try {
            Socket soc = new Socket("localhost", 8080);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter a text");
            String str = userInput.readLine();
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            out.println(str);
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            System.out.println(in.readLine());

            boolean running = true;
            

            while (running) {
                System.out.println("Client:");
                str = userInput.readLine();

                //Sent message to server 
                out.println(str);

                //If client sends "Stop", end connection
                if (str.equalsIgnoreCase("STOP")) {
                    System.out.println("Waiting for server response...");
                    String serverResponse = in.readLine();
                    if ("TERMINATE".equalsIgnoreCase(serverResponse));
                    System.out.println("Server response:" + serverResponse);
                    System.out.println("Server close connection.");
                    running = false;
                } else {
                    // Receive and print the server response
                    String serverResponse = in.readLine();
                    if (serverResponse.startsWith("Error:")) {
                        System.out.println("Server says: " + serverResponse);
                    } else {
                        System.out.println("Message:" + serverResponse);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}
