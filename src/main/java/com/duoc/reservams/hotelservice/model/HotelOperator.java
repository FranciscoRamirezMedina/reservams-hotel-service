package com.duoc.reservams.hotelservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// representa la asignacion de un operador a un hotel
@Entity
@Table(name = "hotel_operators")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelOperator {

    // ID principal de la asignacion
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relaciion con la tabla hotels dentro de este mismo microservicio
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    // ID logico del operador que viene desde user-service
    @Column(name = "operator_user_id", nullable = false)
    private Long operatorUserId;

    // fecha en que el operador fue asignado al hotel
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();
}