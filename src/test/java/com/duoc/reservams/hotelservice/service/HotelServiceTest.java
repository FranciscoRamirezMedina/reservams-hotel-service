package com.duoc.reservams.hotelservice.service;

import com.duoc.reservams.hotelservice.dto.AssignOperatorRequestDTO;
import com.duoc.reservams.hotelservice.dto.HotelOperatorResponseDTO;
import com.duoc.reservams.hotelservice.dto.HotelRequestDTO;
import com.duoc.reservams.hotelservice.dto.HotelResponseDTO;
import com.duoc.reservams.hotelservice.model.Hotel;
import com.duoc.reservams.hotelservice.model.HotelOperator;
import com.duoc.reservams.hotelservice.repository.HotelOperatorRepository;
import com.duoc.reservams.hotelservice.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// pruebas unitarias para HotelService
@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelOperatorRepository hotelOperatorRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void findAll_shouldReturnHotels() {
        // Given
        when(hotelRepository.findAll()).thenReturn(List.of(
                buildHotel(1L, "Hotel Centro", "ACTIVE"),
                buildHotel(2L, "Hotel Norte", "ACTIVE")
        ));

        // When
        List<HotelResponseDTO> response = hotelService.findAll();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Hotel Centro", response.get(0).getName());

        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void findActiveHotels_shouldReturnActiveHotels() {
        // Given
        when(hotelRepository.findByStatus("ACTIVE")).thenReturn(List.of(
                buildHotel(1L, "Hotel Centro", "ACTIVE"),
                buildHotel(2L, "Hotel Norte", "ACTIVE")
        ));

        // When
        List<HotelResponseDTO> response = hotelService.findActiveHotels();

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("ACTIVE", response.get(0).getStatus());

        verify(hotelRepository, times(1)).findByStatus("ACTIVE");
    }

    @Test
    void findById_shouldReturnHotel_whenExists() {
        // Given
        Hotel hotel = buildHotel(1L, "Hotel Centro", "ACTIVE");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        // When
        HotelResponseDTO response = hotelService.findById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Hotel Centro", response.getName());
        assertEquals("Santiago", response.getCity());
        assertEquals("ACTIVE", response.getStatus());

        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenHotelNotFound() {
        // Given
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> hotelService.findById(99L)
        );

        // Then
        assertEquals("Hotel no encontrado", exception.getMessage());

        verify(hotelRepository, times(1)).findById(99L);
    }

    @Test
    void findByCity_shouldReturnHotels() {
        // Given
        when(hotelRepository.findByCity("Santiago")).thenReturn(List.of(
                buildHotel(1L, "Hotel Centro", "ACTIVE"),
                buildHotel(2L, "Hotel Norte", "ACTIVE")
        ));

        // When
        List<HotelResponseDTO> response = hotelService.findByCity("Santiago");

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Santiago", response.get(0).getCity());

        verify(hotelRepository, times(1)).findByCity("Santiago");
    }

    @Test
    void create_shouldCreateHotel() {
        // Given
        HotelRequestDTO request = buildHotelRequest("ACTIVE");

        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> {
            Hotel hotel = invocation.getArgument(0);
            hotel.setId(1L);
            return hotel;
        });

        // When
        HotelResponseDTO response = hotelService.create(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Hotel Centro", response.getName());
        assertEquals("Santiago", response.getCity());
        assertEquals("ACTIVE", response.getStatus());
        assertNotNull(response.getCreatedAt());

        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void update_shouldUpdateHotel_whenExists() {
        // Given
        Hotel hotel = buildHotel(1L, "Hotel Antiguo", "ACTIVE");
        HotelRequestDTO request = buildHotelRequest("INACTIVE");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> {
            Hotel updatedHotel = invocation.getArgument(0);
            return updatedHotel;
        });

        // When
        HotelResponseDTO response = hotelService.update(1L, request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Hotel Centro", response.getName());
        assertEquals("INACTIVE", response.getStatus());

        verify(hotelRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void update_shouldThrowException_whenHotelNotFound() {
        // Given
        HotelRequestDTO request = buildHotelRequest("ACTIVE");

        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> hotelService.update(99L, request)
        );

        // Then
        assertEquals("Hotel no encontrado", exception.getMessage());

        verify(hotelRepository, times(1)).findById(99L);
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void delete_shouldSetHotelInactive_whenExists() {
        // Given
        Hotel hotel = buildHotel(1L, "Hotel Centro", "ACTIVE");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        hotelService.delete(1L);

        // Then
        assertEquals("INACTIVE", hotel.getStatus());

        verify(hotelRepository, times(1)).findById(1L);
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    void delete_shouldThrowException_whenHotelNotFound() {
        // Given
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> hotelService.delete(99L)
        );

        // Then
        assertEquals("Hotel no encontrado", exception.getMessage());

        verify(hotelRepository, times(1)).findById(99L);
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void assignOperator_shouldAssignOperator_whenHotelExistsAndOperatorIsNotAssigned() {
        // Given
        Hotel hotel = buildHotel(1L, "Hotel Centro", "ACTIVE");
        AssignOperatorRequestDTO request = buildAssignOperatorRequest();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelOperatorRepository.existsByHotelIdAndOperatorUserId(1L, 10L)).thenReturn(false);

        when(hotelOperatorRepository.save(any(HotelOperator.class))).thenAnswer(invocation -> {
            HotelOperator hotelOperator = invocation.getArgument(0);
            hotelOperator.setId(1L);
            return hotelOperator;
        });

        // When
        HotelOperatorResponseDTO response = hotelService.assignOperator(1L, request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getHotelId());
        assertEquals("Hotel Centro", response.getHotelName());
        assertEquals(10L, response.getOperatorUserId());
        assertNotNull(response.getAssignedAt());

        verify(hotelRepository, times(1)).findById(1L);
        verify(hotelOperatorRepository, times(1)).existsByHotelIdAndOperatorUserId(1L, 10L);
        verify(hotelOperatorRepository, times(1)).save(any(HotelOperator.class));
    }

    @Test
    void assignOperator_shouldThrowException_whenHotelNotFound() {
        // Given
        AssignOperatorRequestDTO request = buildAssignOperatorRequest();

        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> hotelService.assignOperator(99L, request)
        );

        // Then
        assertEquals("Hotel no encontrado", exception.getMessage());

        verify(hotelRepository, times(1)).findById(99L);
        verify(hotelOperatorRepository, never()).existsByHotelIdAndOperatorUserId(anyLong(), anyLong());
        verify(hotelOperatorRepository, never()).save(any(HotelOperator.class));
    }

    @Test
    void assignOperator_shouldThrowException_whenOperatorAlreadyAssigned() {
        // Given
        Hotel hotel = buildHotel(1L, "Hotel Centro", "ACTIVE");
        AssignOperatorRequestDTO request = buildAssignOperatorRequest();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelOperatorRepository.existsByHotelIdAndOperatorUserId(1L, 10L)).thenReturn(true);

        // When
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> hotelService.assignOperator(1L, request)
        );

        // Then
        assertEquals("El operador ya está asignado a este hotel", exception.getMessage());

        verify(hotelRepository, times(1)).findById(1L);
        verify(hotelOperatorRepository, times(1)).existsByHotelIdAndOperatorUserId(1L, 10L);
        verify(hotelOperatorRepository, never()).save(any(HotelOperator.class));
    }

    @Test
    void findOperatorsByHotel_shouldReturnOperators() {
        // Given
        Hotel hotel = buildHotel(1L, "Hotel Centro", "ACTIVE");

        when(hotelOperatorRepository.findByHotelId(1L)).thenReturn(List.of(
                buildHotelOperator(1L, hotel, 10L),
                buildHotelOperator(2L, hotel, 20L)
        ));

        // When
        List<HotelOperatorResponseDTO> response = hotelService.findOperatorsByHotel(1L);

        // Then
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(1L, response.get(0).getHotelId());
        assertEquals(10L, response.get(0).getOperatorUserId());

        verify(hotelOperatorRepository, times(1)).findByHotelId(1L);
    }

    @Test
    void findHotelsByOperator_shouldReturnHotels() {
        // Given
        Hotel hotel = buildHotel(1L, "Hotel Centro", "ACTIVE");

        when(hotelOperatorRepository.findByOperatorUserId(10L)).thenReturn(List.of(
                buildHotelOperator(1L, hotel, 10L)
        ));

        // When
        List<HotelOperatorResponseDTO> response = hotelService.findHotelsByOperator(10L);

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getHotelId());
        assertEquals(10L, response.get(0).getOperatorUserId());

        verify(hotelOperatorRepository, times(1)).findByOperatorUserId(10L);
    }

    private HotelRequestDTO buildHotelRequest(String status) {
        HotelRequestDTO request = new HotelRequestDTO();
        request.setName("Hotel Centro");
        request.setDescription("Hotel ubicado en el centro de la ciudad");
        request.setAddress("Av. Principal 123");
        request.setCity("Santiago");
        request.setStatus(status);
        return request;
    }

    private AssignOperatorRequestDTO buildAssignOperatorRequest() {
        AssignOperatorRequestDTO request = new AssignOperatorRequestDTO();
        request.setOperatorUserId(10L);
        return request;
    }

    private Hotel buildHotel(Long id, String name, String status) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(name);
        hotel.setDescription("Hotel ubicado en el centro de la ciudad");
        hotel.setAddress("Av. Principal 123");
        hotel.setCity("Santiago");
        hotel.setStatus(status);
        hotel.setCreatedAt(LocalDateTime.now());
        return hotel;
    }

    private HotelOperator buildHotelOperator(Long id, Hotel hotel, Long operatorUserId) {
        HotelOperator hotelOperator = new HotelOperator();
        hotelOperator.setId(id);
        hotelOperator.setHotel(hotel);
        hotelOperator.setOperatorUserId(operatorUserId);
        hotelOperator.setAssignedAt(LocalDateTime.now());
        return hotelOperator;
    }
}