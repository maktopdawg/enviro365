package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.exceptions.CategoryNotFoundException;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipController;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipRepository;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.exceptions.RecyclingTipNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecyclingTipControllerTest {

    private RecyclingTipController recyclingTipController;
    private RecyclingTipRepository recyclingTipRepository;

    @BeforeEach
    void setUp() {
        recyclingTipRepository = mock(RecyclingTipRepository.class);
        recyclingTipController = new RecyclingTipController(recyclingTipRepository, null); // JdbcClient not used in these tests
    }

    @Test
    void testGetAllTips() {
        // Arrange
        List<RecyclingTipDTO> mockTips = List.of(
                new RecyclingTipDTO(1, "Tip 1", "Recycle plastic", 101, 201, LocalDateTime.now()),
                new RecyclingTipDTO(2, "Tip 2", "Compost organic waste", 102, 202, LocalDateTime.now())
        );
        when(recyclingTipRepository.getAllRecyclingTips()).thenReturn(mockTips);

        // Act
        List<RecyclingTipDTO> tips = recyclingTipController.getAllTips();

        // Assert
        assertNotNull(tips, "Tips list should not be null");
        assertEquals(2, tips.size(), "Tips list size should match the mock data");
        assertEquals("Tip 1", tips.get(0).title(), "First tip title should match mock data");
    }

    @Test
    void testGetRecyclingTipByIdFound() {
        // Arrange
        RecyclingTipDTO mockTip = new RecyclingTipDTO(1, "Tip 1", "Recycle plastic", 101, 201, LocalDateTime.now());
        when(recyclingTipRepository.getRecyclingTipById(1)).thenReturn(Optional.of(mockTip));

        // Act
        ResponseEntity<?> response = recyclingTipController.getRecyclingTipById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertTrue(response.getBody() instanceof RecyclingTipDTO, "Response body should be a RecyclingTipDTO");
        assertEquals(mockTip, response.getBody(), "Response body should match the mock tip");
    }

    @Test
    void testGetRecyclingTipByIdNotFound() {
        // Arrange
        when(recyclingTipRepository.getRecyclingTipById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecyclingTipNotFound.class, () -> recyclingTipController.getRecyclingTipById(99), "Should throw RecyclingTipNotFound for non-existent ID");
    }

    @Test
    void testCreateRecyclingTipSuccess() {
        // Arrange
        RecyclingTipDTO newTip = new RecyclingTipDTO(null, "Tip 3", "Use cloth bags", 103, 203, null);
        when(recyclingTipRepository.createNewRecyclingTip(newTip)).thenReturn(true);

        // Act
        ResponseEntity<?> response = recyclingTipController.createRecyclingTip(newTip);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK for successful creation");
        assertNull(response.getBody(), "Response body should be null for successful creation");
    }

    @Test
    void testCreateRecyclingTipFailure() {
        // Arrange
        RecyclingTipDTO newTip = new RecyclingTipDTO(null, "Tip 3", "Use cloth bags", 103, 203, null);
        when(recyclingTipRepository.createNewRecyclingTip(newTip)).thenReturn(false);

        // Act
        ResponseEntity<?> response = recyclingTipController.createRecyclingTip(newTip);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Response status should be INTERNAL_SERVER_ERROR for failed creation");
        assertNotNull(response.getBody(), "Response body should contain an error message");
    }

    @Test
    void testUpdateRecyclingTipSuccess() {
        // Arrange
        RecyclingTipDTO updatedTip = new RecyclingTipDTO(1, "Updated Tip", "Recycle properly", 101, 201, null);
        when(recyclingTipRepository.getRecyclingTipById(1)).thenReturn(Optional.of(updatedTip));
        when(recyclingTipRepository.updateRecyclingTip(updatedTip, 1)).thenReturn(true);

        // Act
        ResponseEntity<?> response = recyclingTipController.updateCategory(updatedTip, 1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK for successful update");
        assertNull(response.getBody(), "Response body should be null for successful update");
    }

    @Test
    void testUpdateRecyclingTipNotFound() {
        // Arrange
        RecyclingTipDTO updatedTip = new RecyclingTipDTO(1, "Updated Tip", "Recycle properly", 101, 201, null);
        when(recyclingTipRepository.getRecyclingTipById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecyclingTipNotFound.class, () -> recyclingTipController.updateCategory(updatedTip, 1), "Should throw RecyclingTipNotFound for non-existent ID");
    }

    @Test
    void testDeleteRecyclingTipSuccess() {
        // Arrange
        RecyclingTipDTO mockTip = new RecyclingTipDTO(1, "Tip 1", "Recycle plastic", 101, 201, LocalDateTime.now());
        when(recyclingTipRepository.getRecyclingTipById(1)).thenReturn(Optional.of(mockTip));

        // Act
        recyclingTipController.deleteCategoryById(1);

        // Assert
        verify(recyclingTipRepository, times(1)).deleteRecyclingTip(1);
    }

    @Test
    void testDeleteRecyclingTipNotFound() {
        // Arrange
        when(recyclingTipRepository.getRecyclingTipById(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecyclingTipNotFound.class, () -> recyclingTipController.deleteCategoryById(99), "Should throw RecyclingTipNotFound for non-existent ID");
    }
}
