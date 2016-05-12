package it.polito.mad.team12.restaurantmanager;


import java.math.BigDecimal;

public class RestaurantMenuItem implements Comparable<RestaurantMenuItem> {
    private ItemData itemData;
    /*
     *  The field counter contains the number of times an item has been served,
     * which is handled by the Reservations part.
     */
    private int counter;

    public RestaurantMenuItem() {
        itemData = new ItemData();
        counter = 0;
    }

    public RestaurantMenuItem(ItemData itemData) {
        this.itemData = itemData;
        counter = 0;
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

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData data) {
        this.itemData = data;
    }

    public boolean isAvailable() {
        return itemData.isAvailable();
    }

    public void setAvailable(boolean available) {
        itemData.setAvailable(available);
    }

    public boolean isGlutenFree() {
        return itemData.isGlutenFree();
    }

    public void setGlutenFree(boolean glutenFree) {
        itemData.setGlutenFree(glutenFree);
    }

    public String getName() {
        return itemData.getName();
    }

    public void setName(String name) {
        itemData.setName(name);
    }

    public String getDescription() {
        return itemData.getDescription();
    }

    public void setDescription(String description) {
        itemData.setDescription(description);
    }

    public BigDecimal getPrice() {
        return itemData.getPrice();
    }

    public void setPrice(BigDecimal price) {
        itemData.setPrice(price);
    }

    public boolean isVegetarian() {
        return itemData.isVegetarian();
    }

    public void setVegetarian(boolean vegetarian) {
        itemData.setVegetarian(vegetarian);
    }

    public boolean isVegan() {
        return itemData.isVegan();
    }

    public void setVegan(boolean vegan) {
        itemData.setVegan(vegan);
    }

    public boolean getHasImage() {
        return itemData.getHasImage();
    }

    public void setHasImage(boolean hasImage) {
        itemData.setHasImage(hasImage);
    }

    @Override
    public int compareTo(RestaurantMenuItem another) {

        if(this.counter == another.counter) {
            return this.itemData.compareTo(another.itemData);
        }

        return another.counter - this.counter;
    }

    /*
     *  The equals method intentionally doesn't include counter related information,
     * because an item in a potential set should only exist once for a given name.
     */
    @Override
    public boolean equals(Object other) {
        RestaurantMenuItem another;

        if(other instanceof RestaurantMenuItem) {
            another = (RestaurantMenuItem) other;
            return this.itemData.getName().equals(another.itemData.getName());
        }

        return false;
    }

    @Override
    public int hashCode(){
        return this.itemData.getName().hashCode();
    }
}
