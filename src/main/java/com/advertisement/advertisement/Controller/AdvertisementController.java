package com.advertisement.advertisement.Controller;

import com.advertisement.advertisement.DTO.AdvertisementDTO;
import com.advertisement.advertisement.Entity.Advertisement;
import com.advertisement.advertisement.Service.AdvertisementService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AdvertisementController {
    @Autowired
    AdvertisementService advertisementService;

    @PostMapping("/addAdvertisement")
    public void add(@RequestBody AdvertisementDTO advertisementDTO){
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(advertisementDTO,advertisement);
        advertisementService.add(advertisement);
    }
    @GetMapping("/getAdvertisement")
    public AdvertisementDTO select(@RequestParam String category){
        System.out.println(category);
        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
        Advertisement advertisement=advertisementService.getDetails(category);
        BeanUtils.copyProperties(advertisement, advertisementDTO);
        return advertisementDTO;
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
    @GetMapping("/getDetails")
    public AdvertisementDTO selectDetails(){
        HashMap<String,Double> map=new HashMap<>();
        map.put("movies",5.4);
        map.put("sports",7.21);
        map.put("space",3.3);
        map.put("fashion",3.6);
        HashMap<String,Double> tmp=sortByValue(map);
        //System.out.println(tmp);
        int num=1;
        List<String> keys = new ArrayList<String>(tmp.keySet());
        Random rand = new Random();
        String key = keys.get(rand.nextInt(keys.size()));
        System.out.println(key);
        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
        Advertisement advertisement=advertisementService.getDetails(key);
        BeanUtils.copyProperties(advertisement, advertisementDTO);
        return advertisementDTO;
    }
    @PutMapping("/updateAdvertisement")
    public void update(@RequestBody AdvertisementDTO advertisementDTO){
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(advertisementDTO,advertisement);
        advertisementService.add(advertisement);
    }
    @DeleteMapping("/deleteAdvertisement")
    public void delete(@RequestParam String advertisementId){
        advertisementService.delete(advertisementId);
    }
}
