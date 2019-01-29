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

    private  HashMap sortByCategoryValue(Map<String,Integer> tmp) {


        List list = new LinkedList(tmp.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    @GetMapping("/getDetails")
    public void selectDetails(){

        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        hmap.put("movies",1);
        hmap.put("sports",9);
        hmap.put("generalknowledge",4);
        hmap.put("quiz",5);

        HashMap<String, Integer> map = sortByCategoryValue(hmap);
//        System.out.println(map);
//        System.out.println(map.size());

        Object[] Keys = map.keySet().toArray();
        Object key = Keys[new Random().nextInt(3)];
        System.out.println("************ Random Value ************ \n" + key + " :: " + map.get(key));

//        Random generator = new Random();
//        System.out.println(generator);
//        Object []values = map.values().toArray();
//        randomValue = values[generator.nextInt(values.length)];
//        System.out.println(randomValue);

//        String category="";
//        AdvertisementDTO advertisementDTO=new AdvertisementDTO();
//        Advertisement advertisement=advertisementService.getDetails(category);
//        BeanUtils.copyProperties(advertisement, advertisementDTO);
//        return advertisementDTO;

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
