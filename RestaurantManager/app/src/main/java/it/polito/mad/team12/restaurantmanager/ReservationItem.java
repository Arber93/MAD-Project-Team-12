package it.polito.mad.team12.restaurantmanager;

/**
 * Created by Pal on 06-May-16.
 */
public class ReservationItem {
    private String name;
    private String offerID;
    private String photo;

    public ReservationItem(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferID() {
        return offerID;
    }

    public void setOfferID(String offerID) {
        this.offerID = offerID;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
