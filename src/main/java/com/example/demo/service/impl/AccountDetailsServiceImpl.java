package com.example.demo.service.impl;

import com.example.demo.config.exception.InvalidException;
import com.example.demo.config.security.AccountDetailsImpl;
import com.example.demo.domain.dto.AccountDto;
import com.example.demo.domain.dto.FeedBackDto;
import com.example.demo.domain.model.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.request.admin.ChangeStatusRequest;
import com.example.demo.request.admin.GetAccountsRequest;
import com.example.demo.response.admin.ListAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AccountRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return AccountDetailsImpl.build(user);
    }

    public ListAccountResponse getListAccount(GetAccountsRequest request) {
        if(request.getKeyword() == null){
            request.setKeyword("");
        }
        List<Account> list = userRepository.getByUsernameNotNull(request);
        List<AccountDto> dtos = new ArrayList<>();
        for (Account account : list) {
            if (request.getIsBlock() != null) {
                if (!request.getIsBlock().equals(account.getIsBLock())) {
                    continue;
                }
            }
            AccountDto dto = new AccountDto();
            dto.setId(account.getId());
            dto.setRole(account.getRole().getName().name());
            if (request.getRole() != null && !request.getRole().equals(dto.getRole())) {
                continue;
            }
            dto.setEmail(account.getEmail());
            dto.setName(account.getName());
            dto.setIsBlock(account.getIsBLock());
            dto.setUserName(account.getUsername());
            dto.setAddress(account.getAddress());
            dto.setBod(account.getBod());
            dto.setPhone(account.getPhone());
            dto.setGender(account.getGender());
            dto.setPoint(account.getPoint());
            dto.setCardId(account.getCardId());

            List<FeedBackDto> feedBacks = new ArrayList<>();
            if (dto.getRole().equals(ERole.ROLE_RENTAL.name()) || dto.getRole().equals(ERole.ROLE_SHOP.name())) {
                for (Vehicle vehicle : account.getVehicles()) {
                    for (Booking booking : vehicle.getBookingList()) {
                        for (FeedBack feedBack : booking.getFeedBackList()) {
                            if (feedBack.isRoleType()) {
                                feedBacks.add(new FeedBackDto(booking.getAccount().getName(), feedBack.getPoint(), feedBack.getContent()));
                            }
                        }
                    }
                }
            }
            if (dto.getRole().equals(ERole.ROLE_CUSTOMER.name())) {
                for (Booking booking : account.getBookings()) {
                    for (FeedBack feedBack : booking.getFeedBackList()) {
                        if (!feedBack.isRoleType()) {
                            feedBacks.add(new FeedBackDto(booking.getVehicle().getAccount().getName(), feedBack.getPoint(), feedBack.getContent()));
                        }
                    }
                }
            }
            dto.setFeedBacks(feedBacks);
            dtos.add(dto);
        }
        return new ListAccountResponse(dtos);
    }

    public void changeStatus(ChangeStatusRequest request) {
        Account account = userRepository.getById(request.getId());
        if (account == null) {
            throw new InvalidException("Tải khoản không tồn tại", "Tải khoản không tồn tại");
        }
        account.setIsBLock(request.getIsBlock());
        userRepository.save(account);
    }


}
