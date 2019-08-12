package com.example.socialmediaapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText edtDisplayName, edtBio;
    private Button btnUpdateInfo;


    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        edtDisplayName = view.findViewById(R.id.edtDisplayName);
        edtBio = view.findViewById(R.id.edtBio);

        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);

        final ParseUser parseUser =  ParseUser.getCurrentUser();

        // Show previously inputted User information
        if (parseUser.get("displayName") == null){
            edtDisplayName.setText("");
        } else {
            edtDisplayName.setText(parseUser.get("displayName").toString());
        }
        if (parseUser.get("bio") == null){
            edtBio.setText("");
        } else {
            edtBio.setText(parseUser.get("bio").toString());
        }

        // Update User information
        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseUser.put("displayName", edtDisplayName.getText().toString());
                parseUser.put("bio", edtBio.getText().toString());
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null) {
                            FancyToast.makeText(getContext(), "Profile updated successfully!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                        } else{
                            FancyToast.makeText(getContext(), e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                        }
                    }
                });
                }
        });
        return view;
    }

}
