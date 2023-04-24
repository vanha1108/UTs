package com.example.demo.service.impl;

import com.example.demo.config.exception.VsException;
import com.example.demo.domain.model.*;
import com.example.demo.repository.*;
import com.example.demo.request.admin.ConfirmVehicleRequest;
import com.example.demo.request.admin.ListVehicleRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceImplTest {

    @InjectMocks
    private VehicleServiceImpl underTest;

    @Mock
    VehicleTypeRepository vehicleTypeRepository;

    @Mock
    VehicleCompanyRepository vehicleCompanyRepository;

    @Mock
    TranmistionTypeRepository tranmistionTypeRepository;

    @Mock
    SeatTypeRepository seatTypeRepository;

    @Mock
    FuelTypeRepository fuelTypeRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    ColorTypeRepository colorTypeRepository;

    @Test
    public void testGetVehicleType() {
        Mockito.when(vehicleTypeRepository.findAll()).thenReturn(Arrays.asList(new VehicleType()));

        Assert.assertNotNull(underTest.getVehicleType());
    }

    @Test
    public void testGetVehicleCompany() {
        Mockito.when(vehicleCompanyRepository.findAll()).thenReturn(Arrays.asList(new VehicleCompany()));

        Assert.assertNotNull(underTest.getVehicleCompany());
    }

    @Test
    public void testGetTranmistionType() {
        Mockito.when(tranmistionTypeRepository.findAll()).thenReturn(Arrays.asList(new TranmistionType()));

        Assert.assertNotNull(underTest.getTranmistionType());
    }

    @Test
    public void testGetSeatType() {
        Mockito.when(seatTypeRepository.findAll()).thenReturn(Arrays.asList(new SeatType()));

        Assert.assertNotNull(underTest.getSeatType());
    }

    @Test
    public void testGetFuelType() {
        Mockito.when(fuelTypeRepository.findAll()).thenReturn(Arrays.asList(new FuelType()));

        Assert.assertNotNull(underTest.getFuelType());
    }

    @Test
    public void testGetColorType() {
        Mockito.when(colorTypeRepository.findAll()).thenReturn(Arrays.asList(new ColorType()));

        Assert.assertNotNull(underTest.getColorType());
    }

    @Test
    public void testGetShop() {
        Mockito.when(accountRepository.findShop(anyString())).thenReturn(Arrays.asList(new Account()));

        Assert.assertNotNull(underTest.getShop(anyString()));
    }

    @Test
    public void testGetVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setAccount(new Account());
        vehicle.setVehicleCompany(new VehicleCompany());
        vehicle.setVehicleType(new VehicleType());

        Mockito.when(vehicleRepository.findByIdAndStatusIsTrueAndDeleteFlgIsFalse(any()))
                .thenReturn(Optional.of(vehicle));

        Assert.assertNotNull(underTest.getVehicle(1L));
    }

    @Test
    public void testGetList() {
        ListVehicleRequest request = new ListVehicleRequest();
        Vehicle vehicle = new Vehicle();
        Account account = new Account();
        Role role = new Role();
        role.setName(ERole.ROLE_CUSTOMER);
        account.setRole(role);
        vehicle.setAccount(account);
        vehicle.setFuelType(new FuelType());
        vehicle.setTranmistionType(new TranmistionType());
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(1L);
        vehicle.setVehicleType(vehicleType);
        vehicle.setVehicleCompany(new VehicleCompany());

        Mockito.when(vehicleRepository.findByAccount_NameContainsAndDeleteFlgIsFalse(any()))
                .thenReturn(Arrays.asList(vehicle));

        Assert.assertNotNull(underTest.getList(request));
    }

    @Test(expected = VsException.class)
    public void testConfirmVehicleWhenVehicleNotExist() {
        Mockito.when(vehicleRepository.getById(any())).thenReturn(null);

        underTest.confirmVehicle(new ConfirmVehicleRequest());
    }

    @Test
    public void testConfirmVehicle() {
        Mockito.when(vehicleRepository.getById(any())).thenReturn(new Vehicle());

        underTest.confirmVehicle(new ConfirmVehicleRequest());
        Mockito.verify(vehicleRepository, Mockito.times(1)).save(any());
    }
}