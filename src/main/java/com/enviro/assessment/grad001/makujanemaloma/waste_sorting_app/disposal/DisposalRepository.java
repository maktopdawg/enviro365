package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The DisposalRepository class provides methods to interact with the Disposal table in the database.
 * It extends the BaseRepository class to perform CRUD operations on Disposal data.
 */
@Repository
public class DisposalRepository extends BaseRepository<DisposalDTO> {
    private final String tableName = "Disposal";

    /**
     * Constructs a DisposalRepository with the provided JdbcClient.
     *
     * @param jdbcClient The JdbcClient instance used for executing SQL queries.
     */
    public DisposalRepository(JdbcClient jdbcClient )   {
        super( jdbcClient, DisposalDTO.class );
    }

    /**
     * Retrieves all disposal records from the Disposal table.
     *
     * @return A list of DisposalDTO objects representing all disposals.
     */
    public List<DisposalDTO> getAllDisposals() {
        return getAll( tableName );
    }

    /**
     * Retrieves a disposal record by its ID from the Disposal table.
     *
     * @param id The ID of the disposal to retrieve.
     * @return An Optional containing the DisposalDTO if found, otherwise an empty Optional.
     */
    public Optional<DisposalDTO> getDisposal(Integer id ) {
        return getById( tableName, id );
    }

    /**
     * Inserts a new disposal record into the Disposal table.
     *
     * @param disposalDTO The DisposalDTO object containing the details of the disposal to insert.
     * @return true if the disposal was successfully inserted, false otherwise.
     */
    public boolean insertNewDisposal( DisposalDTO disposalDTO) {
        return createRecord(
                tableName,
                List.of( disposalDTO.wasteId(), disposalDTO.method(), disposalDTO.instructions(), disposalDTO.location()  ),
                "INSERT INTO " + tableName + " ( wasteId, method, instructions, location, lastUpdated ) VALUES ( ?, ?, ?, ?, CURRENT_TIMESTAMP )"
        );
    }

    /**
     * Updates an existing disposal record in the Disposal table.
     *
     * @param disposalDTO The DisposalDTO object containing the updated details.
     * @param id The ID of the disposal to update.
     * @return true if the disposal was successfully updated, false otherwise.
     */
    public boolean updateDisposal(DisposalDTO disposalDTO, Integer id ) {
        return updateRecord(
                tableName,
                List.of( disposalDTO.wasteId(), disposalDTO.method(), disposalDTO.instructions(), disposalDTO.location(), id ),
                "UPDATE " + tableName + " SET wasteId = ?, method = ?, instructions = ?, location = ?, lastUpdated = CURRENT_TIMESTAMP where id = ?"
        );
    }

    /**
     * Deletes a disposal record by its ID from the Disposal table.
     *
     * @param id The ID of the disposal to delete.
     */
    public void deleteDisposal( Integer id ) {
        delete( tableName, id );
    }
}
