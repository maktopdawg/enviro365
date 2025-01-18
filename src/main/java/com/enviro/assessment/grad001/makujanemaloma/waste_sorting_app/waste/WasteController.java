package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    List<Waste> getAllWaste() {
        return wasteRepository.getAllWaste();
    }

    @GetMapping( "/{id}" )
    Waste getWaste( @PathVariable Integer id ) {
        Optional<Waste> waste = wasteRepository.getWaste( id );
        if ( waste.isEmpty() ) {
            throw new WasteNotFoundException( "Waste with id " + id + " not found" );
        }
        return waste.get();
    }

    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    void createNewWaste( @Valid @RequestBody Waste waste ) {
        wasteRepository.insertNewWaste( waste );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    void updateWaste( @PathVariable Integer id, @Valid @RequestBody Waste waste ) {
        wasteRepository.updateWaste( waste, id );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @DeleteMapping( "/{id}" )
    void deleteWasteById( @PathVariable Integer id ) {
        wasteRepository.deleteWasteById( id );
    }
}
