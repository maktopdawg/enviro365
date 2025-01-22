package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.exceptions.CategoryNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( "/api/categories" )
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final JdbcClient jdbcClient;

    public CategoryController (CategoryRepository categoryRepository, JdbcClient jdbcClient) {
        this.categoryRepository = categoryRepository;
        this.jdbcClient = jdbcClient;
    }

    @GetMapping("")
    List<CategoryDTO> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @GetMapping( "/{id}" )
    ResponseEntity<?> getCategoryById (@PathVariable Integer id ) {
        Optional<CategoryDTO> category = categoryRepository.getCategoryById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }
        return ResponseEntity.ok( category.get() );
    }

    // POST
    @PostMapping( "" )
    ResponseEntity<?> createNewCategory ( @Valid  @RequestBody CategoryDTO categoryDTO) {
        boolean created  = categoryRepository.createNewCategory( categoryDTO );
        if ( created ) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to create new record" );
    }

    // PUT
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    ResponseEntity<?> updateCategory (@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Integer id ) {
        Optional<CategoryDTO> category = categoryRepository.getCategoryById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }
        boolean updated = categoryRepository.updateCategory(categoryDTO, id );
        if ( updated ) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to update record" );
    }

    // DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteCategoryById(@PathVariable Integer id) {
        Optional<CategoryDTO> category = categoryRepository.getCategoryById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }
        categoryRepository.deleteCategoryById( id );
    }
}
