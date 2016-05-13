package it.polito.mad.team12.restaurantmanager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class SimpleItemAdapter extends BaseAdapter {
    private final ArrayList mData;

    public SimpleItemAdapter(Map<String,String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_final_reservation, parent, false);
        }

        Map.Entry<String,String> item = getItem(position);

        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        Log.v("ITEM VALUE", item.getValue());
        name.setText(item.getKey());
        quantity.setText(item.getValue());
        return convertView;
    }
}