package com.example.demo.repository;

import com.example.demo.domain.model.ERole;
import com.example.demo.domain.model.Vehicle;
import com.example.demo.request.booking.HomeBookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query(value = "SELECT p.* FROM vehicle p LEFT JOIN vehicle_type t ON  t.id = p.vehicle_type_id JOIN account a ON a.id = p.account_id" +
            "  WHERE t.id = :#{#request.vehicleType} AND a.address LIKE CONCAT('%',:#{#request.location},'%')" +
            " AND (:#{#request.seatType} IS NULL OR  p.seat_type_id = :#{#request.seatType})" +
            " AND (:#{#request.vehicleCompany} IS NULL OR ( p.vehicle_company_id = :#{#request.vehicleCompany}))" +
            " AND (:#{#request.tranmistion} IS NULL OR ( p.tranmistion_type_id = :#{#request.tranmistion}))" +
            " AND(:#{#request.fuel} IS NULL OR ( p.fuel_type_id = :#{#request.fuel}))" +
            " AND (:#{#request.accountId} IS NULL OR ( p.account_id = :#{#request.accountId}))" +
            " AND (:#{#request.maxPrice} IS NULL OR ( p.price <= :#{#request.maxPrice}))" +
            " AND p.price >= :#{#request.minPrice}" +
            " AND p.status = true " +
            " AND p.deleteFlg = false " +
            " AND p.name LIKE  CONCAT('%',:#{#request.name},'%') " +
            " ORDER BY (p.point) DESC", nativeQuery = true)
    List<Vehicle> getVehicle(@Param("request") HomeBookingRequest request);


    Optional<Vehicle> findByIdAndStatusIsTrueAndDeleteFlgIsFalse(Long id);

    @Query(value = "SELECT p.* FROM vehicle p JOIN account a ON a.id = p.account_id WHERE a.id = :id AND( p.name LIKE CONCAT('%',:name,'%') OR p.license_plates LIKE CONCAT('%',:licensePlates,'%'))",nativeQuery = true)
    List<Vehicle> findByAccountIdAndNameContainsOrLicensePlatesContains(Long id, String name, String licensePlates);


    @Query(value = "SELECT p.* FROM vehicle p JOIN account a ON a.id = p.account_id JOIN role r ON r.id = a.role_id WHERE ( p.name LIKE CONCAT('%',:name,'%') OR a.name LIKE CONCAT('%',:name,'%')) AND p.deleteFlg = false AND r.name = :eRole AND p.status = :status",nativeQuery = true)
    List<Vehicle> findByAccount_NameContainsAndStatusAndAccount_Role_NameAndDeleteFlgIsFalse(String name, Boolean status, String eRole);

    @Query(value = "SELECT p.* FROM vehicle p JOIN account a ON a.id = p.account_id WHERE ( p.name LIKE CONCAT('%',:name,'%') OR a.name LIKE CONCAT('%',:name,'%')) AND p.deleteFlg = false AND p.status = :status",nativeQuery = true)
    List<Vehicle> findByAccount_NameContainsAndStatusAndDeleteFlgIsFalse(String name, Boolean status);

    @Query(value = "SELECT p.* FROM vehicle p JOIN account a ON a.id = p.account_id JOIN role r ON r.id = a.role_id WHERE ( p.name LIKE CONCAT('%',:name,'%') OR a.name LIKE CONCAT('%',:name,'%')) AND p.deleteFlg = false AND r.name = :eRole",nativeQuery = true)
    List<Vehicle> findByAccount_NameContainsAndAccount_Role_NameAndDeleteFlgIsFalse(String name, String eRole);

    @Query(value = "SELECT p.* FROM vehicle p JOIN account a ON a.id = p.account_id WHERE ( p.name LIKE CONCAT('%',:name,'%') OR a.name LIKE CONCAT('%',:name,'%')) AND p.deleteFlg = false",nativeQuery = true)
    List<Vehicle> findByAccount_NameContainsAndDeleteFlgIsFalse(String name);

    Boolean existsByLicensePlatesAndDeleteFlgIsFalse(String licensePlates);
}
