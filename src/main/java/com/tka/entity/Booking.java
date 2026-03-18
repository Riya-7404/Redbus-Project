package com.tka.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String source;
    private String destination;
    private String seatNumbers;
    private double amount;
    private String bookingDate; // Format: "05 March 2026"
}