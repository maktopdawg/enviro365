package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.CategoryRepository;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( "/api/waste" )
public class WasteController {

    private final WasteRepository wasteRepository;
    private final JdbcClient jdbcClient;
    private final CategoryRepository categoryRepository;

    public WasteController(WasteRepository wasteRepository, JdbcClient jdbcClient, CategoryRepository categoryRepository) {
        this.wasteRepository = wasteRepository;
        this.jdbcClient = jdbcClient;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping( "" )
    List<WasteWithCategoryDTO> getAllWaste(
            @RequestParam( value = "category", required = false ) String category
    ) {
        return wasteRepository.getAllWasteWithCategory( category != null ? category.toLowerCase() : category );
    }

    @GetMapping( "/{id}" )
    ResponseEntity<?> getWaste( @PathVariable Integer id ) {
        Optional<WasteWithCategoryDTO> waste = wasteRepository.getWasteWithCategory( id );
        if ( waste.isEmpty() ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND )
                    .body( "<h1>No waste found with ID " + id + "</h2>" );
        }
        return ResponseEntity.ok( waste.get() );
    }

    @GetMapping( "/overview" )
    List<WasteOverviewDTO> getAllWasteWithDisposal(
            @RequestParam( value = "category", required = false ) String category
    ) {
        return wasteRepository.getAllWasteWithDisposal( category != null ? category.toLowerCase() : category );
    }

    @GetMapping( "/overview/{id}" )
    ResponseEntity<?> getWasteOverviewById( @PathVariable Integer id ) {
        Optional<WasteOverviewDTO> waste = wasteRepository.getWasteOverviewById( id );
        if ( waste.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body( "<h1>No waste found with ID " + id + "</h2>" );
        }
        return ResponseEntity.ok( waste.get() );
    }

    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    ResponseEntity<?> createNewWaste( @Valid @RequestBody WasteDTO wasteDTO) {
        boolean created  = wasteRepository.insertNewWaste(wasteDTO);
        if ( created ) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to create new record" );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    ResponseEntity<?> updateWaste( @PathVariable Integer id, @Valid @RequestBody WasteDTO wasteDTO) {
        boolean updated = wasteRepository.updateWaste(wasteDTO, id );
        if ( updated ) {
            return ResponseEntity.status( HttpStatus.NO_CONTENT ).build();
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to update record" );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @DeleteMapping( "/{id}" )
    void deleteWasteById( @PathVariable Integer id ) {
        wasteRepository.deleteWasteById( id );
    }
}
