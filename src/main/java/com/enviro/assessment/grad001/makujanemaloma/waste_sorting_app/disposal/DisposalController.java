package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( "/api/disposals" )
public class DisposalController {
    private final DisposalRepository disposalRepository;
    private final JdbcClient jdbcClient;

    public DisposalController( DisposalRepository disposalRepository, JdbcClient jdbcClient ) {
        this.disposalRepository = disposalRepository;
        this.jdbcClient = jdbcClient;
    }

    @GetMapping( "" )
    List<DisposalDTO> getAllDisposals() {
        return disposalRepository.getAllDisposals();
    }

    @GetMapping( "/{id}" )
    ResponseEntity<?> getDisposal(@PathVariable Integer id ) {
        Optional<DisposalDTO> disposal = disposalRepository.getDisposal( id );
        if ( disposal.isEmpty() ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND )
                    .body( "<h1>No disposal found with ID " + id + "</h2>" );
        }
        return ResponseEntity.ok( disposal.get() );
    }

    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    ResponseEntity<?> createNewDisposal( @Valid @RequestBody DisposalDTO disposalDTO) {
        boolean created  = disposalRepository.insertNewDisposal(disposalDTO);
        if ( created ) {
            return ResponseEntity.status( HttpStatus.CREATED ).build();
        }

        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to create new record" );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    ResponseEntity<?> updateDisposal( @PathVariable Integer id, @Valid @RequestBody DisposalDTO disposalDTO) {
        boolean updated  = disposalRepository.updateDisposal(disposalDTO, id );
        if ( updated ) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to update record" );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @DeleteMapping( "/{id}" )
    void deleteDisposalById( @PathVariable Integer id ) {
        disposalRepository.deleteDisposal( id );
    }
}
