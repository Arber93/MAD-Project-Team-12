package it.polito.mad.team12.restaurantmanager;


import android.net.Uri;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class RestaurantMenuItem implements Comparable<RestaurantMenuItem> {
    private Uri imageUri;
    private String name;
    private String description;
    private BigDecimal price;

    /*
     *  If characteristics like the following increase in number than an
     * integer with bitwise operations could be used to save space.
     */
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;

    /*
     *  The field counter contains the number of times an item has been served,
     * which is handled by the Reservations part.
     */
    private int counter;
    /*
     *   This field is used to indicate whether this item can be prepared
     * by the restaurant at this time.
     */
    private boolean available;


    public RestaurantMenuItem () {
        counter = 0;
        available = true;
        vegetarian = false;
        vegan = false;
        glutenFree = false;
        imageUri = null;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void addToCounter(int number) {
        this.counter += number;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.HALF_EVEN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public int compareTo(RestaurantMenuItem another) {

        if(this.counter == another.counter) {
            return this.name.compareTo(another.name);
        }

        return another.counter - this.counter;
    }

    /*
     *  The equals method intentionally doesn't include counter related information,
     * because an item in a potential set should only exist once for a given name.
     */
    public boolean equals(Object other) {
        RestaurantMenuItem another;

        if(other instanceof RestaurantMenuItem) {
            another = (RestaurantMenuItem) other;
            return this.name.equals(another.name);
        }

        return false;
    }

    public int hashCode(){
        return this.name.hashCode();
    }
}
