package com.example.demo.service.impl;

import com.example.demo.config.exception.InvalidException;
import com.example.demo.domain.dto.BookingDto;
import com.example.demo.domain.dto.FeedBackDto;
import com.example.demo.domain.dto.VehicleDto;
import com.example.demo.domain.model.*;
import com.example.demo.repository.*;
import com.example.demo.request.vehicle_owner.CreateVehicleRequest;
import com.example.demo.request.vehicle_owner.EditBookingRequest;
import com.example.demo.request.vehicle_owner.GetVehiclesRequest;
import com.example.demo.request.vehicle_owner.OwnerBookingRequest;
import com.example.demo.response.vehicle.VehicleDetailsResponse;
import com.example.demo.response.vehicle_owner.GetVehiclesResponse;
import com.example.demo.response.vehicle_owner.OwnerBookingResponse;
import com.example.demo.service.VehicleOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleOwnerServiceImpl implements VehicleOwnerService {

    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    BookingRepository bookingRepository;

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
    ColorTypeRepository colorTypeRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FeatureRepository featureRepository;


    @Override
    public OwnerBookingResponse listBooking(OwnerBookingRequest request) {
        if (request.getKeyword() == null) {
            request.setKeyword("");
        }
        List<Booking> list = bookingRepository.findByVehicle(request);
        if (request.getCreateDate() == null) {
        } else if (request.getCreateDate().equals(true)) {
            list.sort(Comparator.comparing(Booking::getCreateDate));
        } else {
            list.sort(Comparator.comparing(Booking::getCreateDate).reversed());
        }
        OwnerBookingResponse response = new OwnerBookingResponse();
        List<BookingDto> dtoList = new ArrayList<>();
        for (Booking booking : list) {
            if (request.getStatus() != null && !request.getStatus().equals(booking.getStatus())) {
                continue;
            }
            BookingDto dto = new BookingDto();
            dto.setNameCustomer(booking.getAccount().getName());
            dto.setNameVehicle(booking.getVehicle().getName());
            dto.setPhone(booking.getAccount().getPhone());
            dto.setBookingId(booking.getId());
            dto.setBod(booking.getAccount().getBod());
            dto.setCardId(booking.getAccount().getCardId());
            dto.setAddress(booking.getAccount().getAddress());
            dto.setCreateDate(booking.getCreateDate());
            dto.setFrom(booking.getFromDate());
            dto.setTo(booking.getToDate());
            dto.setPoint(booking.getAccount().getPoint());
            dto.setStatus(booking.getStatus());
            dto.setLicensePlates(booking.getVehicle().getLicensePlates());

            String status = setStatusString(booking);
            dto.setStatusString(status);
            dto.setTotal(booking.getTotalPrice());
            dto.setPrice(booking.getVehicle().getPrice());
            dto.setAccount(booking.getAccount().getUsername() == null ? false : true);
            List<FeedBackDto> list1 = new ArrayList<>();
            for (Booking booking1 : booking.getAccount().getBookings()){
                for (FeedBack feedBack : booking1.getFeedBackList()) {
                    if (!feedBack.isRoleType()) {
                        list1.add(new FeedBackDto(booking1.getVehicle().getAccount().getName(), feedBack.getPoint(), feedBack.getContent()));
                    }
                }
            }

            dto.setFeedbacks(list1);

            List<String> images = new ArrayList<>();
            for (Image image : booking.getVehicle().getImageList()) {

                if (!image.getDeleteFlg()) images.add(image.getName());
            }

            dto.setImages(images);
            dtoList.add(dto);
        }
        response.setList(dtoList);
        return response;
    }

    @Override
    public void editBooking(EditBookingRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId()).get();

        Optional<Vehicle> vehicle = vehicleRepository.findById(booking.getVehicle().getId());
        for (Booking book : vehicle.get().getBookingList()) {
            if (booking.getId().equals(book.getId())) {
                continue;
            }
            if (((request.getFrom() != null && request.getFrom().isAfter(book.getFromDate()) && request.getFrom().isBefore(book.getToDate()))
                    || (request.getFrom() != null && request.getTo().isAfter(book.getFromDate()) && request.getTo().isBefore(book.getToDate()))) && book.getStatus() == 1) {
                throw new InvalidException("Xe đã được đặt tại thời gian bạn", "Xe đã được đặt tại thời gian bạn");
            }
        }
        if (request.getName() != null && !request.getName().isBlank()) {
            booking.getAccount().setName(request.getName());
        }
        if (request.getCardId() != null && !request.getCardId().isBlank()) {
            booking.getAccount().setCardId(request.getCardId());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            booking.getAccount().setPhone(request.getPhone());
        }
        if (request.getFrom() != null) {
            booking.setFromDate(request.getFrom());
        }
        if (request.getTo() != null) {
            booking.setToDate(request.getTo());
        }
        if (request.getTotal() != null) {
            booking.setTotalPrice(request.getTotal());
        }
        if (request.getIsAccept() != null) {
            booking.setStatus(request.getIsAccept());
        }
        if (request.getBod() != null) {
            booking.getAccount().setBod(request.getBod());
        }
        if (request.getAddress() != null) {
            booking.getAccount().setAddress(request.getAddress());
        }
        bookingRepository.save(booking);
    }

    @Override
    public GetVehiclesResponse getVehicles(GetVehiclesRequest request) {
        if (request.getName() == null) {
            request.setName("");
        }
        GetVehiclesResponse response = new GetVehiclesResponse();

        List<Vehicle> list = vehicleRepository.findByAccountIdAndNameContainsOrLicensePlatesContains(request.getOwnerId(), request.getName(), request.getName());

        List<VehicleDto> dtosA = new ArrayList<>();
        List<VehicleDto> dtosB = new ArrayList<>();
        List<VehicleDto> dtosC = new ArrayList<>();
        for (Vehicle vehicle : list) {
            if (vehicle.getDeleteFlg() || vehicle.getAccount().getIsBLock()) {
                continue;
            }
            if (request.getStatus() != null) {
                if (request.getStatus() == 0 && vehicle.getStatus() != null) {
                    continue;
                }
                if (request.getStatus() == 1 && (vehicle.getStatus() == null || !vehicle.getStatus())) {
                    continue;
                }
                if (request.getStatus() == 2 && (vehicle.getStatus() == null || vehicle.getStatus())) {
                    continue;
                }
            }
            boolean check = false;
            for (Booking booking : vehicle.getBookingList()) {
                if ((booking.getStatus() != 2 && booking.getStatus() != 4) && booking.getToDate().isAfter(LocalDate.now())) {
                    check = true;
                }

            }
            if (vehicle.getVehicleType().getId().equals(1L)) {
                dtosA.add(new VehicleDto(vehicle.getId(), vehicle.getName(), vehicle.getLicensePlates(), vehicle.getStatus(), check));

            }
            if (vehicle.getVehicleType().getId().equals(2L)) {
                dtosB.add(new VehicleDto(vehicle.getId(), vehicle.getName(), vehicle.getLicensePlates(), vehicle.getStatus(), check));

            }
            if (vehicle.getVehicleType().getId().equals(3L)) {
                dtosC.add(new VehicleDto(vehicle.getId(), vehicle.getName(), vehicle.getLicensePlates(), vehicle.getStatus(), check));

            }
        }
        response.setListCar(dtosA);
        response.setListMotorbike(dtosB);
        response.setListBicycle(dtosC);

        return response;
    }

    @Override
    @Transactional
    public void createVehicle(CreateVehicleRequest request) {
        Account account = accountRepository.getById(request.getAccountId());
        VehicleType type = vehicleTypeRepository.getById(request.getType());
        VehicleCompany vehicleCompany = vehicleCompanyRepository.getById(request.getVehicleCompany());

        if (vehicleRepository.existsByLicensePlatesAndDeleteFlgIsFalse(request.getLicensePlates())) {
            throw new InvalidException("Biển số xe đã được sử dụng", "Biển số xe đã được sử dụng");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setName(request.getName());
        vehicle.setAccount(account);
        vehicle.setVehicleType(type);
        if (request.getColor() != null) {
            ColorType colorType = colorTypeRepository.getById(request.getColor());
            vehicle.setColorType(colorType);
        }
        if (request.getFuel() != null) {
            FuelType fuelType = fuelTypeRepository.getById(request.getFuel());
            vehicle.setFuelType(fuelType);

        }
        if (request.getSeat() != null) {
            SeatType seatType = seatTypeRepository.getById(request.getSeat());
            vehicle.setSeatType(seatType);

        }
        if (request.getTranmistion() != null) {
            TranmistionType tranmistionType = tranmistionTypeRepository.getById(request.getTranmistion());
            vehicle.setTranmistionType(tranmistionType);
        }
        vehicle.setVehicleCompany(vehicleCompany);
        vehicle.setPrice(request.getPrice());
        vehicle.setLocation(request.getLocation());

        vehicle.setPoint(100L);
        vehicle.setRule(request.getRule());
        vehicle.setLicensePlates(request.getLicensePlates());
        vehicle.setDeleteFlg(false);

        vehicleRepository.save(vehicle);
        List<Image> imageList = new ArrayList<>();
        for (String s : request.getImageList()) {
            imageList.add(new Image(s, vehicle, false));
        }

        List<Feature> featureList = new ArrayList<>();
        for (String s : request.getFeatureList()) {
            featureList.add(new Feature(s, vehicle, false));
        }

        imageRepository.saveAll(imageList);
        featureRepository.saveAll(featureList);


    }

    @Override
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.getById(id);
        vehicle.setDeleteFlg(true);
        vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional
    public void editVehicle(Long id, CreateVehicleRequest request) {
        VehicleType type = vehicleTypeRepository.getById(request.getType());

        VehicleCompany vehicleCompany = vehicleCompanyRepository.getById(request.getVehicleCompany());


        Vehicle vehicle = vehicleRepository.findById(id).get();

        if (vehicleRepository.existsByLicensePlatesAndDeleteFlgIsFalse(request.getLicensePlates()) && !vehicle.getLicensePlates().equals(request.getLicensePlates())) {
            throw new InvalidException("Biển số xe đã được đăng ký", "Biển số xe đã được đăng ký");
        }
        vehicle.setName(request.getName());
        vehicle.setVehicleType(type);
        if (request.getColor() != null) {
            ColorType colorType = colorTypeRepository.getById(request.getColor());
            vehicle.setColorType(colorType);
        }
        if (request.getFuel() != null) {
            FuelType fuelType = fuelTypeRepository.getById(request.getFuel());
            vehicle.setFuelType(fuelType);

        }
        if (request.getSeat() != null) {
            SeatType seatType = seatTypeRepository.getById(request.getSeat());
            vehicle.setSeatType(seatType);

        }
        if (request.getTranmistion() != null) {
            TranmistionType tranmistionType = tranmistionTypeRepository.getById(request.getTranmistion());
            vehicle.setTranmistionType(tranmistionType);
        }
        vehicle.setVehicleCompany(vehicleCompany);
        vehicle.setPrice(request.getPrice());
        vehicle.setLocation(request.getLocation());

        vehicle.setPoint(100L);
        vehicle.setRule(request.getRule());
        vehicle.setLicensePlates(request.getLicensePlates());

        vehicleRepository.save(vehicle);

        List<Image> i = new ArrayList<>();
        if (vehicle.getImageList() != null || !vehicle.getImageList().isEmpty()) {
            for (Image image : vehicle.getImageList()) {
                if (!image.getDeleteFlg()) {
                    image.setDeleteFlg(true);
                    i.add(image);
                }

            }
        }
        if (!i.isEmpty()) {
            imageRepository.saveAll(i);

        }
        List<Feature> featureList1 = new ArrayList<>();
        if (vehicle.getFeatureList() != null || !vehicle.getFeatureList().isEmpty()) {

            for (Feature image : vehicle.getFeatureList()) {
                if (!image.getDeleteFlg()) {
                    image.setDeleteFlg(true);
                    featureList1.add(image);
                }
            }
        }
        if (!featureList1.isEmpty()) {
            featureRepository.saveAll(featureList1);

        }

        List<Image> imageList = new ArrayList<>();
        for (String s : request.getImageList()) {
            imageList.add(new Image(s, vehicle, false));
        }

        List<Feature> featureList = new ArrayList<>();
        for (String s : request.getFeatureList()) {
            featureList.add(new Feature(s, vehicle, false));
        }

        imageRepository.saveAll(imageList);
        featureRepository.saveAll(featureList);

    }

    @Override
    public void addImage(Long id, String image) {
        Account account = accountRepository.getById(id);

        if (account == null) {
            throw new InvalidException("Tài khoản không tồn tại", "Tài khoản không tồn tại");
        }

        account.setImage(image);
        accountRepository.save(account);
    }

    @Override
    public VehicleDetailsResponse detailVehicle(Long id) {
        VehicleDetailsResponse response = new VehicleDetailsResponse();
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);

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
        response.setSeat(vehicle.get().getSeatType() == null ? null :  vehicle.get().getSeatType().getName());

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
        for (Booking booking : vehicle.get().getBookingList()) {
            if (booking.getStatus() != 0 || booking.getStatus() != 2) {
                for (FeedBack feedBack : booking.getFeedBackList()) {
                    if (!feedBack.isRoleType()) {
                        feedBackList.add(new FeedBackDto(booking.getAccount().getName(), feedBack.getPoint(), feedBack.getContent()));
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

    public String setStatusString(Booking booking) {
        if (booking.getStatus() == 1) {
            return ACCEPT;
        }
        if (booking.getStatus() == 2) {
            return REFUSE;
        }
        if (booking.getStatus() == 3) {
            return RUN;
        }
        if (booking.getStatus() == 0) {
            return WAIT;
        }
        if (booking.getStatus() == 4) {
            return END;
        }
        return "";
    }

    String RUN = "Đã nhận xe";
    String WAIT = "Đang chờ";
    String END = "Kết thúc";
    String ACCEPT = "Chấp nhận";

    String REFUSE = "Từ chối";


}
