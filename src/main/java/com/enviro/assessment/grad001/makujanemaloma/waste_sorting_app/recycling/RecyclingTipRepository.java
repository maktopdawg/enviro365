package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class RecyclingTipRepository extends BaseRepository<RecyclingTipDTO> {
    private final String tableName = "recyclingTips";

    public RecyclingTipRepository( JdbcClient jdbcClient ) {
        super( jdbcClient, RecyclingTipDTO.class );
    }

    public List<RecyclingTipDTO> getAllRecyclingTips() {
        return getAll( tableName );
    }

    public Optional<RecyclingTipDTO> getRecyclingTipById(Integer id ) {
        return getById( tableName, id );
    }

    public boolean createNewRecyclingTip(RecyclingTipDTO recyclingTipDTO) {
        if (recyclingTipDTO.title() == null || recyclingTipDTO.tip() == null) {
            throw new IllegalArgumentException("Title and Tip cannot be null");
        }

        // Use Arrays.asList to handle potential null values
        List<Object> params = Arrays.asList(
                recyclingTipDTO.title(),
                recyclingTipDTO.tip(),
                recyclingTipDTO.categoryId(),
                recyclingTipDTO.wasteId()
        );

        String sql = "INSERT INTO " + tableName + " (title, tip, categoryId, wasteId, lastUpdated) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        return createRecord(tableName, params, sql);
    }


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

    public void deleteRecyclingTip( Integer id ) {
        delete( tableName, id );
    }
}
