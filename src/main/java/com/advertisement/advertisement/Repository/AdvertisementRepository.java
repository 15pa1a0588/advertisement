package com.advertisement.advertisement.Repository;

import com.advertisement.advertisement.Entity.Advertisement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementRepository extends CrudRepository<Advertisement,String> {
//    Advertisement getAdvertisementDetails(String userId);
    @Query(value = "select * from advertisement where category=?1",nativeQuery = true)
    Advertisement getDetails(String category);
}
