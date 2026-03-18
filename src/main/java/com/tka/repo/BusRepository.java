package com.tka.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tka.entity.Bus;
import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {
    
    // Custom query: Spring Data JPA ise khud samajh lega
    List<Bus> findBySourceAndDestination(String source, String destination);
}