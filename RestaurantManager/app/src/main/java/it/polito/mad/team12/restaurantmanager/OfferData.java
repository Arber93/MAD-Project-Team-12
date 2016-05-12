package it.polito.mad.team12.restaurantmanager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OfferData extends ItemData {
    private final static int REGULAR = 128; // indicates the schedule is permanent

    /*
     *    The following values represent the bit values associated to each day of the week.
     *  These will be used to store availability periods in a more compact way. If, e.g., the manager
     *  wanted to define a permanent offer on Mondays, Tuesday and Fridays the value inside
     */
    private final static int MONDAY = 64;
    private final static int TUESDAY = 32;
    private final static int WEDNESDAY = 16;
    private final static int THURSDAY = 8;
    private final static int FRIDAY = 4;
    private final static int SATURDAY = 2;
    private final static int SUNDAY = 1;

    private int offeredOn;
    private Map<String, Boolean> offers;

    public OfferData() {
        super();
        offeredOn = 0;  // this means the offer is never valid
        offers = new HashMap<>();
    }

    public boolean offeredMonday() {
        return (offeredOn & MONDAY) != 0;
    }

    public boolean offeredTuesday() {
        return (offeredOn & TUESDAY) != 0;
    }

    public boolean offeredWednesday() {
        return (offeredOn & WEDNESDAY) != 0;
    }

    public boolean offeredThursday() {
        return (offeredOn & THURSDAY) != 0;
    }

    public boolean offeredFriday() {
        return (offeredOn & FRIDAY) != 0;
    }

    public boolean offeredSaturday() {
        return (offeredOn & SATURDAY) != 0;
    }

    public boolean offeredSunday() {
        return (offeredOn & SUNDAY) != 0;
    }

    public boolean offeredRegularly() {
        return (offeredOn & REGULAR) != 0;
    }

    public int getOfferedOn() {
        return offeredOn;
    }

    public void setOfferedOn(int offeredOn) {
        this.offeredOn = offeredOn;
    }

    public Map<String, Boolean> getOffers() {
        return offers;
    }

    public void setOffers(Map<String, Boolean> offers) {
        this.offers = offers;
    }
}
