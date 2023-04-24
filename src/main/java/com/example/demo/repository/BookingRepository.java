package com.example.demo.repository;

import com.example.demo.domain.model.Booking;
import com.example.demo.request.vehicle_owner.OwnerBookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT b.* FROM booking b JOIN vehicle v ON  b.vehicle_id = v.id JOIN account a ON b.account_id = a.id" +
            " WHERE v.account_id = :#{#re.ownerId} AND v.status = true" +
            " AND (a.name LIKE CONCAT('%',:#{#re.keyword},'%')" +
            " OR a.card_id LIKE CONCAT('%',:#{#re.keyword},'%')" +
            " OR b.id LIKE CONCAT('%',:#{#re.keyword},'%')" +
            " OR v.license_plates LIKE CONCAT('%',:#{#re.keyword},'%')" +
            " OR a.phone LIKE CONCAT('%',:#{#re.keyword},'%'))" +
            "ORDER BY b.createDate ASC "
            , nativeQuery = true)
    List<Booking> findByVehicle(OwnerBookingRequest re);


    @Query(value = "SELECT b.* FROM vehicle p JOIN booking b ON p.id = b.vehicle_id  JOIN account a ON a.id = b.account_id JOIN account a1 ON p.account_id = a1.id  WHERE a.id =:accountId AND  ( p.name LIKE CONCAT('%',:name,'%') OR a1.name LIKE CONCAT('%',:name,'%')) AND p.deleteFlg = false ORDER BY b.id desc",nativeQuery = true)
    List<Booking> findByAccountIdAndVehicleNameContains(Long accountId, String name);
}
