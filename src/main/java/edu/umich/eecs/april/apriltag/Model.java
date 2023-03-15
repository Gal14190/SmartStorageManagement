package edu.umich.eecs.april.apriltag;

import android.content.Intent;

public class Model {
    public enum mode { STORAGE, ITEMS, SPECIFIC_ITEM }

    private static mode mModeDetection;
    private static int mSpecificPartId;

    private PanelItemPopupModel panelItemPopupModel;
    private static DataFetchThread mData;

    private static int mItemIdDetected;
    private static boolean mIsDetected;

    private static String userName;

    public Model() {
        mModeDetection = mode.SPECIFIC_ITEM;
        mSpecificPartId = 1;
    }

    public Intent sendItemDetail(Intent intent) {
        ItemModel item = mData.getItemById(mItemIdDetected);
        intent.putExtra("item.id", item.getId());
        intent.putExtra("item.name", item.getName());
        intent.putExtra("item.amount", item.getAmount());
        intent.putExtra("item.description", item.getDescription());
        intent.putExtra("item.serialNumber", item.getSerialNumber());

        return intent;
    }

    public static Model getInstance() { return new Model();}

    public static int getItemIdDetected() {
        return mItemIdDetected;
    }

    public static void setItemIdDetected(int itemIdDetected) {
        mItemIdDetected = itemIdDetected;
    }

    public static boolean isDetected() {
        return mIsDetected;
    }

    public static void setDetected(boolean detected) {
        mIsDetected = detected;
    }

    public static DataFetchThread getDataFetch() {
        return mData;
    }

    public static void setDataFetch(DataFetchThread data) {
        mData = data;
    }

    public PanelItemPopupModel getPanelItemPopupModel() {
        return panelItemPopupModel;
    }

    public void setPanelItemPopupModel(PanelItemPopupModel panelItemPopupModel) {
        this.panelItemPopupModel = panelItemPopupModel;
    }

    public static mode getModeDetection() {
        return mModeDetection;
    }

    public static void setModeDetection(mode modeDetection) {
        mModeDetection = modeDetection;
    }

    public static int getSpecificPartId() {
        return mSpecificPartId;
    }

    public static void setSpecificPartId(int specificPartId) {
        mSpecificPartId = specificPartId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Model.userName = userName;
    }
}
