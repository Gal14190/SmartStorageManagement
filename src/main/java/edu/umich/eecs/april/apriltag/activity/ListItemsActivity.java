package edu.umich.eecs.april.apriltag.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import edu.umich.eecs.april.apriltag.R;
import edu.umich.eecs.april.apriltag.helper.DownloadImageAsync;
import edu.umich.eecs.april.apriltag.model.ItemModel;
import edu.umich.eecs.april.apriltag.model.Model;
import edu.umich.eecs.april.apriltag.model.StorageModel;

public class ListItemsActivity extends AppCompatActivity {
    private final int STORAGE_BEGIN_NUMBER = 500;
    private final int NUMBER_OF_STORAGE = 20;

    private ListView listView;
    private ArrayList<ItemModel> items;
    private ArrayList<String> storageSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        overridePendingTransition(R.anim.slide_scale, R.anim.slide_down);
        setContentView(R.layout.list_items_activity);

        listView = (ListView) findViewById(R.id.list_items);
        items = Model.getDataFetch().getAllItems();
        reListView();

        // STORAGE LIST INIT
        LinearLayout storageList_layout = (LinearLayout) findViewById(R.id.storageList);
        for (StorageModel storage:Model.getDataFetch().getAllStorage()) {
            addButton(storageList_layout, storage.getId());
        }
    }

    private void reListView() {
        listView.setAdapter(null);

        ArrayList<ItemModel> newData = new ArrayList<>();

        for (ItemModel d: items) {
            if(storageSelected.contains(Integer.toString(d.getStorageId()))){
                newData.add(d);
            }
        }

        ItemsAdapter adapter = new ItemsAdapter(this, newData);
        listView.setAdapter(adapter);
    }

    private void addButton(LinearLayout layout, int number) {
        ToggleButton newBtn = new ToggleButton(this);
        newBtn.setText(Integer.toString(number));
        newBtn.setTextOn(Integer.toString(number));
        newBtn.setTextOff(Integer.toString(number));
        newBtn.setBackgroundColor(Color.rgb(0x4B,0x5D,0xFB));
        newBtn.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(100),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dpToPx(6), dpToPx(10), dpToPx(6), dpToPx(10));
        newBtn.setLayoutParams(params);

        newBtn.setOnClickListener(view -> {
            if(newBtn.isChecked()) {
                view.setBackgroundColor(Color.rgb(0x00, 0x0A, 0x67));
                storageSelected.add(Integer.toString(number));
            } else {
                view.setBackgroundColor(Color.rgb(0x4B,0x5D,0xFB));
                storageSelected.remove(Integer.toString(number));
            }

            reListView();
        });

        layout.addView(newBtn);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}

/**
 * Item adapter for list view
 */
class ItemsAdapter extends ArrayAdapter<ItemModel> {
    Context context;
    ArrayList<ItemModel> data;

    public ItemsAdapter(Context context, ArrayList<ItemModel> data) {
        super(context, R.layout.item_row, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_row, null);
        }

        ItemModel contact = data.get(position);

        // download image item from firebase storage
        new DownloadImageAsync(((ImageView) view.findViewById(R.id.itemImageList)), parent.getContext()).execute(contact.getImageURL());

        // set name and serial number text views
        ((TextView) view.findViewById(R.id.itemNameList)).setText(contact.getName());
        ((TextView) view.findViewById(R.id.itemSerialList)).setText(contact.getSerialNumber());

        // push item id into the main activity on click item event
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ApriltagDetectorActivity.class);
                intent.putExtra("itemID", contact.getId());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
