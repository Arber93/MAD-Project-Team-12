package it.polito.mad.team12.restaurantmanager;





public class RestaurantDetails {

    private String restaurantName;
    private String restaurantAddress;
    private String telephone;
    private String mondayFrom, mondayTo;
    private String tuesdayFrom, tuesdayTo;
    private String wednesdayFrom, wednesdayTo;
    private String thursdayFrom, thursdayTo;
    private String fridayFrom, fridayTo;
    private String saturdayFrom, saturdayTo;
    private String sundayFrom, sundayTo;
    private String categoryFood, categoryFoodIT;
    private String restaurantLogo;
    private Double[] location;
    private boolean monclosed, tueclosed, wedclosed, thurclosed, friclosed, satclosed, sunclosed;

    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;


    public RestaurantDetails(){

    }

    public RestaurantDetails(String newer){
          vegan=false;
        vegetarian=false;
        glutenFree=false;
        monclosed=tueclosed=wedclosed= thurclosed=friclosed=satclosed=sunclosed = false;
        mondayFrom=mondayTo=telephone=tuesdayTo=tuesdayFrom=wednesdayFrom=wednesdayTo=thursdayTo=thursdayFrom=fridayFrom=fridayTo=saturdayTo=saturdayFrom=sundayFrom=sundayTo=" ";
        restaurantLogo="null";

    }


    public void setFridayFrom(String fridayFrom) {
        this.fridayFrom = fridayFrom;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public void setFridayTo(String fridayTo) {
        this.fridayTo = fridayTo;
    }

    public void setMondayFrom(String mondayFrom) {
        this.mondayFrom = mondayFrom;
    }

    public void setMondayTo(String mondayTo) {
        this.mondayTo = mondayTo;
    }

    public void setSaturdayFrom(String saturdayFrom) {
        this.saturdayFrom = saturdayFrom;
    }

    public void setSaturdayTo(String saturdayTo) {
        this.saturdayTo = saturdayTo;
    }

    public void setSundayFrom(String sundayFrom) {
        this.sundayFrom = sundayFrom;
    }

    public void setSundayTo(String sundayTo) {
        this.sundayTo = sundayTo;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setThursdayFrom(String thursdayFrom) {
        this.thursdayFrom = thursdayFrom;
    }

    public void setThursdayTo(String thursdayTo) {
        this.thursdayTo = thursdayTo;
    }

    public void setTuesdayFrom(String tuesdayFrom) {
        this.tuesdayFrom = tuesdayFrom;
    }

    public void setTuesdayTo(String tuesdayTo) {
        this.tuesdayTo = tuesdayTo;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public void setWednesdayFrom(String wednesdayFrom) {
        this.wednesdayFrom = wednesdayFrom;
    }

    public void setWednesdayTo(String wednesdayTo) {
        this.wednesdayTo = wednesdayTo;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public String getFridayFrom() {
        return fridayFrom;
    }

    public String getFridayTo() {
        return fridayTo;
    }

    public String getMondayFrom() {
        return mondayFrom;
    }

    public String getMondayTo() {
        return mondayTo;
    }

    public String getSaturdayFrom() {
        return saturdayFrom;
    }

    public String getSaturdayTo() {
        return saturdayTo;
    }

    public String getSundayFrom() {
        return sundayFrom;
    }

    public String getSundayTo() {
        return sundayTo;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getThursdayFrom() {
        return thursdayFrom;
    }

    public String getThursdayTo() {
        return thursdayTo;
    }

    public String getTuesdayFrom() {
        return tuesdayFrom;
    }

    public String getTuesdayTo() {
        return tuesdayTo;
    }

    public String getWednesdayFrom() {
        return wednesdayFrom;
    }

    public String getWednesdayTo() {
        return wednesdayTo;
    }

    public void setFriclosed(boolean friclosed) {
        this.friclosed = friclosed;
    }

    public void setMonclosed(boolean monclosed) {
        this.monclosed = monclosed;
    }

    public void setSatclosed(boolean satclosed) {
        this.satclosed = satclosed;
    }

    public void setSunclosed(boolean sunclosed) {
        this.sunclosed = sunclosed;
    }

    public void setThurclosed(boolean thurclosed) {
        this.thurclosed = thurclosed;
    }

    public void setTueclosed(boolean tueclosed) {
        this.tueclosed = tueclosed;
    }

    public void setWedclosed(boolean wedclosed) {
        this.wedclosed = wedclosed;
    }

    public boolean isFriclosed() {
        return friclosed;
    }

    public boolean isMonclosed() {
        return monclosed;
    }

    public boolean isSatclosed() {
        return satclosed;
    }

    public boolean isSunclosed() {
        return sunclosed;
    }

    public boolean isThurclosed() {
        return thurclosed;
    }

    public boolean isTueclosed() {
        return tueclosed;
    }

    public boolean isWedclosed() {
        return wedclosed;
    }


    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getCategoryFood() {
        return categoryFood;
    }

    public void setCategoryFood(String categoryFood) {
        this.categoryFood = categoryFood;
    }

    public String getRestaurantLogo() {
        return restaurantLogo;
    }

    public void setRestaurantLogo(String restaurantLogo) {
        this.restaurantLogo = restaurantLogo;
    }

    public String getCategoryFoodIT() {
        return categoryFoodIT;
    }

    public void setCategoryFoodIT(String categoryFoodIT) {
        this.categoryFoodIT = categoryFoodIT;
    }

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

}
