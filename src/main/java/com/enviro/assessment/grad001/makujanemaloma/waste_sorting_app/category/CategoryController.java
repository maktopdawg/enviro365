package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    CategoryDTO getCategoryById (@PathVariable Integer id) {

        Optional<CategoryDTO>  category = categoryRepository.getCategoryById( id );
        if ( category.isEmpty() ) {
            throw new CategoryNotFoundException( "Category with id " + id + " not found" );
        }

        return category.get();
    }

    // POST
    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    void createNewCategory ( @Valid  @RequestBody CategoryDTO categoryDTO) {
        categoryRepository.createNewCategory(categoryDTO);
    }

    // PUT
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    void updateCategory (@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Integer id ) {
        categoryRepository.updateCategory(categoryDTO, id );
    }

    // DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteCategoryById(@PathVariable Integer id) {
        categoryRepository.deleteCategoryById( id );
    }
}
