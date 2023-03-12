package edu.umich.eecs.april.apriltag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ItemManageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        setContentView(R.layout.item_manage_activity);

        int id = (int)getIntent().getIntExtra("item.id", -1);
        String name = (String) getIntent().getStringExtra("item.name");
        int amount = (int) getIntent().getIntExtra("item.amount", -1);
        String description = (String) getIntent().getStringExtra("item.description");
        String serialNumber = (String) getIntent().getStringExtra("item.serialNumber");

        if(id != -1) {
            ((TextView) findViewById(R.id.itemNameManage)).setText(name);
            ((TextView) findViewById(R.id.itemSerialNumberManage)).setText(serialNumber);
            ((TextView) findViewById(R.id.itemDescriptionManage)).setText(description);
            ((TextView) findViewById(R.id.itemAmountManage)).setText("" + amount);
        }
    }
}
