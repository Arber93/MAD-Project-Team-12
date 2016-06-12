package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Currency;
import java.util.Locale;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

public class ShowMenuViewHolder extends RecyclerView.ViewHolder {
    View item;
    TextView itemName;
    TextView itemPrice;
    ImageView imgItem;
    ImageView imgFirst;
    ImageView imgSecond;
    ImageView imgPlus;
    int position;
    // The system for currency handling might be made more sophisticated if necessary
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);
    private ItemData itemData;
    private Context context;
    private String restaurantName;

    public ShowMenuViewHolder(View itemView) {
        super(itemView);
        item  = itemView;
        itemName = (TextView) itemView.findViewById(R.id.mt_item_name);
        itemPrice = (TextView) itemView.findViewById(R.id.mt_item_price);
        imgItem = (ImageView) itemView.findViewById(R.id.mt_item_image);
        imgFirst = (ImageView) itemView.findViewById(R.id.mt_first_icon);
        imgSecond = (ImageView) itemView.findViewById(R.id.mt_second_icon);
        imgPlus = (ImageView) itemView.findViewById(R.id.mt_plus_icon);
    }

    public void setData(ItemData item, int position) {
        itemName.setText(item.getName());
        itemPrice.setText(item.getPrice().toString() + " " + italianCurrency.getSymbol());

        if(item.isVegan()) {
            imgFirst.setImageResource(R.drawable.ic_veganup);
            imgSecond.setImageResource(R.drawable.ic_noglutenup);
        } else if (item.isVegetarian()) {
            imgFirst.setImageResource(R.drawable.ic_vegup);
            imgSecond.setImageResource(R.drawable.ic_noglutenup);
        } else if(item.isGlutenFree()) {
            imgFirst.setImageResource(R.drawable.ic_noglutenup);
        }

        if(!item.getHasImage()) {
            imgItem.setImageResource(R.drawable.default_food_image);
        } else {
            Utility.getItemImageFrom(restaurantName, itemData.getName())
                    .addListenerForSingleValueEvent(new ImageViewValueListener(imgItem));
        }
        this.position = position;
        this.itemData = item;
    }

    public void setListeners() {
        this.item.setOnClickListener(new ItemClickListener(restaurantName, itemData, context));
        this.imgPlus.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sharedPreferences = context.getSharedPreferences(restaurantName, Context.MODE_PRIVATE);

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(!sharedPreferences.contains(itemData.getName())){    // the item isn't present in the current reservation
                    editor.putString(itemData.getName(), "1");
                    Toast.makeText(context, context.getResources().getString(R.string.item_added_first_time), Toast.LENGTH_SHORT).show();
                } else {    // plus is being pressed for the second time or more get the current value and increment it
                    int currentCount = Integer.parseInt(sharedPreferences.getString(itemData.getName(),"0"));
                    currentCount++;
                    editor.putString(itemData.getName(), "" + currentCount);
                    Toast.makeText(context, context.getResources().getString(R.string.item_added_successive_times), Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });
    }

    public void passContext(Context context) {
        this.context = context;
    }

    public void passRestaurantId(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}