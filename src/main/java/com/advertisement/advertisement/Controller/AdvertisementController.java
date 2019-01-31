package com.advertisement.advertisement.Controller;

import com.advertisement.advertisement.DTO.AdvertisementDTO;
import com.advertisement.advertisement.DTO.ResponseDTO;
import com.advertisement.advertisement.Entity.Advertisement;
import com.advertisement.advertisement.Service.AdvertisementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class AdvertisementController {
    @Autowired
    AdvertisementService advertisementService;

    @PostMapping("/addadvertisement")
    public void add(@RequestBody AdvertisementDTO advertisementDTO){
        Advertisement advertisement=new Advertisement();
        BeanUtils.copyProperties(advertisementDTO,advertisement);
        advertisementService.add(advertisement);
    }
    @GetMapping("/getAdvertisement")
    public AdvertisementDTO select(@RequestParam String category){
        System.out.println(category);
        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
        Advertisement advertisement=advertisementService.getDetails(category).get(0);
        BeanUtils.copyProperties(advertisement, advertisementDTO);
        return advertisementDTO;
    }
//    @GetMapping("/getadvertisement")
//    public AdvertisementDTO select(@RequestParam String category){
//        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
//        Advertisement advertisement=advertisementService.getDetails(category);
//        BeanUtils.copyProperties(advertisement, advertisementDTO);
//        return advertisementDTO;
//    }

//    private  HashMap sortByCategoryValue(Map<String,Integer> tmp) {
//
//
//        List list = new LinkedList(tmp.entrySet());
//        // Defined Custom Comparator here
//        Collections.sort(list, new Comparator() {
//            public int compare(Object o1, Object o2) {
//                return ((Comparable) ((Map.Entry) (o2)).getValue())
//                        .compareTo(((Map.Entry) (o1)).getValue());
//            }
//        });
//
//        HashMap sortedHashMap = new LinkedHashMap();
//        for (Iterator it = list.iterator(); it.hasNext();) {
//            Map.Entry entry = (Map.Entry) it.next();
//            sortedHashMap.put(entry.getKey(), entry.getValue());
//        }
//        return sortedHashMap;
//    }

//    @GetMapping("/getDetails")
//    public void selectDetails(){
//
//        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
//        hmap.put("movies",1);
//        hmap.put("sports",9);
//        hmap.put("generalknowledge",4);
//        hmap.put("quiz",5);
//
//        HashMap<String, Integer> map = sortByCategoryValue(hmap);
////        System.out.println(map);
////        System.out.println(map.size());
//
//        Object[] Keys = map.keySet().toArray();
//        Object key = Keys[new Random().nextInt(3)];
//        System.out.println("************ Random Value ************ \n" + key + " :: " + map.get(key));
//
////        Random generator = new Random();
////        System.out.println(generator);
////        Object []values = map.values().toArray();
////        randomValue = values[generator.nextInt(values.length)];
////        System.out.println(randomValue);
//
////        String category="";
////        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
////        Advertisement advertisement=advertisementService.getDetails(category);
////        BeanUtils.copyProperties(advertisement, advertisementDTO);
////        return advertisementDTO;
//
//    }

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
    @GetMapping("/getAdvertisementDetails/{id}")
    public AdvertisementDTO selectDetails(@PathVariable String id){
        RestTemplate restTemplate=new RestTemplate();
        ResponseDTO<HashMap<String,Double>> responseDTO;
        responseDTO = restTemplate.getForObject("http://10.177.7.135:8000/recommendation/get/" + id, ResponseDTO.class);
        Object obj=new Object();
        obj=responseDTO.getResponse();
        if(responseDTO.getStatus().equals("FAILURE")) {
            responseDTO = restTemplate.getForObject("http://10.177.7.135:8000/recommendation/getForNewUser", ResponseDTO.class);
            obj=responseDTO.getResponse();
        }
        ObjectMapper oMapper = new ObjectMapper();
        HashMap<String, Double> map = oMapper.convertValue(obj, HashMap.class);
        HashMap<String,Double> tmp=sortByValue(map);
        System.out.println(tmp);
        List<String> keys = new ArrayList<String>(tmp.keySet());
        Random rand = new Random();
        String key = keys.get(rand.nextInt(keys.size()));
        System.out.println(key);
        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
        Advertisement advertisement=advertisementService.getDetails(key).get(0);
        BeanUtils.copyProperties(advertisement, advertisementDTO);
        return advertisementDTO;
    }

    @GetMapping("/getAdvertisementDetails/{userId}/{count}")
    public List<AdvertisementDTO> bulkAds(@PathVariable String userId,@PathVariable int count)
    {
        List<AdvertisementDTO> bulkAds=new ArrayList<AdvertisementDTO>();
        while(bulkAds.size()<count)
        {
            AdvertisementDTO advertisementDTO=selectDetails(userId);
            bulkAds.add(advertisementDTO);
        }
        return bulkAds;
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
