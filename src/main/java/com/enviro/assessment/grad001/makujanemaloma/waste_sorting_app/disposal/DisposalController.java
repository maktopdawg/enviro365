package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    List<Disposal> getAllDisposals() {
        return disposalRepository.getAllDisposals();
    }

    @GetMapping( "/{id}" )
    Disposal getDisposal( @PathVariable Integer id ) {
        Optional<Disposal> disposal = disposalRepository.getDisposal( id );
        if ( disposal.isEmpty() ) {
            throw new DisposalNotFoundException( "Disposal with id " + id + " not found" );
        }
        return disposal.get();
    }

    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    void createNewDisposal( @Valid @RequestBody Disposal disposal ) {
        disposalRepository.insertNewDisposal( disposal );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    void updateDisposal( @PathVariable Integer id, @Valid @RequestBody Disposal disposal ) {
        disposalRepository.updateDisposal( disposal, id );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @DeleteMapping( "/{id}" )
    void deleteDisposalById( @PathVariable Integer id ) {
        disposalRepository.deleteDisposal( id );
    }
}
