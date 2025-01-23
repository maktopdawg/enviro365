package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.exceptions.DisposalNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The DisposalController class provides endpoints for managing disposal records.
 * It uses the DisposalRepository for performing CRUD operations on disposal data.
 */
@RestController
@RequestMapping( "/api/disposals" )
public class DisposalController {
    private final DisposalRepository disposalRepository;
    private final JdbcClient jdbcClient;

    /**
     * Constructs a DisposalController with the provided DisposalRepository and JdbcClient.
     *
     * @param disposalRepository The repository used for CRUD operations on disposal records.
     * @param jdbcClient The JdbcClient instance used for executing SQL queries.
     */
    public DisposalController( DisposalRepository disposalRepository, JdbcClient jdbcClient ) {
        this.disposalRepository = disposalRepository;
        this.jdbcClient = jdbcClient;
    }

    /**
     * Retrieves all disposal records.
     *
     * @return A list of DisposalDTO objects representing all disposals.
     */
    @GetMapping( "" )
    public List<DisposalDTO> getAllDisposals() {
        return disposalRepository.getAllDisposals();
    }

    /**
     * Retrieves a disposal record by its ID.
     *
     * @param id The ID of the disposal to retrieve.
     * @return A ResponseEntity containing the DisposalDTO if found, or an error response if not found.
     */
    @GetMapping( "/{id}" )
    public ResponseEntity<?> getDisposal(@PathVariable Integer id ) {
        Optional<DisposalDTO> disposal = disposalRepository.getDisposal( id );
        if ( disposal.isEmpty() ) {
            throw new DisposalNotFoundException( "Disposal with id " + id + " not found" );
        }
        return ResponseEntity.ok( disposal.get() );
    }

    /**
     * Creates a new disposal record.
     *
     * @param disposalDTO The DisposalDTO object containing the details of the new disposal.
     * @return A ResponseEntity indicating the success or failure of the operation.
     */
    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    public ResponseEntity<?> createNewDisposal( @Valid @RequestBody DisposalDTO disposalDTO) {
        boolean created  = disposalRepository.insertNewDisposal(disposalDTO);
        if ( created ) {
            return ResponseEntity.status( HttpStatus.CREATED ).build();
        }

        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to create new record" );
    }

    /**
     * Updates an existing disposal record.
     *
     * @param id The ID of the disposal to update.
     * @param disposalDTO The DisposalDTO object containing the updated details.
     * @return A ResponseEntity indicating the success or failure of the update operation.
     */
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    public ResponseEntity<?> updateDisposal( @PathVariable Integer id, @Valid @RequestBody DisposalDTO disposalDTO) {
        Optional<DisposalDTO> disposal = disposalRepository.getDisposal( id );
        if ( disposal.isEmpty() ) {
            throw new DisposalNotFoundException( "Disposal with id " + id + " not found" );
        }

        boolean updated  = disposalRepository.updateDisposal(disposalDTO, id );
        if ( updated ) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to update record" );
    }

    /**
     * Deletes a disposal record by its ID.
     *
     * @param id The ID of the disposal to delete.
     */
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @DeleteMapping( "/{id}" )
    public void deleteDisposalById( @PathVariable Integer id ) {
        Optional<DisposalDTO> disposal = disposalRepository.getDisposal( id );
        if ( disposal.isEmpty() ) {
            throw new DisposalNotFoundException( "Disposal with id " + id + " not found" );
        }

        disposalRepository.deleteDisposal( id );
    }
}
