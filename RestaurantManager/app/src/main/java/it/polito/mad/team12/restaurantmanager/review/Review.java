package it.polito.mad.team12.restaurantmanager.review;

/**
 * Created by Antonio on 10/04/16.
 */
public class Review {
    String reviewID;
    String title;
    String text;
    String stars;
    String userID;
    String restaurantID;
    String dataReview;
    String reply;
    String dataReply;

    public Review(){}

    public String getReviewID(){ return reviewID; }

    public void setReviewID(String reviewID){ this.reviewID =reviewID; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getDataReview() {
        return dataReview;
    }

    public void setDataReview(String dataReview) {
        this.dataReview = dataReview;
    }

    public String getReply() {
        return this.reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDataReply() {
        return this.dataReply;
    }

    public void setDataReply(String dataReply) {
        this.dataReply = dataReply;
    }
}
