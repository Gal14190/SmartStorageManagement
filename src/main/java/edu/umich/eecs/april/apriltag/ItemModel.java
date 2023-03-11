package edu.umich.eecs.april.apriltag;

public class ItemModel {
    private int id;
    private String serialNumber;
    private int storageId;
    private String name;
    private String description;
    private int amount;
    private String imageURL;

    public ItemModel(int id, String serialNumber, int storageId, String name, String description, int amount, String imageURL) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.storageId = storageId;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.imageURL = imageURL;
    }

    public ItemModel() {
        this(-1, "", -1, "N/A", "N/A", -1, "N/A");
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() { return name; }

    public int getId() {
        return id;
    }

    public int getStorageId() {
        return storageId;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public String getImageURL() {
        return imageURL;
    }
}
