package com.advertisement.advertisement.Controller;

import com.advertisement.advertisement.DTO.AdvertisementDTO;
import com.advertisement.advertisement.DTO.ResponseDTO;
import com.advertisement.advertisement.Entity.Advertisement;
import com.advertisement.advertisement.Service.AdvertisementService;
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

    public HashMap<String,Double> sortByValue(HashMap<String,Double> m){
        List<Map.Entry<String, Double> > list =
                new ArrayList<Map.Entry<String,Double>>(m.entrySet());
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        int count=0;
        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            if(count<3) {
                count++;
                temp.put(aa.getKey(), aa.getValue());
            }
        }
        return temp;
    }
    @GetMapping("/advertisement/get/{id}")
    public ResponseDTO<AdvertisementDTO> selectDetails(@PathVariable String id){

        RestTemplate recommendationService=new RestTemplate();

        //try getting recommendation for the user
        ResponseDTO<HashMap<String,Double>> recommendationResponse=recommendationService.getForObject(
                recommendationServiceAddress+"recommendation/get",
                ResponseDTO.class);

        if(recommendationResponse.getStatus().equals("FAILURE"))
        {
            recommendationResponse=recommendationService.getForObject(
                    recommendationServiceAddress+"recommendation/getForNewUser",
                    ResponseDTO.class);

        }

        if(recommendationResponse.getStatus().equals("FAILURE")||recommendationResponse.getResponse()==null)
        {

            ResponseDTO<AdvertisementDTO> responseDTO=new ResponseDTO<AdvertisementDTO>();
            responseDTO.setStatus("FAILURE");
            responseDTO.setMessage("FAILED DUE TO SOME INTERNAL ERROR #E1");
            responseDTO.setResponse(null);
            return responseDTO;
        }

        TreeMap<Double,String> sortedCategories=new TreeMap<Double, String>();

        double sumOfScores=0;

        for(String categories: recommendationResponse.getResponse().keySet())
        {
            double thisScore=recommendationResponse.getResponse().get(categories);
            sortedCategories.put(thisScore,categories);
            sumOfScores+=thisScore;
        }

        Iterator<Double> categoryIterator=sortedCategories.descendingKeySet().descendingIterator();

        double currentScore=0;

        while (categoryIterator.hasNext())
        {
            currentScore+=categoryIterator.next();
            if(currentScore>=0.7*sumOfScores) break;
        }

        while (categoryIterator.hasNext())
        {
            sortedCategories.remove(categoryIterator.next());
        }

        int randomIndex=(int)Math.floor(Math.random()*(sortedCategories.size()-1));

        String selectedCategory=null;
        for(String category: sortedCategories.values())
        {
            selectedCategory=category;
            randomIndex--;
            if(randomIndex<0) break;
        }

        if(selectedCategory==null)
        {
            ResponseDTO<AdvertisementDTO> responseDTO=new ResponseDTO<AdvertisementDTO>();
            responseDTO.setStatus("FAILURE");
            responseDTO.setMessage("FAILED DUE TO SOME INTERNAL ERROR #E2");
            responseDTO.setResponse(null);
            return responseDTO;
        }

        List<Advertisement> selectedAd=  advertisementService.getDetails(selectedCategory);

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
        responseDTO.setMessage(selectedCategory);
        responseDTO.setResponse(selectedAdDTO);

        return responseDTO;

    }

    @GetMapping("/getAdvertisementDetails/{userId}/{count}")
    public ResponseDTO<List<AdvertisementDTO>> bulkAds(@PathVariable String userId,@PathVariable int count)
    {
        List<AdvertisementDTO> bulkAds=new ArrayList<AdvertisementDTO>();
        while(bulkAds.size()<count)
        {
            ResponseDTO<AdvertisementDTO> advertisementDTO=selectDetails(userId);
            bulkAds.add(advertisementDTO.getResponse());
        }
        ResponseDTO<List<AdvertisementDTO>> responseDTO=new ResponseDTO<List<AdvertisementDTO>>();
        responseDTO.setStatus("SUCCESS");
        responseDTO.setMessage("SUCCESS");
        responseDTO.setResponse(bulkAds);
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
