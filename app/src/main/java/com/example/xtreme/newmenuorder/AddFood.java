package com.example.xtreme.newmenuorder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFood extends AppCompatActivity {


    private  static  final int GALLREQ =1; //Image Gallery redirect request using ImageButton
    private ImageButton foodImage;//Get a reference to the buttons from the activity_add_food.xml
    private EditText name,desc,price;
    private Uri uri = null;//Uri for the image
    //Add reference to the firebase cloud storage
    private StorageReference mStorageReference = null;
    //Connecting to the cloud database
    private DatabaseReference mRef;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        //Initialize the local global variables above
        name = (EditText) findViewById(R.id.itemName);
        desc = (EditText) findViewById(R.id.itemDesc);
        price = (EditText) findViewById(R.id.itemPrice);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        //Get reference to the place we want to store the values in the database
        mRef = FirebaseDatabase.getInstance().getReference("Item");
    }

    /**
     * This button start the galerry intent
     * @param view
     */
    public void imageButtonClicked(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*"); //Important to enable selection of image
        startActivityForResult(galleryIntent,GALLREQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Check for the request and resultCode
        if (requestCode == GALLREQ && resultCode==RESULT_OK){
            uri = data.getData();
            //Reference to the food image button
            foodImage =(ImageButton) findViewById(R.id.foodImageButton);
            foodImage.setImageURI(uri); //Set the food image to uri
        }
    }

    public void addItemButtonClicked(View view)
    {
        //Extract values from the EditText as strings value
        final String name_text = name.getText().toString().trim();
        final String desc_text = desc.getText().toString().trim();
        final String price_text = price.getText().toString().trim();
        //Check to see if a field is empty before uploading to database
        if (!TextUtils.isEmpty(name_text) && !TextUtils.isEmpty(desc_text) && !TextUtils.isEmpty(price_text) ){
            //Put the uri last pathe to the storage reference
            StorageReference filepath = mStorageReference.child(uri.getLastPathSegment());

            //put the file into the storage, the addOnSuccessListener inform that the data was uploaded
            //successfully by linking it to the onSuccess method.
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //final Uri downloadurl = taskSnapshot.getDownloadUrl();

                     final Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    //Show a message to the user of the success
                    Toast.makeText(AddFood.this,"Image uploaded",Toast.LENGTH_LONG).show();
                    //Post the new values to the mRef location
                    final DatabaseReference newPost = mRef.push();
                    newPost.child("name").setValue(name_text);
                    newPost.child("desc").setValue(desc_text);
                    newPost.child("price").setValue(price_text);
                    newPost.child("image").setValue(downloadUrl.toString());


                }
            });
        }
    }




}
