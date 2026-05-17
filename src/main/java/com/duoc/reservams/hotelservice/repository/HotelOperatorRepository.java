package com.duoc.reservams.hotelservice.repository;

import com.duoc.reservams.hotelservice.model.HotelOperator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// repository para manejar operadores asignados a hoteles
public interface HotelOperatorRepository extends JpaRepository<HotelOperator, Long> {

    // busca asignaciones por hotel
    List<HotelOperator> findByHotelId(Long hotelId);

    // busca hoteles asignados a un operador
    List<HotelOperator> findByOperatorUserId(Long operatorUserId);

    // evita asignar dos veces el mismo operador al mismo hotel
    boolean existsByHotelIdAndOperatorUserId(Long hotelId, Long operatorUserId);
}