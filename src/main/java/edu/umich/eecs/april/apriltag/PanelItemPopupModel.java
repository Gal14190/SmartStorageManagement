package edu.umich.eecs.april.apriltag;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PanelItemPopupModel {
    public Context context;
    private LinearLayout mPanel;
    private TextView mItemNameTextView;
    private TextView mItemSerialNumberTextView;

    public PanelItemPopupModel(Context context) { this.context = context;}

    public LinearLayout getPanel() {
        return mPanel;
    }

    public void setPanel(LinearLayout Panel) {
        this.mPanel = Panel;
    }

    public TextView getItemNameTextView() {
        return mItemNameTextView;
    }

    public void setItemNameTextView(TextView itemNameTextView) {
        this.mItemNameTextView = itemNameTextView;
    }

    public TextView getItemSerialNumberTextView() {
        return mItemSerialNumberTextView;
    }

    public void setItemSerialNumberTextView(TextView itemSerialNumberTextView) {
        this.mItemSerialNumberTextView = itemSerialNumberTextView;
    }
}
