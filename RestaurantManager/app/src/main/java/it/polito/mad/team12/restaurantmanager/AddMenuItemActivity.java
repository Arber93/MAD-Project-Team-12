package it.polito.mad.team12.restaurantmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class AddMenuItemActivity extends AppCompatActivity {
    private static final String MENU_ITEM_KEY = "menu item key";
    public static final String MENU_ITEM_DATA = "menu item data";
    public static final String MENU_ITEM_POSITION = "menu item position";

    // The system for currency handling might be made more sophisticated if necessary
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);

    private RestaurantMenuItem menuItem;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        // TODO use the intent extras to determine whether your adding/editing the item
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.ami_toolbar);
        setSupportActionBar(toolbar);

        ((TextView)findViewById(R.id.ami_currency_symbol)).setText(italianCurrency.getSymbol());

        if(intent.getExtras() == null) {    // Adding a new item
            menuItem = new RestaurantMenuItem();
            getSupportActionBar().setTitle(R.string.ami_menu_new_item_title);
        } else {                            // Editing an existing item
            Gson gson = new Gson();
            Bundle extras = getIntent().getExtras();

            menuItem = gson.fromJson(extras.getString(MENU_ITEM_DATA), RestaurantMenuItem.class);
            position = extras.getInt(MENU_ITEM_POSITION);

            populateLayout(menuItem);

            getSupportActionBar().setTitle(R.string.ami_menu_edit_item_title);
        }

        Button saveButton = (Button) findViewById(R.id.ami_save_button);
        Button cancelButton = (Button) findViewById(R.id.ami_cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO handle images
                EditText name = (EditText) findViewById(R.id.ami_name_field);
                EditText description= (EditText) findViewById(R.id.ami_description_field);
                EditText price = (EditText) findViewById(R.id.ami_price_field);
                CheckBox glutenFree = (CheckBox) findViewById(R.id.ami_checkbox_gluten_free);
                CheckBox vegan = (CheckBox) findViewById(R.id.ami_checkbox_vegan);
                CheckBox vegetarian = (CheckBox) findViewById(R.id.ami_checkbox_vegetarian);

                menuItem.setName(name.getText().toString());
                menuItem.setDescription(description.getText().toString());

                if(price.getText().toString() != null) {
                    try {
                        float value = Float.parseFloat(price.getText().toString());
                        menuItem.setPrice(new BigDecimal(price.getText().toString()));
                    } catch (NumberFormatException e) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }

                menuItem.setGlutenFree(glutenFree.isChecked());
                menuItem.setVegan(vegan.isChecked());
                menuItem.setVegetarian(vegetarian.isChecked());

                Intent result = new Intent();
                Gson gson = new Gson();
                String data = gson.toJson(menuItem);
                Bundle bundle = new Bundle();

                bundle.putString(MENU_ITEM_DATA, data);

                if(position != -1) {
                    bundle.putInt(MENU_ITEM_POSITION, position);
                }

                result.putExtras(bundle);
                setResult(RESULT_OK, result);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO make and handle a menu to add an image of the item
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        outState.putString(MENU_ITEM_KEY, json);

        if(position != -1) {
            outState.putInt(MENU_ITEM_POSITION, position);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Gson gson = new Gson();
        menuItem = gson.fromJson(savedInstanceState.getString(MENU_ITEM_KEY), RestaurantMenuItem.class);

        if(savedInstanceState.containsKey(MENU_ITEM_POSITION)) {
            position = savedInstanceState.getInt(MENU_ITEM_POSITION);
        }

        populateLayout(menuItem);
    }

    private void populateLayout(RestaurantMenuItem menuItem) {
        EditText name = (EditText) findViewById(R.id.ami_name_field);
        EditText description= (EditText) findViewById(R.id.ami_description_field);
        EditText price = (EditText) findViewById(R.id.ami_price_field);
        CheckBox glutenFree = (CheckBox) findViewById(R.id.ami_checkbox_gluten_free);
        CheckBox vegan = (CheckBox) findViewById(R.id.ami_checkbox_vegan);
        CheckBox vegetarian = (CheckBox) findViewById(R.id.ami_checkbox_vegetarian);

        //TODO handle the image as well

        if(menuItem.getName() != null) {
            name.setText(menuItem.getName());
        }
        if(menuItem.getDescription() != null) {
            name.setText(menuItem.getDescription());
        }
        if(menuItem.getPrice() != null) {
            price.setText("" + menuItem.getPrice());
        }

        glutenFree.setChecked(menuItem.isGlutenFree());
        vegan.setChecked(menuItem.isVegan());
        vegetarian.setChecked(menuItem.isVegetarian());
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.ami_checkbox_gluten_free:
                if(checked) {
                    menuItem.setGlutenFree(true);
                } else {
                    menuItem.setGlutenFree(false);
                }
                break;
            case R.id.ami_checkbox_vegan:
                if(checked) {
                    menuItem.setVegan(true);
                } else {
                    menuItem.setVegan(false);
                }
                break;
            case R.id.ami_checkbox_vegetarian:
                if(checked) {
                    menuItem.setVegetarian(true);
                } else {
                    menuItem.setVegetarian(false);
                }
                break;
        }
    }
}
