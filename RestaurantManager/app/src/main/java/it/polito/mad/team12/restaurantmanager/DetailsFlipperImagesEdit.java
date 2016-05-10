package it.polito.mad.team12.restaurantmanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;


/**
 * Created by Andrea on 03/05/2016.
 */
public class DetailsFlipperImagesEdit  extends DialogFragment implements View.OnClickListener {


    public static final int IMAGE_GALLERY_REQUEST1 = 20;
    public static final int IMAGE_GALLERY_REQUEST2 = 22;
    public static final int IMAGE_GALLERY_REQUEST3 = 23;
    public static final int IMAGE_GALLERY_REQUEST4 = 24;
    public static final int CAMERA_IMAGE_REQUEST1 = 31;
    public static final int CAMERA_IMAGE_REQUEST2 = 32;
    public static final int CAMERA_IMAGE_REQUEST3 = 33;
    public static final int CAMERA_IMAGE_REQUEST4 = 34;


    private View myFragment;

    private RestaurantDetails desR;
    Firebase mPhotosRef, photos;


    private Button camera1,roll1,delete1,camera2,roll2,delete2,camera3,roll3,delete3,camera4,roll4,delete4;
    private ImageView photo1,photo2,photo3,photo4;
    private String b64photo1,b64photo2,b64photo3,b64photo4;
    private String newb64ph1, newb64ph2,newb64ph3,newb64ph4;
    private Bitmap ph1,ph2,ph3,ph4;
    private Button confirm, cancel;
    private boolean deleted1,deleted2,deleted3,deleted4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myFragment= inflater.inflate(R.layout.fragment_layout_edit_flipper, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        camera1=(Button) myFragment.findViewById(R.id.flipper_camera_photo1);
        camera2=(Button) myFragment.findViewById(R.id.flipper_camera_photo2);
        camera3=(Button) myFragment.findViewById(R.id.flipper_camera_photo3);
        camera4=(Button) myFragment.findViewById(R.id.flipper_camera_photo4);

        roll1=(Button) myFragment.findViewById(R.id.flipper_roll_photo1);
        roll2=(Button) myFragment.findViewById(R.id.flipper_roll_photo2);
        roll3=(Button) myFragment.findViewById(R.id.flipper_roll_photo3);
        roll4=(Button) myFragment.findViewById(R.id.flipper_roll_photo4);

        delete1=(Button) myFragment.findViewById(R.id.flipper_delete_photo1);
        delete2=(Button) myFragment.findViewById(R.id.flipper_delete_photo2);
        delete3=(Button) myFragment.findViewById(R.id.flipper_delete_photo3);
        delete4=(Button) myFragment.findViewById(R.id.flipper_delete_photo4);

        confirm = (Button) myFragment.findViewById(R.id.flipper_save_button);
        cancel = (Button) myFragment.findViewById(R.id.flipper_cancel_button);

        photo1 = (ImageView) myFragment.findViewById(R.id.flipper_photo1);
        photo2 = (ImageView) myFragment.findViewById(R.id.flipper_photo2);
        photo3 = (ImageView) myFragment.findViewById(R.id.flipper_photo3);
        photo4 = (ImageView) myFragment.findViewById(R.id.flipper_photo4);

        camera1.setOnClickListener(this);
        camera2.setOnClickListener(this);
        camera3.setOnClickListener(this);
        camera4.setOnClickListener(this);

        roll1.setOnClickListener(this);
        roll2.setOnClickListener(this);
        roll3.setOnClickListener(this);
        roll4.setOnClickListener(this);

        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        delete3.setOnClickListener(this);
        delete4.setOnClickListener(this);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        newb64ph1=newb64ph2=newb64ph3=newb64ph4=null;


        photos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> mapP = dataSnapshot.getValue(Map.class);

                b64photo1=(mapP.get("photo1"));   //set them to be what you find in Firebase
                b64photo2=(mapP.get("photo2"));
                b64photo3=(mapP.get("photo3"));
                b64photo4=(mapP.get("photo4"));

                if (b64photo1!=null){
                    if (b64photo1.equals("deleted")==false && b64photo1.equals("null")==false ){
                        ph1 = decodeBase64(b64photo1);
                        photo1.setImageBitmap(ph1);
                    }
                }

                if (b64photo2 != null){
                    if (b64photo2.equals("deleted") == false && b64photo2.equals("null")==false) {
                        ph2 = decodeBase64(b64photo2);
                        photo2.setImageBitmap(ph2);
                    }
                }

                if (b64photo3 != null){
                    if (b64photo3.equals("deleted") == false && b64photo3.equals("null")==false) {
                        ph3 = decodeBase64((b64photo3));
                        photo3.setImageBitmap(ph3);
                    }
                }

                if (b64photo4 != null){
                    if (b64photo4.equals("deleted") == false && b64photo4.equals("null")==false) {
                        ph4 = decodeBase64(b64photo4);
                        photo4.setImageBitmap(ph4);
                    }
                }

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

        String restaurant11= "Tutto PizzaCorso Duca Degli Abruzzi 19";

        // CHECK FOR PHOTOS AS WELL
        mPhotosRef = new Firebase("https://popping-inferno-6667.firebaseio.com/photos");
        photos = mPhotosRef.child(restaurant11);



    }

        @Override
    public void onClick(View v) {
        if (v==cancel){
            dismiss();
        }
        if (v==camera1){
            Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST1);

        }
        if (v==camera2){
            Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST2);
        }
        if (v==camera3){
            Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST3);
        }
        if (v==camera4){
            Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, CAMERA_IMAGE_REQUEST4);
        }
        if(v==roll1){
            Intent photopicker = new Intent(Intent.ACTION_PICK);   //invoking image gallery

            File picturedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String picdirStr = picturedir.getPath();

            Uri data = Uri.parse(picdirStr);

            photopicker.setDataAndType(data, "image/*");   //get me ANY image type

            startActivityForResult(photopicker, IMAGE_GALLERY_REQUEST1);   //get image back from roll



        }
        if(v==roll2){
            Intent photopicker = new Intent(Intent.ACTION_PICK);   //invoking image gallery

            File picturedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String picdirStr = picturedir.getPath();

            Uri data = Uri.parse(picdirStr);

            photopicker.setDataAndType(data, "image/*");   //get me ANY image type

            startActivityForResult(photopicker, IMAGE_GALLERY_REQUEST2);   //get image back from roll



        }
        if(v==roll3){
            Intent photopicker = new Intent(Intent.ACTION_PICK);   //invoking image gallery

            File picturedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String picdirStr = picturedir.getPath();

            Uri data = Uri.parse(picdirStr);

            photopicker.setDataAndType(data, "image/*");   //get me ANY image type

            startActivityForResult(photopicker, IMAGE_GALLERY_REQUEST3);   //get image back from roll

        }
        if(v==roll4){
            Intent photopicker = new Intent(Intent.ACTION_PICK);   //invoking image gallery

            File picturedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String picdirStr = picturedir.getPath();

            Uri data = Uri.parse(picdirStr);

            photopicker.setDataAndType(data, "image/*");   //get me ANY image type

            startActivityForResult(photopicker, IMAGE_GALLERY_REQUEST4);   //get image back from roll

        }
        if(v==delete1){
            deleted1=true;
            newb64ph1="deleted";
            photo1.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
        }
        if(v==delete2){
            deleted2=true;
            newb64ph2="deleted";
            photo2.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
        }
        if(v==delete3){
            deleted3=true;
            newb64ph3="deleted";
            photo3.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
        }
        if (v==delete4){
            deleted4=true;
            newb64ph4="deleted";
            photo4.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
        }
        if (v==confirm){
            // SAVE IMAGES ON JSON FILE
           if (newb64ph1 != null) photos.child("photo1").setValue(newb64ph1);
           if (newb64ph2 != null) photos.child("photo2").setValue(newb64ph2);
           if (newb64ph3 != null) photos.child("photo3").setValue(newb64ph3);
           if (newb64ph4 != null) photos.child("photo4").setValue(newb64ph4);


            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction;

            transaction = manager.beginTransaction();
            transaction.replace(R.id.flContent, new DetailsFragment());
            transaction.commit();
            dismiss();



        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == IMAGE_GALLERY_REQUEST1){
               Uri imageUri= data.getData();   //Uri = address of image on sd card
               InputStream inputS;

                try {
                    inputS = getContext().getContentResolver().openInputStream(imageUri);  //getting input stream based on Uri

                    ph1=BitmapFactory.decodeStream(inputS);
                    photo1.setImageBitmap(ph1);

                    newb64ph1 = encodeToBase64(ph1, Bitmap.CompressFormat.JPEG, 50);  //the NEW image encoded ready to be sent to JSON file

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }if (requestCode == IMAGE_GALLERY_REQUEST2){
                Uri imageUri= data.getData();   //Uri = address of image on sd card
                InputStream inputS;

                try {
                    inputS = getContext().getContentResolver().openInputStream(imageUri);  //getting input stream based on Uri

                    ph2=BitmapFactory.decodeStream(inputS);
                    photo2.setImageBitmap(ph2);

                    newb64ph2 = encodeToBase64(ph2,Bitmap.CompressFormat.JPEG, 50 );

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }if (requestCode == IMAGE_GALLERY_REQUEST3){
                Uri imageUri= data.getData();   //Uri = address of image on sd card
                InputStream inputS;

                try {
                    inputS = getContext().getContentResolver().openInputStream(imageUri);  //getting input stream based on Uri

                    ph3=BitmapFactory.decodeStream(inputS);
                    photo3.setImageBitmap(ph3);

                    newb64ph3 = encodeToBase64(ph3, Bitmap.CompressFormat.JPEG, 50);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }if (requestCode == IMAGE_GALLERY_REQUEST4){
                Uri imageUri= data.getData();   //Uri = address of image on sd card
                InputStream inputS;

                try {
                    inputS = getContext().getContentResolver().openInputStream(imageUri);  //getting input stream based on Uri

                    ph4=BitmapFactory.decodeStream(inputS);
                    photo4.setImageBitmap(ph4);

                    newb64ph4 = encodeToBase64(ph4, Bitmap.CompressFormat.JPEG, 50);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }if (requestCode == CAMERA_IMAGE_REQUEST1){
                Bitmap cameraph1 = (Bitmap) data.getExtras().get("data");   //ACCESS THE IMAGE
                photo1.setImageBitmap(cameraph1);
                newb64ph1 = encodeToBase64(cameraph1, Bitmap.CompressFormat.JPEG, 50);  //the NEW image encoded ready to be sent to JSON file

            }if (requestCode == CAMERA_IMAGE_REQUEST2){
                Bitmap cameraph2 = (Bitmap) data.getExtras().get("data");   //ACCESS THE IMAGE
                photo2.setImageBitmap(cameraph2);
                newb64ph2 = encodeToBase64(cameraph2, Bitmap.CompressFormat.JPEG, 50);  //the NEW image encoded ready to be sent to JSON file

            }if (requestCode == CAMERA_IMAGE_REQUEST3){
                Bitmap cameraph3 = (Bitmap) data.getExtras().get("data");   //ACCESS THE IMAGE
                photo3.setImageBitmap(cameraph3);
                newb64ph3 = encodeToBase64(cameraph3, Bitmap.CompressFormat.JPEG, 50);  //the NEW image encoded ready to be sent to JSON file

            }if (requestCode == CAMERA_IMAGE_REQUEST4){
                Bitmap cameraph4 = (Bitmap) data.getExtras().get("data");   //ACCESS THE IMAGE
                photo4.setImageBitmap(cameraph4);
                newb64ph4 = encodeToBase64(cameraph4, Bitmap.CompressFormat.JPEG, 50);  //the NEW image encoded ready to be sent to JSON file

            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }


        if (b64photo1 != null) {
            if (b64photo1.equals("deleted") == false && b64photo1.equals("null") == false) {
                photo1.setImageBitmap(ph1);
            } else
                photo1.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
        }

        if (b64photo2 != null) {
            if (b64photo2.equals("deleted") == false && b64photo2.equals("null") == false) {
                photo2.setImageBitmap(ph2);

            } else
                photo2.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
        }

        if (b64photo3 != null) {
            if (b64photo3.equals("deleted") == false && b64photo3.equals("null") == false) {
                photo3.setImageBitmap(ph3);

            } else
                photo3.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
        }

        if (b64photo4 != null) {
            if (b64photo4.equals("deleted") == false && b64photo4.equals("null") == false) {
                photo4.setImageBitmap(ph4);

            } else
                photo4.setImageDrawable(getResources().getDrawable(R.drawable.ic_empty_flipper_image));
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

    private void loadDataFromJsonFile(){
        String filename = "details.xml";
        FileInputStream fis;
        InputStreamReader isr;
        BufferedReader bufferedReader;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            fis = getActivity().openFileInput(filename);
            isr = new InputStreamReader(fis);
            bufferedReader = new BufferedReader(isr);

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            fis.close();
        }catch (IOException e) {
            // TODO handle exception
        }

        String data = sb.toString();

        Gson gson = new Gson();
        Type DetailsType = new TypeToken<RestaurantDetails>(){}.getType();
        desR=gson.fromJson(data, DetailsType);

    }


    private void saveDataToJsonFile(){
        Gson gson = new Gson();
        Type DetailsType = new TypeToken<RestaurantDetails>(){}.getType();
        String data = gson.toJson(desR, DetailsType);
        String filename = "details.xml";

        FileOutputStream outputStream;

        try {
            outputStream = getActivity().getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
