package com.advertisement.advertisement.Service.Impl;

import com.advertisement.advertisement.Entity.Advertisement;
import com.advertisement.advertisement.Repository.AdvertisementRepository;
import com.advertisement.advertisement.Service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {
    @Autowired
    AdvertisementRepository advertisementRepository;
    @Override
    public void add(Advertisement advertisement){
        advertisementRepository.save(advertisement);
    }

    @Override
    public void update(Advertisement advertisement){
        advertisementRepository.save(advertisement);
    }

    @Override
    public void delete(String advertisementId){
        advertisementRepository.delete(advertisementId);
    }

    @Override
    public List<Advertisement> getAnyAds() {

        return advertisementRepository.getAnyAd();

    }

    @Override
    public List<Advertisement> getDetails(String category){
        return advertisementRepository.getDetails(category);
    }


}
