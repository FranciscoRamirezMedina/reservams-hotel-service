package com.duoc.reservams.hotelservice.service;

import com.duoc.reservams.hotelservice.dto.*;
import com.duoc.reservams.hotelservice.model.Hotel;
import com.duoc.reservams.hotelservice.model.HotelOperator;
import com.duoc.reservams.hotelservice.repository.HotelOperatorRepository;
import com.duoc.reservams.hotelservice.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// aqui va la logica de negocio de hoteles
@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelOperatorRepository hotelOperatorRepository;

    public HotelService(HotelRepository hotelRepository,
                        HotelOperatorRepository hotelOperatorRepository) {
        this.hotelRepository = hotelRepository;
        this.hotelOperatorRepository = hotelOperatorRepository;
    }

    public List<HotelResponseDTO> findAll() {
        return hotelRepository.findAll()
                .stream()
                .map(this::toHotelResponseDTO)
                .toList();
    }

    public List<HotelResponseDTO> findActiveHotels() {
        return hotelRepository.findByStatus("ACTIVE")
                .stream()
                .map(this::toHotelResponseDTO)
                .toList();
    }

    public HotelResponseDTO findById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        return toHotelResponseDTO(hotel);
    }

    public List<HotelResponseDTO> findByCity(String city) {
        return hotelRepository.findByCity(city)
                .stream()
                .map(this::toHotelResponseDTO)
                .toList();
    }

    public HotelResponseDTO create(HotelRequestDTO request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setStatus(request.getStatus());
        hotel.setCreatedAt(LocalDateTime.now());

        Hotel savedHotel = hotelRepository.save(hotel);

        return toHotelResponseDTO(savedHotel);
    }

    public HotelResponseDTO update(Long id, HotelRequestDTO request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setStatus(request.getStatus());

        Hotel updatedHotel = hotelRepository.save(hotel);

        return toHotelResponseDTO(updatedHotel);
    }

    public void delete(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        // no borramos el hotel fisicamente, solo lo dejamos inactivo
        hotel.setStatus("INACTIVE");
        hotelRepository.save(hotel);
    }

    public HotelOperatorResponseDTO assignOperator(Long hotelId, AssignOperatorRequestDTO request) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        boolean alreadyAssigned = hotelOperatorRepository
                .existsByHotelIdAndOperatorUserId(hotelId, request.getOperatorUserId());

        if (alreadyAssigned) {
            throw new RuntimeException("El operador ya está asignado a este hotel");
        }

        HotelOperator hotelOperator = new HotelOperator();
        hotelOperator.setHotel(hotel);
        hotelOperator.setOperatorUserId(request.getOperatorUserId());
        hotelOperator.setAssignedAt(LocalDateTime.now());

        HotelOperator savedAssignment = hotelOperatorRepository.save(hotelOperator);

        return toHotelOperatorResponseDTO(savedAssignment);
    }

    public List<HotelOperatorResponseDTO> findOperatorsByHotel(Long hotelId) {
        return hotelOperatorRepository.findByHotelId(hotelId)
                .stream()
                .map(this::toHotelOperatorResponseDTO)
                .toList();
    }

    public List<HotelOperatorResponseDTO> findHotelsByOperator(Long operatorUserId) {
        return hotelOperatorRepository.findByOperatorUserId(operatorUserId)
                .stream()
                .map(this::toHotelOperatorResponseDTO)
                .toList();
    }

    // convierte la entidad Hotel a DTO de respuesta
    private HotelResponseDTO toHotelResponseDTO(Hotel hotel) {
        return new HotelResponseDTO(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getAddress(),
                hotel.getCity(),
                hotel.getStatus(),
                hotel.getCreatedAt()
        );
    }

    // convierte la entidad HotelOperator a DTO de respuesta
    private HotelOperatorResponseDTO toHotelOperatorResponseDTO(HotelOperator hotelOperator) {
        return new HotelOperatorResponseDTO(
                hotelOperator.getId(),
                hotelOperator.getHotel().getId(),
                hotelOperator.getHotel().getName(),
                hotelOperator.getOperatorUserId(),
                hotelOperator.getAssignedAt()
        );
    }
}