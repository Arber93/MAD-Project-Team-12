package it.polito.mad.team12.restaurantmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RestaurantMenuOffer implements Comparable<RestaurantMenuOffer> {
    OfferData offerData;
    List<RestaurantMenuItem> items;

    public RestaurantMenuOffer() {
        offerData = new OfferData();
        items = new ArrayList<>();
    }

    public void addItem(RestaurantMenuItem item) {
        items.add(item);
    }

    public List<RestaurantMenuItem> getItems() {
        return items;
    }

    public void setItems(Collection<? extends RestaurantMenuItem> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public void addItems(Collection<? extends RestaurantMenuItem> items) {
        this.addItems(items);
    }

    public OfferData getOfferData() {
        return offerData;
    }

    public void setOfferData(OfferData offerData) {
        this.offerData = offerData;
    }

    @Override
    public int compareTo(RestaurantMenuOffer another) {
        return this.offerData.compareTo(another.offerData);
    }

    @Override
    public boolean equals(Object other) {
        RestaurantMenuOffer another;

        if(other instanceof RestaurantMenuOffer) {
            another = (RestaurantMenuOffer) other;
            return this.offerData.getName().equals(another.offerData.getName());
        }

        return false;
    }

    public int hashCode(){
        return this.offerData.getName().hashCode();
    }
}
