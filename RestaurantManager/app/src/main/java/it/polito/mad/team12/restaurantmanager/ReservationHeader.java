package it.polito.mad.team12.restaurantmanager;

import java.util.ArrayList;

public class ReservationHeader {
    private String header;
    private ArrayList<ReservationDetails> details;

    public String getHeader() {
        return header;
    }

    public void setHeader(String name) {
        this.header = name;
    }

    public ArrayList<ReservationDetails> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<ReservationDetails> Details) {
        this.details = Details;
    }
    public ArrayList<ReservationDetails> getItems() {
        return details;
    }

}
