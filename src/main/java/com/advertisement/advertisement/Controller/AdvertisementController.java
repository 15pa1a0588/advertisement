package com.advertisement.advertisement.Controller;

import com.advertisement.advertisement.DTO.AdvertisementDTO;
import com.advertisement.advertisement.DTO.ResponseDTO;
import com.advertisement.advertisement.Entity.Advertisement;
import com.advertisement.advertisement.Service.AdvertisementService;
import com.advertisement.advertisement.util.CategoryContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class AdvertisementController {



    @Autowired
    AdvertisementService advertisementService;

    @Value("${recommendation.service.address}")
    private String recommendationServiceAddress;

    @PostMapping("/advertisement/add")
    public void add(@RequestBody AdvertisementDTO advertisementDTO){
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(advertisementDTO,advertisement);
        advertisementService.add(advertisement);
    }


    @GetMapping("/categoryPreference/get/{userId}")
    public ResponseDTO<LinkedList<CategoryContainer>> selectCategory(@PathVariable String userId)
    {
        RestTemplate recommendationService=new RestTemplate();

        //try getting recommendation for the user
        ResponseDTO<HashMap<String,Double>> recommendationResponse=recommendationService.getForObject(
                "http://"+recommendationServiceAddress+"/recommendation/get/"+userId,
                ResponseDTO.class);

        if(recommendationResponse.getStatus().equals("FAILURE"))
        {
            recommendationResponse=recommendationService.getForObject(
                    "http://"+recommendationServiceAddress+"/recommendation/getForNewUser",
                    ResponseDTO.class);

        }

        if(recommendationResponse.getStatus().equals("FAILURE")||recommendationResponse.getResponse()==null)
        {

            ResponseDTO<LinkedList<CategoryContainer>> responseDTO=new ResponseDTO<>();
            responseDTO.setStatus("FAILURE");
            responseDTO.setMessage("FAILED DUE TO SOME INTERNAL ERROR #E1");
            responseDTO.setResponse(null);
            return responseDTO;
        }

        LinkedList<CategoryContainer> selectedQueue=new LinkedList<CategoryContainer>();

        double sumOfScores=0;

        for(String categories: recommendationResponse.getResponse().keySet())
        {
            double thisScore=recommendationResponse.getResponse().get(categories);
            selectedQueue.add(new CategoryContainer(thisScore,categories));
            sumOfScores+=thisScore;
        }

        for(CategoryContainer c:selectedQueue)
        {
            c.setScore(c.getScore()/sumOfScores);
        }

        Collections.sort(selectedQueue, new Comparator<CategoryContainer>() {
            @Override
            public int compare(CategoryContainer o1, CategoryContainer o2) {
                return Double.compare(o1.getScore(),o2.getScore());
            }
        });

        sumOfScores=1;
        double coverageLimit=0.7*sumOfScores;

        while(selectedQueue.size()>0&&sumOfScores>coverageLimit)
        {
            double thisScore=selectedQueue.peekFirst().getScore();

            if(sumOfScores-thisScore<=coverageLimit) break;

            sumOfScores-=thisScore;

            selectedQueue.pollFirst();
        }

        ResponseDTO<LinkedList<CategoryContainer>> responseDTO=new ResponseDTO<>();
        responseDTO.setStatus("SUCCESS");
        responseDTO.setMessage("SUCCESS");
        responseDTO.setResponse(selectedQueue);

        return responseDTO;
    }

    @GetMapping("/advertisement/get/{id}")
    public ResponseDTO<AdvertisementDTO> selectDetails(@PathVariable String id){


        ResponseDTO<LinkedList<CategoryContainer>> categoryResponse=selectCategory(id);

        if((!categoryResponse.getStatus().equals("SUCCESS"))||categoryResponse.getResponse()==null)
        {
            ResponseDTO<AdvertisementDTO> responseDTO=new ResponseDTO<AdvertisementDTO>();
            responseDTO.setStatus("FAILURE");
            responseDTO.setMessage(categoryResponse.getMessage());
            responseDTO.setResponse(null);
            return responseDTO;
        }

        LinkedList<CategoryContainer> sortedCategories=categoryResponse.getResponse();

        int randomIndex=(int)Math.floor(Math.random()*(sortedCategories.size()-1));

        CategoryContainer selectedCategory=sortedCategories.get(randomIndex);

        if(selectedCategory==null)
        {
            ResponseDTO<AdvertisementDTO> responseDTO=new ResponseDTO<AdvertisementDTO>();
            responseDTO.setStatus("FAILURE");
            responseDTO.setMessage("FAILED DUE TO SOME INTERNAL ERROR #E2");
            responseDTO.setResponse(null);
            return responseDTO;
        }

        List<Advertisement> selectedAd=  advertisementService.getDetails(selectedCategory.getCategoryName());

        if(selectedAd==null||selectedAd.size()==0)
        {
            ResponseDTO<AdvertisementDTO> responseDTO=new ResponseDTO<AdvertisementDTO>();
            responseDTO.setStatus("FAILURE");
            responseDTO.setMessage("FAILED DUE TO SOME INTERNAL ERROR #E3");
            responseDTO.setResponse(null);
            return responseDTO;
        }

        AdvertisementDTO selectedAdDTO=new AdvertisementDTO();
        BeanUtils.copyProperties(selectedAd.get(0),selectedAdDTO);

        ResponseDTO<AdvertisementDTO> responseDTO=new ResponseDTO<AdvertisementDTO>();
        responseDTO.setStatus("SUCCESS");
        responseDTO.setMessage(selectedCategory.getCategoryName());
        responseDTO.setResponse(selectedAdDTO);

        return responseDTO;

    }

    @GetMapping("/advertisement/bulk/{userId}/{count}")
    public ResponseDTO<List<AdvertisementDTO>> bulkAds(@PathVariable String userId,@PathVariable int count)
    {
        ResponseDTO<LinkedList<CategoryContainer>> selectedCategoriesResponse=selectCategory(userId);

        if(!selectedCategoriesResponse.getStatus().equals("SUCCESS"))
        {
            ResponseDTO<List<AdvertisementDTO>> responseDTO=new ResponseDTO<>();
            responseDTO.setStatus(selectedCategoriesResponse.getStatus());
            responseDTO.setMessage(selectedCategoriesResponse.getMessage());
            responseDTO.setResponse(new LinkedList<AdvertisementDTO>());
            return responseDTO;
        }
        LinkedList<CategoryContainer> selectedCategories=selectedCategoriesResponse.getResponse();

        List<AdvertisementDTO> selectedAds=new LinkedList<>();
        CategoryContainer currentAd=selectedCategories.peekLast();

        while (selectedAds.size()<count&&selectedCategories.size()>0)
        {
            List<Advertisement> listOfAdvertisement=advertisementService.getDetails(currentAd.getCategoryName());

            int indexLimit=(int)(Math.random()*(count-selectedAds.size()));
            indexLimit=Math.max(0,indexLimit-1);

            indexLimit=Math.min(listOfAdvertisement.size()-1,indexLimit);

            for(int index=0;index<=indexLimit;index++)
            {
                AdvertisementDTO advertisementDTO=new AdvertisementDTO();
                BeanUtils.copyProperties(listOfAdvertisement.get(index),advertisementDTO);
                selectedAds.add(advertisementDTO);
            }

            if(selectedCategories.size()>0)
            {
                selectedCategories.pollLast();
                currentAd=selectedCategories.peekLast();
            }

        }

        while (selectedAds.size()<count)
        {
            int randomIndex=(int)((Math.random())*(selectedAds.size()));
            randomIndex=Math.max(0,randomIndex-1);
            selectedAds.add(selectedAds.get(randomIndex));
        }

        ResponseDTO<List<AdvertisementDTO>> responseDTO=new ResponseDTO<>();
        responseDTO.setStatus("SUCCESS");
        responseDTO.setMessage("SUCCESS");
        responseDTO.setResponse(selectedAds);

        return responseDTO;

    }

    @PutMapping("/updateadvertisement")
    public void update(@RequestBody AdvertisementDTO advertisementDTO){
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(advertisementDTO,advertisement);
        advertisementService.add(advertisement);
    }

    @DeleteMapping("/deleteadvertisement")
    public void delete(@RequestParam String advertisementId){
        advertisementService.delete(advertisementId);
    }
}
