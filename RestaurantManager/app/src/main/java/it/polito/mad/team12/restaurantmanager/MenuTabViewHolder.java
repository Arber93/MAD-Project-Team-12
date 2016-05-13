package it.polito.mad.team12.restaurantmanager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Currency;
import java.util.Locale;

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

    public MenuTabViewHolder(View itemView) {
        super(itemView);
        item  = itemView;
        itemName = (TextView) itemView.findViewById(R.id.mt_item_name);
        itemDescription = (TextView) itemView.findViewById(R.id.mt_item_description);
        itemPrice = (TextView) itemView.findViewById(R.id.mt_item_price);
        imgItem = (ImageView) itemView.findViewById(R.id.mt_item_image);
    }

    public void setData(RestaurantMenuItem item, int position) {
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemPrice.setText(item.getPrice().toString() + " " + italianCurrency.getSymbol());
        if(!item.getHasImage()) {
            // TODO find a better image
            imgItem.setImageResource(R.drawable.default_food_image);
        } else {
            // TODO handle image loading from Firebase
            //imgItem.setImageBitmap(Utility.decodeSampledBitmapFromFile(item.getImageUri(), imgItem.getWidth(), imgItem.getHeight()));
        }
        this.position = position;
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
}
