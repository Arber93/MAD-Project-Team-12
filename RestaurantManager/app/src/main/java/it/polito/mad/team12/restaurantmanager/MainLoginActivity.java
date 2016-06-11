package it.polito.mad.team12.restaurantmanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

//import butterknife.ButterKnife;
//import butterknife.InjectView;

public class MainLoginActivity extends AppCompatActivity {

    private TextView signupLink;
    private EditText email,password;
    private Button loginbutton;
    private static final int REQUEST_SIGNUP = 0;
    Firebase mRef = new Firebase("https://popping-inferno-6667.firebaseio.com");
    String potentialRestaurant;
    String userName, userUID;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        signupLink = (TextView) findViewById(R.id.main_login_signup);
        email= (EditText) findViewById(R.id.main_login_email);
        password = (EditText) findViewById(R.id.main_login_password);
        loginbutton = (Button) findViewById(R.id.main_login_button_login);



       // ButterKnife.inject(this);
        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        editor = pref.edit();
        String getStatus=pref.getString("loggedin", "nil");
        if(getStatus.equals("true")) {
            //ALREADY LOGGED IN
            if (pref.getString("restID","nil").equals("null")) {
                Intent i = new Intent(this, CustomerMainActivity.class);
                String uI = pref.getString("userID", "nil");
                i.putExtra("userUID", uI);
                startActivity(i);
            }else{
                Intent i = new Intent(this, MainActivity.class);
                String rI = pref.getString("restID", "nil");
                i.putExtra("restName", rI );
                startActivity(i);
            }

        }else{


        }

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (email.getText().toString().length() > 0  && password.getText().toString().length() > 0) {
                        login();
                    }
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainSignupActivity.class);
                startActivity(intent);
            }
        });

    }




    public void login(){

        final ProgressDialog progressDialog = new ProgressDialog(MainLoginActivity.this,
                ProgressDialog.THEME_HOLO_DARK);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.authenticating));
        progressDialog.show();

        mRef.authWithPassword(email.getText().toString(), password.getText().toString(),
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Firebase user;
                        user=mRef.child("users").child(authData.getUid());  //RETRIEVE THE USER
                        userUID=authData.getUid();
                        user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                   potentialRestaurant = (String) snapshot.child("managerOf").getValue();
                                   userName = (String) snapshot.child("userName").getValue();

                                enterActivity();
                                progressDialog.cancel();


                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });



                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainLoginActivity.this);
                        builder1.setMessage(getResources().getString(R.string.failedlogin));
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                getResources().getString(R.string.signup),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(getApplicationContext(), MainSignupActivity.class);
                                        startActivity(intent);
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                getResources().getString(R.string.retry),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }



                });



    }





    public void enterActivity(){

        editor.putString("loggedin","true");
        editor.putString("userID",userUID);
        editor.putString("restID",potentialRestaurant);
        editor.putString("nameU", userName );
        editor.commit();

       if (potentialRestaurant.equals("null")) {
            Intent i = new Intent(this, CustomerMainActivity.class);
            i.putExtra("userUID", userUID);
            startActivity(i);
           finish();
        }else{
            Intent i = new Intent(this, MainActivity.class);
           i.putExtra("restName", potentialRestaurant);
           startActivity(i);
           finish();
       }

    }
}
