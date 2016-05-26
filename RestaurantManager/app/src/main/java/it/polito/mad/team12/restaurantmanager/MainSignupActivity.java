package it.polito.mad.team12.restaurantmanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class MainSignupActivity extends AppCompatActivity {

    private EditText email,password,name,resname,restreet;
    private Button signupbutton;
    private RadioButton simple,manager;
    Firebase mRef = new Firebase("https://popping-inferno-6667.firebaseio.com");
    String potentialRestaurant;
    String userName, userUID;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private TextView loginLink;
    private Spinner spinnerCategories;
    private String category="";
    private String categoryIT="";
    ArrayAdapter<CharSequence> catAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signup);

        loginLink = (TextView) findViewById(R.id.link_login);
        name = (EditText) findViewById(R.id.signup_input_name);
        resname = (EditText) findViewById(R.id.signup_restaurantname);
        restreet = (EditText) findViewById(R.id.signup_streetrestaurant);
        email= (EditText) findViewById(R.id.signup_input_email);
        password = (EditText) findViewById(R.id.signup_input_password);
        signupbutton = (Button) findViewById(R.id.btn_signup);
        spinnerCategories = (Spinner) findViewById(R.id.customer_spinner_foodcategory);

        catAd = ArrayAdapter.createFromResource(this, R.array.foodcategories, android.R.layout.simple_spinner_item);
        catAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);   //built-in layout resources
        spinnerCategories.setAdapter(catAd);

        simple = (RadioButton) findViewById(R.id.signup_simple_customer);
        manager = (RadioButton) findViewById(R.id.signup_restaurant_manager);

        if (simple.isChecked()){

            resname.setVisibility(View.GONE);
            restreet.setVisibility(View.GONE);
            spinnerCategories.setVisibility(View.GONE);
        }

        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resname.setVisibility(View.GONE);
                restreet.setVisibility(View.GONE);
                spinnerCategories.setVisibility(View.GONE);

            }
        });

        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resname.setVisibility(View.VISIBLE);
                restreet.setVisibility(View.VISIBLE);
                spinnerCategories.setVisibility(View.VISIBLE);
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainLoginActivity.class);
                startActivity(intent);
            }
        });


        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (id == 0) {   //ALL THE CATEGORIES
                    category = "wrong";
                }
                if (id == 1) {   //AMERICAN
                    category="American";
                    categoryIT="Americano";
                }
                if (id == 2) {    //ASIAN
                    category="Asian";
                    categoryIT="Asiatico";
                }
                if (id == 3) {   //BEVERAGES
                    category="Beverages";
                    categoryIT="Bevande";
                }
                if (id == 4) {   //HAMBURGERS
                    category="Hamburgers";
                    categoryIT="Hamburgers";
                }
                if (id == 5) {   //INDIAN
                    category="Indian";
                    categoryIT="Indiano";
                }
                if (id == 6) {   //ITALIAN
                    category="Italian";
                    categoryIT="Italiano";
                }
                if (id == 7) {   //GREEK
                    category="Greek";
                    categoryIT="Greco";
                }
                if (id == 8) {   //JAPANESE
                    category="Japanese";
                    categoryIT="Giapponese";
                }
                if (id == 9) {   //MEXICAN
                    category="Mexican";
                    categoryIT="Messicano";
                }
                if (id == 10) {  //PIZZA
                    category="Pizza";
                    categoryIT="Pizza";
                }
                if (id == 11) {  //THAI
                    category="Thai";
                    categoryIT="Tailandese";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category="wrong";
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simple.isChecked()){
                    if (name.getText().toString().length() >0 && email.getText().toString().length() > 0 && password.getText().toString().length() > 0){
                                signupsimple();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainSignupActivity.this);
                        builder.setMessage(getResources().getString(R.string.errorsignup))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                } else if (manager.isChecked()) {
                    if (name.getText().toString().length() >0 && email.getText().toString().length() >0 && password.getText().toString().length() >0 && resname.getText().toString().length() >0 && restreet.getText().toString().length() >0){


                        if (category.equals("wrong")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainSignupActivity.this);
                            builder.setMessage(getResources().getString(R.string.selectcategory))
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            signupmanager();
                        }


                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainSignupActivity.this);
                        builder.setMessage(getResources().getString(R.string.errorsignup))
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }


                }


        });



    }

    public void signupsimple(){

        final ProgressDialog progressDialog = new ProgressDialog(MainSignupActivity.this,
                ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.authenticating));
        progressDialog.show();

        mRef.createUser(email.getText().toString(),password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>(){
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));

                Firebase usersRef = mRef.child("users").child(result.get("uid").toString());
                Map<String, String> userMap = new HashMap<String, String>();
                userMap.put("userName", name.getText().toString());
                userMap.put("managerOf", "null");
                Map<String, Map<String, String>> users = new HashMap<String, Map<String, String>>();
                users.put(result.get("uid").toString(), userMap);
                usersRef.setValue(users);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainSignupActivity.this);
                builder.setMessage(getResources().getString(R.string.signupsuccess))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.gotologin), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(MainSignupActivity.this, MainLoginActivity.class);
                                startActivity(i);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
            @Override
            public void onError(FirebaseError firebaseError) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainSignupActivity.this);
                builder.setMessage(getResources().getString(R.string.signupwrong))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                progressDialog.cancel();

            }
        });



    }

    public void signupmanager(){

        final ProgressDialog progressDialog = new ProgressDialog(MainSignupActivity.this,
                ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.authenticating));
        progressDialog.show();


        mRef.createUser(email.getText().toString(),password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>(){
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));

                Firebase usersRef = mRef.child("users").child(result.get("uid").toString());
                usersRef.child("managerOf").setValue(resname.getText().toString()+" "+restreet.getText().toString());
                usersRef.child("userName").setValue(name.getText().toString());

                Firebase restaurantRef = mRef.child("restaurants").child(resname.getText().toString()+" "+restreet.getText().toString());

                RestaurantDetails dres = new RestaurantDetails("newer");
                dres.setRestaurantAddress(restreet.getText().toString());
                dres.setCategoryFood(category);
                dres.setCategoryFoodIT(categoryIT);
                dres.setRestaurantName(resname.getText().toString());

                restaurantRef.setValue(dres);

                Firebase photosRed = mRef.child("photos").child(resname.getText().toString()+" "+restreet.getText().toString());
                photosRed.child("photo1").setValue("null");
                photosRed.child("photo2").setValue("null");
                photosRed.child("photo3").setValue("null");
                photosRed.child("photo4").setValue("null");


                AlertDialog.Builder builder = new AlertDialog.Builder(MainSignupActivity.this);
                builder.setMessage(getResources().getString(R.string.signupsuccess))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.gotologin), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent i = new Intent(MainSignupActivity.this, MainLoginActivity.class);
                                startActivity(i);
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                progressDialog.cancel();


            }
            @Override
            public void onError(FirebaseError firebaseError) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainSignupActivity.this);
                builder.setMessage(getResources().getString(R.string.signupwrong))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                progressDialog.cancel();

            }
        });




    }



}
