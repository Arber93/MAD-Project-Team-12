package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class SimpleItemAdapter extends BaseAdapter {
    Context context;
    private final ArrayList mData;
    SharedPreferences sharedPreferences;

    public SimpleItemAdapter(Map<String,String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String,String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_final_reservation, parent, false);
        }

        sharedPreferences = context.getSharedPreferences("CartItems", Context.MODE_PRIVATE);

        final Map.Entry<String,String> item = getItem(position);

        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        Button btn_remove = (Button) convertView.findViewById(R.id.btn_remove);
        Log.v("ITEM VALUE", item.getValue());
        name.setText(item.getKey());
        quantity.setText(item.getValue());

        btn_remove.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.remove(position);
                        notifyDataSetChanged();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(item.getKey());
                        editor.apply();
                    }
                });


        return convertView;
    }
}