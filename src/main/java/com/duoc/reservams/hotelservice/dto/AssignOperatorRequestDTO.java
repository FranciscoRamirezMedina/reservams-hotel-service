package com.duoc.reservams.hotelservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

// DTO para asignar un operador a un hotel
@Data
public class AssignOperatorRequestDTO {

    @NotNull(message = "El ID del operador es obligatorio")
    private Long operatorUserId;
}