package com.example.demo.service.impl;

import com.example.demo.config.exception.InvalidException;
import com.example.demo.domain.dto.BookingDto;
import com.example.demo.domain.dto.EmailDetails;
import com.example.demo.domain.dto.FeedBackDto;
import com.example.demo.domain.dto.VehicleDto;
import com.example.demo.domain.model.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.request.booking.BookingRequest;
import com.example.demo.request.booking.BookingsRequest;
import com.example.demo.request.booking.HomeBookingRequest;
import com.example.demo.request.booking.PutBookingRequest;
import com.example.demo.response.booking.BookingDetailsResponse;
import com.example.demo.response.booking.BookingResponse;
import com.example.demo.response.booking.HomeBookingResponse;
import com.example.demo.response.booking.UserBookingsResponse;
import com.example.demo.service.BookingService;
import com.example.demo.service.EmailService;
import com.example.demo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    EmailService emailService;

    @Autowired
    VehicleOwnerServiceImpl vehicleOwnerService;

    @Override
    public HomeBookingResponse getVehicle(HomeBookingRequest request) {
        if (request.getName() == null) {
            request.setName("");
        }
        LocalDate dateTime = LocalDate.now();
        if (request.getFromDate().isBefore(dateTime) || request.getToDate().isBefore(dateTime) || request.getFromDate().isAfter(request.getToDate())) {
            throw new InvalidException(" Thời gian đăng ký phải ơ tương lai ", " Thời gian đăng ký phải ơ tương lai");
        }
        List<Vehicle> list = vehicleRepository.getVehicle(request);
        if (request.getOrderPrice() != null && request.getOrderPrice()) {
            list.sort(Comparator.comparing(Vehicle::getPrice));
        }
        if (request.getOrderPrice() != null && !request.getOrderPrice()) {
            list.sort(Comparator.comparing(Vehicle::getPrice).reversed());
        }
        String background = null;
        List<VehicleDto> dtos = new ArrayList<>();
        for (Vehicle vehicle : list) {
            if (request.getStar() != null) {
                if (request.getStar().equals(0L) && vehicle.getAccount().getPoint() > 9) {
                    continue;
                }
                if (request.getStar().equals(1L) && (vehicle.getAccount().getPoint() > 29 || vehicle.getAccount().getPoint() < 10)) {
                    continue;
                }
                if (request.getStar().equals(2L) && (vehicle.getAccount().getPoint() > 49 || vehicle.getAccount().getPoint() < 30)) {
                    continue;
                }
                if (request.getStar().equals(3L) && (vehicle.getAccount().getPoint() > 69 || vehicle.getAccount().getPoint() < 50)) {
                    continue;
                }
                if (request.getStar().equals(4L) && (vehicle.getAccount().getPoint() > 89 || vehicle.getAccount().getPoint() < 70)) {
                    continue;
                }
                if (request.getStar().equals(5L) && vehicle.getAccount().getPoint() < 90) {
                    continue;
                }
            }


            String image = null;
            if (vehicle.getImageList() != null && !vehicle.getImageList().isEmpty()) {
                for (Image image1 : vehicle.getImageList()) {
                    if (!image1.getDeleteFlg()) {
                        image = image1.getName();
                        break;
                    }
                }
            }

            VehicleDto dto = new VehicleDto(vehicle.getId(), vehicle.getName(), vehicle.getAccount().getAddress(), image, vehicle.getPrice());
            boolean check = false;
            for (Booking booking : vehicle.getBookingList()) {
                if ((!((request.getFromDate().isAfter(booking.getToDate()) && request.getToDate().isAfter(booking.getToDate()))
                        || (request.getFromDate().isBefore(booking.getFromDate()) && request.getToDate().isBefore(booking.getFromDate())))) && (booking.getStatus() == 1 || booking.getStatus() == 3)) {
                    check = true;
                    break;
                }
            }
            if (check) {
                dto.setNotOrder(true);
            }
            dto.setPoint(vehicle.getAccount().getPoint());
            dtos.add(dto);
            if(vehicle.getAccount().getImage() != null){
                background = vehicle.getAccount().getImage();
            }
        }
        HomeBookingResponse response = new HomeBookingResponse();
        response.setList(dtos);
        if(request.getAccountId() != null){
            response.setBackground(background);
        }

        return response;
    }

    @Override
    public BookingResponse booking(BookingRequest request) {
        BookingResponse response = new BookingResponse();
        Optional<Vehicle> vehicle = vehicleRepository.findById(request.getVehicleId());
        if (request.getFrom().isBefore(LocalDate.now())) {
            throw new InvalidException("xe phải đặt sau ngày hiện tại", "xe phải đặt sau ngày hiện tại");
        }
        for (Booking booking : vehicle.get().getBookingList()) {
            if ((!((request.getFrom().isAfter(booking.getToDate()) && request.getTo().isAfter(booking.getToDate()))
                    || (request.getFrom().isBefore(booking.getFromDate()) && request.getTo().isBefore(booking.getFromDate())))) && (booking.getStatus() == 1 || booking.getStatus() == 3)) {
                throw new InvalidException("Xe đã được đặt tại thời gian chọn", "Xe đã được đặt tại thời gian chọn");
            }
        }
        if (!vehicleRepository.existsById(request.getVehicleId())) {
            throw new InvalidException("Xe không tồn tại", "Xe không tồn tại");
        }
        Long accountId = request.getAccountId();
        if (request.getAccountId() == null || request.getAccountId().equals(0L)) {
            if (request.getCardId() == null || request.getBod() == null || request.getPhone() == null || request.getAddress() == null || request.getName() == null) {
                throw new InvalidException("Khách hàng đang nhập thiếu dữ liệu cần thiết", "Khách hàng đang nhập thiếu dữ liệu cần thiết");

            }
            Account account = new Account();
            account.setName(request.getName());
            account.setAddress(request.getAddress());
            account.setPhone(request.getPhone());
            account.setBod(request.getBod());
            account.setCardId(request.getCardId());
            account.setEmail(request.getEmail());


            Optional<Role> role = roleRepository.findByName(ERole.ROLE_ANONYMOUS);
            account.setRole(role.get());
            accountRepository.save(account);
            accountId = account.getId();
        }
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new InvalidException("User không tồn tại", "User không tồn tại");
        }
        Booking booking = new Booking();
        booking.setCreateDate(LocalDateTime.now());
        booking.setFromDate(request.getFrom());
        booking.setToDate(request.getTo());
        booking.setStatus(0);
        booking.setTotalPrice(request.getTotal());
        booking.setVehicle(vehicle.get());
        booking.setAccount(account.get());
        bookingRepository.save(booking);
        response.setKey(booking.getId());
        return response;
    }

    @Override
    public void sendEmail(BookingRequest request) {
        List<Booking> list = bookingRepository.findAll();
        Booking booking = list.get(list.size() - 1);
        Long id = booking.getId();
        if (request.getEmail() != null) {
            emailService.sendMail(new EmailDetails(request.getEmail(), "Mã đặt xe của bạn là :" + id, "Mã đặt xe Bokar"));
        }
    }

    @Override
    public BookingDetailsResponse bookingDetails(Long id) {

        BookingDetailsResponse response = new BookingDetailsResponse();

        Optional<Booking> booking = bookingRepository.findById(id);
        response.setVehicleDetails(vehicleService.getVehicle(booking.get().getVehicle().getId()));
        response.setFrom(booking.get().getFromDate());
        response.setTo(booking.get().getToDate());
        response.setLocation(booking.get().getAccount().getAddress());
        response.setTotal(booking.get().getTotalPrice());
        response.setName(booking.get().getAccount().getName());
        response.setAccountId(booking.get().getAccount().getId());
        response.setAddress(booking.get().getAccount().getAddress());
        response.setPhone(booking.get().getAccount().getPhone());
        response.setBod(booking.get().getAccount().getBod());
        response.setCardId(booking.get().getAccount().getCardId());
        response.setStatus(booking.get().getStatus());
        response.setStatusString(setStatusString(booking.get()));
        response.setEmail(booking.get().getAccount().getEmail());
        if (response.getStatusString().equals(END)) {
            boolean check = true;
            for (FeedBack feedBack : booking.get().getFeedBackList()) {
                if (feedBack.isRoleType()) {
                    response.setFeedBack(new FeedBackDto(booking.get().getAccount().getName(),feedBack.getPoint(),feedBack.getContent()));
                    check = false;
                }
            }
            response.setTurnOnFeedBack(check);
        }

        return response;
    }

    @Override
    public UserBookingsResponse userBookings(BookingsRequest request) {
        if (request.getName() == null) {
            request.setName("");
        }
        List<Booking> bookings = bookingRepository.findByAccountIdAndVehicleNameContains(request.getAccountId(), request.getName());

        List<BookingDto> dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            if (request.getStatus() != null && request.getStatus() != booking.getStatus()) {
                continue;
            }
            BookingDto dto = new BookingDto();
            dto.setBookingId(booking.getId());
            dto.setNameVehicle(booking.getVehicle().getName());
            dto.setNameOwner(booking.getVehicle().getAccount().getName());
            dto.setStatusString(vehicleOwnerService.setStatusString(booking));
            dtos.add(dto);
        }
        return new UserBookingsResponse(dtos);
    }

    @Override
    public void putBooking(PutBookingRequest request) {
        Booking booking = bookingRepository.getById(request.getBookingId());
        if (booking == null) {
            throw new InvalidException("Đơn không tồn tại", "Đơn không tồn tại");
        }

        if (request.getFrom() != null && request.getTo() != null) {
            Vehicle vehicle = vehicleRepository.getById(booking.getVehicle().getId());
            if (request.getFrom().isBefore(LocalDate.now())) {
                throw new InvalidException("xe phải đặt sau ngày hiện tại", "xe phải đặt sau ngày hiện tại");
            }
            for (Booking book : vehicle.getBookingList()) {
                if ((!((request.getFrom().isAfter(book.getToDate()) && request.getTo().isAfter(book.getToDate()))
                        || (request.getFrom().isBefore(book.getFromDate()) && request.getTo().isBefore(book.getFromDate())))) && (book.getStatus() == 1 || book.getStatus() == 3)) {
                    throw new InvalidException("Xe đã được đặt tại thời gian bạn", "Xe đã được đặt tại thời gian bạn");
                }
            }
            booking.setFromDate(request.getFrom());
            booking.setToDate(request.getTo());
        }


        if (request.getCancel() != null && request.getCancel().equals(true)) {
            booking.setStatus(2);
        }
        bookingRepository.save(booking);
    }


    public String setStatusString(Booking booking) {
        if (booking.getStatus() == 3) {
            return RUN;
        }
        if (booking.getStatus() == 4) {
            return END;
        }
        if (booking.getStatus() == 1) {
            return ACCEPT;
        }
        if (booking.getStatus() == 0) {
            return WAIT;
        }
        if (booking.getStatus() == 2) {
            return REFUSE;
        }
        return "";
    }

    String RUN = "Đang diễn ra!";
    String WAIT = "Đặt xe thành công. Vui lòng chờ chủ xe xác nhận!";
    String END = "Đơn đặt xe của bạn đã kết thúc!";
    String ACCEPT = "Đơn đặt xe của bạn đã được xác nhận. Vui lòng nhận xe đúng thời gian đã đặt!";
    String REFUSE = " Đơn đặt xe của bạn đã bị huỷ!";

}
