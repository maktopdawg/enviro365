package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The RecyclingTipRepository class provides methods for interacting with the recyclingTips table in the database.
 * It extends BaseRepository and contains CRUD operations for managing recycling tips.
 */
@Repository
public class RecyclingTipRepository extends BaseRepository<RecyclingTipDTO> {
    private final String tableName = "recyclingTips";

    /**
     * Constructs a RecyclingTipRepository with the provided JdbcClient.
     *
     * @param jdbcClient The JdbcClient instance used for executing SQL queries.
     */
    public RecyclingTipRepository( JdbcClient jdbcClient ) {
        super( jdbcClient, RecyclingTipDTO.class );
    }

    /**
     * Retrieves all recycling tips.
     *
     * @return A list of RecyclingTipDTO objects representing all recycling tips.
     */
    public List<RecyclingTipDTO> getAllRecyclingTips() {
        return getAll( tableName );
    }

    /**
     * Retrieves a recycling tip by its ID.
     *
     * @param id The ID of the recycling tip to retrieve.
     * @return An Optional containing the RecyclingTipDTO if found, otherwise empty.
     */
    public Optional<RecyclingTipDTO> getRecyclingTipById(Integer id ) {
        return getById( tableName, id );
    }

    /**
     * Creates a new recycling tip record in the database.
     *
     * @param recyclingTipDTO The RecyclingTipDTO object containing the details of the new recycling tip.
     * @return A boolean indicating whether the record was successfully created.
     * @throws IllegalArgumentException if the title or tip is null.
     */
    public boolean createNewRecyclingTip(RecyclingTipDTO recyclingTipDTO) {
        if (recyclingTipDTO.title() == null || recyclingTipDTO.tip() == null) {
            throw new IllegalArgumentException("Title and Tip cannot be null");
        }

        List<Object> params = Arrays.asList(
                recyclingTipDTO.title(),
                recyclingTipDTO.tip(),
                recyclingTipDTO.categoryId(),
                recyclingTipDTO.wasteId()
        );

        String sql = "INSERT INTO " + tableName + " (title, tip, categoryId, wasteId, lastUpdated) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        return createRecord(tableName, params, sql);
    }

    /**
     * Updates an existing recycling tip record in the database.
     *
     * @param recyclingTipDTO The RecyclingTipDTO object containing the updated details.
     * @param id The ID of the recycling tip to update.
     * @return A boolean indicating whether the record was successfully updated.
     */
    public boolean updateRecyclingTip( RecyclingTipDTO recyclingTipDTO, Integer id ) {
        List<Object> params = Arrays.asList(
                recyclingTipDTO.title(),
                recyclingTipDTO.tip(),
                recyclingTipDTO.categoryId(),
                recyclingTipDTO.wasteId(),
                id
        );
        return updateRecord(
                tableName,
                params,
                "UPDATE " + tableName + " SET  title = ?, tip = ?, categoryId = ?, wasteId = ?, lastUpdated = CURRENT_TIMESTAMP where id = ?"
        );
    }

    /**
     * Deletes a recycling tip record by its ID.
     *
     * @param id The ID of the recycling tip to delete.
     */
    public void deleteRecyclingTip( Integer id ) {
        delete( tableName, id );
    }
}
