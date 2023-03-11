package edu.umich.eecs.april.apriltag;

import android.util.Log;

import java.util.Vector;

public class DataFetchThread extends Thread {
    private Vector<ItemModel> mPartData;

    public Model model;

    public DataFetchThread(Model model) {
        this.model = model;

        try {
            mPartData = new Vector<>();
            mPartData.add(new ItemModel(0, "ABC123456", 0, "Test Name 0", "Test Description 0", 5, "http://URL0"));
            mPartData.add(new ItemModel(1, "ABC123457", 0, "Test Name 1", "Test Description 1", 6, "http://URL1"));
        } catch (Exception e) {
            Log.e("Data", e.getMessage());
        }
    }

    @Override
    public void run() {

    }

    public ItemModel getPartById(int id) {
        for (ItemModel part: mPartData) {
            if(part.getId() == id)
                return part;
        }

        return new ItemModel();
    }
}
