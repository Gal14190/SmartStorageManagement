package edu.umich.eecs.april.apriltag;

import android.content.Context;
import android.widget.TextView;

public class PanelItemPopupModel {
    public Context context;
    private TextView mItemNameTextView;
    private TextView mItemSerialNumberTextView;

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
