package com.example.demo.service.impl;

import com.example.demo.config.exception.InvalidException;
import com.example.demo.domain.model.Account;
import com.example.demo.domain.model.ERole;
import com.example.demo.domain.model.Role;
import com.example.demo.repository.AccountRepository;
import com.example.demo.request.admin.ChangeStatusRequest;
import com.example.demo.request.admin.GetAccountsRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AccountDetailsServiceImplTest {

    @InjectMocks
    private AccountDetailsServiceImpl underTest;

    @Mock
    private AccountRepository userRepository;

    @Test
    public void testLoadUserByUsername() {
        Account account = new Account();
        Role role = new Role();
        role.setName(ERole.ROLE_CUSTOMER);
        account.setRole(role);

        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(account));

        Assert.assertNotNull(underTest.loadUserByUsername("username"));
    }

    @Test
    public void testGetListAccount() {
        GetAccountsRequest request = new GetAccountsRequest();
        List<Account> accounts = new ArrayList<>();
        Account account = new Account();
        Role role = new Role();
        role.setName(ERole.ROLE_CUSTOMER);
        account.setRole(role);
        accounts.add(account);

        Mockito.when(userRepository.getByUsernameNotNull(any())).thenReturn(accounts);

        Assert.assertNotNull(underTest.getListAccount(request));
    }

    @Test
    public void testChangeStatusWhenSuccessful() {
        Mockito.when(userRepository.getById(any())).thenReturn(new Account());

        underTest.changeStatus(new ChangeStatusRequest());
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test(expected = InvalidException.class)
    public void testChangeStatusWhenFailed() {
        underTest.changeStatus(new ChangeStatusRequest());
    }
}