package com.duoc.reservams.hotelservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// DTO para crear o actualizar hoteles
@Data
public class HotelRequestDTO {

    @NotBlank(message = "El nombre del hotel es obligatorio")
    private String name;

    private String description;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotBlank(message = "La ciudad es obligatoria")
    private String city;

    @NotBlank(message = "El estado es obligatorio")
    private String status;
}