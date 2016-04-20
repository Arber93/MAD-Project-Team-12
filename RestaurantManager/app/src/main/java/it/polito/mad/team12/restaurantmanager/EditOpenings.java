package it.polito.mad.team12.restaurantmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class EditOpenings extends Fragment implements View.OnClickListener{

    EditText phone;
    EditText mon;
    EditText tu;
    EditText we;
    EditText th;
    EditText fr;
    EditText sa;
    EditText su;


    public EditOpenings() {
        // Required empty public constructor
    }

    private View myFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myFragment= inflater.inflate(R.layout.fragment_layout_edit_openings, container, false);
        Button confirm = (Button) myFragment.findViewById(R.id.confirmb);
        Button cancel =(Button) myFragment.findViewById(R.id.cancelb);

        phone=(EditText) myFragment.findViewById(R.id.phoneNumber);
        mon=(EditText) myFragment.findViewById(R.id.editText2);
        tu=(EditText) myFragment.findViewById(R.id.editText9);
        we=(EditText) myFragment.findViewById(R.id.editText10);
        th=(EditText) myFragment.findViewById(R.id.editText11);
        fr=(EditText) myFragment.findViewById(R.id.editText12);
        sa=(EditText) myFragment.findViewById(R.id.editText13);
        su=(EditText) myFragment.findViewById(R.id.editText14);



        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return myFragment;
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("details.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.confirmb) {

            try {
                JSONObject obj = new JSONObject(loadJSONFromAsset());
                JSONArray m_jArry = obj.getJSONArray("details");

                for (int i = 0; i < m_jArry.length(); i++) {

                    JSONObject jsonObject = m_jArry.getJSONObject(i);

                   jsonObject.put("phonen", 33);





                }

            } catch (JSONException e) {
                e.printStackTrace();
            }




        } else if(v.getId() == R.id.cancelb) {

        }
    }
}
