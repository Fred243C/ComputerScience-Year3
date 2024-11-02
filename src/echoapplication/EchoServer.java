/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package echoapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.System.in;
import static java.lang.System.out;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alexb
 */
public class EchoServer {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("Waiting for clients..." + PORT);
        try {
            ServerSocket ss = new ServerSocket(PORT);
            while (true) {

                Socket soc = ss.accept();
                System.out.println("Connection established" + soc.getInetAddress());

                //Handle client in a new thread 
                Thread one = new Thread(new Threadclient(soc));
                one.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class Threadclient implements Runnable {

    private Socket soc;
    private static List<Event> events = new ArrayList<>();

    public Threadclient(Socket socket) {
        this.soc = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream())); PrintWriter out = new PrintWriter(soc.getOutputStream(), true)) {

            String clientMessage;
            boolean running = true;

            while (running && (clientMessage = in.readLine()) != null) {
                System.out.println("Client message:" + clientMessage);
                try {
                    validateAction(clientMessage);
                    if (clientMessage.equalsIgnoreCase("STOP")) {
                        out.println("TERMINATE");
                        System.out.println("Closing connection.");
                        running = false;
                    } else {
                        String response = "Server response to: " + clientMessage;
                        out.println(response);
                        System.out.println("Sent: " + response);
                        break;
                    }

                    //Action and event details
                    String[] parts = clientMessage.split(":", 2);
                    if (parts.length < 2) {
                        System.out.println("Invalid format, Action expected: Event Details");
                        continue;
                    }
                    String action = parts[0].trim().toLowerCase();
                    String eventDetails = parts[1].trim();
                    String response = handleAction(action, eventDetails);
                    System.out.println(response);

                } catch (IncorrectActionException e) {
                    out.println("Error:" + e.getMessage());
                    System.out.println("IncorrectActionException:" + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                soc.close();
                in.close();
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to close client socket:" + e.getMessage());
            }

        }

    }

    private void validateAction(String action) throws IncorrectActionException {
        
        if (!action.matches(":")) {
            throw new IncorrectActionException("Invalid action provided");
        }
        
        String date = details[0].trim();
        String time = details[1].trim();
        String description = details[2].trim();
        
        validateDateTime(date,time);
        
    }
}
