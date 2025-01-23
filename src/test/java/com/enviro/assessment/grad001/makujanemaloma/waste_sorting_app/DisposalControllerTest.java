package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.DisposalController;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.DisposalDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.DisposalRepository;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.exceptions.DisposalNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisposalControllerTest {

    private DisposalController disposalController;
    private DisposalRepository disposalRepository;

    @BeforeEach
    void setUp() {
        disposalRepository = mock(DisposalRepository.class);
        disposalController = new DisposalController(disposalRepository, null); // JdbcClient is not used in these tests
    }

    @Test
    void testGetAllDisposals() {
        // Arrange
        List<DisposalDTO> mockDisposals = List.of(
                new DisposalDTO(1, 101, "Recycling Center", "Recycle at center", "123 Green Street", null),
                new DisposalDTO(2, 102, "Landfill", "Dispose of in landfill", "456 Landfill Ave", null)
        );
        when(disposalRepository.getAllDisposals()).thenReturn(mockDisposals);

        // Act
        List<DisposalDTO> disposals = disposalController.getAllDisposals();

        // Assert
        assertNotNull(disposals, "Disposals list should not be null");
        assertEquals(2, disposals.size(), "Disposals list size should match the mock data");
        assertEquals("Recycling Center", disposals.get(0).method(), "First disposal method should match mock data");
    }

    @Test
    void testGetDisposalByIdFound() {
        // Arrange
        DisposalDTO mockDisposal = new DisposalDTO(1, 101, "Recycling Center", "Recycle at center", "123 Green Street", null);
        when(disposalRepository.getDisposal(1)).thenReturn(Optional.of(mockDisposal));

        // Act
        ResponseEntity<?> response = disposalController.getDisposal(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertTrue(response.getBody() instanceof DisposalDTO, "Response body should be a DisposalDTO");
        assertEquals(mockDisposal, response.getBody(), "Response body should match the mock disposal");
    }

    @Test
    void testGetDisposalByIdNotFound() {
        // Arrange
        when(disposalRepository.getDisposal(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DisposalNotFoundException.class, () -> disposalController.getDisposal(99), "Should throw DisposalNotFoundException for non-existent ID");
    }

    @Test
    void testCreateNewDisposalSuccess() {
        // Arrange
        DisposalDTO newDisposal = new DisposalDTO(3, 103, "Composting", "Compost organic waste", "789 Green Lane", null);
        when(disposalRepository.insertNewDisposal(newDisposal)).thenReturn(true);

        // Act
        ResponseEntity<?> response = disposalController.createNewDisposal(newDisposal);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response status should be CREATED");
        assertNull(response.getBody(), "Response body should be null for successful creation");
    }

    @Test
    void testCreateNewDisposalFailure() {
        // Arrange
        DisposalDTO newDisposal = new DisposalDTO(3, 103, "Composting", "Compost organic waste", "789 Green Lane", null);
        when(disposalRepository.insertNewDisposal(newDisposal)).thenReturn(false);

        // Act
        ResponseEntity<?> response = disposalController.createNewDisposal(newDisposal);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Response status should be INTERNAL_SERVER_ERROR");
        assertNotNull(response.getBody(), "Response body should contain an error message");
    }

    @Test
    void testUpdateDisposalSuccess() {
        // Arrange
        DisposalDTO updatedDisposal = new DisposalDTO(1, 101, "Updated Method", "Updated Instructions", "Updated Location", null);
        when(disposalRepository.getDisposal(1)).thenReturn(Optional.of(updatedDisposal));
        when(disposalRepository.updateDisposal(updatedDisposal, 1)).thenReturn(true);

        // Act
        ResponseEntity<?> response = disposalController.updateDisposal(1, updatedDisposal);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK for successful update");
        assertNull(response.getBody(), "Response body should be null for successful update");
    }

    @Test
    void testUpdateDisposalNotFound() {
        // Arrange
        DisposalDTO updatedDisposal = new DisposalDTO(1, 101, "Updated Method", "Updated Instructions", "Updated Location", null);
        when(disposalRepository.getDisposal(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DisposalNotFoundException.class, () -> disposalController.updateDisposal(1, updatedDisposal), "Should throw DisposalNotFoundException for non-existent ID");
    }

    @Test
    void testDeleteDisposalByIdSuccess() {
        // Arrange
        DisposalDTO mockDisposal = new DisposalDTO(1, 101, "Recycling Center", "Recycle at center", "123 Green Street", null);
        when(disposalRepository.getDisposal(1)).thenReturn(Optional.of(mockDisposal));

        // Act
        disposalController.deleteDisposalById(1);

        // Assert
        verify(disposalRepository, times(1)).deleteDisposal(1);
    }

    @Test
    void testDeleteDisposalByIdNotFound() {
        // Arrange
        when(disposalRepository.getDisposal(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DisposalNotFoundException.class, () -> disposalController.deleteDisposalById(99), "Should throw DisposalNotFoundException for non-existent ID");
    }
}
