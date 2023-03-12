package edu.umich.eecs.april.apriltag;

import android.content.Intent;

public class Model {
    public enum mode { STORAGE, ITEMS, SPECIFIC_ITEM }

    private mode mModeDetection;
    private int mSpecificPartId;

    private PanelItemPopupModel panelItemPopupModel;
    private DataFetchThread data;

    private int itemIdDetected;
    private boolean isDetected;

    public Model() {
        this.mModeDetection = mode.SPECIFIC_ITEM;
        this.mSpecificPartId = 1;
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

    public int getItemIdDetected() {
        return itemIdDetected;
    }

    public void setItemIdDetected(int itemIdDetected) {
        this.itemIdDetected = itemIdDetected;
    }

    public boolean isDetected() {
        return isDetected;
    }

    public void setDetected(boolean detected) {
        isDetected = detected;
    }

    public DataFetchThread getDataFetch() {
        return data;
    }

    public void setDataFetch(DataFetchThread data) {
        this.data = data;
    }

    public PanelItemPopupModel getPanelItemPopupModel() {
        return panelItemPopupModel;
    }

    public void setPanelItemPopupModel(PanelItemPopupModel panelItemPopupModel) {
        this.panelItemPopupModel = panelItemPopupModel;
    }

    public mode getModeDetection() {
        return mModeDetection;
    }

    public void setModeDetection(mode modeDetection) {
        this.mModeDetection = modeDetection;
    }

    public int getSpecificPartId() {
        return mSpecificPartId;
    }

    public void setSpecificPartId(int specificPartId) {
        this.mSpecificPartId = specificPartId;
    }
}
