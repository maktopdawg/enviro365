package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.DisposalDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.*;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.exceptions.WasteNotFoundException;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.models.WasteWithTipsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
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

    @Test
    void testGetAllWasteWithRecyclingTips() {
        // Arrange
        List<WasteWithTipsDTO> mockWasteWithTips = List.of(
                new WasteWithTipsDTO(
                        1,
                        "Plastic Bottle",
                        "Plastic bottle description",
                        "Plastic",
                        List.of(
                                new RecyclingTipDTO(1, "Tip 1", "Wash thoroughly", null, 1, LocalDateTime.now()),
                                new RecyclingTipDTO(2, "Tip 2", "Crush to save space", null, 1, LocalDateTime.now())
                        )
                ),
                new WasteWithTipsDTO(
                        2,
                        "Paper",
                        "Paper description",
                        "Paper",
                        List.of(
                                new RecyclingTipDTO(3, "Tip 3", "Remove staples", null, 2, LocalDateTime.now())
                        )
                )
        );
        when(wasteRepository.getAllWasteWithTips()).thenReturn(mockWasteWithTips);

        // Act
        List<WasteWithTipsDTO> wasteWithTips = wasteController.getWasteWithRecyclingTips();

        // Assert
        assertNotNull(wasteWithTips, "WasteWithTips list should not be null");
        assertEquals(2, wasteWithTips.size(), "WasteWithTips list size should match the mock data");
        assertEquals("Plastic Bottle", wasteWithTips.get(0).name(), "First waste name should match mock data");
        assertEquals(2, wasteWithTips.get(0).recyclingTips().size(), "First waste should have 2 recycling tips");
    }

    @Test
    void testGetWasteWithRecyclingTipsByIdFound() {
        // Arrange
        WasteWithTipsDTO mockWasteWithTips = new WasteWithTipsDTO(
                1,
                "Plastic Bottle",
                "Plastic bottle description",
                "Plastic",
                List.of(
                        new RecyclingTipDTO(1, "Tip 1", "Wash thoroughly", null, 1, LocalDateTime.now()),
                        new RecyclingTipDTO(2, "Tip 2", "Crush to save space", null, 1, LocalDateTime.now())
                )
        );
        when(wasteRepository.getWasteWithTipsByID(1)).thenReturn(Optional.of(mockWasteWithTips));

        // Act
        ResponseEntity<?> response = wasteController.getWasteWithRecyclingTipsById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status should be OK");
        assertTrue(response.getBody() instanceof WasteWithTipsDTO, "Response body should be a WasteWithTipsDTO");
        assertEquals(mockWasteWithTips, response.getBody(), "Response body should match the mock waste");
    }

    @Test
    void testGetWasteWithRecyclingTipsByIdNotFound() {
        // Arrange
        when(wasteRepository.getWasteWithTipsByID(99)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(WasteNotFoundException.class, () -> wasteController.getWasteWithRecyclingTipsById(99), "Should throw WasteNotFoundException for non-existent ID");
    }

    @Test
    void testGetAllWasteWithDisposalNoCategory() {
        // Arrange
        List<WasteOverviewDTO> mockWasteOverview = List.of(
                new WasteOverviewDTO(
                        1,
                        "Plastic Bottle",
                        "Plastic bottle description",
                        "Plastic",
                        "Plastic items",
                        List.of(
                                new DisposalDTO(1, 101, "Recycling Center", "Recycle at center", "123 Green Street", null)
                        )
                ),
                new WasteOverviewDTO(
                        2,
                        "Paper",
                        "Paper description",
                        "Paper",
                        "Paper products",
                        List.of(
                                new DisposalDTO(2, 102, "Composting", "Compost if possible", null, null)
                        )
                )
        );
        when(wasteRepository.getAllWasteWithDisposal(null)).thenReturn(mockWasteOverview);

        // Act
        List<WasteOverviewDTO> wasteWithDisposal = wasteController.getAllWasteWithDisposal(null);

        // Assert
        assertNotNull(wasteWithDisposal, "WasteOverview list should not be null");
        assertEquals(2, wasteWithDisposal.size(), "WasteOverview list size should match the mock data");
        assertEquals("Plastic Bottle", wasteWithDisposal.get(0).name(), "First waste name should match mock data");
    }

    @Test
    void testGetAllWasteWithDisposalWithCategory() {
        // Arrange
        String category = "Plastic";
        List<WasteOverviewDTO> mockWasteOverview = List.of(
                new WasteOverviewDTO(
                        1,
                        "Plastic Bottle",
                        "Plastic bottle description",
                        category,
                        "Plastic items",
                        List.of(
                                new DisposalDTO(1, 101, "Recycling Center", "Recycle at center", "123 Green Street", null)
                        )
                )
        );
        when(wasteRepository.getAllWasteWithDisposal(category.toLowerCase())).thenReturn(mockWasteOverview);

        // Act
        List<WasteOverviewDTO> wasteWithDisposal = wasteController.getAllWasteWithDisposal(category);

        // Assert
        assertNotNull(wasteWithDisposal, "WasteOverview list should not be null");
        assertEquals(1, wasteWithDisposal.size(), "WasteOverview list size should match the mock data with category filter");
        assertEquals(category, wasteWithDisposal.get(0).category(), "First waste category should match the filter");
    }
}
