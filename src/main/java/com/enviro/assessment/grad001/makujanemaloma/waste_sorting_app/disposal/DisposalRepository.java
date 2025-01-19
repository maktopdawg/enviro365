package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DisposalRepository extends BaseRepository<DisposalDTO> {
    private final String tableName = "Disposal";

    public DisposalRepository(JdbcClient jdbcClient )   {
        super( jdbcClient, DisposalDTO.class );
    }

    public List<DisposalDTO> getAllDisposals() {
        return getAll( tableName );
    }

    public Optional<DisposalDTO> getDisposal(Integer id ) {
        return getById( tableName, id );
    }

    public void insertNewDisposal( DisposalDTO disposalDTO) {
        create(
                tableName,
                List.of( disposalDTO.wasteId(), disposalDTO.method(), disposalDTO.instructions(), disposalDTO.location()  ),
                "INSERT INTO " + tableName + " ( wasteId, method, instructions, location, lastUpdated ) VALUES ( ?, ?, ?, ?, CURRENT_TIMESTAMP )"
        );
    }

    public void updateDisposal(DisposalDTO disposalDTO, Integer id ) {
        update(
                tableName,
                List.of( disposalDTO.wasteId(), disposalDTO.method(), disposalDTO.instructions(), disposalDTO.location(), id ),
                "UPDATE " + tableName + " SET wasteId = ?, method = ?, instructions = ?, location = ?, lastUpdated = CURRENT_TIMESTAMP where id = ?"
        );
    }

    public void deleteDisposal( Integer id ) {
        delete( tableName, id );
    }
}
