package com.example.demo.service.impl;

import com.example.demo.config.exception.VsException;
import com.example.demo.domain.dto.FeedBackDto;
import com.example.demo.domain.dto.VehicleDetailDto;
import com.example.demo.domain.model.*;
import com.example.demo.repository.*;
import com.example.demo.request.admin.ConfirmVehicleRequest;
import com.example.demo.request.admin.ListVehicleRequest;
import com.example.demo.response.vehicle.GetVehiclesResponse;
import com.example.demo.response.admin.ListVehicleResponse;
import com.example.demo.response.vehicle.VehicleDetailsResponse;
import com.example.demo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    VehicleCompanyRepository vehicleCompanyRepository;
    @Autowired
    TranmistionTypeRepository tranmistionTypeRepository;
    @Autowired
    SeatTypeRepository seatTypeRepository;
    @Autowired
    FuelTypeRepository fuelTypeRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    ColorTypeRepository colorTypeRepository;


    @Override
    public GetVehiclesResponse getVehicleType() {
        List<VehicleType> list = vehicleTypeRepository.findAll();
        List<VehicleDetailDto> dtos = new ArrayList<>();
        for (VehicleType type : list) {
            dtos.add(new VehicleDetailDto(type.getId(), type.getName()));
        }
        return new GetVehiclesResponse(dtos);
    }

    @Override
    public GetVehiclesResponse getVehicleCompany() {
        List<VehicleCompany> list = vehicleCompanyRepository.findAll();
        List<VehicleDetailDto> dtos = new ArrayList<>();
        for (VehicleCompany type : list) {
            dtos.add(new VehicleDetailDto(type.getId(), type.getName()));
        }
        return new GetVehiclesResponse(dtos);
    }

    @Override
    public GetVehiclesResponse getTranmistionType() {
        List<TranmistionType> list = tranmistionTypeRepository.findAll();
        List<VehicleDetailDto> dtos = new ArrayList<>();
        for (TranmistionType type : list) {
            dtos.add(new VehicleDetailDto(type.getId(), type.getName()));
        }
        return new GetVehiclesResponse(dtos);
    }

    @Override
    public GetVehiclesResponse getSeatType() {
        List<SeatType> list = seatTypeRepository.findAll();
        List<VehicleDetailDto> dtos = new ArrayList<>();
        for (SeatType type : list) {
            dtos.add(new VehicleDetailDto(type.getId(), type.getName()));
        }
        return new GetVehiclesResponse(dtos);
    }

    @Override
    public GetVehiclesResponse getFuelType() {
        List<FuelType> list = fuelTypeRepository.findAll();
        List<VehicleDetailDto> dtos = new ArrayList<>();
        for (FuelType type : list) {
            dtos.add(new VehicleDetailDto(type.getId(), type.getName()));
        }
        return new GetVehiclesResponse(dtos);
    }

    @Override
    public GetVehiclesResponse getColorType() {
        List<ColorType> list = colorTypeRepository.findAll();
        List<VehicleDetailDto> dtos = new ArrayList<>();
        for (ColorType type : list) {
            dtos.add(new VehicleDetailDto(type.getId(), type.getName()));
        }
        return new GetVehiclesResponse(dtos);
    }

    @Override
    public GetVehiclesResponse getShop(String location) {
        List<Account> list = accountRepository.findShop(location);
        List<VehicleDetailDto> dtos = new ArrayList<>();
        for (Account type : list) {
            dtos.add(new VehicleDetailDto(type.getId(), type.getName()));
        }
        return new GetVehiclesResponse(dtos);
    }

    @Override
    public VehicleDetailsResponse getVehicle(Long id) {
        VehicleDetailsResponse response = new VehicleDetailsResponse();
        Optional<Vehicle> vehicle = vehicleRepository.findByIdAndStatusIsTrueAndDeleteFlgIsFalse(id);

        response.setOwner(vehicle.get().getAccount().getName());
        response.setPhone(vehicle.get().getAccount().getPhone());
        response.setRule(vehicle.get().getRule());
        response.setId(vehicle.get().getId());

        List<String> images = new ArrayList<>();
        for (Image image : vehicle.get().getImageList()) {
            if (!image.getDeleteFlg()) images.add(image.getName());
        }
        response.setImageList(images);
        response.setColor(vehicle.get().getColorType() == null ? null : vehicle.get().getColorType().getName());
        response.setFuel(vehicle.get().getFuelType() == null ? null : vehicle.get().getFuelType().getName());
        response.setSeat(vehicle.get().getSeatType() == null ? null : vehicle.get().getSeatType().getName());

        List<String> featureList = new ArrayList<>();
        for (Feature feature : vehicle.get().getFeatureList()) {
            if (!feature.getDeleteFlg()) {
                featureList.add(feature.getName());
            }
        }
        response.setFeatureList(featureList);
        response.setPrice(vehicle.get().getPrice());
        Long totalRun = 0L;
        List<FeedBackDto> feedBackList = new ArrayList<>();
        for (Booking booking : vehicle.get().getBookingList()) {
            if (booking.getStatus() == 4 && booking.getToDate().isBefore(LocalDate.now().plusDays(1))) {
                totalRun++;

            }

        }
        for (Vehicle vehicle1 : vehicle.get().getAccount().getVehicles()) {
            for (Booking booking : vehicle1.getBookingList()) {
                if (booking.getStatus() != 0 || booking.getStatus() != 2) {
                    for (FeedBack feedBack : booking.getFeedBackList()) {
                        if (feedBack.isRoleType()) {
                            feedBackList.add(new FeedBackDto(booking.getAccount().getName(), feedBack.getPoint(), feedBack.getContent()));
                        }
                    }
                }

            }
        }

        response.setTotalRun(totalRun);
        response.setFeedBackList(feedBackList);
        response.setLocation(vehicle.get().getAccount().getAddress());
        response.setTranmistion(vehicle.get().getTranmistionType() == null ? null : vehicle.get().getTranmistionType().getName());
        response.setLicensePlates(vehicle.get().getLicensePlates());
        response.setVehicleName(vehicle.get().getName());
        response.setVehicleCompany(vehicle.get().getVehicleCompany().getName());
        response.setVehicleType(vehicle.get().getVehicleType().getName());
        response.setStatus(vehicle.get().getStatus());
        response.setPoint(vehicle.get().getAccount().getPoint());

        return response;
    }

    @Override
    public ListVehicleResponse getList(ListVehicleRequest request) {
        if (request.getName() == null) {
            request.setName("");
        }
        List<Vehicle> list = vehicleRepository.findByAccount_NameContainsAndDeleteFlgIsFalse(request.getName());
        if (request.getStatus() != null && request.getRole() != null) {
            Boolean status = null;
            if (request.getStatus().equals(2)) {
                status = true;
            }
            if (request.getStatus().equals(3)) {
                status = false;
            }


            if (request.getRole().equals(1)) {
                list = vehicleRepository.findByAccount_NameContainsAndStatusAndAccount_Role_NameAndDeleteFlgIsFalse(request.getName(), status, ERole.ROLE_RENTAL.name());
            }
            if (request.getRole().equals(2)) {
                list = vehicleRepository.findByAccount_NameContainsAndStatusAndAccount_Role_NameAndDeleteFlgIsFalse(request.getName(), status, ERole.ROLE_SHOP.name());

            }

        }
        if (request.getStatus() != null && request.getRole() == null) {
            Boolean status = null;
            if (request.getStatus().equals(2)) {
                status = true;
            }
            if (request.getStatus().equals(3)) {
                status = false;
            }
            list = vehicleRepository.findByAccount_NameContainsAndStatusAndDeleteFlgIsFalse(request.getName(), status);
        }

        if (request.getStatus() == null && request.getRole() != null) {
            if (request.getRole().equals(1)) {
                list = vehicleRepository.findByAccount_NameContainsAndAccount_Role_NameAndDeleteFlgIsFalse(request.getName(), ERole.ROLE_RENTAL.name());
            }
            if (request.getRole().equals(2)) {
                list = vehicleRepository.findByAccount_NameContainsAndAccount_Role_NameAndDeleteFlgIsFalse(request.getName(), ERole.ROLE_SHOP.name());

            }
        }
        List<VehicleDetailsResponse> car = new ArrayList<>();
        List<VehicleDetailsResponse> motor = new ArrayList<>();
        List<VehicleDetailsResponse> bike = new ArrayList<>();
        for (Vehicle vehicle : list) {
            VehicleDetailsResponse v = new VehicleDetailsResponse();
            v.setId(vehicle.getId());
            v.setOwner(vehicle.getAccount().getName());
            v.setPhone(v.getPhone());
            List<String> images = new ArrayList<>();
            for (Image image : vehicle.getImageList()) {
                if (!image.getDeleteFlg()) images.add(image.getName());
            }
            v.setImageList(images);
            v.setColor(vehicle.getColorType() == null ? null : vehicle.getColorType().getName());
            v.setFuel(vehicle.getFuelType() == null ? null : vehicle.getFuelType().getName());
            v.setSeat(vehicle.getSeatType() == null ? null : vehicle.getSeatType().getName());
            List<String> features = new ArrayList<>();
            for (Feature image : vehicle.getFeatureList()) {
                if (!image.getDeleteFlg()) features.add(image.getName());
            }
            v.setFeatureList(features);
            v.setPrice(vehicle.getPrice());
            v.setTranmistion(vehicle.getTranmistionType() == null ? null : vehicle.getTranmistionType().getName());
            v.setVehicleName(vehicle.getName());
            v.setRole(vehicle.getAccount().getRole().getName().name());
            v.setVehicleType(vehicle.getVehicleType().getName());

            List<FeedBackDto> feedBackList = new ArrayList<>();
            Long totalRun = 0L;
            for (Booking booking : vehicle.getBookingList()) {
                if (booking.getStatus() == 4 && booking.getToDate().isBefore(LocalDate.now())) {
                    totalRun++;

                }

            }
            boolean check = false;
            for (Booking booking : vehicle.getBookingList()) {
                if (booking.getStatus() != 0 || booking.getStatus() != 2) {
                    for (FeedBack feedBack : booking.getFeedBackList()) {
                        if (feedBack.isRoleType()) {
                            feedBackList.add(new FeedBackDto(booking.getAccount().getName(), feedBack.getPoint(), feedBack.getContent()));
                        }
                    }
                }
                if ((booking.getStatus() != 2 && booking.getStatus() != 4) && booking.getToDate().isAfter(LocalDate.now())) {
                    check = true;
                }

            }
            v.setTotalRun(totalRun);
            v.setFeedBackList(feedBackList);
            v.setLocation(vehicle.getAccount().getAddress());
            v.setRule(vehicle.getRule());
            v.setStatus(vehicle.getStatus());
            v.setId(vehicle.getId());
            v.setLicensePlates(vehicle.getLicensePlates());
            v.setVehicleType(vehicle.getVehicleType().getName());
            v.setStatus(vehicle.getStatus());
            v.setNotDelete(check);
            v.setVehicleCompany(vehicle.getVehicleCompany().getName());
            if (vehicle.getVehicleType().getId().equals(1L)) {
                car.add(v);
            }
            if (vehicle.getVehicleType().getId().equals(2L)) {
                motor.add(v);
            }
            if (vehicle.getVehicleType().getId().equals(3L)) {
                bike.add(v);
            }
        }

        return new ListVehicleResponse(car, motor, bike);
    }

    @Override
    public void confirmVehicle(ConfirmVehicleRequest request) {
        Vehicle vehicle = vehicleRepository.getById(request.getId());

        if (vehicle == null) {
            throw new VsException("Vehicle khong ton tai", "Vehicle khong ton tai");
        }
        vehicle.setStatus(request.getStatus());
        vehicleRepository.save(vehicle);
    }
}
