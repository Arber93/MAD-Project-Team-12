package it.polito.mad.team12.restaurantmanager.review;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.TreeMap;

import it.polito.mad.team12.restaurantmanager.Restaurant;
import it.polito.mad.team12.restaurantmanager.Utility;

/**
 * Created by Antonio on 10/04/16.
 */
public class ReviewUtility {

    public static TreeMap<String, Review> reviewForRestaurant = new TreeMap<>();
    public static TreeMap<String, Review> reviewForUser = new TreeMap<>();
    //public static TreeMap<String, ArrayList<Review>> reviewForRestaurant = new TreeMap<>();
    public static TreeMap<String, Restaurant> restaurant = new TreeMap<>();

    public static ArrayList<Review> getReviews(String restaurantID) {
        return new ArrayList<>(reviewForRestaurant.values());//get(restaurantID);
    }

    public static void removeReview(String reviewID){
        reviewForUser.remove(reviewID);
    }

    public static ArrayList<Review> getReviewsForUser(String userID) {
        return new ArrayList<>(reviewForUser.values());//get(restaurantID);
    }

    public static Review getReview(String reviewID){
        return reviewForRestaurant.get(reviewID);
    }

    public static Review getReviewForUser(String reviewID){
        return reviewForUser.get(reviewID);
    }

    public static Float getStarsRestaurant(String restaurantID) {
        Float f = 0f;
        for(Review r : reviewForRestaurant.values())
            f+=Float.parseFloat(r.getStars());
        return f/reviewForRestaurant.size();
    }

    public static Integer numberOfReviews(String restaurantID) {
        return reviewForRestaurant.size();
    }

    public static Integer numberOfReviewsForUser(String restaurantID) {
        return reviewForUser.size();
    }

    public static String getImageName(String restaurantID) {
        return restaurant.get(restaurantID)==null?"":restaurant.get(restaurantID).getimageName();
    }

    public static void clear(){
        reviewForRestaurant = new TreeMap<>();
        restaurant = new TreeMap<>();
    }

    public static void loadReviewsForUser(final String userID){

        Firebase myFirebaseRef = new Firebase("https://popping-inferno-6667.firebaseio.com/reviews");

        myFirebaseRef.child("reviews").orderByChild("userID").equalTo(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Review r = dataSnapshot.getValue(Review.class);
                r.setReviewID(dataSnapshot.getKey());
                reviewForUser.put(r.getReviewID(), r);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Review r = dataSnapshot.getValue(Review.class);
                r.setReviewID(dataSnapshot.getKey());
                reviewForUser.put(r.getReviewID(), r);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Review r = dataSnapshot.getValue(Review.class);
                r.setReviewID(dataSnapshot.getKey());
                reviewForUser.remove(r.getReviewID());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void loadReviews(final String restaurantID){

        Firebase myFirebaseRef = Utility.getFirebaseReviewsRef();

        myFirebaseRef.child("reviews").orderByChild("restaurantID").equalTo(restaurantID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Review r = dataSnapshot.getValue(Review.class);
                r.setReviewID(dataSnapshot.getKey());
                reviewForRestaurant.put(r.getReviewID(), r);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Review r = dataSnapshot.getValue(Review.class);
                r.setReviewID(dataSnapshot.getKey());
                reviewForRestaurant.put(r.getReviewID(), r);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Review r = dataSnapshot.getValue(Review.class);
                r.setReviewID(dataSnapshot.getKey());
                reviewForRestaurant.remove(r.getReviewID());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void loadRestaurant(final String restaurantID){

        Firebase myFirebaseRef = Utility.getFirebaseReviewsRef();

        myFirebaseRef.child("Restaurant").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " Restaurant");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Restaurant r = postSnapshot.getValue(Restaurant.class);
                    if(r.getRestaurantID().equals(restaurantID)) {
                        System.out.println(r.getRestaurantID() + " - " + r.getStars());
                        restaurant.put(r.getRestaurantID(),r);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    /*public static void loadJSONFromAsset(InputStream is) {

        String sJson = "";

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy",Locale.ITALIAN);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            sJson = new String(buffer, "UTF-8");

            reviewForRestaurant = new TreeMap<>();
            restaurant = new TreeMap<>();

            JSONObject jsonRootObject = new JSONObject(sJson);

            JSONArray jsonArray = jsonRootObject.optJSONArray("reviews");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String restaurantID = jsonObject.optString("restaurantID").toString();
                String userID = jsonObject.optString("userID").toString();
                String data = jsonObject.optString("dataReview").toString();
                String title = jsonObject.optString("title").toString();
                String text = jsonObject.optString("text").toString();
                String stars = jsonObject.optString("stars").toString();
                String reply = jsonObject.optString("reply").toString();

                Review r = new Review();
                r.setTitle(title);
                r.setText(text);
                r.setDataReview(data);
                r.setUserID(userID);
                r.setRestaurantID(restaurantID);
                r.setStars(stars);
                r.setReply(reply);
                ArrayList<Review> mReviews = reviewForRestaurant.get(restaurantID);
                if (mReviews == null)
                    mReviews = new ArrayList<>();
                mReviews.add(r);
                reviewForRestaurant.put(restaurantID, mReviews);
                Log.d("***", r.getReply());
            }

            jsonArray = jsonRootObject.optJSONArray("Restaurant");


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String restaurantID = jsonObject.optString("restaurantID").toString();
                String stars = jsonObject.optString("stars").toString();
                String imageName = jsonObject.optString("imageName").toString();

                Restaurant rest = new Restaurant();
                rest.setRestaurantID(restaurantID);
                rest.setStars(stars);
                rest.setimageName(imageName);

                restaurant.put(restaurantID, rest);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

}
