package com.example.demo.service.impl;

import com.example.demo.config.exception.InvalidException;
import com.example.demo.domain.dto.FeedBackDto;
import com.example.demo.domain.model.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.FeedBackRepository;
import com.example.demo.request.comment.CommentRequest;
import com.example.demo.response.ListFeedBack;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Override
    @Transactional
    public void comment(CommentRequest request) {

        Booking booking = bookingRepository.getById(request.getBookingId());

        if (booking == null) {
            throw new InvalidException(" ĐƠn không tồn tại", " ĐƠn không tồn tại");
        }

        Account account = accountRepository.getById(request.getAccountId());

        if (account == null) {
            throw new InvalidException(" Account không tồn tại", " Account không tồn tại");
        }

        if ((!booking.getAccount().getId().equals(account.getId())) && !booking.getVehicle().getAccount().getId().equals(account.getId())) {
            throw new InvalidException(" Account không  đặt đơn này", " Account không  đặt đơn này");
        }

        List<FeedBack> feeds = feedBackRepository.findByBookingId(request.getBookingId());

        FeedBack feedBack = new FeedBack();
        if (account.getRole().getName().equals(ERole.ROLE_CUSTOMER)) {
            for (FeedBack feedBack1 : feeds) {
                if (feedBack1.isRoleType()) {
                    throw new InvalidException(" Mỗi người chỉ được feed back 1 lần với 1 đơn booking", "Mỗi người chỉ được feed back 1 lần với 1 đơn booking");
                }
            }
            feedBack.setRoleType(true);

        } else {
            for (FeedBack feedBack1 : feeds) {
                if (!feedBack1.isRoleType()) {
                    throw new InvalidException(" Mỗi người chỉ được feed back 1 lần với 1 đơn booking", "Mỗi người chỉ được feed back 1 lần với 1 đơn booking");
                }
            }
            feedBack.setRoleType(false);

        }
        feedBack.setContent(request.getComment());
        feedBack.setBooking(booking);

        if (request.getPoint() != null) {
            Account account1 = new Account();

            if (account.getRole().getName().equals(ERole.ROLE_CUSTOMER)) {
                account1 = booking.getVehicle().getAccount();

            } else {
                account1 = booking.getAccount();

            }
            feedBack.setPoint(request.getPoint());

            if (account1.getPoint() != null) {
                if (request.getPoint().equals(0L)) {
                    account1.setPoint(account1.getPoint() - 5L);
                }

                if (request.getPoint().equals(1L)) {
                    account1.setPoint(account1.getPoint() - 3L);
                }

                if (request.getPoint().equals(2L)) {
                    account1.setPoint(account1.getPoint() - 1L);
                }

                if (request.getPoint().equals(3L)) {
                    account1.setPoint(account1.getPoint() + 1L);
                }

                if (request.getPoint().equals(4L)) {
                    account1.setPoint(account1.getPoint() + 3L);
                }

                if (request.getPoint().equals(5L)) {
                    account1.setPoint(account1.getPoint() + 5L);
                }
                accountRepository.save(account1);
            }

        }

        feedBackRepository.save(feedBack);

    }

    @Override
    public ListFeedBack feedBack(Long id) {
        Account account = accountRepository.getById(id);

        if (account == null) throw new InvalidException("Account không tồn tại", "Account không tồn tại");

        List<FeedBackDto> dtos = new ArrayList<>();

        if (account.getRole().getName().equals(ERole.ROLE_CUSTOMER)) {
            for (Booking booking : account.getBookings()) {
                for (FeedBack feedBack : booking.getFeedBackList()) {
                    if (!feedBack.isRoleType()) {
                        dtos.add(new FeedBackDto(booking.getVehicle().getAccount().getName(), feedBack.getPoint(), feedBack.getContent()));
                    }
                }
            }
        } else {
            for (Vehicle vehicle : account.getVehicles()) {
                for (Booking booking : vehicle.getBookingList()) {
                    for (FeedBack feedBack : booking.getFeedBackList()) {
                        if (feedBack.isRoleType()) {
                            dtos.add(new FeedBackDto(booking.getAccount().getName(), feedBack.getPoint(), feedBack.getContent()));
                        }
                    }
                }
            }
        }

        return new ListFeedBack(dtos);
    }

    @Override
    @Transactional
    public void editComment(CommentRequest request) {

        Booking booking = bookingRepository.getById(request.getBookingId());

        if (booking == null) {
            throw new InvalidException(" ĐƠn không tồn tại", " ĐƠn không tồn tại");
        }

        Account account = accountRepository.getById(request.getAccountId());

        if (account == null) {
            throw new InvalidException(" Account không tồn tại", " Account không tồn tại");
        }

        if ((!booking.getAccount().getId().equals(account.getId())) && !booking.getVehicle().getAccount().getId().equals(account.getId())) {
            throw new InvalidException(" Account không  đặt đơn này", " Account không  đặt đơn này");
        }

        List<FeedBack> feeds = feedBackRepository.findByBookingId(request.getBookingId());

        Long oldPoint = 100L;
        if (account.getRole().getName().equals(ERole.ROLE_CUSTOMER)) {
            for (FeedBack feedBack1 : feeds) {
                if (feedBack1.isRoleType()) {
                    oldPoint = feedBack1.getPoint();
                    feedBack1.setPoint(request.getPoint());
                    feedBack1.setContent(request.getComment());
                    feedBackRepository.save(feedBack1);
                    break;
                }
            }

        } else {
            for (FeedBack feedBack1 : feeds) {
                if (!feedBack1.isRoleType()) {
                    oldPoint = feedBack1.getPoint();
                    feedBack1.setPoint(request.getPoint());
                    feedBack1.setContent(request.getComment());
                    feedBackRepository.save(feedBack1);
                    break;

                }
            }

        }

        if (request.getPoint() != null) {
            Account account1 = new Account();

            if (account.getRole().getName().equals(ERole.ROLE_CUSTOMER)) {
                account1 = booking.getVehicle().getAccount();

            } else {
                account1 = booking.getAccount();

            }

            if (account1.getPoint() != null) {
                if (request.getPoint().equals(0L)) {
                    account1.setPoint(account1.getPoint() - 5L - rollBackPoint(oldPoint));
                }

                if (request.getPoint().equals(1L)) {
                    account1.setPoint(account1.getPoint() - 3L - rollBackPoint(oldPoint));
                }

                if (request.getPoint().equals(2L)) {
                    account1.setPoint(account1.getPoint() - 1L - rollBackPoint(oldPoint));
                }

                if (request.getPoint().equals(3L)) {
                    account1.setPoint(account1.getPoint() + 1L - rollBackPoint(oldPoint));
                }

                if (request.getPoint().equals(4L)) {
                    account1.setPoint(account1.getPoint() + 3L - rollBackPoint(oldPoint));
                }

                if (request.getPoint().equals(5L)) {
                    account1.setPoint(account1.getPoint() + 5L - rollBackPoint(oldPoint));
                }
                accountRepository.save(account1);
            }

        }

    }

    public Long rollBackPoint(Long oldPoint) {
        if (oldPoint.equals(0L)) {
            return -5L;
        }

        if (oldPoint.equals(1L)) {
            return -3L;
        }

        if (oldPoint.equals(2L)) {
            return -1L;
        }

        if (oldPoint.equals(3L)) {
            return 1L;
        }

        if (oldPoint.equals(4L)) {
            return 3L;
        }

        if (oldPoint.equals(5L)) {
            return 5L;
        }
        return 0L;
    }

}
