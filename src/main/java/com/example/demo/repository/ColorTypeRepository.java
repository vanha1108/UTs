package com.example.demo.repository;

import com.example.demo.domain.model.ColorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorTypeRepository extends JpaRepository<ColorType, Long> {

}
