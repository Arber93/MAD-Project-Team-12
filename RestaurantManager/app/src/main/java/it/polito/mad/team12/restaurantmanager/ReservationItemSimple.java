package it.polito.mad.team12.restaurantmanager;

/**
 * Created by Pal on 08-May-16.
 */
public class ReservationItemSimple {
    private String name;
    private String quantity;

    public ReservationItemSimple(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
