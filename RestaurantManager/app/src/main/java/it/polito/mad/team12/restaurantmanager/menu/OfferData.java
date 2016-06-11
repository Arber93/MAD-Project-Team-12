package it.polito.mad.team12.restaurantmanager.menu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class OfferData extends ItemData {
    public static final String OFFERS = "offers";

    private boolean offeredMonday;
    private boolean offeredTuesday;
    private boolean offeredWednesday;
    private boolean offeredThursday;
    private boolean offeredFriday;
    private boolean offeredSaturday;
    private boolean offeredSunday;

    public OfferData() {
        super();
        setCategory(OFFERS);
    }

    public boolean isOfferedMonday() {
        return offeredMonday;
    }

    public boolean isOfferedTuesday() {
        return offeredTuesday;
    }

    public boolean isOfferedWednesday() {
        return offeredWednesday;
    }

    public boolean isOfferedThursday() {
        return offeredThursday;
    }

    public boolean isOfferedFriday() {
        return offeredFriday;
    }

    public boolean isOfferedSaturday() {
        return offeredSaturday;
    }

    public boolean isOfferedSunday() {
        return offeredSunday;
    }

    public void setOfferedMonday(boolean offered) {
        this.offeredMonday = offered;
    }

    public void setOfferedTuesday(boolean offered) {
        this.offeredTuesday = offered;
    }

    public void setOfferedWednesday(boolean offered) {
        this.offeredWednesday = offered;
    }

    public void setOfferedThursday(boolean offered) {
        this.offeredThursday = offered;
    }

    public void setOfferedFriday(boolean offered) {
        this.offeredFriday = offered;
    }

    public void setOfferedSaturday(boolean offered) {
        this.offeredSaturday = offered;
    }

    public void setOfferedSunday(boolean offered) {
        this.offeredSunday = offered;
    }
}
