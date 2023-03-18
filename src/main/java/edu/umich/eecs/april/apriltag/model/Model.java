package edu.umich.eecs.april.apriltag.model;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import edu.umich.eecs.april.apriltag.thread.DataFetchThread;

public class Model extends ViewModel {
    public enum mode { STORAGE, ITEMS, SPECIFIC_ITEM }

    private static Context mainContext;

    private static mode mModeDetection;
    private static int mSpecificPartId;

    private static PanelItemPopupModel mPanelItemPopupModel;
    public static DataFetchThread mData;

    private static int mItemIdDetected;
    private static boolean mIsDetected;

    private static String userName;

    public static void init() {
        mModeDetection = mode.SPECIFIC_ITEM;
        mSpecificPartId = 1;
    }

    public static Context getMainContext() {
        return mainContext;
    }

    public static void setMainContext(Context mainContext) {
        Model.mainContext = mainContext;
    }

    public static void addItem(ItemModel item) {
        mData.addItem(item);
    }

    public static void addStorage(StorageModel storage) {
        mData.addStorage(storage);
    }

    public static void addTrack(TrackModel track) {
        mData.addTrack(track);
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

    public static PanelItemPopupModel getPanelItemPopupModel() {
        return mPanelItemPopupModel;
    }

    public static void setPanelItemPopupModel(PanelItemPopupModel panelItemPopupModel) {
        mPanelItemPopupModel = panelItemPopupModel;
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
