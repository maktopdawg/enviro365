package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.CategoryDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.exceptions.CategoryNotFoundException;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.exceptions.RecyclingTipNotFound;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The RecyclingTipController class is a Spring MVC controller that provides RESTful endpoints for managing recycling tips.
 * It handles HTTP requests related to recycling tips, including retrieving, creating, updating, and deleting recycling tips.
 */
@RestController
@RequestMapping( "/api/recycling-tips" )
public class RecyclingTipController {
    private final RecyclingTipRepository recyclingTipRepository;
    private final JdbcClient jdbcClient;

    /**
     * Constructs a RecyclingTipController with the provided RecyclingTipRepository and JdbcClient.
     *
     * @param recyclingTipRepository The RecyclingTipRepository instance for interacting with recycling tips data.
     * @param jdbcClient The JdbcClient instance used for executing SQL queries.
     */
    public RecyclingTipController( RecyclingTipRepository recyclingTipRepository, JdbcClient jdbcClient ) {
        this.recyclingTipRepository = recyclingTipRepository;
        this.jdbcClient = jdbcClient;
    }

    /**
     * Retrieves all recycling tips.
     *
     * @return A list of RecyclingTipDTO objects representing all recycling tips.
     */
    @GetMapping( "" )
    List<RecyclingTipDTO> getAllTips() {
        return recyclingTipRepository.getAllRecyclingTips();
    }

    /**
     * Retrieves a recycling tip by its ID.
     *
     * @param id The ID of the recycling tip to retrieve.
     * @return A ResponseEntity containing the RecyclingTipDTO if found, otherwise throws RecyclingTipNotFound exception.
     */
    @GetMapping( "/{id}" )
    ResponseEntity<?> getRecyclingTipById( @PathVariable Integer id ) {
        Optional<RecyclingTipDTO> recyclingTipDTO = recyclingTipRepository.getRecyclingTipById( id );
        if ( recyclingTipDTO.isEmpty() ) {
            throw new RecyclingTipNotFound( "Recycling Tip with id " + id + " not found"  );
        }
        return ResponseEntity.ok( recyclingTipDTO.get() );
    }

    /**
     * Creates a new recycling tip record in the database.
     *
     * @param recyclingTipDTO The RecyclingTipDTO object containing the details of the new recycling tip.
     * @return A ResponseEntity indicating whether the recycling tip was successfully created or not.
     */
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PostMapping( "" )
    ResponseEntity<?> createRecyclingTip( @Valid @RequestBody RecyclingTipDTO recyclingTipDTO ) {
        boolean created = recyclingTipRepository.createNewRecyclingTip( recyclingTipDTO );
        if ( created ) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to create new record" );
    }

    /**
     * Updates an existing recycling tip record in the database.
     *
     * @param recyclingTipDTO The RecyclingTipDTO object containing the updated details.
     * @param id The ID of the recycling tip to update.
     * @return A ResponseEntity indicating whether the recycling tip was successfully updated or not.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping( "/{id}" )
    ResponseEntity<?> updateCategory ( @Valid @RequestBody RecyclingTipDTO recyclingTipDTO, @PathVariable Integer id ) {
        Optional<RecyclingTipDTO> recyclingTip = recyclingTipRepository.getRecyclingTipById( id );
        if ( recyclingTip.isEmpty() ) {
            throw new CategoryNotFoundException( "Recycling Tip with id " + id + " not found" );
        }
        boolean updated = recyclingTipRepository.updateRecyclingTip(recyclingTipDTO, id );
        if ( updated ) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to update record" );
    }

    /**
     * Deletes a recycling tip record by its ID.
     *
     * @param id The ID of the recycling tip to delete.
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteCategoryById(@PathVariable Integer id) {
        Optional<RecyclingTipDTO> recyclingTip = recyclingTipRepository.getRecyclingTipById( id );
        if ( recyclingTip.isEmpty() ) {
            throw new CategoryNotFoundException( "Recycling Tip with id " + id + " not found" );
        }
        recyclingTipRepository.deleteRecyclingTip( id );
    }
}
