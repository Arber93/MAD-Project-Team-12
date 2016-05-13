package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.client.Query;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class Utility {
    // TODO Fill this class with constants and methods to be used by multiple activities/fragments
    public static final String LOG_WARNING_TAG = "warning";
    public static final String IMAGE_EXTENSION = ".jpg";
    public static final String FIREBASE_ROOT = "https://boiling-torch-1263.firebaseio.com/";
    public static final String FIREBASE_RESTAURANTS = "https://boiling-torch-1263.firebaseio.com/restaurants/";
    public static final String FIREBASE_MENU_PATH = "/menu";
    public static final String FIREBASE_MENU_ITEMS_PATH = FIREBASE_MENU_PATH + "/items";
    public static final String FIREBASE_MENU_OFFERS_PATH = FIREBASE_MENU_PATH + "/offers";

    public static final int TAKE_PHOTO = 1;
    public static final int BROWSE_GALLERY = 2;

    private static final Firebase root = new Firebase(FIREBASE_ROOT);

    // empty private constructor, because it doesn't make sense to initialize the Utility class
    private Utility() {}

    //TODO add some javadoc comments for these methods
    public static Bitmap decodeSampledBitmapFromFile(Uri file, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getPath(), options);
    }


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeFromBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static Firebase getFirebaseRoot() {
        return root;
    }

    public static Firebase getMenuItemsFrom(String restaurantID) {
        Firebase result = new Firebase(FIREBASE_RESTAURANTS + restaurantID + FIREBASE_MENU_ITEMS_PATH);
        return result;
    }

    public static Firebase getMenuOffersFrom(String restaurantID) {
        Firebase result = new Firebase(FIREBASE_RESTAURANTS + restaurantID + FIREBASE_MENU_OFFERS_PATH);
        return result;
    }

    public static class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                mDivider = context.getDrawable(R.drawable.horizontal_divider);
            } else {
                mDivider = context.getResources().getDrawable(R.drawable.horizontal_divider);
            }
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

}
