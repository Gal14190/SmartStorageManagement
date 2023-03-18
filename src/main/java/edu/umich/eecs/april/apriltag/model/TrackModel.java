package edu.umich.eecs.april.apriltag.model;

public class TrackModel {
    private int itemId;
    private String userName;
    private String time;
    private boolean func;

    public TrackModel(int itemId, String userName, String time, boolean func) {
        this.itemId = itemId;
        this.userName = userName;
        this.time = time;
        this.func = func;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isFunc() {
        return func;
    }

    public void setFunc(boolean func) {
        this.func = func;
    }
}
