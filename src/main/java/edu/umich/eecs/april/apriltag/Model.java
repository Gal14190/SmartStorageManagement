package edu.umich.eecs.april.apriltag;

import android.content.Intent;

public class Model {
    public enum mode { STORAGE, ITEMS, SPECIFIC_ITEM }

    private static mode mModeDetection;
    private static int mSpecificPartId;

    private PanelItemPopupModel panelItemPopupModel;
    private static DataFetchThread data;

    private static int itemIdDetected;
    private static boolean isDetected;

    public Model() {
        mModeDetection = mode.SPECIFIC_ITEM;
        mSpecificPartId = 1;
    }

    public Intent sendItemDetail(Intent intent) {
        ItemModel item = data.getItemById(itemIdDetected);
        intent.putExtra("item.id", item.getId());
        intent.putExtra("item.name", item.getName());
        intent.putExtra("item.amount", item.getAmount());
        intent.putExtra("item.description", item.getDescription());
        intent.putExtra("item.serialNumber", item.getSerialNumber());

        return intent;
    }

    public static Model getInstance() { return new Model();}

    public static int getItemIdDetected() {
        return itemIdDetected;
    }

    public static void setItemIdDetected(int itemIdDetected) {
        itemIdDetected = itemIdDetected;
    }

    public static boolean isDetected() {
        return isDetected;
    }

    public static void setDetected(boolean detected) {
        isDetected = detected;
    }

    public static DataFetchThread getDataFetch() {
        return data;
    }

    public static void setDataFetch(DataFetchThread data) {
        data = data;
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
}
