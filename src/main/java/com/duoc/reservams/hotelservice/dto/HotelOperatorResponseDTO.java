package com.duoc.reservams.hotelservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// DTO para responder asignaciones de operadores
@Data
@AllArgsConstructor
public class HotelOperatorResponseDTO {

    private Long id;
    private Long hotelId;
    private String hotelName;
    private Long operatorUserId;
    private LocalDateTime assignedAt;
}