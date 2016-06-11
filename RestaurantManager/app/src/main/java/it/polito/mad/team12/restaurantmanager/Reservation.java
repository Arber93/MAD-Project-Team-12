package it.polito.mad.team12.restaurantmanager;
import java.util.Map;

public class Reservation {
    private String reservationID;
    private Map<String,String> items;
    private String dateTime;
    private String notes;
    private String senderID;
    private String receiverID;

    public Reservation() {
    }

    public Reservation(String reservationID, Map<String, String> items, String dateTime, String notes, String senderID, String receiverID) {
        this.reservationID = reservationID;
        this.items = items;
        this.dateTime = dateTime;
        this.notes = notes;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public Map<String, String> getItems() {
        return items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }
}

