package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DisposalRepository extends BaseRepository<Disposal> {
    private final String tableName = "Disposal";

    public DisposalRepository(JdbcClient jdbcClient )   {
        super( jdbcClient, Disposal.class );
    }

    public List<Disposal> getAllDisposals() {
        return getAll( tableName );
    }

    public Optional<Disposal> getDisposal(Integer id ) {
        return getById( tableName, id );
    }

    public void insertNewDisposal( Disposal disposal ) {
        create(
                tableName,
                List.of( disposal.wasteId(), disposal.method(), disposal.instructions(), disposal.location()  ),
                "INSERT INTO " + tableName + " ( wasteId, method, instructions, location, lastUpdated ) VALUES ( ?, ?, ?, ?, CURRENT_TIMESTAMP )"
        );
    }

    public void updateDisposal( Disposal disposal, Integer id ) {
        update(
                tableName,
                List.of( disposal.wasteId(), disposal.method(), disposal.instructions(), disposal.location(), id ),
                "UPDATE " + tableName + " SET wasteId = ?, method = ?, instructions = ?, location = ?, lastUpdated = CURRENT_TIMESTAMP where id = ?"
        );
    }

    public void deleteDisposal( Integer id ) {
        delete( tableName, id );
    }
}
