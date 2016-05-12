package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Currency;
import java.util.Locale;

public class ShowOffersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    View item;
    TextView itemName;
    TextView itemDescription;
    TextView itemPrice;
    ImageView imgItem;
    ContextMenuRecyclerView recyclerView;
    int position;
    // The system for currency handling might be made more sophisticated if necessary
    private Currency italianCurrency = Currency.getInstance(Locale.ITALY);
    private OfferData offerData;
    private Context context;

    public ShowOffersViewHolder(View itemView) {
        super(itemView);
        item  = itemView;
        itemName = (TextView) itemView.findViewById(R.id.mt_item_name);
        itemDescription = (TextView) itemView.findViewById(R.id.mt_item_description);
        itemPrice = (TextView) itemView.findViewById(R.id.mt_item_price);
        imgItem = (ImageView) itemView.findViewById(R.id.mt_item_image);
    }

    public void setData(OfferData offerData, int position) {
        itemName.setText(offerData.getName());
        itemDescription.setText(offerData.getDescription());
        itemPrice.setText(offerData.getPrice().toString() + " " + italianCurrency.getSymbol());
        if(!offerData.getHasImage()) {
            // TODO find a better image
            imgItem.setImageResource(R.drawable.default_food_image);
        } else {
            // TODO handle image loading from Firebase
            //imgItem.setImageBitmap(Utility.decodeSampledBitmapFromFile(item.getImageUri(), imgItem.getWidth(), imgItem.getHeight()));
        }
        this.position = position;
        this.offerData = offerData;
    }

    public void setListeners() {
        this.item.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ShowOfferDetailsActivity.class);
        Gson gson = new Gson();
        String data = gson.toJson(offerData);

        intent.putExtra(ShowOfferDetailsActivity.OFFER_DATA, data);
        context.startActivity(intent);
    }

    public void passContext(Context context) {
        this.context = context;
    }
}
