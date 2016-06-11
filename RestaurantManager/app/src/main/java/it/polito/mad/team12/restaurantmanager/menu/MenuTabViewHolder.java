package it.polito.mad.team12.restaurantmanager.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Currency;
import java.util.Locale;

import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

public class MenuTabViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    View item;
    TextView itemName;
    TextView itemDescription;
    TextView itemPrice;
    ImageView imgItem;
    ContextMenuRecyclerView recyclerView;
    int position;
    // The system for currency handling might be made more sophisticated if necessary
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);
    private String restaurantID;
    private Context context;
    private ItemData itemData;

    public MenuTabViewHolder(View itemView) {
        super(itemView);
        item  = itemView;
        itemName = (TextView) itemView.findViewById(R.id.mt_item_name);
        itemDescription = (TextView) itemView.findViewById(R.id.mt_item_description);
        itemPrice = (TextView) itemView.findViewById(R.id.mt_item_price);
        imgItem = (ImageView) itemView.findViewById(R.id.mt_item_image);
    }

    public void setData(ItemData item, int position) {
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemPrice.setText(item.getPrice().toString() + " " + italianCurrency.getSymbol());
        if(!item.getHasImage()) {
            imgItem.setImageResource(R.drawable.default_food_image);
        } else {
            Utility.getItemImageFrom(restaurantID, item.getName())
                    .addListenerForSingleValueEvent(new ImageViewValueListener(imgItem));
        }
        this.position = position;
        this.itemData = item;
    }

    public void setRecyclerView(ContextMenuRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setListeners() {
        this.item.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        this.recyclerView.showContextMenuForChild(v);
        return true;
    }

    public void passRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
    public void passContext(Context context) {
        this.context = context;
    }

    public void requestMove() {
        Utility.moveItem(restaurantID, itemData);
    }

    public void requestEdit() {
        Intent intent = new Intent(context, AddMenuItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Utility.RESTAURANT_ID_KEY, restaurantID);
        bundle.putBoolean(Utility.EDIT_MODE_KEY, true);
        Gson gson = new Gson();
        String data = gson.toJson(itemData);
        bundle.putString(Utility.ITEM_DATA_KEY, data);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void requestRemoval() {
        Utility.removeItem(restaurantID, itemData);
    }
}
