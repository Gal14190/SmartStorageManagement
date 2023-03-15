package edu.umich.eecs.april.apriltag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemManageActivity extends AppCompatActivity {
    private final String BUY_URL_WEBSITE = "https://www.google.com/search?q=";
    private ItemModel item;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        setContentView(R.layout.item_manage_activity);

        item = Model.getDataFetch().getItemById(Model.getItemIdDetected());
        index = Model.getDataFetch().getAllItems().indexOf(item);

        ((TextView) findViewById(R.id.itemNameManage)).setText(item.getName());
        ((TextView) findViewById(R.id.itemSerialNumberManage)).setText(item.getSerialNumber());
        ((TextView) findViewById(R.id.itemDescriptionManage)).setText(item.getDescription());
        ((TextView) findViewById(R.id.itemAmountManage)).setText(Integer.toString(item.getAmount()));
        new DownloadImageAsync(((ImageView) findViewById(R.id.itemImage))).execute(item.getImageURL());

        ((Button) findViewById(R.id.incBtn)).setOnClickListener(view -> {
            addAmount(1);
        });

        ((Button) findViewById(R.id.decBtn)).setOnClickListener(view -> {
            addAmount(-1);
        });

        WebView webView = (WebView) findViewById(R.id.webViewMange);
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript (optional)
        webView.loadUrl(BUY_URL_WEBSITE + item.getSerialNumber());

    }

    private void addAmount(int n) {
        int amount = Model.getDataFetch().getAllItems().get(index).getAmount();

        if(amount + n >= 0)
            Model.getDataFetch().getAllItems().get(index).setAmount(amount + n);

        ((TextView) findViewById(R.id.itemAmountManage)).setText(Integer.toString(item.getAmount()));
    }
}
