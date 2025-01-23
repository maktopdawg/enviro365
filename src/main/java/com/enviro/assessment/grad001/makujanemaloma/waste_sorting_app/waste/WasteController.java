package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.CategoryRepository;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.exceptions.WasteNotFoundException;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.models.WasteWithTipsDTO;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * The `WasteController` class handles HTTP requests related to waste management,
 * including CRUD operations for waste records and operations to retrieve waste with
 * associated categories, disposals, and recycling tips.
 * This controller interacts with the `WasteRepository` for data access,
 * and utilizes `CategoryRepository` for category-related data.
 */
@RestController
@RequestMapping( "/api/waste" )
public class WasteController {

    private final WasteRepository wasteRepository;
    private final JdbcClient jdbcClient;
    private final CategoryRepository categoryRepository;

    /**
     * Constructs a `WasteController` with the provided repositories.
     *
     * @param wasteRepository The repository for interacting with the waste data.
     * @param jdbcClient The client for running SQL queries.
     * @param categoryRepository The repository for interacting with category data.
     */
    public WasteController(
            WasteRepository wasteRepository,
            JdbcClient jdbcClient,
            CategoryRepository categoryRepository
    ) {
        this.wasteRepository = wasteRepository;
        this.jdbcClient = jdbcClient;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves a list of all waste records, optionally filtered by category.
     *
     * @param category The category filter for waste records. If null, no filtering occurs.
     * @return A list of `WasteWithCategoryDTO` objects representing the waste records.
     */
    @GetMapping( "" )
    public List<WasteWithCategoryDTO> getAllWaste(
            @RequestParam( value = "category", required = false ) String category
    ) {
        return wasteRepository.getAllWasteWithCategory( category != null ? category.toLowerCase() : category );
    }

    /**
     * Retrieves a specific waste record by its ID.
     *
     * @param id The ID of the waste record.
     * @return A `ResponseEntity` containing the `WasteWithCategoryDTO` object for the specified waste.
     * @throws WasteNotFoundException if no waste record is found with the given ID.
     */
    @GetMapping( "/{id}" )
    public ResponseEntity<?> getWaste( @PathVariable Integer id ) {
        Optional<WasteWithCategoryDTO> waste = wasteRepository.getWasteWithCategory( id );
        if ( waste.isEmpty() ) {
            throw new WasteNotFoundException( "Waste with id " + id + " not found" );
        }

        return ResponseEntity.ok( waste.get() );
    }

    /**
     * Retrieves a list of all waste records with associated disposal information,
     * optionally filtered by category.
     *
     * @param category The category filter for waste records. If null, no filtering occurs.
     * @return A list of `WasteOverviewDTO` objects representing waste with disposal data.
     */
    @GetMapping( "/overview" )
    public List<WasteOverviewDTO> getAllWasteWithDisposal(
            @RequestParam( value = "category", required = false ) String category
    ) {
        return wasteRepository.getAllWasteWithDisposal( category != null ? category.toLowerCase() : category );
    }

    /**
     * Retrieves the waste overview for a specific waste record by its ID.
     *
     * @param id The ID of the waste record.
     * @return A `ResponseEntity` containing the `WasteOverviewDTO` object for the specified waste.
     * @throws WasteNotFoundException if no waste overview is found for the given ID.
     */
    @GetMapping( "/{id}/overview" )
    public ResponseEntity<?> getWasteOverviewById( @PathVariable Integer id ) {
        Optional<WasteOverviewDTO> waste = wasteRepository.getWasteOverviewById( id );
        if ( waste.isEmpty() ) {
            throw new WasteNotFoundException( "Waste with id " + id + " not found" );
        }
        return ResponseEntity.ok( waste.get() );
    }

    /**
     * Creates a new waste record.
     *
     * @param wasteDTO The data transfer object representing the new waste record.
     * @return A `ResponseEntity` with the appropriate HTTP status based on the result.
     * @throws WasteNotFoundException if creation fails.
     */
    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    public ResponseEntity<?> createNewWaste( @Valid @RequestBody WasteDTO wasteDTO) {
        boolean created  = wasteRepository.insertNewWaste(wasteDTO);
        if ( created ) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to create new record" );
    }

    /**
     * Updates an existing waste record by its ID.
     *
     * @param id The ID of the waste record to update.
     * @param wasteDTO The data transfer object containing the updated waste information.
     * @return A `ResponseEntity` with the appropriate HTTP status based on the result.
     * @throws WasteNotFoundException if no waste record is found with the given ID.
     */
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    public ResponseEntity<?> updateWaste( @PathVariable Integer id, @Valid @RequestBody WasteDTO wasteDTO) {
        Optional<WasteWithCategoryDTO> waste = wasteRepository.getWasteWithCategory( id );
        if ( waste.isEmpty() ) {
            throw new WasteNotFoundException( "Waste with id " + id + " not found" );
        }

        boolean updated = wasteRepository.updateWaste(wasteDTO, id );
        if ( updated ) {
            return ResponseEntity.status( HttpStatus.NO_CONTENT ).build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to update record" );
    }

    /**
     * Deletes a specific waste record by its ID.
     *
     * @param id The ID of the waste record to delete.
     * @throws WasteNotFoundException if no waste record is found with the given ID.
     */
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @DeleteMapping( "/{id}" )
    public ResponseEntity<?> deleteWasteById( @PathVariable Integer id ) {
        Optional<WasteWithCategoryDTO> waste = wasteRepository.getWasteWithCategory( id );
        if ( waste.isEmpty() ) {
            throw new WasteNotFoundException( "Waste with id " + id + " not found" );
        }
        wasteRepository.deleteWasteById( id );
        return ResponseEntity.status( HttpStatus.NO_CONTENT ).build();
    }

    /**
     * Retrieves a list of all waste records with associated recycling tips.
     *
     * @return A list of `WasteWithTipsDTO` objects representing waste with recycling tips.
     */
    @GetMapping( "/recycling-tips" )
    List<WasteWithTipsDTO> getWasteWithRecyclingTips() {
        return wasteRepository.getAllWasteWithTips();
    }

    /**
     * Retrieves the recycling tips for a specific waste record by its ID.
     *
     * @param id The ID of the waste record.
     * @return A `ResponseEntity` containing the `WasteWithTipsDTO` object for the specified waste.
     * @throws WasteNotFoundException if no recycling tips are found for the given ID.
     */
    @GetMapping( "/{id}/recycling-tips" )
    ResponseEntity<?> getWasteWithRecyclingTipsById( @PathVariable Integer id ) {
        Optional<WasteWithTipsDTO> waste = wasteRepository.getWasteWithTipsByID( id );
        if ( waste.isEmpty() ) {
            throw new WasteNotFoundException( "Waste with id " + id + " not found" );
        }
        return ResponseEntity.ok( waste.get() );
    }
}
