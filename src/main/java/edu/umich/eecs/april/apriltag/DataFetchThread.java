package edu.umich.eecs.april.apriltag;

import android.util.Log;

import java.util.Vector;

public class DataFetchThread extends Thread {
    private DataSet mDataSet;

    public Model model;

    public DataFetchThread(Model model) {
        this.model = model;
        mDataSet = DataSet.getInstance();

        try {
            // SAMPLE ITEMS
            mDataSet.getItem().add(new ItemModel(0, "ABC123456", 500, "Test Name 0", "Test Description 0", 5, "https://4.img-dpreview.com/files/p/E~TS590x0~articles/3925134721/0266554465.jpeg"));
            mDataSet.getItem().add(new ItemModel(1, "ABC123457", 501, "Test Name 1", "Test Description 1", 6, "https://fujifilm-x.com/wp-content/uploads/2021/01/gfx100s_sample_04_thum-1.jpg"));

            mDataSet.getItem().add(mDataSet.getItem().get(0));
            mDataSet.getItem().add(mDataSet.getItem().get(0));
            mDataSet.getItem().add(mDataSet.getItem().get(0));
            mDataSet.getItem().add(mDataSet.getItem().get(0));
            mDataSet.getItem().add(mDataSet.getItem().get(0));
            mDataSet.getItem().add(mDataSet.getItem().get(1));

            // SAMPLE STORAGE
            mDataSet.getStorage().add(new StorageModel(500));
            mDataSet.getStorage().add(new StorageModel(501));
            mDataSet.getStorage().add(new StorageModel(502));
        } catch (Exception e) {
            Log.e("Data", e.getMessage());
        }
    }

    @Override
    public void run() {

    }

    public Vector<ItemModel> getAllItems() {
        return this.mDataSet.getItem();
    }

    public Vector<StorageModel> getAllStorage() {
        return this.mDataSet.getStorage();
    }

    public ItemModel getItemById(int id) {
        for (ItemModel item: mDataSet.getItem()) {
            if(item.getId() == id)
                return item;
        }

        return new ItemModel();
    }
}

class DataSet {
    private Vector<ItemModel> item;
    private Vector<StorageModel> storage;
    private Vector<MemberModel> member;

    public DataSet() {
        item = new Vector<>();
        storage = new Vector<>();
        member = new Vector<>();
    }

    public static DataSet getInstance() {return new DataSet();}

    public Vector<ItemModel> getItem() {
        return item;
    }

    public void setItem(Vector<ItemModel> item) {
        this.item = item;
    }

    public Vector<StorageModel> getStorage() {
        return storage;
    }

    public void setStorage(Vector<StorageModel> storage) {
        this.storage = storage;
    }

    public Vector<MemberModel> getMember() {
        return member;
    }

    public void setMember(Vector<MemberModel> member) {
        this.member = member;
    }
}
