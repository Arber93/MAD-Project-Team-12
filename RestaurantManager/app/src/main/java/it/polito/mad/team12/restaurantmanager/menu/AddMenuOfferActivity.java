package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import java.io.File;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

public class AddMenuOfferActivity extends AppCompatActivity {
    private static final String OFFER_DATA_KEY = "offer data key";

    private static final String TEMP_PHOTO_KEY = "temp photo key";
    private static final String SPINNER_POSITION = "spinner position";

    private static final String ITEM_LIST ="item list";

    // The system for currency handling might be made more sophisticated if necessary
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);

    private OfferData offerData;
    private Bitmap imageBitmap;
    private String restaurantName;
    private boolean editing;

    private EditText name;
    private EditText description;
    private EditText price;
    private ToggleButton tbMonday;
    private ToggleButton tbTuesday;
    private ToggleButton tbWednesday;
    private ToggleButton tbThursday;
    private ToggleButton tbFriday;
    private ToggleButton tbSaturday;
    private ToggleButton tbSunday;
    private ImageView imageView;
    private CheckBox glutenFree;
    private CheckBox vegan;
    private CheckBox vegetarian;
    private ListView listView;
    private Spinner spinner;
    private Button addNewItem;
    private List<String> itemList;
    private ItemAdapter listAdapter;
    private SpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_offer);

        name = (EditText) findViewById(R.id.amo_name_field);
        description= (EditText) findViewById(R.id.amo_description_field);
        price = (EditText) findViewById(R.id.amo_price_field);
        tbMonday = (ToggleButton) findViewById(R.id.amo_tb_Monday);
        tbTuesday = (ToggleButton) findViewById(R.id.amo_tb_Tuesday);
        tbWednesday = (ToggleButton) findViewById(R.id.amo_tb_Wednesday);
        tbThursday = (ToggleButton) findViewById(R.id.amo_tb_Thursday);
        tbFriday = (ToggleButton) findViewById(R.id.amo_tb_Friday);
        tbSaturday = (ToggleButton) findViewById(R.id.amo_tb_Saturday);
        tbSunday = (ToggleButton) findViewById(R.id.amo_tb_Sunday);
        imageView = (ImageView) findViewById(R.id.amo_item_image);
        glutenFree = (CheckBox) findViewById(R.id.amo_checkbox_gluten_free);
        vegan = (CheckBox) findViewById(R.id.amo_checkbox_vegan);
        vegetarian = (CheckBox) findViewById(R.id.amo_checkbox_vegetarian);
        listView = (ListView) findViewById(R.id.amo_listview);
        spinner = (Spinner) findViewById(R.id.amo_spinner);
        addNewItem = (Button) findViewById(R.id.amo_new_item_button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.amo_toolbar);
        setSupportActionBar(toolbar);

        ((TextView)findViewById(R.id.amo_currency_symbol)).setText(italianCurrency.getSymbol());

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            editing = extras.getBoolean(Utility.EDIT_MODE_KEY);

            if (editing) {
                getSupportActionBar().setTitle(R.string.amo_menu_edit_offer_title);
                Gson gson = new Gson();

                name.setEnabled(false);
                offerData = gson.fromJson(extras.getString(Utility.OFFER_DATA_KEY), OfferData.class);
                populateLayout();
            } else {
                getSupportActionBar().setTitle(R.string.amo_menu_new_offer_title);
                offerData = new OfferData();
                offerData.setCategory(OfferData.OFFERS);
            }

            restaurantName = extras.getString(Utility.RESTAURANT_ID_KEY);
        }

        setupAdapters();

        Button saveButton = (Button) findViewById(R.id.amo_save_button);
        final Button cancelButton = (Button) findViewById(R.id.amo_cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean failed = false;

                if(name.getText().toString().length() != 0) {
                    offerData.setName(name.getText().toString());
                } else {
                    failed = true;
                    Toast.makeText(getApplicationContext(), R.string.ami_name_warning, Toast.LENGTH_SHORT).show();
                }

                if(description.getText().toString().length() != 0) {
                    offerData.setDescription(description.getText().toString());
                } else {
                    failed = true;
                    Toast.makeText(getApplicationContext(), R.string.ami_description_warning, Toast.LENGTH_SHORT).show();
                }

                if(price.getText().toString() != null) {
                    try {
                        float value = Float.parseFloat(price.getText().toString());
                        offerData.setPrice(new BigDecimal(value));
                    } catch (NumberFormatException e) {
                        failed = true;
                        Toast.makeText(getApplicationContext(), R.string.ami_price_warning, Toast.LENGTH_SHORT).show();
                    }
                }

                if(offerData.getLinks() == null) {
                    failed = true;
                    Toast.makeText(getApplicationContext(), R.string.amo_item_number_warning, Toast.LENGTH_SHORT).show();
                }

                offerData.setGlutenFree(glutenFree.isChecked());
                offerData.setVegan(vegan.isChecked());
                offerData.setVegetarian(vegetarian.isChecked());

                saveOfferedOnData();

                offerData.passLinks(listAdapter.getValues());

                if(!failed) {
                    if(imageBitmap != null) {
                        Utility.addOfferImage(restaurantName, offerData.getName(), Utility.encodeToBase64(imageBitmap, Bitmap.CompressFormat.JPEG, 100));
                        offerData.setHasImage(true);
                    }
                    Utility.addOffer(restaurantName, offerData);
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

    private void setupAdapters() {
        itemList = Utility.getItemSet(restaurantName, this);

        List<String> listAdapterData = null;
        if(offerData != null) {
            if (offerData.getLinks() != null) {
                listAdapterData = new LinkedList<>(offerData.getLinks().keySet());
            }
        }

        listAdapter = new ItemAdapter(this,listAdapterData, itemList);
        listView.setAdapter(listAdapter);

        spinnerAdapter = new SpinnerAdapter(this, itemList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);

        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = (String) spinner.getSelectedItem();
                itemList.remove(itemName);
                listAdapter.addItem(itemName);
                listAdapter.notifyDataSetChanged();
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void saveOfferedOnData() {
        offerData.setOfferedMonday(tbMonday.isChecked());
        offerData.setOfferedTuesday(tbTuesday.isChecked());
        offerData.setOfferedWednesday(tbWednesday.isChecked());
        offerData.setOfferedThursday(tbThursday.isChecked());
        offerData.setOfferedFriday(tbFriday.isChecked());
        offerData.setOfferedSaturday(tbSaturday.isChecked());
        offerData.setOfferedSunday(tbSunday.isChecked());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.amo_take_photo:
                Intent tpIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (tpIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(tpIntent, Utility.TAKE_PHOTO);
                }
                break;
            case R.id.amo_browse_gallery:
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
        ImageView imageView = (ImageView) findViewById(R.id.amo_item_image);

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
        if(listAdapter.getValues() != null) {
            offerData.passLinks(listAdapter.getValues());
        }
        saveOfferedOnData();
        Gson gson = new Gson();
        String json = gson.toJson(offerData);
        outState.putString(OFFER_DATA_KEY, json);
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
        offerData = gson.fromJson(savedInstanceState.getString(OFFER_DATA_KEY), OfferData.class);
        restaurantName = savedInstanceState.getString(Utility.RESTAURANT_ID_KEY);
        editing = savedInstanceState.getBoolean(Utility.EDIT_MODE_KEY);
        spinner.setSelection(savedInstanceState.getInt(SPINNER_POSITION));
        if(savedInstanceState.containsKey(TEMP_PHOTO_KEY)) {
            imageBitmap = Utility.decodeFromBase64(savedInstanceState.getString(TEMP_PHOTO_KEY));
        }

        setupAdapters();
        populateLayout();
    }

    private void populateLayout() {
        if(imageBitmap != null) {
            imageView.setImageBitmap(imageBitmap);
        } else if(offerData.getHasImage()) {
            Utility.getItemImageFrom(restaurantName, offerData.getName())
                    .addListenerForSingleValueEvent(new ImageViewValueListener(imageView));
        } else if(!offerData.getHasImage()) {
            imageView.setImageResource(R.drawable.default_food_image);
        }


        tbMonday.setChecked(offerData.isOfferedMonday());
        tbTuesday.setChecked(offerData.isOfferedTuesday());
        tbWednesday.setChecked(offerData.isOfferedWednesday());
        tbThursday.setChecked(offerData.isOfferedThursday());
        tbFriday.setChecked(offerData.isOfferedFriday());
        tbSaturday.setChecked(offerData.isOfferedSaturday());
        tbSunday.setChecked(offerData.isOfferedSunday());

        if(editing) {
            name.setEnabled(false);
        }

        if(offerData.getName() != null) {
            name.setText(offerData.getName());
        }
        if(offerData.getDescription() != null) {
            description.setText(offerData.getDescription());
        }
        if(offerData.getPrice() != null) {
            price.setText("" + offerData.getPrice());
        }

        glutenFree.setChecked(offerData.isGlutenFree());
        vegan.setChecked(offerData.isVegan());
        vegetarian.setChecked(offerData.isVegetarian());
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.ami_checkbox_gluten_free:
                if(checked) {
                    offerData.setGlutenFree(true);
                } else {
                    offerData.setGlutenFree(false);
                }
                break;
            case R.id.ami_checkbox_vegan:
                if(checked) {
                    offerData.setVegan(true);
                    offerData.setVegetarian(true);   // if a dish is vegan it is also vegetarian
                    // check the vegetarian checkbox as well to reflect the change
                    ((CheckBox)findViewById(R.id.ami_checkbox_vegetarian)).setChecked(true);
                } else {
                    offerData.setVegan(false);
                }
                break;
            case R.id.ami_checkbox_vegetarian:
                if(checked) {
                    offerData.setVegetarian(true);
                } else {
                    offerData.setVegetarian(false);
                }
                break;
        }
    }

    public void notifyDataLoaded() {
        spinnerAdapter.removeValues(listAdapter.getValues());
        spinnerAdapter.notifyDataSetChanged();
    }

    public class SpinnerAdapter extends BaseAdapter {
        private final Context context;
        private List<String> values;

        public SpinnerAdapter(Context context, List<String> values) {
            this.values = values;
            this.context = context;
        }

        @Override
        public int getCount() {
            if(values == null) {
                return 0;
            }
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            if(values == null) {
                return null;
            }
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(android.R.layout.simple_spinner_dropdown_item, null);
            }

            CheckedTextView ctv = (CheckedTextView) convertView.findViewById(android.R.id.text1);

            if(values != null) {
                ctv.setText(values.get(position));
                ctv.setPadding(0, (int) (getResources().getDimension(R.dimen.default_top_padding)/2) , 0, (int) (getResources().getDimension(R.dimen.default_bottom_padding)/2));
            }

            return convertView;
        }

        public void removeValues(List<String> itemList) {
            values.removeAll(itemList);
        }
    }

    public class ItemAdapter extends BaseAdapter {
        private final List<String> spinnerItems;
        private Context context;
        private final List<String> values;

        public ItemAdapter(Context context, List<String> values, List<String> spinnerItems) {
            this.context = context;
            this.values = values;
            this.spinnerItems = spinnerItems;
        }

        @Override
        public int getCount() {
            if(values == null) {
                return 0;
            }
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            if(values == null) {
                return null;
            }
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = li.inflate(R.layout.item_offer_includes, null);
            }

            final String data = values.get(position);

            TextView textView = (TextView) convertView.findViewById(R.id.new_item_name);
            textView.setText(data);

            ImageView deleteItem = (ImageView) convertView.findViewById(R.id.delete_item);
            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    values.remove(data);
                    spinnerItems.add(data);
                    notifyDataSetChanged();
                    spinnerAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        public void addItem(String itemName) {
            if(itemName != null) {
                values.add(itemName);
            }
        }

        public List<String> getValues() {
            return values;
        }
    }

}
