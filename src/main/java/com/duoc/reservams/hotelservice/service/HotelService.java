package com.duoc.reservams.hotelservice.service;

import com.duoc.reservams.hotelservice.dto.*;
import com.duoc.reservams.hotelservice.model.Hotel;
import com.duoc.reservams.hotelservice.model.HotelOperator;
import com.duoc.reservams.hotelservice.repository.HotelOperatorRepository;
import com.duoc.reservams.hotelservice.repository.HotelRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

// aqui va la logica de negocio de hoteles
@Service
public class HotelService {

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;
    private final HotelOperatorRepository hotelOperatorRepository;

    public HotelService(HotelRepository hotelRepository,
                        HotelOperatorRepository hotelOperatorRepository) {
        this.hotelRepository = hotelRepository;
        this.hotelOperatorRepository = hotelOperatorRepository;
    }

    public List<HotelResponseDTO> findAll() {
        logger.info("Listando todos los hoteles");

        List<HotelResponseDTO> hotels = hotelRepository.findAll()
                .stream()
                .map(this::toHotelResponseDTO)
                .toList();

        logger.info("Se encontraron {} hoteles", hotels.size());

        return hotels;
    }

    public List<HotelResponseDTO> findActiveHotels() {
        logger.info("Listando hoteles activos");

        List<HotelResponseDTO> hotels = hotelRepository.findByStatus("ACTIVE")
                .stream()
                .map(this::toHotelResponseDTO)
                .toList();

        logger.info("Se encontraron {} hoteles activos", hotels.size());

        return hotels;
    }

    public HotelResponseDTO findById(Long id) {
        logger.info("Buscando hotel por ID {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se encontro hotel con ID {}", id);
                    return new RuntimeException("Hotel no encontrado");
                });

        logger.info("Hotel encontrado con ID {} y nombre {}", hotel.getId(), hotel.getName());

        return toHotelResponseDTO(hotel);
    }

    public List<HotelResponseDTO> findByCity(String city) {
        logger.info("Buscando hoteles por ciudad {}", city);

        List<HotelResponseDTO> hotels = hotelRepository.findByCity(city)
                .stream()
                .map(this::toHotelResponseDTO)
                .toList();

        logger.info("Se encontraron {} hoteles en la ciudad {}", hotels.size(), city);

        return hotels;
    }

    public HotelResponseDTO create(HotelRequestDTO request) {
        logger.info("Iniciando creacion de hotel con nombre {} en ciudad {}", request.getName(), request.getCity());

        Hotel hotel = new Hotel();
        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setStatus(request.getStatus());
        hotel.setCreatedAt(LocalDateTime.now());

        logger.info("Guardando hotel {} con estado {}", hotel.getName(), hotel.getStatus());

        Hotel savedHotel = hotelRepository.save(hotel);

        logger.info("Hotel creado correctamente con ID {} y nombre {}", savedHotel.getId(), savedHotel.getName());

        return toHotelResponseDTO(savedHotel);
    }

    public HotelResponseDTO update(Long id, HotelRequestDTO request) {
        logger.info("Iniciando actualizacion de hotel con ID {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se pudo actualizar. Hotel no encontrado con ID {}", id);
                    return new RuntimeException("Hotel no encontrado");
                });

        hotel.setName(request.getName());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setStatus(request.getStatus());

        logger.info("Guardando cambios del hotel con ID {}", id);

        Hotel updatedHotel = hotelRepository.save(hotel);

        logger.info("Hotel actualizado correctamente con ID {} y estado {}", updatedHotel.getId(), updatedHotel.getStatus());

        return toHotelResponseDTO(updatedHotel);
    }

    public void delete(Long id) {
        logger.info("Iniciando desactivacion de hotel con ID {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No se pudo desactivar. Hotel no encontrado con ID {}", id);
                    return new RuntimeException("Hotel no encontrado");
                });

        // no borramos el hotel fisicamente, solo lo dejamos inactivo
        hotel.setStatus("INACTIVE");
        hotelRepository.save(hotel);

        logger.info("Hotel con ID {} fue desactivado correctamente", id);
    }

    public HotelOperatorResponseDTO assignOperator(Long hotelId, AssignOperatorRequestDTO request) {
        logger.info("Iniciando asignacion de operador ID {} al hotel ID {}", request.getOperatorUserId(), hotelId);

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> {
                    logger.warn("No se pudo asignar operador. Hotel no encontrado con ID {}", hotelId);
                    return new RuntimeException("Hotel no encontrado");
                });

        boolean alreadyAssigned = hotelOperatorRepository
                .existsByHotelIdAndOperatorUserId(hotelId, request.getOperatorUserId());

        if (alreadyAssigned) {
            logger.warn("No se pudo asignar operador. El operador ID {} ya esta asignado al hotel ID {}",
                    request.getOperatorUserId(), hotelId);
            throw new RuntimeException("El operador ya está asignado a este hotel");
        }

        HotelOperator hotelOperator = new HotelOperator();
        hotelOperator.setHotel(hotel);
        hotelOperator.setOperatorUserId(request.getOperatorUserId());
        hotelOperator.setAssignedAt(LocalDateTime.now());

        logger.info("Guardando asignacion de operador ID {} al hotel ID {}", request.getOperatorUserId(), hotelId);

        HotelOperator savedAssignment = hotelOperatorRepository.save(hotelOperator);

        logger.info("Operador ID {} asignado correctamente al hotel ID {} con asignacion ID {}",
                savedAssignment.getOperatorUserId(),
                savedAssignment.getHotel().getId(),
                savedAssignment.getId());

        return toHotelOperatorResponseDTO(savedAssignment);
    }

    public List<HotelOperatorResponseDTO> findOperatorsByHotel(Long hotelId) {
        logger.info("Listando operadores asignados al hotel ID {}", hotelId);

        List<HotelOperatorResponseDTO> operators = hotelOperatorRepository.findByHotelId(hotelId)
                .stream()
                .map(this::toHotelOperatorResponseDTO)
                .toList();

        logger.info("Se encontraron {} operadores asignados al hotel ID {}", operators.size(), hotelId);

        return operators;
    }

    public List<HotelOperatorResponseDTO> findHotelsByOperator(Long operatorUserId) {
        logger.info("Listando hoteles asignados al operador ID {}", operatorUserId);

        List<HotelOperatorResponseDTO> hotels = hotelOperatorRepository.findByOperatorUserId(operatorUserId)
                .stream()
                .map(this::toHotelOperatorResponseDTO)
                .toList();

        logger.info("Se encontraron {} hoteles asignados al operador ID {}", hotels.size(), operatorUserId);

        return hotels;
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