package it.polito.mad.team12.restaurantmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import it.polito.mad.team12.restaurantmanager.menu.AddMenuOfferActivity;
import it.polito.mad.team12.restaurantmanager.menu.ItemData;
import it.polito.mad.team12.restaurantmanager.menu.OfferData;

public final class Utility {
    // TODO Fill this class with constants and methods to be used by multiple activities/fragments
    public static final String LOG_WARNING_TAG = "warning";
    public static final String IMAGE_EXTENSION = ".jpg";
    public static final String FIREBASE_ROOT = "https://restaurant-manager.firebaseio.com/";
    public static final String FIREBASE_RESTAURANTS = FIREBASE_ROOT + "/restaurants";
    public static final String FIREBASE_GEOFIRE = FIREBASE_ROOT + "/geofire";
    public static final String FIREBASE_REVIEWS = FIREBASE_ROOT + "/reviews";
    public static final String FIREBASE_MENUS = FIREBASE_ROOT + "/menus/";
    public static final String FIREBASE_PHOTOS = FIREBASE_ROOT + "/photos/";
    public static final String FIREBASE_MENU_AVAILABLE_ITEMS_PATH = "/available/";
    public static final String FIREBASE_MENU_UNAVAILABLE_ITEMS_PATH = "/unavailable/";
    public static final String FIREBASE_MENU_ITEMS_PATH = "/items/";
    public static final String FIREBASE_MENU_OFFERS_PATH = "/offers/";

    public static final int TAKE_PHOTO = 1;
    public static final int BROWSE_GALLERY = 2;

    public static final String CART_ITEMS = "CartItems";
    public static final String OFFER_KEY_PREFIX = "$$$ ";
    public final static String CATEGORY_ID_KEY = "category id";
    public final static String RESTAURANT_ID_KEY = "restaurant ID";
    public static final String ITEM_DATA_KEY = "item data";
    public static final String OFFER_DATA_KEY = "offer data";
    public static final String EDIT_MODE_KEY = "edit mode";

    private static final Firebase root = new Firebase(FIREBASE_ROOT);

    // empty private constructor, because it doesn't make sense to initialize the Utility class
    private Utility() {}


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

    public static Firebase getFirebaseReviewsRef() {
        Firebase path = new Firebase(FIREBASE_REVIEWS);
        return path;
    }

    public static Firebase getFirebaseRestaurantsRef() {
        Firebase path = new Firebase(FIREBASE_RESTAURANTS);
        return path;
    }

    public static Firebase getFirebaseGeofireRef() {
        Firebase path = new Firebase(FIREBASE_GEOFIRE);
        return path;
    }

    public static Firebase getFirebasePhotosRef() {
        Firebase path = new Firebase(FIREBASE_PHOTOS);
        return path;
    }

    public static Firebase getMenuItemsFrom(String restaurantID, boolean availability) {
		String path = (availability) ? FIREBASE_MENU_AVAILABLE_ITEMS_PATH : FIREBASE_MENU_UNAVAILABLE_ITEMS_PATH;
        Firebase result = new Firebase(FIREBASE_MENUS + restaurantID + path);
        return result;
    }

    public static Firebase getMenuOffersFrom(String restaurantID) {
        Firebase result = new Firebase(FIREBASE_MENUS + restaurantID + FIREBASE_MENU_OFFERS_PATH);
        return result;
    }

    public static Firebase getItemImageFrom(String restaurantID, String name) {
        Firebase path = new Firebase(FIREBASE_PHOTOS + restaurantID + FIREBASE_MENU_ITEMS_PATH + name);
        return path;
    }
	
	public static Firebase getOfferImageFrom(String restaurantID, String name) {
        Firebase path = new Firebase(FIREBASE_PHOTOS + restaurantID + FIREBASE_MENU_OFFERS_PATH + name);
        return path;
    }
	
	public static void addItemImage(String restaurantID, String itemName, String encodedImage) {
		getItemImageFrom(restaurantID, itemName).setValue(encodedImage);
	}

	public static void addOfferImage(String restaurantID, String offerName, String encodedImage) {
		getOfferImageFrom(restaurantID, offerName).setValue(encodedImage);
	}
	
	public static void checkOfferAvailability(final String restaurantID, final String offerName) {

        getMenuOffersFrom(restaurantID).child(offerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final OfferData offerData = dataSnapshot.getValue(OfferData.class);

                    for (String link : offerData.getLinks().keySet()) {
                        getMenuItemsFrom(restaurantID, true).child(link).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    if (offerData.isAvailable()) {
                                        getMenuOffersFrom(restaurantID).child(offerData.getName() + "/available").setValue(false);
                                    }
                                    offerData.setAvailable(false);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
	}

    //TODO add a method to just check if the name is valid so that the user doesn't have to discard his new item in case of duplicate name
	public static void addItem(final String restaurantID, final boolean available, final ItemData itemData) {
        getItemsPath(restaurantID).child(itemData.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    getMenuItemsFrom(restaurantID, available).child(itemData.getName()).setValue(itemData);
                    getItemsPath(restaurantID).child(itemData.getName()).setValue(true);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
	}
	
	public static void addOffer(String restaurantID, final OfferData offerData) {
		getMenuOffersFrom(restaurantID).child(offerData.getName()).setValue(offerData);

        for(String link: offerData.getLinks().keySet()) {
            final Firebase availableItem = getMenuItemsFrom(restaurantID, true).child(link);
            availableItem.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("links/" + offerData.getName(), true);
                        availableItem.updateChildren(map);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            final Firebase unavailableItem = getMenuItemsFrom(restaurantID, false).child(link);
            unavailableItem.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("links/" + offerData.getName(), true);
                        unavailableItem.updateChildren(map);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        checkOfferAvailability(restaurantID, offerData.getName());
	}
	
	public static void moveItem(String restaurantID, ItemData itemData) {
		final Firebase fromPath = getMenuItemsFrom(restaurantID, itemData.isAvailable()).child(itemData.getName());
		final Firebase toPath = getMenuItemsFrom(restaurantID, !itemData.isAvailable()).child(itemData.getName());

        if(itemData.getLinks() != null) {
            if (itemData.isAvailable()) {    // the item is being made unavailable all related offer will also be unavailable
                for (String link : itemData.getLinks().keySet()) {
                    getMenuOffersFrom(restaurantID).child(link + "/available").setValue(false);
                }
            } else {    // when an item is made available check the other item to see if related offers are also made available
                for (String link : itemData.getLinks().keySet()) {
                    checkOfferAvailability(restaurantID, link);
                }
            }
        }
	
		itemData.setAvailable(!itemData.isAvailable());
		toPath.setValue(itemData);
		fromPath.setValue(null);
	}
	
	public static void removeItem(String restaurantID, final ItemData itemData) {

        if(itemData.getLinks() != null) {
            for (String link : itemData.getLinks().keySet()) {
                final Firebase offer = getMenuOffersFrom(restaurantID).child(link);
                offer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("links/" + itemData.getName(), null);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        }

        if(itemData.getHasImage()){
            getItemImageFrom(restaurantID, itemData.getName()).setValue(null);
        }

		getMenuItemsFrom(restaurantID, itemData.isAvailable()).child(itemData.getName()).setValue(null);
        getItemsPath(restaurantID).child(itemData.getName()).setValue(null);
	}
	
	public static void removeOffer(String restaurantID, final OfferData offerData) {
        for(String link: offerData.getLinks().keySet()) {
            final Firebase availableItem = getMenuItemsFrom(restaurantID, true).child(link);
            availableItem.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("links/" + offerData.getName(), null);
                        availableItem.updateChildren(map);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            final Firebase unavailableItem = getMenuItemsFrom(restaurantID, false).child(link);
            unavailableItem.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("links/" + offerData.getName(), null);
                        unavailableItem.updateChildren(map);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        if(offerData.getHasImage()){
            getOfferImageFrom(restaurantID, offerData.getName()).setValue(null);
        }

		getMenuOffersFrom(restaurantID).child(offerData.getName()).setValue(null);
	}

    public static Firebase getItemsPath(String restaurantID) {
        Firebase path = new Firebase(FIREBASE_MENUS + restaurantID + FIREBASE_MENU_ITEMS_PATH);
        return path;
    }

    public static List<String> getItemSet(String restaurantID, final AddMenuOfferActivity addMenuOfferActivity) {
        final List<String> listRef = new CopyOnWriteArrayList<>();

        getItemsPath(restaurantID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Map<String, Boolean> map = (Map<String, Boolean>) dataSnapshot.getValue();
                    listRef.addAll(map.keySet());
                    addMenuOfferActivity.notifyDataLoaded();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return listRef;
    }

    public static class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        public static final int PADDING_LEFT = 1;
        public static final int PADDING_RIGHT = 2;
        public static final int PADDING_BOTH = 0;

        private Drawable mDivider;
        private Context context;
        private int paddingType;
        private int dividerPadding;

        public SimpleDividerItemDecoration(Context context, int paddingType, int dividerPadding) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                mDivider = context.getDrawable(R.drawable.horizontal_divider);
            } else {
                mDivider = context.getResources().getDrawable(R.drawable.horizontal_divider);
            }
            this.context = context;
            this.paddingType = paddingType;
            this.dividerPadding = dividerPadding;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            if(paddingType == PADDING_BOTH || paddingType == PADDING_LEFT) {
                left += dividerPadding;
                if(left > right) {
                    left = right;
                }
            }
            if(paddingType == PADDING_BOTH || paddingType == PADDING_RIGHT) {
                right -= dividerPadding;
                if(right < left) {
                    right = left;
                }
            }

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
