package it.polito.mad.team12.restaurantmanager;

public class ReservationDetails {

    private String customerName;
    private String customerPhoneNumber;
    private String orderedItems;
    private String timeDate;
    private String notes;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(String orderedItems) {
        this.orderedItems = orderedItems;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

