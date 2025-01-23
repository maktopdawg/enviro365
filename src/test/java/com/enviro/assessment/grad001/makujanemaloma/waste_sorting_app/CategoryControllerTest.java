package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.CategoryController;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.CategoryDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.CategoryRepository;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.exceptions.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// Example test class for CategoryController
class CategoryControllerTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCategories() {
        // Arrange
        List<CategoryDTO> mockCategories = List.of(
                new CategoryDTO(1, "Plastic", "description", LocalDateTime.now()),
                new CategoryDTO(2, "Organic", "Description", LocalDateTime.now()),
                new CategoryDTO(3, "Metal", "Description for metal waste", LocalDateTime.now())
        );
        when(categoryRepository.getAllCategories()).thenReturn(mockCategories);

        // Act
        List<CategoryDTO> response = categoryController.getAllCategories();

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(3, response.size(), "Response size should be 3");

        assertEquals(1, response.get(0).id(), "First category ID should be 1");
        assertEquals("Plastic", response.get(0).name(), "First category name should be 'Plastic'");

        assertEquals(2, response.get(1).id(), "Second category ID should be 2");
        assertEquals("Organic", response.get(1).name(), "Second category name should be 'Organic'");

        assertEquals(3, response.get(2).id(), "Third category ID should be 3");
        assertEquals("Metal", response.get(2).name(), "Third category name should be 'Metal'");

        // Verify the interaction with the mock repository
        verify(categoryRepository, times(1)).getAllCategories();
    }

    @Test
    void testGetAllCategoriesEmptyList() {
        // Arrange
        when(categoryRepository.getAllCategories()).thenReturn(List.of());

        // Act
        List<CategoryDTO> response = categoryController.getAllCategories();

        // Assert
        assertNotNull(response, "Response should not be null");
        assertTrue(response.isEmpty(), "Response should be an empty list");

        // Verify the interaction with the mock repository
        verify(categoryRepository, times(1)).getAllCategories();
    }

    @Test
    void testGetCategory() {
        // Arrange
        List<CategoryDTO> mockCategories = List.of(
                new CategoryDTO(1, "Plastic", "description", LocalDateTime.now()),
                new CategoryDTO(2, "Organic", "Description", LocalDateTime.now())
        );

        // Mocking the behavior of the repository to return an Optional of the first category
        when(categoryRepository.getCategoryById(1)).thenReturn(Optional.of(mockCategories.get(0)));

        // Act
        ResponseEntity<?> response = categoryController.getCategoryById(1);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Status code should be 200");
        assertTrue(response.getBody() instanceof CategoryDTO, "Response body should be of type CategoryDTO");

        CategoryDTO category = (CategoryDTO) response.getBody();
        assertEquals(1, category.id(), "Category ID should be 1");
        assertEquals("Plastic", category.name(), "Category name should be 'Plastic'");
        assertEquals("description", category.description(), "Category description should be 'description'");

        // Verify the interaction with the mock repository
        verify(categoryRepository, times(1)).getCategoryById(1);
    }

    @Test
    void testGetCategoryNotFound() {
        // Arrange
        when(categoryRepository.getCategoryById(1)).thenReturn(Optional.empty());

        // Act & Assert
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryController.getCategoryById(1),
                "Expected a CategoryNotFoundException when the category does not exist");

        assertEquals("Category with id 1 not found", exception.getMessage(), "Exception message should match");
        verify(categoryRepository, times(1)).getCategoryById(1);
    }
}