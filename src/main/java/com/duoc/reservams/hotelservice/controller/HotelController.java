package com.duoc.reservams.hotelservice.controller;

import com.duoc.reservams.hotelservice.dto.*;
import com.duoc.reservams.hotelservice.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controlador REST para los hoteles
@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // lista todos los hoteles
    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> findAll() {
        return ResponseEntity.ok(hotelService.findAll());
    }

    // lista solo hoteles activos
    @GetMapping("/active")
    public ResponseEntity<List<HotelResponseDTO>> findActiveHotels() {
        return ResponseEntity.ok(hotelService.findActiveHotels());
    }

    // busca hotel por ID
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.findById(id));
    }

    // busca hoteles por ciudad
    @GetMapping("/city/{city}")
    public ResponseEntity<List<HotelResponseDTO>> findByCity(@PathVariable String city) {
        return ResponseEntity.ok(hotelService.findByCity(city));
    }

    // crea un hotel nuevo
    @PostMapping
    public ResponseEntity<HotelResponseDTO> create(@Valid @RequestBody HotelRequestDTO request) {
        return ResponseEntity.ok(hotelService.create(request));
    }

    // actualiza un hotel existente
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequestDTO request) {

        return ResponseEntity.ok(hotelService.update(id, request));
    }

    // desactiva un hotel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // asigna un operador a un hotel
    @PostMapping("/{hotelId}/operators")
    public ResponseEntity<HotelOperatorResponseDTO> assignOperator(
            @PathVariable Long hotelId,
            @Valid @RequestBody AssignOperatorRequestDTO request) {

        return ResponseEntity.ok(hotelService.assignOperator(hotelId, request));
    }

    // lista operadores asignados a un hotel
    @GetMapping("/{hotelId}/operators")
    public ResponseEntity<List<HotelOperatorResponseDTO>> findOperatorsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.findOperatorsByHotel(hotelId));
    }

    // lista hoteles asignados a un operador
    @GetMapping("/operator/{operatorUserId}")
    public ResponseEntity<List<HotelOperatorResponseDTO>> findHotelsByOperator(@PathVariable Long operatorUserId) {
        return ResponseEntity.ok(hotelService.findHotelsByOperator(operatorUserId));
    }
}