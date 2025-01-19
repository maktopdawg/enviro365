package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.*;

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
    List<WasteDTO> getAllWaste() {
        return wasteRepository.getAllWaste();
    }

    @GetMapping( "/with-disposals" )
    List<WasteWithDisposalsDTO> getAllWasteWithDisposal() {
        return wasteRepository.getAllWasteWithDisposal();
    }

    @GetMapping( "/{id}" )
    WasteDTO getWaste(@PathVariable Integer id ) {
        Optional<WasteDTO> waste = wasteRepository.getWaste( id );
        if ( waste.isEmpty() ) {
            throw new WasteNotFoundException( "Waste with id " + id + " not found" );
        }
        return waste.get();
    }

    @ResponseStatus( HttpStatus.CREATED )
    @PostMapping( "" )
    void createNewWaste( @Valid @RequestBody WasteDTO wasteDTO) {
        wasteRepository.insertNewWaste(wasteDTO);
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @PutMapping( "/{id}" )
    void updateWaste( @PathVariable Integer id, @Valid @RequestBody WasteDTO wasteDTO) {
        wasteRepository.updateWaste(wasteDTO, id );
    }

    @ResponseStatus( HttpStatus.NO_CONTENT )
    @DeleteMapping( "/{id}" )
    void deleteWasteById( @PathVariable Integer id ) {
        wasteRepository.deleteWasteById( id );
    }
}
