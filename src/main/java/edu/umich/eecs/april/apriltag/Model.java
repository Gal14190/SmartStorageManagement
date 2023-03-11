package edu.umich.eecs.april.apriltag;

public class Model {
    public enum mode { STORAGE, ITEMS, SPECIFIC_ITEM }

    private mode mModeDetection;
    private int mSpecificPartId;

    private PanelItemPopupModel panelItemPopupModel;
    private DataFetchThread data;

    public Model() {
        this.mModeDetection = mode.SPECIFIC_ITEM;
        this.mSpecificPartId = 1;
    }

    public static Model getInstance() { return new Model();}

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
