package com.cabApplication.admin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cabApplication.admin.entity.TripCabInfo;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripCabInfoRepository extends MongoRepository<TripCabInfo, Long> {


    List<TripCabInfo> findByCabNumber(String cabNumber);

    @Query("{cabNumber:?0, status:Assigned}")
    TripCabInfo findIfTripExists(String cabNumber);


}
