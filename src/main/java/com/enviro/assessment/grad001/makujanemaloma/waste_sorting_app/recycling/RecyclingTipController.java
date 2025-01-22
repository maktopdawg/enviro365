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

@RestController
@RequestMapping( "/api/recycling-tips" )
public class RecyclingTipController {
    private final RecyclingTipRepository recyclingTipRepository;
    private final JdbcClient jdbcClient;

    public RecyclingTipController( RecyclingTipRepository recyclingTipRepository, JdbcClient jdbcClient ) {
        this.recyclingTipRepository = recyclingTipRepository;
        this.jdbcClient = jdbcClient;
    }

    @GetMapping( "" )
    List<RecyclingTipDTO> getAllTips() {
        return recyclingTipRepository.getAllRecyclingTips();
    }

    @GetMapping( "/{id}" )
    ResponseEntity<?> getRecyclingTipById( @PathVariable Integer id ) {
        Optional<RecyclingTipDTO> recyclingTipDTO = recyclingTipRepository.getRecyclingTipById( id );
        if ( recyclingTipDTO.isEmpty() ) {
            throw new RecyclingTipNotFound( "Recycling Tip with id " + id + " not found"  );
        }
        return ResponseEntity.ok( recyclingTipDTO.get() );
    }

    @PostMapping( "" )
    ResponseEntity<?> createRecyclingTip( @Valid @RequestBody RecyclingTipDTO recyclingTipDTO ) {
        boolean created = recyclingTipRepository.createNewRecyclingTip( recyclingTipDTO );
        if ( created ) {
            return ResponseEntity.ok( recyclingTipDTO );
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to create new record" );
    }

    @PutMapping( "/{id}" )
    ResponseEntity<?> updateCategory (@Valid @RequestBody RecyclingTipDTO recyclingTipDTO, @PathVariable Integer id ) {
        Optional<RecyclingTipDTO> recyclingTip = recyclingTipRepository.getRecyclingTipById( id );
        if ( recyclingTip.isEmpty() ) {
            throw new CategoryNotFoundException( "Recycling Tip with id " + id + " not found" );
        }
        boolean updated = recyclingTipRepository.updateRecyclingTip(recyclingTipDTO, id );
        if ( updated ) {
            return ResponseEntity.ok( recyclingTip );
        }
        return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
                .body( "Failed to update record" );
    }

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
