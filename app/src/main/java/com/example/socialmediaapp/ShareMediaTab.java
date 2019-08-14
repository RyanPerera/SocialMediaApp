package com.example.socialmediaapp;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareMediaTab extends Fragment implements View.OnClickListener{

    private ImageView imgShareImg;
    private EditText edtImgDescription;
    private Button btnShare;

    Bitmap receivedImgBmp;

    public ShareMediaTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_share_media_tab, container, false);

        imgShareImg = view.findViewById(R.id.imgShareImg);
        edtImgDescription = view.findViewById(R.id.edtImgDescription);
        btnShare = view.findViewById(R.id.btnShare);

        imgShareImg.setOnClickListener(ShareMediaTab.this);
        btnShare.setOnClickListener(ShareMediaTab.this);


        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.imgShareImg:

                if (android.os.Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                } else{
                    getChosenImg();
                }

                break;
            case R.id.btnShare:

                if (receivedImgBmp != null){
                    if (edtImgDescription.getText().toString().equals("")){
                        FancyToast.makeText(getContext(), "Give your image a description!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    } else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImgBmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("Image");
                        parseObject.put("image", parseFile);
                        parseObject.put("imgDescription", edtImgDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog =  new ProgressDialog(getContext());
                        dialog.setMessage("Uploading! Please wait.");
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){
                                    FancyToast.makeText(getContext(), "Image successfully uploaded!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

                                } else{
                                    FancyToast.makeText(getContext(), "Upload failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                                }
                                dialog.dismiss();
                            }
                        });
                    }

                } else{
                    FancyToast.makeText(getContext(), "Set an image to upload!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

                }

                break;
        }
    }

    private void getChosenImg(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getChosenImg();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000){

            if (resultCode == Activity.RESULT_OK){

                // Do something with captured image
                try {
                    Uri selectedImg = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImg, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    receivedImgBmp = BitmapFactory.decodeFile(picturePath);
                    imgShareImg.setImageBitmap(receivedImgBmp);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
}
