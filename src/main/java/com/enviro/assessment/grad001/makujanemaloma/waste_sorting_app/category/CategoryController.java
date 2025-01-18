package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @GetMapping( "/{id}" )
    Category getCategoryById (@PathVariable Integer id) {

        Optional<Category>  category = categoryRepository.getCategoryById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }

        return category.get();
    }

    // POST
    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    void createNewCategory ( @Valid  @RequestBody Category category ) {
        categoryRepository.createNewCategory( category );
    }

    // PUT
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    void updateCategory ( @Valid @RequestBody Category category, @PathVariable Integer id ) {
        categoryRepository.updateCategory( category, id );
    }

    // DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteCategoryById(@PathVariable Integer id) {
        categoryRepository.deleteCategoryById( id );
    }
}
