package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.exceptions.CategoryNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The CategoryController class handles HTTP requests related to the categories in the waste sorting application.
 * It provides endpoints for retrieving, creating, updating, and deleting categories as well as getting recycling tips.
 */
@RestController
@RequestMapping( "/api/categories" )
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final JdbcClient jdbcClient;

    /**
     * Constructs a CategoryController instance with the given CategoryRepository and JdbcClient.
     *
     * @param categoryRepository The repository used for interacting with category data.
     * @param jdbcClient The JdbcClient used for database interactions.
     */
    public CategoryController (CategoryRepository categoryRepository, JdbcClient jdbcClient) {
        this.categoryRepository = categoryRepository;
        this.jdbcClient = jdbcClient;
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return A list of CategoryDTO objects representing all categories.
     */
    @GetMapping("")
    List<CategoryDTO> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return A ResponseEntity containing the category if found, otherwise throws a CategoryNotFoundException.
     */
    @GetMapping( "/{id}" )
    ResponseEntity<?> getCategoryById (@PathVariable Integer id ) {
        Optional<CategoryDTO> category = categoryRepository.getCategoryById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }
        return ResponseEntity.ok( category.get() );
    }

    /**
     * Creates a new category in the database.
     *
     * @param categoryDTO The CategoryDTO object containing the information for the new category.
     * @return A ResponseEntity with status CREATED if the category was successfully created, or INTERNAL_SERVER_ERROR if failed.
     */
    @PostMapping( "" )
    ResponseEntity<?> createNewCategory ( @Valid  @RequestBody CategoryDTO categoryDTO) {
        boolean created  = categoryRepository.createNewCategory( categoryDTO );
        if ( created ) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to create new record" );
    }

    /**
     * Updates an existing category by its ID.
     *
     * @param categoryDTO The CategoryDTO object containing the updated information.
     * @param id The ID of the category to update.
     * @return A ResponseEntity with status NO_CONTENT if the update was successful, or INTERNAL_SERVER_ERROR if failed.
     */
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    ResponseEntity<?> updateCategory (@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Integer id ) {
        Optional<CategoryDTO> category = categoryRepository.getCategoryById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }
        boolean updated = categoryRepository.updateCategory(categoryDTO, id );
        if ( updated ) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to update record" );
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id The ID of the category to delete.
     * @throws CategoryNotFoundException if the category with the given ID does not exist.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteCategoryById(@PathVariable Integer id) {
        Optional<CategoryDTO> category = categoryRepository.getCategoryById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }
        categoryRepository.deleteCategoryById( id );
    }

    /**
     * Retrieves all categories along with their recycling tips.
     *
     * @return A list of CategoryWithTipsDTO objects representing categories with recycling tips.
     */
    @GetMapping( "/recycling-tips" )
    List<CategoryWithTipsDTO> getCategoriesWithTips() {
        return categoryRepository.getCategoriesWithTips();
    }

    /**
     * Retrieves a specific category along with its recycling tips by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return A ResponseEntity containing the category with tips if found, otherwise throws a CategoryNotFoundException.
     */
    @GetMapping( "/{id}/recycling-tips" )
    ResponseEntity<?> getCategoryWithTipsById( @PathVariable Integer id ) {
        Optional<CategoryWithTipsDTO> category = categoryRepository.getCategoryWithTipsById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }
        return ResponseEntity.ok( category.get() );
    }
}
