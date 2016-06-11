package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

public class AddMenuItemActivity extends AppCompatActivity {
    private static final String MENU_ITEM_KEY = "menu item key";
	
    private static final String TEMP_PHOTO_KEY = "temp photo key";
    private static final String SPINNER_POSITION = "spinner position";

    // The system for currency handling might be made more sophisticated if necessary
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);

    private ItemData menuItem;
	private Bitmap imageBitmap;
	private String restaurantName;
    private boolean editing;

    private EditText name;
    private EditText description;
    private EditText price;
    private ImageView imageView;
    private CheckBox glutenFree;
    private CheckBox vegan;
    private CheckBox vegetarian;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        name = (EditText) findViewById(R.id.ami_name_field);
        description= (EditText) findViewById(R.id.ami_description_field);
        price = (EditText) findViewById(R.id.ami_price_field);
        imageView = (ImageView) findViewById(R.id.ami_item_image);
        glutenFree = (CheckBox) findViewById(R.id.ami_checkbox_gluten_free);
        vegan = (CheckBox) findViewById(R.id.ami_checkbox_vegan);
        vegetarian = (CheckBox) findViewById(R.id.ami_checkbox_vegetarian);
        spinner = (Spinner) findViewById(R.id.ami_category_spinner);

        List<String> categories = new LinkedList<>();
        categories.add(getString(R.string.first_courses));
        categories.add(getString(R.string.second_courses));
        categories.add(getString(R.string.side_dishes));
        categories.add(getString(R.string.drinks));
        categories.add(getString(R.string.desserts));
        categories.add(getString(R.string.offers));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ami_toolbar);
        setSupportActionBar(toolbar);

        ((TextView)findViewById(R.id.ami_currency_symbol)).setText(italianCurrency.getSymbol());

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            editing = extras.getBoolean(Utility.EDIT_MODE_KEY);

            if (editing) {
                getSupportActionBar().setTitle(R.string.ami_menu_edit_item_title);
                Gson gson = new Gson();

                name.setEnabled(false);
                menuItem = gson.fromJson(extras.getString(Utility.ITEM_DATA_KEY), ItemData.class);
                populateLayout();
            } else {
                getSupportActionBar().setTitle(R.string.ami_menu_new_item_title);
                menuItem = new ItemData();
                menuItem.setCategory(ItemData.FIRST_COURSES);
            }

            restaurantName = extras.getString(Utility.RESTAURANT_ID_KEY);
        }

        Button saveButton = (Button) findViewById(R.id.ami_save_button);
        final Button cancelButton = (Button) findViewById(R.id.ami_cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean failed = false;

                if(name.getText().toString().length() != 0) {
                    menuItem.setName(name.getText().toString());
                } else {
                    failed = true;
                    Toast.makeText(getApplicationContext(), R.string.ami_name_warning, Toast.LENGTH_SHORT).show();
                }

                if(description.getText().toString().length() != 0) {
                    menuItem.setDescription(description.getText().toString());
                } else {
                    failed = true;
                    Toast.makeText(getApplicationContext(), R.string.ami_description_warning, Toast.LENGTH_SHORT).show();
                }

                if(price.getText().toString() != null) {
                    try {
                        float value = Float.parseFloat(price.getText().toString());
                        menuItem.setPrice(new BigDecimal(value));
                    } catch (NumberFormatException e) {
                        failed = true;
                        Toast.makeText(getApplicationContext(), R.string.ami_price_warning, Toast.LENGTH_SHORT).show();
                    }
                }

                menuItem.setGlutenFree(glutenFree.isChecked());
                menuItem.setVegan(vegan.isChecked());
                menuItem.setVegetarian(vegetarian.isChecked());
                menuItem.setCategory(getCategoryId());

                if(!failed) {
                    if(imageBitmap != null) {
                        Utility.addItemImage(restaurantName, menuItem.getName(), Utility.encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG, 100));
                        menuItem.setHasImage(true);
                    }
                    Utility.addItem(restaurantName, menuItem.isAvailable(), menuItem);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        switch (item.getItemId()) {
            case R.id.ami_take_photo:
                Intent tpIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (tpIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(tpIntent, Utility.TAKE_PHOTO);
                }
                break;
            case R.id.ami_browse_gallery:
                Intent bgIntent = new Intent();
                bgIntent.setType("image/*");
                bgIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(bgIntent, "Select Picture"), Utility.BROWSE_GALLERY);
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = (ImageView) findViewById(R.id.ami_item_image);

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case Utility.TAKE_PHOTO:
					Bundle extras = data.getExtras();
					imageBitmap = (Bitmap) extras.get("data");
					imageView.setImageBitmap(imageBitmap);
                    break;
                case Utility.BROWSE_GALLERY:
					Uri uri = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePath[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    imageBitmap = Utility.decodeSampledBitmapFromFile(Uri.fromFile(new File(picturePath)), imageView.getWidth(), imageView.getHeight());
					imageView.setImageBitmap(imageBitmap);
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        outState.putString(MENU_ITEM_KEY, json);
        outState.putString(Utility.RESTAURANT_ID_KEY, restaurantName);
        outState.putBoolean(Utility.EDIT_MODE_KEY, editing);
        outState.putInt(SPINNER_POSITION, spinner.getSelectedItemPosition());
        if(imageBitmap != null) {
            outState.putString(TEMP_PHOTO_KEY, Utility.encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG, 100));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Gson gson = new Gson();
        menuItem = gson.fromJson(savedInstanceState.getString(MENU_ITEM_KEY), ItemData.class);
        restaurantName = savedInstanceState.getString(Utility.RESTAURANT_ID_KEY);
        editing = savedInstanceState.getBoolean(Utility.EDIT_MODE_KEY);
        spinner.setSelection(savedInstanceState.getInt(SPINNER_POSITION));
        menuItem.setCategory(getCategoryId());
        if(savedInstanceState.containsKey(TEMP_PHOTO_KEY)) {
            imageBitmap = Utility.decodeFromBase64(savedInstanceState.getString(TEMP_PHOTO_KEY));
        }

        populateLayout();
    }

    private void populateLayout() {
		if(imageBitmap != null) {
			imageView.setImageBitmap(imageBitmap);
		} else if(menuItem.getHasImage()) {
			Utility.getItemImageFrom(restaurantName, menuItem.getName())
                    .addListenerForSingleValueEvent(new ImageViewValueListener(imageView));
		} else if(!menuItem.getHasImage()) {
			imageView.setImageResource(R.drawable.default_food_image);
		}

        spinner.setSelection(getCategorySpinnerPosition());

        if(editing) {
            name.setEnabled(false);
        }

        if(menuItem.getName() != null) {
            name.setText(menuItem.getName());
        }
        if(menuItem.getDescription() != null) {
            description.setText(menuItem.getDescription());
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
                    menuItem.setVegetarian(true);   // if a dish is vegan it is also vegetarian
                    // check the vegetarian checkbox as well to reflect the change
                    ((CheckBox)findViewById(R.id.ami_checkbox_vegetarian)).setChecked(true);
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

    private String getCategoryId() {
        String result = null;

        switch(spinner.getSelectedItemPosition()) {
            case 0:
                result = ItemData.FIRST_COURSES;
                break;
            case 1:
                result = ItemData.SECOND_COURSES;
                break;
            case 2:
                result = ItemData.SIDE_DISHES;
                break;
            case 3:
                result = ItemData.DRINKS;
                break;
            case 4:
                result = ItemData.DESSERTS;
                break;
            case 5:
                result = ItemData.OTHERS;
                break;
        }

        return result;
    }

    public int getCategorySpinnerPosition() {
        int pos = 0;

        switch (menuItem.getCategory()) {
            case ItemData.FIRST_COURSES:
                pos = 0;
                break;
            case ItemData.SECOND_COURSES:
                pos = 1;
                break;
            case ItemData.SIDE_DISHES:
                pos = 2;
                break;
            case ItemData.DRINKS:
                pos = 3;
                break;
            case ItemData.DESSERTS:
                pos = 4;
                break;
            case ItemData.OTHERS:
                pos = 5;
                break;
        }

        return pos;
    }

}
