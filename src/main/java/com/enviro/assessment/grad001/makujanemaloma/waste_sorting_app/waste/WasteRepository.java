package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WasteRepository extends BaseRepository<Waste> {
    private final String tableName = "Waste";

    public WasteRepository( JdbcClient jdbcClient ) {
        super( jdbcClient, Waste.class );
    }

    public List<Waste> getAllWaste() {
        return getAll( tableName );
    }

    public Optional<Waste> getWaste( Integer id ) {
        return getById( tableName, id );
    }

    public void insertNewWaste( Waste waste ) {
        create(
                tableName,
                List.of( waste.name(), waste.description(), waste.categoryId() ),
                "INSERT INTO " + tableName + " ( name, description, categoryId, lastUpdated ) VALUES ( ?, ?, ?, CURRENT_TIMESTAMP )"
        );
    }

    public void updateWaste( Waste waste, Integer id ) {
        update(
                tableName,
                List.of( waste.name(), waste.description(), waste.categoryId(), id ),
                "UPDATE " + tableName + " SET name = ?, description = ?, categoryId = ?, lastUpdated = CURRENT_TIMESTAMP WHERE id = ?"
        );
    }

    public void deleteWasteById( Integer id ) {
        delete( tableName, id );
    }
}
