package edu.umich.eecs.april.apriltag.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.umich.eecs.april.apriltag.R;
import edu.umich.eecs.april.apriltag.model.ItemModel;
import edu.umich.eecs.april.apriltag.model.Model;

public class AddItemActivity extends AppCompatActivity {

    private final int REQUEST_CODE_SELECT_IMAGE = 1;
    private String imgUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        setContentView(R.layout.add_item_activity);

        int id = Model.getItemIdDetected();

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
            imgUri = selectedImageUri.toString();
            // Do something with the selected image URI
        }
    }

}
