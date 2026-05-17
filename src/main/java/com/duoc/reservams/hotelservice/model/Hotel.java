package com.duoc.reservams.hotelservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// esta clase representa la tabla hotels
@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    // ID principal del hotel
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nombre del hotel
    @Column(nullable = false, length = 100)
    private String name;

    // descripcion corta del hotel
    @Column(length = 255)
    private String description;

    // direccion fisica del hotel
    @Column(nullable = false, length = 150)
    private String address;

    // ciudad donde se ubica el hotel
    @Column(nullable = false, length = 80)
    private String city;

    // estado del hotel, ACTIVE o INACTIVE
    @Column(nullable = false, length = 30)
    private String status;

    // fecha en que se registro el hotel
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}