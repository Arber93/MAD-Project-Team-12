package it.polito.mad.team12.restaurantmanager.review;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;

import it.polito.mad.team12.restaurantmanager.Restaurant;

/**
 * Created by Antonio on 10/04/16.
 */
public class ReviewUtility {

    public static TreeMap<String, ArrayList<Review>> reviewForRestaurant = new TreeMap<>();
    public static TreeMap<String, Restaurant> restaurant = new TreeMap<>();

    public static ArrayList<Review> getReviews(String restaurantID) {
        return reviewForRestaurant.get(restaurantID);
    }

    public static Float getStarsRestaurant(String restaurantID) {
        return restaurant.get(restaurantID).getStars();
    }

    public static Integer numberOfReviews(String restaurantID) {
        return reviewForRestaurant.get(restaurantID).size();
    }

    public static String getImageName(String restaurantID) {
        return restaurant.get(restaurantID).getimageName();
    }

    public static void clear(){
        reviewForRestaurant = new TreeMap<>();
        restaurant = new TreeMap<>();
    }

    public static void loadJSONFromAsset(InputStream is) {

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
                Float stars = Float.parseFloat(jsonObject.optString("stars").toString());
                String reply = jsonObject.optString("reply").toString();

                Review r = new Review();
                r.setTitle(title);
                r.setText(text);
                r.setDataReview(sdf.parse(data));
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
                Float stars = Float.parseFloat(jsonObject.optString("stars").toString());
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

    }

}
