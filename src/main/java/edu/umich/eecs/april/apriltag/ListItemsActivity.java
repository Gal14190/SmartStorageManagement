package edu.umich.eecs.april.apriltag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Vector;

public class ListItemsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        overridePendingTransition(R.anim.slide_scale, R.anim.slide_down);
        setContentView(R.layout.list_items_activity);

        //String[] myData = {"Item 1", "Item 2", "Item 3"};
        Model model = Model.getInstance();
        model.setDataFetch(new DataFetchThread(model));
        Vector<ItemModel> myData = model.getDataFetch().getAllItems();

        ItemsAdapter adapter = new ItemsAdapter(this, myData);
        ListView listView = (ListView) findViewById(R.id.list_items);
        listView.setAdapter(adapter);

        listView.setAdapter(adapter);
    }
}


class ItemsAdapter extends ArrayAdapter<ItemModel> {
    Context context;
    Vector<ItemModel> data;

    public ItemsAdapter(Context context, Vector<ItemModel> data) {
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

        new DownloadImageAsync(((ImageView) view.findViewById(R.id.itemImageList))).execute(contact.getImageURL());

        ((TextView) view.findViewById(R.id.itemNameList)).setText(contact.getName());
        ((TextView) view.findViewById(R.id.itemSerialList)).setText(contact.getSerialNumber());

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
