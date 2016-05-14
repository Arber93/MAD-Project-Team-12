package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.HashMap;

public class FoodCategoryActivity extends AppCompatActivity {
    public static final String CART_ITEMS = "CartItems";
    Firebase rootRef;
    private String restaurantID;
    private String category;
    RecyclerView recyclerView;
    Button btn_submit;
    SharedPreferences sharedPreferences;
    public static int amount;
    public static String selectedItem;
    public static HashMap<String,String> selectedItems = new HashMap<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        sharedPreferences = getSharedPreferences(CART_ITEMS, Context.MODE_PRIVATE);

        // recyclerView setup
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rootRef = new Firebase("https://blistering-inferno-3678.firebaseio.com");
        Bundle b = getIntent().getExtras();
        restaurantID = b.getString("restaurantID");
        category = b.getString("category");
        Log.v("RESTAURANT ID", restaurantID);
        Log.v("CATEGORY", category);

        // FirebaseRecyclerAdapter setup
        Firebase restaurantsRef = rootRef.child("restaurants");
        Firebase restaurantRef = restaurantsRef.child(restaurantID);
        Firebase menuRef = restaurantRef.child("menu");
        Firebase categoryRef = menuRef.child(category);
        FirebaseRecyclerAdapter<ReservationItem, MyViewHolder> adapter = new
                FirebaseRecyclerAdapter<ReservationItem, MyViewHolder>(ReservationItem.class,R.layout.item_temp_reservation, MyViewHolder.class, categoryRef) {
                    @Override
                    protected void populateViewHolder(MyViewHolder myViewHolder, ReservationItem item, int i) {
                        //get the name of the current item
                        String currentItem = item.getName();
                        // check if the item was already selected and the screen was rotated
                        if (savedInstanceState != null){
                            myViewHolder.quantity.setText(savedInstanceState.getString(currentItem,null));
                        } else if (sharedPreferences.getString(currentItem,null) != null){
                            myViewHolder.quantity.setText(sharedPreferences.getString(currentItem,null));
                        } else {
                            myViewHolder.quantity.setText("0");
                        }
                        // set the name of the item
                        myViewHolder.name.setText(currentItem);
                    }
                };
        recyclerView.setAdapter(adapter);


    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public static final int MAX_ITEM = 20;
        ImageView photo;
        TextView name;
        TextView quantity;
        Button btn_plus;
        Button btn_minus;

        public MyViewHolder(View v){
            super(v);
            photo = (ImageView) v.findViewById(R.id.photo);
            name = (TextView) v.findViewById(R.id.name);
            quantity = (TextView) v.findViewById(R.id.quantity);
            btn_plus = (Button) v.findViewById(R.id.btn_plus);
            btn_minus = (Button) v.findViewById(R.id.btn_minus);
            btn_plus.setOnClickListener(this);
            btn_minus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // get the current item and its quantity
            selectedItem = name.getText().toString();
            amount = Integer.parseInt(quantity.getText().toString());
            if (v.getId() == btn_plus.getId()){
                //TODO set a correct upper bound
                if(amount < MAX_ITEM){
                    amount++;
                } else {
                    amount = MAX_ITEM;
                }
                // store the quantity in a hashmap and show it in the TextView
                selectedItems.put(selectedItem, String.valueOf(amount));
                //Log.v("VALUE", String.valueOf(amount));
                quantity.setText(String.valueOf(amount));
            } else {
                if(amount > 0){
                    amount--;
                    // here amount can be zero
                    selectedItems.put(selectedItem, String.valueOf(amount));
                } else {
                    amount = 0;
                }
                Log.v("VALUE", String.valueOf(amount));
                quantity.setText(String.valueOf(amount));
            }
            Log.v("HASHMAP",selectedItems.toString());
        }
    }

    // store the temporary hashmap in the SP
    public void saveData(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String s : selectedItems.keySet()) {
            if (!selectedItems.get(s).equals("0")) {
                editor.putString(s, selectedItems.get(s));
                Log.v("SP KEY ", s);
                Log.v("SP VALUE ", selectedItems.get(s));
            } else {
                editor.remove(s);
            }
            editor.commit();
            Toast.makeText(this, getResources().getString(R.string.data_success), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    // delete the temporary items from the hashmap
    public void cancelData(View view){
        selectedItems.clear();
        Toast.makeText(this, getResources().getString(R.string.modifications_not_saved), Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (String s : selectedItems.keySet()) {
            if (!selectedItems.get(s).equals("0")) {
                outState.putString(s, selectedItems.get(s));
            }
        }
    }
}
