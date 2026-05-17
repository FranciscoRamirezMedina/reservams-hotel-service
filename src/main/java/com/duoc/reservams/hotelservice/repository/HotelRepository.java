package com.duoc.reservams.hotelservice.repository;

import com.duoc.reservams.hotelservice.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// repository para acceder a la tabla hotels
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    // lista hoteles por estado, ej ACTIVE
    List<Hotel> findByStatus(String status);

    // lista hoteles por ciudad
    List<Hotel> findByCity(String city);
}