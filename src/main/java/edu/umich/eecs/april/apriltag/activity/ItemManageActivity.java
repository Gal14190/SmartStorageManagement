package edu.umich.eecs.april.apriltag.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.umich.eecs.april.apriltag.R;
import edu.umich.eecs.april.apriltag.helper.DownloadImageAsync;
import edu.umich.eecs.april.apriltag.model.ItemModel;
import edu.umich.eecs.april.apriltag.model.Model;
import edu.umich.eecs.april.apriltag.model.TrackModel;

public class ItemManageActivity extends AppCompatActivity {
    private final String BUY_URL_WEBSITE = "https://www.google.com/search?q=";
    private ItemModel item;
    private int index;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        setContentView(R.layout.item_manage_activity);

        model = new ViewModelProvider(this).get(Model.class);

        item = Model.getDataFetch().getItemById(Model.getItemIdDetected());
        index = Model.getDataFetch().getAllItems().indexOf(item);

        ((TextView) findViewById(R.id.itemNameManage)).setText(item.getName());
        ((TextView) findViewById(R.id.itemSerialNumberManage)).setText(item.getSerialNumber());
        ((TextView) findViewById(R.id.itemDescriptionManage)).setText(item.getDescription());
        ((TextView) findViewById(R.id.itemAmountManage)).setText(Integer.toString(item.getAmount()));
        new DownloadImageAsync(((ImageView) findViewById(R.id.itemImage))).execute(item.getImageURL());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        String dateString = format.format(calendar.getTime()); // Get the formatted date as a string

        ((Button) findViewById(R.id.incBtn)).setOnClickListener(view -> {
            addAmount(1);
            Model.getDataFetch().addTrack(new TrackModel(item.getId(), Model.getUserName(), dateString, true));
        });

        ((Button) findViewById(R.id.decBtn)).setOnClickListener(view -> {
            addAmount(-1);
            Model.addTrack(new TrackModel(item.getId(), Model.getUserName(), dateString, false));
        });

        WebView webView = (WebView) findViewById(R.id.webViewMange);
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript (optional)
        webView.loadUrl(BUY_URL_WEBSITE + item.getSerialNumber());

    }

    private void addAmount(int n) {
        ItemModel item = Model.getDataFetch().getAllItems().get(index);
        int amount = item.getAmount() + n;

        if(amount >= 0) {
            item.setAmount(amount);
            Model.getDataFetch().updateAmount(item.getDoc(), amount);
        }

        ((TextView) findViewById(R.id.itemAmountManage)).setText(Integer.toString(item.getAmount()));
    }
}
