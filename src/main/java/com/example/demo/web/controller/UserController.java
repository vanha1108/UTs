package com.example.demo.web.controller;

import com.example.demo.request.booking.BookingsRequest;
import com.example.demo.request.booking.PutBookingRequest;
import com.example.demo.request.comment.CommentRequest;
import com.example.demo.service.BookingService;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/user")
public class UserController extends BaseController {


    @Autowired
    private CommentService commentService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("comment")
    public ResponseEntity<?> comment(@Valid @RequestBody CommentRequest request) {
        commentService.comment(request);
        return successResponse();
    }

    @GetMapping("feedback/{id}")
    @PreAuthorize("hasRole('ROLE_RENTAL') OR hasRole('ROLE_SHOP') OR hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> getFeedBach(@PathVariable Long id) {
        return successResponse(commentService.feedBack(id));
    }


    @PostMapping("bookings")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> getBooking(@Valid @RequestBody BookingsRequest request) {

        return successResponse(bookingService.userBookings(request));
    }

    @PutMapping("booking")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<?> putBooking(@Valid @RequestBody PutBookingRequest request) {
        bookingService.putBooking(request);
        return successResponse();
    }

    @PutMapping("comment")
    public ResponseEntity<?> editComment(@Valid @RequestBody CommentRequest request) {
        commentService.editComment(request);
        return successResponse();
    }

}
