package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Currency;
import java.util.Locale;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

public class ShowItemDetailsActivity extends AppCompatActivity {

    private ItemData itemData;
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);
    private String restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sid_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.getExtras() != null){
            Gson gson = new Gson();
            Bundle extras = intent.getExtras();

            itemData = gson.fromJson(extras.getString(Utility.ITEM_DATA_KEY), ItemData.class);
            getSupportActionBar().setTitle(itemData.getName());

            populateLayout();

            restaurantName = extras.getString(Utility.RESTAURANT_ID_KEY);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateLayout() {
        ImageView imageView = (ImageView) findViewById(R.id.sid_image);
        TextView itemName = (TextView) findViewById(R.id.sid_item_name);
        TextView itemPrice = (TextView) findViewById(R.id.sid_item_price);
        TextView itemDescription = (TextView) findViewById(R.id.sid_item_description);
        TextView itemCharacteristics = (TextView) findViewById(R.id.sid_item_characteristics);
        StringBuilder characteristics = new StringBuilder("");

        if(itemData.getHasImage()) {
            Utility.getItemImageFrom(restaurantName, itemData.getName())
                    .addListenerForSingleValueEvent(new ImageViewValueListener(imageView));
        } else {
            imageView.setImageResource(R.drawable.default_food_image);
        }

        itemName.setText(itemData.getName());

        itemDescription.setText('\t' + itemData.getDescription());

        itemPrice.setText(itemData.getPrice().toString() + " " + italianCurrency.getSymbol());

        if(itemData.isVegan()) {
            characteristics.append(getResources().getString(R.string.ami_vegan_title));
        } else if(itemData.isVegetarian()) {
            characteristics.append(getResources().getString(R.string.ami_vegetarian_title));
        }

        if(itemData.isGlutenFree()) {
            if(!characteristics.toString().isEmpty()){
                characteristics.append(", ");
            }
            characteristics.append(getResources().getString(R.string.ami_gluten_free_title));
        }

        if(!characteristics.toString().isEmpty()){
            itemCharacteristics.setText('\t' + characteristics.toString());
        }
    }

}
