package com.advertisement.advertisement.Service;

import com.advertisement.advertisement.Entity.Advertisement;

import java.util.List;

public interface AdvertisementService {
    public void add(Advertisement advertisement);
    public void update(Advertisement advertisement);
    public List<Advertisement> getDetails(String category);
    public void delete(String advertisementId);
}
