package it.polito.mad.team12.restaurantmanager.review;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Antonio on 10/04/16.
 */
public class Review {
    String Title;
    String text;
    Float stars;
    String userID;
    String RestaurantID;
    Date dataReview;
    String reply;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getStars() {
        return stars;
    }

    public void setStars(Float stars) {
        this.stars = stars;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        RestaurantID = restaurantID;
    }

    public String getDataReview() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return sdf.format(dataReview);
    }

    public void setDataReview(Date dataReview) {
        this.dataReview = dataReview;
    }

    public String getReply() {
        return this.reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
