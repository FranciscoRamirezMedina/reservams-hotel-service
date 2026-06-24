package com.duoc.reservams.hotelservice.controller;

import com.duoc.reservams.hotelservice.dto.AssignOperatorRequestDTO;
import com.duoc.reservams.hotelservice.dto.HotelOperatorResponseDTO;
import com.duoc.reservams.hotelservice.dto.HotelRequestDTO;
import com.duoc.reservams.hotelservice.dto.HotelResponseDTO;
import com.duoc.reservams.hotelservice.service.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// pruebas unitarias para HotelController
@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private HotelController hotelController;

    @Test
    void findAll_shouldReturnHotels() {
        // Given
        when(hotelService.findAll()).thenReturn(List.of(
                buildHotelResponse(1L, "Hotel Centro", "ACTIVE"),
                buildHotelResponse(2L, "Hotel Norte", "ACTIVE")
        ));

        // When
        ResponseEntity<List<HotelResponseDTO>> response = hotelController.findAll();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(hotelService, times(1)).findAll();
    }

    @Test
    void findActiveHotels_shouldReturnActiveHotels() {
        // Given
        when(hotelService.findActiveHotels()).thenReturn(List.of(
                buildHotelResponse(1L, "Hotel Centro", "ACTIVE"),
                buildHotelResponse(2L, "Hotel Norte", "ACTIVE")
        ));

        // When
        ResponseEntity<List<HotelResponseDTO>> response = hotelController.findActiveHotels();

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("ACTIVE", response.getBody().get(0).getStatus());

        verify(hotelService, times(1)).findActiveHotels();
    }

    @Test
    void findById_shouldReturnHotel() {
        // Given
        when(hotelService.findById(1L)).thenReturn(
                buildHotelResponse(1L, "Hotel Centro", "ACTIVE")
        );

        // When
        ResponseEntity<HotelResponseDTO> response = hotelController.findById(1L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Hotel Centro", response.getBody().getName());

        verify(hotelService, times(1)).findById(1L);
    }

    @Test
    void findByCity_shouldReturnHotels() {
        // Given
        when(hotelService.findByCity("Santiago")).thenReturn(List.of(
                buildHotelResponse(1L, "Hotel Centro", "ACTIVE"),
                buildHotelResponse(2L, "Hotel Norte", "ACTIVE")
        ));

        // When
        ResponseEntity<List<HotelResponseDTO>> response = hotelController.findByCity("Santiago");

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Santiago", response.getBody().get(0).getCity());

        verify(hotelService, times(1)).findByCity("Santiago");
    }

    @Test
    void create_shouldReturnCreatedHotel() {
        // Given
        HotelRequestDTO request = buildHotelRequest("ACTIVE");

        when(hotelService.create(request)).thenReturn(
                buildHotelResponse(1L, "Hotel Centro", "ACTIVE")
        );

        // When
        ResponseEntity<HotelResponseDTO> response = hotelController.create(request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Hotel Centro", response.getBody().getName());
        assertEquals("ACTIVE", response.getBody().getStatus());

        verify(hotelService, times(1)).create(request);
    }

    @Test
    void update_shouldReturnUpdatedHotel() {
        // Given
        HotelRequestDTO request = buildHotelRequest("INACTIVE");

        when(hotelService.update(1L, request)).thenReturn(
                buildHotelResponse(1L, "Hotel Centro", "INACTIVE")
        );

        // When
        ResponseEntity<HotelResponseDTO> response = hotelController.update(1L, request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("INACTIVE", response.getBody().getStatus());

        verify(hotelService, times(1)).update(1L, request);
    }

    @Test
    void delete_shouldReturnNoContent() {
        // Given
        doNothing().when(hotelService).delete(1L);

        // When
        ResponseEntity<Void> response = hotelController.delete(1L);

        // Then
        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(hotelService, times(1)).delete(1L);
    }

    @Test
    void assignOperator_shouldReturnAssignedOperator() {
        // Given
        AssignOperatorRequestDTO request = buildAssignOperatorRequest();

        when(hotelService.assignOperator(1L, request)).thenReturn(
                buildHotelOperatorResponse(1L, 1L, "Hotel Centro", 10L)
        );

        // When
        ResponseEntity<HotelOperatorResponseDTO> response = hotelController.assignOperator(1L, request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(1L, response.getBody().getHotelId());
        assertEquals(10L, response.getBody().getOperatorUserId());

        verify(hotelService, times(1)).assignOperator(1L, request);
    }

    @Test
    void findOperatorsByHotel_shouldReturnOperators() {
        // Given
        when(hotelService.findOperatorsByHotel(1L)).thenReturn(List.of(
                buildHotelOperatorResponse(1L, 1L, "Hotel Centro", 10L),
                buildHotelOperatorResponse(2L, 1L, "Hotel Centro", 20L)
        ));

        // When
        ResponseEntity<List<HotelOperatorResponseDTO>> response = hotelController.findOperatorsByHotel(1L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getHotelId());

        verify(hotelService, times(1)).findOperatorsByHotel(1L);
    }

    @Test
    void findHotelsByOperator_shouldReturnHotelsAssignedToOperator() {
        // Given
        when(hotelService.findHotelsByOperator(10L)).thenReturn(List.of(
                buildHotelOperatorResponse(1L, 1L, "Hotel Centro", 10L),
                buildHotelOperatorResponse(2L, 2L, "Hotel Norte", 10L)
        ));

        // When
        ResponseEntity<List<HotelOperatorResponseDTO>> response = hotelController.findHotelsByOperator(10L);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(10L, response.getBody().get(0).getOperatorUserId());

        verify(hotelService, times(1)).findHotelsByOperator(10L);
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

    private HotelResponseDTO buildHotelResponse(Long id, String name, String status) {
        return new HotelResponseDTO(
                id,
                name,
                "Hotel ubicado en el centro de la ciudad",
                "Av. Principal 123",
                "Santiago",
                status,
                LocalDateTime.now()
        );
    }

    private HotelOperatorResponseDTO buildHotelOperatorResponse(
            Long id,
            Long hotelId,
            String hotelName,
            Long operatorUserId) {

        return new HotelOperatorResponseDTO(
                id,
                hotelId,
                hotelName,
                operatorUserId,
                LocalDateTime.now()
        );
    }
}