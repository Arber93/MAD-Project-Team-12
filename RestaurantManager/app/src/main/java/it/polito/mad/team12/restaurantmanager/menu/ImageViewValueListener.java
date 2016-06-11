package it.polito.mad.team12.restaurantmanager.menu;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import it.polito.mad.team12.restaurantmanager.Utility;

public class ImageViewValueListener implements ValueEventListener {
    private ImageView imageView;

    public ImageViewValueListener(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {
            String imageData = dataSnapshot.getValue(String.class);
            Bitmap bm = Utility.decodeFromBase64(imageData);
            imageView.setImageBitmap(bm);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
