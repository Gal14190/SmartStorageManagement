package edu.umich.eecs.april.apriltag.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import edu.umich.eecs.april.apriltag.R;
import edu.umich.eecs.april.apriltag.model.ItemModel;
import edu.umich.eecs.april.apriltag.model.Model;
import edu.umich.eecs.april.apriltag.model.TrackModel;

public class ListTrackActivity extends AppCompatActivity {
    private final int STORAGE_BEGIN_NUMBER = 500;
    private final int NUMBER_OF_STORAGE = 20;

    private Model model;

    private ListView listView;
    private ArrayList<TrackModel> track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_scale, R.anim.slide_down);
        setContentView(R.layout.list_track_activity);

        model = new ViewModelProvider(this).get(Model.class);

        listView = (ListView) findViewById(R.id.list_track);
        track = Model.getDataFetch().getAllTracks();

        TrackAdapter adapter = new TrackAdapter(this, track);
        listView.setAdapter(adapter);
    }
}


class TrackAdapter extends ArrayAdapter<TrackModel> {
    Context context;
    ArrayList<TrackModel> data;

    public TrackAdapter(Context context, ArrayList<TrackModel> data) {
        super(context, R.layout.track_row, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.track_row, null);
        }

        TrackModel contact = data.get(position);
        ItemModel item = Model.getDataFetch().getItemById(contact.getItemId());

        ((TextView) view.findViewById(R.id.itemName)).setText(item.getName());
        ((TextView) view.findViewById(R.id.itemSerialNumber)).setText(item.getSerialNumber());

        ((TextView) view.findViewById(R.id.userName)).setText(contact.getUserName().replace(" ", "\n"));
        ((TextView) view.findViewById(R.id.timeDate)).setText(contact.getTime().replace(" ", "\n"));

        ((ImageView) view.findViewById(R.id.iconFunc)).setImageResource(contact.isFunc()? R.drawable.increase : R.drawable.decrease);

        return view;
    }
}
