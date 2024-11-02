/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package echoapplication;

/**
 *
 * @author alexb
 */
public class Event {

    private int eventId;
    private String title;
    private String date;
    private String location;
    private static int idCounter = 1; // Generate unique IDs for each event

    public Event(int eventId, String title, String date, String location) {
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.location = location;
    }

    public int getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    @Override
    public String toString() {
        return "Event{" + "eventId=" + eventId + ", title=" + title + ", date=" + date + ", location=" + location + '}';
    }

}
