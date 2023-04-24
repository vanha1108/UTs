package com.example.demo.service.impl;

import com.example.demo.config.exception.InvalidException;
import com.example.demo.domain.model.Account;
import com.example.demo.domain.model.Booking;
import com.example.demo.domain.model.Vehicle;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.request.booking.BookingRequest;
import com.example.demo.request.booking.BookingsRequest;
import com.example.demo.request.booking.HomeBookingRequest;
import com.example.demo.request.booking.PutBookingRequest;
import com.example.demo.response.booking.HomeBookingResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.VehicleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl underTest;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private EmailService emailService;

    @Mock
    private VehicleOwnerServiceImpl vehicleOwnerService;

    @Test(expected = InvalidException.class)
    public void testGetVehicleWhenFailed() {
        HomeBookingRequest request = new HomeBookingRequest();
        request.setFromDate(LocalDate.now().minusDays(1));

        underTest.getVehicle(request);
    }

    @Test
    public void testGetVehicleWhenSuccessful() {
        HomeBookingRequest request = new HomeBookingRequest();
        LocalDate now = LocalDate.now();
        request.setFromDate(now.plusDays(1));
        request.setToDate(now.plusDays(2));
        Vehicle vehicle = new Vehicle();
        vehicle.setAccount(new Account());

        Mockito.when(vehicleRepository.getVehicle(any())).thenReturn(Arrays.asList(vehicle));

        HomeBookingResponse result = underTest.getVehicle(request);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getList().size());
    }

    @Test(expected = InvalidException.class)
    public void testBookingWhenFromInvalid() {
        BookingRequest request = new BookingRequest();
        request.setFrom(LocalDate.now().minusDays(1));

        underTest.booking(request);
    }

    @Test(expected = InvalidException.class)
    public void testBookingWhenVehicleNotExist() {
        BookingRequest request = new BookingRequest();
        request.setFrom(LocalDate.now().plusDays(1));
        Vehicle vehicle = new Vehicle();

        Mockito.when(vehicleRepository.findById(any())).thenReturn(Optional.of(vehicle));

        underTest.booking(request);
    }

    @Test
    public void testBooking() {
        BookingRequest request = new BookingRequest();
        request.setFrom(LocalDate.now().plusDays(1));
        request.setAccountId(1L);
        request.setTotal(0L);
        Vehicle vehicle = new Vehicle();

        Mockito.when(vehicleRepository.findById(any())).thenReturn(Optional.of(vehicle));
        Mockito.when(vehicleRepository.existsById(any())).thenReturn(Boolean.TRUE);
        Mockito.when(accountRepository.findById(any())).thenReturn(Optional.of(new Account()));

        Assert.assertNotNull(underTest.booking(request));
    }

    @Test
    public void testSendEmail() {
        BookingRequest request = new BookingRequest();
        request.setEmail("test@yopmail.com");

        Mockito.when(bookingRepository.findAll()).thenReturn(Arrays.asList(new Booking()));

        underTest.sendEmail(request);
        Mockito.verify(emailService, Mockito.times(1)).sendMail(any());
    }

    @Test
    public void testBookingDetails() {
        Booking booking = new Booking();
        booking.setAccount(new Account());
        booking.setStatus(1);
        booking.setVehicle(new Vehicle());

        Mockito.when(bookingRepository.findById(any())).thenReturn(Optional.of(booking));

        Assert.assertNotNull(underTest.bookingDetails(1L));
    }

    @Test
    public void testUserBookings() {
        BookingsRequest request = new BookingsRequest();
        Booking booking = new Booking();
        Vehicle vehicle = new Vehicle();
        vehicle.setAccount(new Account());
        booking.setVehicle(vehicle);

        Mockito.when(bookingRepository.findByAccountIdAndVehicleNameContains(any(), any()))
                .thenReturn(Arrays.asList(booking));

        Assert.assertNotNull(underTest.userBookings(request));
    }

    @Test(expected = InvalidException.class)
    public void testPutBookingWhenBookingNotExist() {
        Mockito.when(bookingRepository.getById(any()))
                .thenReturn(null);

        underTest.putBooking(new PutBookingRequest());
    }

    @Test
    public void testPutBooking() {
        PutBookingRequest request = new PutBookingRequest();
        Booking booking = new Booking();
        Vehicle vehicle = new Vehicle();
        vehicle.setAccount(new Account());
        booking.setVehicle(vehicle);

        Mockito.when(bookingRepository.getById(any()))
                .thenReturn(booking);

        underTest.putBooking(request);
        Mockito.verify(bookingRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void testSetStatusStringWhenStatusIsRUN() {
        Booking booking = new Booking();
        booking.setStatus(3);

        Assert.assertEquals("Đang diễn ra!", underTest.setStatusString(booking));
    }

    @Test
    public void testSetStatusStringWhenStatusIsEmpty() {
        Booking booking = new Booking();
        booking.setStatus(5);

        Assert.assertEquals("", underTest.setStatusString(booking));
    }
}