package com.duoc.reservams.hotelservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// DTO para responder datos de hoteles
@Data
@AllArgsConstructor
public class HotelResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String status;
    private LocalDateTime createdAt;
}