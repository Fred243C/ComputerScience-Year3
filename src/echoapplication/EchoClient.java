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

        try {
            Socket soc = new Socket("localhost", 8080);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            System.out.println("Server connected, Enter a text");

            String str = userInput.readLine();
            boolean running = true;

            out.println(str);

            System.out.println(in.readLine());

            while (true) {
                System.out.println("Client:");
                str = userInput.readLine();

                //Sent message to server 
                out.println(str);

                //If client sends "Stop", end connection
                if (str.equalsIgnoreCase("STOP")) {
                    System.out.println("Waiting for server response...");
                    String serverResponse = in.readLine();
                    if (serverResponse != null && serverResponse.equalsIgnoreCase("TERMINATE")) {
                        System.out.println("Server Terminated" + serverResponse);
                        System.out.println("Server connection closed.");
                        running = false;
                    }

                } else {
                    // Receive and print the server response
                    String serverResponse = in.readLine();

                    System.out.println("Server response: " + serverResponse);

                }

            }

        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }
}
