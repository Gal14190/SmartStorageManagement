package edu.umich.eecs.april.apriltag.thread;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.umich.eecs.april.apriltag.model.ItemModel;
import edu.umich.eecs.april.apriltag.model.Model;
import edu.umich.eecs.april.apriltag.model.StorageModel;
import edu.umich.eecs.april.apriltag.model.TrackModel;

public class DataFetchThread extends Thread {
    private DataSet mDataSet;
    private CollectionReference dbTrack;
    private CollectionReference dbStorage;
    private CollectionReference dbItem;

    private boolean flag_fetch[] = {false, false, false};

    private AlertDialog mDialog;

    public DataFetchThread(AlertDialog dialog) {
        mDataSet = DataSet.getInstance();

        mDialog = dialog;

        try {
            // Get storage documents
            dbStorage = FirebaseFirestore.getInstance().collection(mDataSet.STORAGE_COLLECTION_STR);
            dbTrack = FirebaseFirestore.getInstance().collection(mDataSet.TRACK_COLLECTION_STR);
            dbItem = FirebaseFirestore.getInstance().collection(mDataSet.ITEM_COLLECTION_STR);
        } catch (Exception e) {
            Log.e("Data", e.getMessage());
        }
    }

    @Override
    public void run() {
        mDataSet.getStorage().clear();
        mDataSet.getTrack().clear();
        mDataSet.getItem().clear();

        fetchStorages();
        fetchTracks();
        fetchItems();

        while (!flag_fetch[0] || !flag_fetch[1] || !flag_fetch[2]);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mDialog.cancel();
    }

    private void fetchStorages() {
        flag_fetch[0] = false;

        dbStorage.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()) {
                    switch (doc.getType()) {
                        case ADDED:
                            try {
                                int id = Integer.parseInt(doc.getDocument().getData().get("id").toString());
                                StorageModel storage = new StorageModel(id);
                                mDataSet.getStorage().add(storage);
                            } catch (Exception e) {}
                            flag_fetch[0] = true;
                            break;
                    }
                }
            }
        });
    }

    private void fetchTracks() {
        flag_fetch[1] = false;

        dbTrack.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()) {
                    switch (doc.getType()) {
                        case ADDED:
                            try {
                                int itemId = Integer.parseInt(doc.getDocument().getData().get("itemId").toString());
                                String userName = doc.getDocument().getData().get("userName").toString();
                                String time = doc.getDocument().getData().get("time").toString();
                                boolean func = Boolean.parseBoolean(doc.getDocument().getData().get("func").toString());

                                TrackModel track = new TrackModel(itemId, userName, time, func);
                                mDataSet.getTrack().add(track);
                            } catch (Exception e) {}
                            flag_fetch[1] = true;
                            break;
                    }
                }
            }
        });
    }

    private void fetchItems() {
        flag_fetch[2] = false;

        dbItem.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc : value.getDocumentChanges()) {
                    switch (doc.getType()) {
                        case ADDED:
                            try {
                                int id = Integer.parseInt(doc.getDocument().getData().get("id").toString());
                                String serialNumber = doc.getDocument().getData().get("serialNumber").toString();
                                int storageId = Integer.parseInt(doc.getDocument().getData().get("storageId").toString());
                                String name = doc.getDocument().getData().get("name").toString();
                                String description = doc.getDocument().getData().get("description").toString();
                                int amount = Integer.parseInt(doc.getDocument().getData().get("amount").toString());
                                String imageURL = doc.getDocument().getData().get("imageURL").toString();

                                ItemModel item = new ItemModel(id, serialNumber, storageId, name, description, amount, imageURL);
                                item.setDoc(doc.getDocument().getId());
                                mDataSet.getItem().add(item);
                            } catch (Exception e) {}
                            flag_fetch[2] = true;
                            break;
                    }
                }
            }
        });
    }

    public void addItem(ItemModel item) {
        mDataSet.itemDoc_count++;
        this.mDataSet.getItem().add(item);

        dbItem.document(Integer.toString(mDataSet.getItem().size() - 1)).set(item).addOnFailureListener(e ->
                Toast.makeText(Model.getMainContext(), "Fail to add item " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    public void addStorage(StorageModel storage) {
        this.mDataSet.getStorage().add(storage);
    }

    public void addTrack(TrackModel track) {
        mDataSet.trackDoc_count++;
        dbTrack.document(mDataSet.trackDoc_count + "").set(track).addOnFailureListener(e ->
                Toast.makeText(Model.getMainContext(), "Fail to add track " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    public void updateAmount(String doc, int v) {
        dbItem.document(doc).update("amount", v);
    }

    public ArrayList<ItemModel> getAllItems() {
        return this.mDataSet.getItem();
    }

    public ArrayList<StorageModel> getAllStorage() {
        return this.mDataSet.getStorage();
    }

    public ArrayList<TrackModel> getAllTracks() {return this.mDataSet.getTrack();}

    public ItemModel getItemById(int id) {
        for (ItemModel item: mDataSet.getItem()) {
            if(item.getId() == id)
                return item;
        }

        return new ItemModel();
    }
}

class DataSet {
    private ArrayList<ItemModel> item;
    private ArrayList<StorageModel> storage;
    private ArrayList<TrackModel> track;

    public final String STORAGE_COLLECTION_STR = "Storage";
    public final String TRACK_COLLECTION_STR = "Tracks";
    public final String ITEM_COLLECTION_STR = "Items";

    public int trackDoc_count;
    public int storageDoc_count;
    public int itemDoc_count;

    public DataSet() {
        item = new ArrayList<>();
        storage = new ArrayList<>();
        track = new ArrayList<>();
    }

    public static DataSet getInstance() {return new DataSet();}

    public ArrayList<ItemModel> getItem() {
        return item;
    }

    public void setItem(ArrayList<ItemModel> item) {
        this.item = item;
    }

    public ArrayList<StorageModel> getStorage() {
        return storage;
    }

    public void setStorage(ArrayList<StorageModel> storage) {
        this.storage = storage;
    }

    public ArrayList<TrackModel> getTrack() {
        return track;
    }

    public void setTrack(ArrayList<TrackModel> track) {
        this.track = track;
    }
}
