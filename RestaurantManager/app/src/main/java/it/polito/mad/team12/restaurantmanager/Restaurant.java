package it.polito.mad.team12.restaurantmanager;

/**
 * Created by Antonio on 10/04/16.
 */
public class Restaurant {

    String restaurantID;
    Float stars;
    String imageName;

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public String getimageName() {
        return imageName;
    }

    public void setimageName(String imageName) {
        this.imageName = imageName;
    }
}
