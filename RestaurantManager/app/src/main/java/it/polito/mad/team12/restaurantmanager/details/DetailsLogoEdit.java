package it.polito.mad.team12.restaurantmanager.details;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import it.polito.mad.team12.restaurantmanager.MainActivity;
import it.polito.mad.team12.restaurantmanager.R;
import it.polito.mad.team12.restaurantmanager.Utility;

/**
 * Created by Andrea on 08/05/2016.
 */
public class DetailsLogoEdit extends DialogFragment implements View.OnClickListener {

    private View myFragment;

    public static final int IMAGE_GALLERY_REQUEST = 28;
    public static final int CAMERA_IMAGE_REQUEST = 29;
    private Button camera,roll;
    private ImageView photo;
    private String b64photo;
    private String newb64ph;
    private Bitmap ph;

    Button confirm, cancel;
    Firebase mRootRef,restaurant;

    public DetailsLogoEdit(){

    }

    RestaurantDetails desR;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.details_logo_edit_layout, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        camera=(Button)myFragment.findViewById(R.id.logo_camera_photo);
        roll=(Button)myFragment.findViewById(R.id.logo_roll_photo);

        confirm = (Button) myFragment.findViewById(R.id.logo_save_button);
        cancel = (Button) myFragment.findViewById(R.id.logo_cancel_button);

        photo = (ImageView) myFragment.findViewById(R.id.logo_photo);
        camera.setOnClickListener(this);
        roll.setOnClickListener(this);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);


        restaurant.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                desR = dataSnapshot.getValue(RestaurantDetails.class);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        return myFragment;
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getContext());

        MainActivity act = (MainActivity) getActivity();
        String restaurant11 = act.retrieveRestID(); // I RETRIEVE THE INFO OF THE RESTAURANT FROM HERE

        // CHECK FOR PHOTOS AS WELL
        mRootRef = Utility.getFirebaseRestaurantsRef();
        restaurant = mRootRef.child(restaurant11);



    }



    @Override
    public void onClick(View v) {
            if (v==cancel){
                dismiss();
            }
            if (v==confirm){
                desR.setRestaurantLogo(newb64ph);

                restaurant.setValue(desR);

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction;

                transaction = manager.beginTransaction();
                transaction.replace(R.id.flContent , new DetailsFragment());
                transaction.commit();
                dismiss();
            }
            if (v==camera){
                Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST);
            }
            if (v==roll){
                Intent photopicker = new Intent(Intent.ACTION_PICK);   //invoking image gallery

                File picturedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String picdirStr = picturedir.getPath();

                Uri data = Uri.parse(picdirStr);

                photopicker.setDataAndType(data, "image/*");   //get me ANY image type

                startActivityForResult(photopicker, IMAGE_GALLERY_REQUEST);   //get image back from roll
            }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == IMAGE_GALLERY_REQUEST){
                Uri imageUri= data.getData();   //Uri = address of image on sd card
                InputStream inputS;

                try {
                    inputS = getContext().getContentResolver().openInputStream(imageUri);  //getting input stream based on Uri

                    ph= BitmapFactory.decodeStream(inputS);
                    photo.setImageBitmap(ph);

                    newb64ph = encodeToBase64(ph, Bitmap.CompressFormat.JPEG, 40);  //the NEW image encoded ready to be sent to JSON file

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }if (requestCode == CAMERA_IMAGE_REQUEST){
                Bitmap cameraph1 = (Bitmap) data.getExtras().get("data");   //ACCESS THE IMAGE
                photo.setImageBitmap(cameraph1);
                newb64ph = encodeToBase64(cameraph1, Bitmap.CompressFormat.JPEG, 40);  //the NEW image encoded ready to be sent to JSON file

            }
        }

    }


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
