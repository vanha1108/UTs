package com.example.demo.repository;

import com.example.demo.domain.model.FeedBack;
import com.example.demo.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

    List<FeedBack> findByBookingId(Long id);

}
