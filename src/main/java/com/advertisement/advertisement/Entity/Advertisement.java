package com.advertisement.advertisement.Entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Advertisement {
    public static final String ID_COLUMN="ID";
    @Id
    @GeneratedValue(generator = "uuid")                 // hibernate
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = Advertisement.ID_COLUMN)
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

    @Override
    public String toString() {
        return "Advertisement{" +
                "advertisementId='" + advertisementId + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
