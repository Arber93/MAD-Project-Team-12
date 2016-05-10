package it.polito.mad.team12.restaurantmanager;

/**
 * Created by Antonio on 10/04/16.
 */
public class Restaurant {

    String restaurantID;
    String stars;
    String imageName;

    public Restaurant(){}

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getimageName() {
        return imageName;
    }

    public void setimageName(String imageName) {
        this.imageName = imageName;
    }
}
