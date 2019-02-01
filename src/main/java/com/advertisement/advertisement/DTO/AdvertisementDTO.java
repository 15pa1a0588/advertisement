package com.advertisement.advertisement.DTO;

public class AdvertisementDTO {

    private String advertisementId;
    private String imageURL;
    private String category;
    private String advertisementName;
    private String advertisementDescription;
    private String clickURL;

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

    public String getAdvertisementName() {
        return advertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        this.advertisementName = advertisementName;
    }

    public String getAdvertisementDescription() {
        return advertisementDescription;
    }

    public void setAdvertisementDescription(String advertisementDescription) {
        this.advertisementDescription = advertisementDescription;
    }

    public String getClickURL() {
        return clickURL;
    }

    public void setClickURL(String clickURL) {
        this.clickURL = clickURL;
    }

    @Override
    public String toString() {
        return "AdvertisementDTO{" +
                "advertisementId='" + advertisementId + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", category='" + category + '\'' +
                ", advertisementName='" + advertisementName + '\'' +
                ", advertisementDescription='" + advertisementDescription + '\'' +
                ", clickURL='" + clickURL + '\'' +
                '}';
    }
}
