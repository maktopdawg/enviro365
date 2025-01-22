package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.models.WasteWithTipsDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class CategoryRepository extends BaseRepository<CategoryDTO> {
    private final String tableName = "Category";
    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository( JdbcClient jdbcClient, JdbcTemplate jdbcTemplate ) {
        super( jdbcClient, CategoryDTO.class );
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CategoryDTO> getAllCategories() {
        return getAll( tableName );
    }

    public Optional<CategoryDTO> getCategoryById( Integer id ) {
        return getById( tableName, id );
    }

    public boolean createNewCategory( CategoryDTO categoryDTO) {
        return createRecord(
                tableName,
                List.of( categoryDTO.name().toLowerCase(), categoryDTO.description() ),
                "INSERT INTO " + tableName + " ( name, description, lastUpdated ) VALUES ( ?, ?, CURRENT_TIMESTAMP )"
        );
    }

    public boolean updateCategory(CategoryDTO categoryDTO, Integer id ) {
        return updateRecord(
                tableName,
                List.of( categoryDTO.name(), categoryDTO.description(), id ),
                "UPDATE " + tableName + " SET name = ?, description = ?, lastUpdated = CURRENT_TIMESTAMP WHERE id = ?"
        );
    }

    public void deleteCategoryById( Integer id ) {
        delete( tableName, id );
    }

    public List<CategoryWithTipsDTO> getCategoriesWithTips() {
        String sql = """
            SELECT c.id AS categoryId,
                   c.name AS categoryName,
                   c.description AS description,
                   rt.id AS recyclingTipId,
                   rt.title AS title,
                   rt.tip AS tip,
                   rt.categoryId AS categoryId,
                   rt.lastUpdated AS lastUpdated
            FROM Category c
            LEFT JOIN RecyclingTips rt ON c.id = rt.categoryId
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList( sql );

        return sqlDataMapperForTips(rows);
    }

    public Optional<CategoryWithTipsDTO> getCategoryWithTipsById( Integer wasteId ) {
        String sql = """
            SELECT c.id AS categoryId,
                   c.name AS categoryName,
                   c.description AS description,
                   rt.id AS recyclingTipId,
                   rt.title AS title,
                   rt.tip AS tip,
                   rt.categoryId AS categoryId,
                   rt.lastUpdated AS lastUpdated
            FROM Category c
            LEFT JOIN RecyclingTips rt ON c.id = rt.categoryId
            WHERE c.id = ?
        """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList( sql, wasteId );
        if ( rows.isEmpty() ) {
            return Optional.empty();
        }
        return Optional.of( sqlDataMapperForTips( rows ).get( 0 ) );
    }

    private List<CategoryWithTipsDTO> sqlDataMapperForTips(List<Map<String, Object>> rows ) {

        Map<Integer, CategoryWithTipsDTO> categoryMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Integer id = ( Integer ) row.get( "categoryId" );
            String name = ( String ) row.get("categoryName" );
            String description = ( String ) row.get( "description" );

            RecyclingTipDTO recycleTip = null;
            if ( row.get( "recyclingTipId" ) != null ) {
                recycleTip = new RecyclingTipDTO(
                        ( Integer ) row.get( "recyclingTipId" ),
                        ( String ) row.get( "title" ),
                        ( String ) row.get( "tip" ),
                        ( Integer ) row.get( "categoryId" ),
                        null,
                        row.get("lastUpdated" ) != null
                                ? ( ( Timestamp ) row.get( "lastUpdated" ) ).toLocalDateTime()
                                : null
                );
            }

            CategoryWithTipsDTO category = categoryMap.get( id );
            if ( category == null ) {
                category = new CategoryWithTipsDTO(
                        id,
                        name,
                        description,
                        new ArrayList<>()
                );
                categoryMap.put( id, category );
            }

            if ( recycleTip != null ) {
                category.recyclingTips().add( recycleTip );
            }
        }

        return new ArrayList<>( categoryMap.values() );
    }
}
