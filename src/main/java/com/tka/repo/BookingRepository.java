package com.tka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tka.entity.Booking;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Ye magic method hai: automatic Descending order mein data layega
    List<Booking> findAllByOrderByIdDesc();
}