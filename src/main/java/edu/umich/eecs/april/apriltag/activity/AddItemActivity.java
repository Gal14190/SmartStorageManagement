package edu.umich.eecs.april.apriltag.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import edu.umich.eecs.april.apriltag.R;
import edu.umich.eecs.april.apriltag.model.ItemModel;
import edu.umich.eecs.april.apriltag.model.Model;

public class AddItemActivity extends AppCompatActivity {
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private final int REQUEST_CODE_SELECT_IMAGE = 1;
    private String imgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        setContentView(R.layout.add_item_activity);

        int id = Model.mData.getAllItems().size() - 1;

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ((Button) findViewById(R.id.itemImageBtn)).setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_CODE_SELECT_IMAGE);
        });

        ((Button) findViewById(R.id.addBtn)).setOnClickListener(view -> {
            String name = ((EditText) findViewById(R.id.itemNameInput)).getText().toString();
            String serialNumber = ((EditText) findViewById(R.id.itemSerialNumberInput)).getText().toString();
            int amount = 0;
            try {
                amount = Integer.parseInt(((EditText) findViewById(R.id.itemAmountInput)).getText().toString());
            } catch (Exception e) {
                amount = 0;
            }
            int storage = 0;
            try {
                storage = Integer.parseInt(((EditText) findViewById(R.id.itemStorageIdInput)).getText().toString());
            } catch (Exception e) {
                storage = 0;
            }
            String description = ((EditText) findViewById(R.id.itemDescriptionInput)).getText().toString();


            if(name.isEmpty() || serialNumber.isEmpty() || description.isEmpty() || imgUri.isEmpty() || amount == 0 || storage == 0) {
                Toast.makeText(this, "Please insert information", Toast.LENGTH_SHORT).show();
            } else {
                Model.addItem(new ItemModel(id, serialNumber, storage, name, description, amount, imgUri));
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imgUri = uploadImage(selectedImageUri);
        }
    }

    // UploadImage method
    private String uploadImage(Uri filePath)
    {
        String uuid = null;

        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            uuid = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/" + uuid);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e)
                        {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage( "Uploaded " + (int)progress + "%");
                                }
                            });
        }

        return uuid;
    }
}
