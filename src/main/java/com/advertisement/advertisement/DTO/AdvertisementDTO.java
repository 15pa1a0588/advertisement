package com.advertisement.advertisement.DTO;

public class AdvertisementDTO {
    private String advertisementId;
    private String imageURL;
    private String category;

    public String getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public AdvertisementDTO(String advertisementId, String imageURL, String category) {

        this.advertisementId = advertisementId;
        this.imageURL = imageURL;
        this.category = category;
    }
}
