package com.advertisement.advertisement.Controller;

import com.advertisement.advertisement.DTO.AdvertisementDTO;
import com.advertisement.advertisement.Entity.Advertisement;
import com.advertisement.advertisement.Service.AdvertisementService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AdvertisementController {
    @Autowired
    AdvertisementService advertisementService;

    @PostMapping("/postmapping")
    public void add(@RequestBody AdvertisementDTO advertisementDTO){
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(advertisementDTO,advertisement);
        advertisementService.add(advertisement);
    }
    @GetMapping("/getadvertisement")
    public AdvertisementDTO select(@RequestParam String category){
        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
        Advertisement advertisement=advertisementService.getDetails(category);
        BeanUtils.copyProperties(advertisement, advertisementDTO);
        return advertisementDTO;
    }
    @GetMapping("/getDetails")
    public AdvertisementDTO selectDetails(){

        String category="";
        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
        Advertisement advertisement=advertisementService.getDetails(category);
        BeanUtils.copyProperties(advertisement, advertisementDTO);
        return advertisementDTO;
    }
    @PutMapping("/putmapping")
    public void update(@RequestBody AdvertisementDTO advertisementDTO){
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(advertisementDTO,advertisement);
        advertisementService.add(advertisement);
    }
    @DeleteMapping("/deletemapping")
    public void delete(@RequestParam String advertisementId){
        advertisementService.delete(advertisementId);
    }
}
