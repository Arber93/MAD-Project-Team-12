package it.polito.mad.team12.restaurantmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class AddMenuItemActivity extends AppCompatActivity {
    private static final String MENU_ITEM_KEY = "menu item key";
    public static final String MENU_ITEM_DATA = "menu item data";
    public static final String MENU_ITEM_POSITION = "menu item position";
    private static final String IMAGE_EXTENSION = ".jpg";
    private static final String DEFAULT_MENU_ITEM_IMAGE_NAME = "temp";

    // The system for currency handling might be made more sophisticated if necessary
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);

    private RestaurantMenuItem menuItem;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

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
        final Button cancelButton = (Button) findViewById(R.id.ami_cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean failed = false;
                EditText name = (EditText) findViewById(R.id.ami_name_field);
                EditText description= (EditText) findViewById(R.id.ami_description_field);
                EditText price = (EditText) findViewById(R.id.ami_price_field);
                CheckBox glutenFree = (CheckBox) findViewById(R.id.ami_checkbox_gluten_free);
                CheckBox vegan = (CheckBox) findViewById(R.id.ami_checkbox_vegan);
                CheckBox vegetarian = (CheckBox) findViewById(R.id.ami_checkbox_vegetarian);

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

                if(!failed) {

                    //TODO image handling with Firebase
                    /*if(menuItem.getImageUri() != null) {
                        File file = new File(menuItem.getImageUri().toString());
                        Log.i("info", "current file name = " + file.getAbsolutePath());
                        File newFile = Utility.generateImageFile(getApplicationContext(), Utility.MENU_DIRECTORY, menuItem.getName() + IMAGE_EXTENSION);
                        Log.i("info", "file renaming = " + file.renameTo(newFile));

                        if(Utility.moveFile(file.toString(), newFile.toString())) {
                            menuItem.setImageUri(Uri.fromFile(newFile));
                        }
                    }*/

                    Intent result = new Intent();
                    Gson gson = new Gson();
                    String data = gson.toJson(menuItem);
                    Bundle bundle = new Bundle();

                    bundle.putString(MENU_ITEM_DATA, data);

                    if (position != -1) {
                        bundle.putInt(MENU_ITEM_POSITION, position);
                    }

                    result.putExtras(bundle);
                    setResult(RESULT_OK, result);
                    finish();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Delete any files created for the new photo
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

        switch (item.getItemId()) {
            case R.id.ami_take_photo:
                String filename;
                Intent tpIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(!menuItem.getHasImage()) {    // an image is being associated to this menu item for the first time
                    if(menuItem.getName() == null) {    // the user has not yet typed a name for the new item
                        filename = DEFAULT_MENU_ITEM_IMAGE_NAME + IMAGE_EXTENSION;
                    } else {    // use that name for the new file
                        filename = menuItem.getName() + IMAGE_EXTENSION;
                    }
                } else {    // There is presently an old photo of the item. This photo will be stored just in case the user cancels
                    filename = menuItem.getName() + "_" + DEFAULT_MENU_ITEM_IMAGE_NAME + IMAGE_EXTENSION;
                }

                //File outputFile = Utility.generateImageFile(getApplicationContext(), Utility.MENU_DIRECTORY, filename);
                //TODO handle images in Firebase
                //menuItem.setImageUri(Uri.fromFile(outputFile));
                //tpIntent.putExtra(MediaStore.EXTRA_OUTPUT, menuItem.getImageUri());

                // make sure that there is a camera app to handle the intent request
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
        Bitmap bitmap;
        ImageView imageView = (ImageView) findViewById(R.id.ami_item_image);

        if(resultCode == RESULT_OK) {
            switch (resultCode) {
                case Utility.TAKE_PHOTO:
                    //TODO handle images with firebase
                    //bitmap = Utility.decodeSampledBitmapFromFile(menuItem.getImageUri(), imageView.getWidth(), imageView.getHeight());
                    //imageView.setImageBitmap(bitmap);
                    break;
                case Utility.BROWSE_GALLERY:

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
        ImageView imageView = (ImageView) findViewById(R.id.ami_item_image);
        CheckBox glutenFree = (CheckBox) findViewById(R.id.ami_checkbox_gluten_free);
        CheckBox vegan = (CheckBox) findViewById(R.id.ami_checkbox_vegan);
        CheckBox vegetarian = (CheckBox) findViewById(R.id.ami_checkbox_vegetarian);
        Bitmap bitmap;

        //TODO handle images with firebase
        /*if(menuItem.getImageUri() != null) {    // use the image related to the menuItem
            bitmap = Utility.decodeSampledBitmapFromFile(menuItem.getImageUri(), imageView.getWidth(), imageView.getHeight());
            imageView.setImageBitmap(bitmap);
        }*/

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
}
