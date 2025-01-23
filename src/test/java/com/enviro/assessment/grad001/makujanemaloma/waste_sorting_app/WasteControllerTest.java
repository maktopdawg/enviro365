package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.WasteController;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.WasteDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.WasteRepository;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.WasteWithCategoryDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.exceptions.WasteNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

class WasteControllerTest {

    @Mock
    private WasteRepository wasteRepository;

    @InjectMocks
    private WasteController wasteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWasteById_ReturnsWasteDetails() {
        // Arrange
        WasteWithCategoryDTO mockWaste = new WasteWithCategoryDTO(1, "Paper", "Recyclable paper", "Recyclables", "Waste that can be recycled");
        when(wasteRepository.getWasteWithCategory(1)).thenReturn(Optional.of(mockWaste));

        // Act
        ResponseEntity<?> response = wasteController.getWaste(1);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200");
        assertTrue(response.getBody() instanceof WasteWithCategoryDTO, "Response body should be of type WasteWithCategoryDTO");

        WasteWithCategoryDTO waste = (WasteWithCategoryDTO) response.getBody();
        assertEquals(1, waste.wasteId(), "Waste ID should be 1");
        assertEquals("Paper", waste.wasteName(), "Waste name should be 'Paper'");
        assertEquals("Recyclables", waste.categoryName(), "Category should be 'Recyclables'");
        assertEquals("Waste that can be recycled", waste.categoryDescription(), "Category description should match");

        // Verify the interaction with the mock repository
        verify(wasteRepository, times(1)).getWasteWithCategory(1);
    }

    @Test
    void testGetWasteById_ThrowsWasteNotFoundException() {
        // Arrange
        when(wasteRepository.getWasteWithCategory(1)).thenReturn(Optional.empty());

        // Act & Assert
        WasteNotFoundException exception = assertThrows(WasteNotFoundException.class,
                () -> wasteController.getWaste(1),
                "Expected a WasteNotFoundException when waste does not exist");

        assertEquals("Waste with id 1 not found", exception.getMessage(), "Exception message should match");
        verify(wasteRepository, times(1)).getWasteWithCategory(1);
    }

    @Test
    void testCreateNewWaste_Success() {
        // Arrange
        WasteDTO mockWasteDTO = new WasteDTO(null, "Plastic", "Plastic waste", 2, null);
        when(wasteRepository.insertNewWaste(any(WasteDTO.class))).thenReturn(true);

        // Act
        ResponseEntity<?> response = wasteController.createNewWaste(mockWasteDTO);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(201, response.getStatusCodeValue(), "Status code should be 201");
        assertEquals(null, response.getBody(), "Response body should confirm successful creation");

        // Verify the interaction with the mock repository
        verify(wasteRepository, times(1)).insertNewWaste(mockWasteDTO);
    }

    @Test
    void testCreateNewWaste_FailsToCreate() {
        // Arrange
        WasteDTO mockWasteDTO = new WasteDTO(null, "Plastic", "Plastic waste", 2, null);
        when(wasteRepository.insertNewWaste(any(WasteDTO.class))).thenReturn(false);

        // Act
        ResponseEntity<?> response = wasteController.createNewWaste(mockWasteDTO);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(500, response.getStatusCodeValue(), "Status code should be 500");
        assertEquals("Failed to create new record", response.getBody(), "Response body should confirm failure");

        // Verify the interaction with the mock repository
        verify(wasteRepository, times(1)).insertNewWaste(mockWasteDTO);
    }

    @Test
    void testDeleteWasteById_Success() {
        // Arrange
        WasteWithCategoryDTO mockWaste = new WasteWithCategoryDTO(1, "Paper", "Recyclable paper", "Recyclables", "Waste that can be recycled");
        when(wasteRepository.getWasteWithCategory(1)).thenReturn(Optional.of(mockWaste));

        // Act
        ResponseEntity<?> response = wasteController.deleteWasteById(1);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(204, response.getStatusCodeValue(), "Status code should be 204");

        // Verify the interaction with the mock repository
        verify(wasteRepository, times(1)).getWasteWithCategory(1);
    }

    @Test
    void testDeleteWasteById_ThrowsWasteNotFoundException() {
        // Arrange
        when(wasteRepository.getWasteWithCategory(1)).thenReturn(Optional.empty());

        // Act & Assert
        WasteNotFoundException exception = assertThrows(WasteNotFoundException.class,
                () -> wasteController.deleteWasteById(1),
                "Expected a WasteNotFoundException when waste does not exist");

        assertEquals("Waste with id 1 not found", exception.getMessage(), "Exception message should match");
        verify(wasteRepository, times(1)).getWasteWithCategory(1);
    }
}
