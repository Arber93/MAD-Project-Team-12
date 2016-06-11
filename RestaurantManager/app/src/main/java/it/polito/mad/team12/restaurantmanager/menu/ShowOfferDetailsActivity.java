package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

public class ShowOfferDetailsActivity extends AppCompatActivity {

    private OfferData offerData;
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);
    private String restaurantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sod_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if(intent.getExtras() != null){
            Gson gson = new Gson();
            Bundle extras = intent.getExtras();

            offerData = gson.fromJson(extras.getString(Utility.OFFER_DATA_KEY), OfferData.class);
            getSupportActionBar().setTitle(offerData.getName());

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
        ImageView imageView = (ImageView) findViewById(R.id.sod_image);
        TextView offerName = (TextView) findViewById(R.id.sod_offer_name);
        TextView offerPrice = (TextView) findViewById(R.id.sod_offer_price);
        TextView offerDescription = (TextView) findViewById(R.id.sod_offer_description);
        TextView offerIncludes = (TextView) findViewById(R.id.sod_offer_includes);
        StringBuilder includes = new StringBuilder("");
        List<String> itemsIncluded = new ArrayList<>();
        TextView offerCharacteristics = (TextView) findViewById(R.id.sod_offer_characteristics);
        StringBuilder characteristics = new StringBuilder("");

        if(offerData.getHasImage()) {
            Utility.getItemImageFrom(restaurantName, offerData.getName())
                    .addListenerForSingleValueEvent(new ImageViewValueListener(imageView));
        } else {
            imageView.setImageResource(R.drawable.default_food_image);
        }

        offerName.setText(offerData.getName());

        offerDescription.setText('\t' + offerData.getDescription());

        offerPrice.setText(offerData.getPrice().toString() + " " + italianCurrency.getSymbol());

        itemsIncluded.addAll(offerData.getLinks().keySet());
        for(int i = 0; i < itemsIncluded.size(); i++) {
            includes.append(itemsIncluded.get(i));
            if(i != itemsIncluded.size() - 1) {
                includes.append(", ");
            }
        }

        offerIncludes.setText(includes);

        if(offerData.isVegan()) {
            characteristics.append(getResources().getString(R.string.ami_vegan_title));
        } else if(offerData.isVegetarian()) {
            characteristics.append(getResources().getString(R.string.ami_vegetarian_title));
        }

        if(offerData.isGlutenFree()) {
            if(!characteristics.toString().isEmpty()){
                characteristics.append(", ");
            }
            characteristics.append(getResources().getString(R.string.ami_gluten_free_title));
        }

        if(!characteristics.toString().isEmpty()){
            offerCharacteristics.setText('\t' + characteristics.toString());
        }
    }
}
