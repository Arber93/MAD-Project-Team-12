package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Pal on 07-May-16.
 */
public class ReservationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FIRST_COURSES = "first courses";
    public static final String SECOND_COURSES = "second courses";
    public static final String SIDE_DISHES = "side dishes";
    public static final String SANDWICHES = "sandwiches";
    public static final String DRINKS = "drinks";
    public static final String OTHERS = "others";
    public static final String CART_ITEMS = "CartItems";
    Button btn_first_courses;
    Button btn_second_courses;
    Button btn_side_dishes;
    Button btn_sandwiches;
    Button btn_drinks;
    Button btn_others;
    Button btn_cart;
    Button btn_cancel;
    private String restaurantID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        restaurantID = b.getString("restaurantID");
        Log.v("resID---------", restaurantID);

        setContentView(R.layout.activity_reservation);
        btn_first_courses = (Button) findViewById(R.id.btn_first_courses);
        btn_second_courses = (Button) findViewById(R.id.btn_second_courses);
        btn_side_dishes = (Button) findViewById(R.id.btn_side_dishes);
        btn_sandwiches = (Button) findViewById(R.id.btn_sandwiches);
        btn_drinks = (Button) findViewById(R.id.btn_drinks);
        btn_others = (Button) findViewById(R.id.btn_others);
        btn_cart = (Button) findViewById(R.id.btn_cart);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        btn_first_courses.setOnClickListener(this);
        btn_second_courses.setOnClickListener(this);
        btn_side_dishes.setOnClickListener(this);
        btn_sandwiches.setOnClickListener(this);
        btn_drinks.setOnClickListener(this);
        btn_others.setOnClickListener(this);
        btn_cart.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }

    public void onClick(View v){
        if (v.getId() == R.id.btn_cancel){
            SharedPreferences sp = getSharedPreferences(CART_ITEMS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            Toast.makeText(this, getResources().getString(R.string.data_canceled), Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_cart){
            Intent intent = new Intent(getApplicationContext(),ConfirmReservationActivity.class);
            Bundle b = new Bundle();
            b.putString("restaurantID", restaurantID);
            intent.putExtras(b);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(),FoodCategoryActivity.class);
            Bundle b = new Bundle();
            b.putString("restaurantID", restaurantID);

            switch (v.getId()){
                case R.id.btn_first_courses: {
                    b.putString("category", FIRST_COURSES);
                    break;
                }
                case R.id.btn_second_courses: {
                    b.putString("category", SECOND_COURSES);
                    break;
                }
                case R.id.btn_side_dishes: {
                    b.putString("category", SIDE_DISHES);
                    break;
                }
                case R.id.btn_sandwiches: {
                    b.putString("category", SANDWICHES);
                    break;
                }
                case R.id.btn_drinks: {
                    b.putString("category", DRINKS);
                    break;
                }
                case R.id.btn_others: {
                    b.putString("category", OTHERS);
                    break;
                }
            }
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}
