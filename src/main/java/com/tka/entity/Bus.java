package com.tka.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "buses")
@Data // Ye automatically getters/setters bana dega
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busName;
    private String source;
    private String destination;
    private String departureTime;
    private double price;
    @Column(name="bus_type")
    private String busType;
    private int availableSeats; 
 // Bus.java mein check karein
    private Integer distanceKm; // Agar ye Integer hai toh null ho sakta hai
}