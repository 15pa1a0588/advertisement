package com.advertisement.advertisement.Entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = Advertisement.TABLE_NAME)
public class Advertisement {

    public static final String TABLE_NAME = "ADVERTISEMENT";

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClickURL() {
        return clickURL;
    }

    public void setClickURL(String clickURL) {
        this.clickURL = clickURL;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "advertisementId='" + advertisementId + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", category='" + category + '\'' +
                ", advertisementName='" + advertisementName + '\'' +
                ", advertisementDescription='" + advertisementDescription + '\'' +
                ", clickURL='" + clickURL + '\'' +
                '}';
    }
}
