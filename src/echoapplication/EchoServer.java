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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            System.out.println("Server exception:"+ e.getMessage());
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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream())); 
             PrintWriter out = new PrintWriter(soc.getOutputStream(), true)) {

            String clientMessage;
            boolean running = true;

            while (running && (clientMessage = in.readLine()) != null) {
                System.out.println("Client message:" + clientMessage);

                if (clientMessage.equalsIgnoreCase("STOP")) {
                    out.println("TERMINATE");
                    System.out.println("Closing connection.");
                    running= false;
                }else{
                    String response = "Server sentd to:"+clientMessage;
                    out.println(response);
                    System.out.println("Sent:"+response);
                }               

                try {
                    String response = validateAction(clientMessage);
                    out.println(response);

                } catch (IncorrectActionException e) {
                    out.println("Error:" + e.getMessage());
                    
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            try {
                soc.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to close client socket:" + e.getMessage());
            }

        }

    }

    private String validateAction(String clientMessage) throws IncorrectActionException {
        String[] parts = clientMessage.split(":", 2);
        if (parts.length < 2) {
            throw new IncorrectActionException("Invalid action provided");
        }

        String action = parts[0].toLowerCase();
        String eventDetails = parts[1];

        switch (action) {
            case "add":
                return addEvent(eventDetails);
            case "remove":
                return removeEvent(eventDetails);
            default:
                throw new IncorrectActionException("Action not recognised:" + action);

        }
    }
    //Add and Return event for the same date

    private String addEvent(String eventDetails) throws IncorrectActionException {
        try {
            String[] details = eventDetails.split(":", 4);
            String title = details[0].trim();
            String date = details[1].trim();
            String time = details[2].trim();
            String location = details[3].trim();

            Event event = new Event(title, date, time, location);
            events.add(event);

            return "Event added " + date + ":" + getEventsByDate(date);
        } catch (Exception e) {
            throw new IncorrectActionException("Invalid Event Format.");
        }
    }

    // Remove event from the list and return events for the same date
    private String removeEvent(String eventDetails) throws IncorrectActionException {
        try {
            String[] details = eventDetails.split(":", 4);
            String title = details[0].trim();
            String date = details[1].trim();
            String time = details[2].trim();
            String location = details[3].trim();

            Event eventToRemove = new Event(title, date, time, location);
            events.removeIf(event -> event.getTitle().equals(title) && event.getDate().equals(date) && event.getTime().equals(time) && event.getLocation().equals(location));
            return "Event removed " + date + ":" + getEventsByDate(date);
        } catch (Exception e) {
            throw new IncorrectActionException("Invalid Event Format");
        }
    }

    // Retrieve all events for a given date
    private String getEventsByDate(String date) {
        StringBuilder sb = new StringBuilder();
        for (Event event : events) {
            if (event.getDate().equals(date)) {
                sb.append(event.toString()).append("\n");
            }
        }
        return sb.length() > 0 ? sb.toString() : "No events for this date.";
    }

}
