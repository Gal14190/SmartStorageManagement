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

        Model model = Model.getInstance();

        int itemId = getIntent().getIntExtra("itemId", -1);

        if(itemId != -1) {
            ((TextView) findViewById(R.id.itemNameManage)).setText(model.getDataFetch().getItemById(itemId).getName());
        }
    }
}
