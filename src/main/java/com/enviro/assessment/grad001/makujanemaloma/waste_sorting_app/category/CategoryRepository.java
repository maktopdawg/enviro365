package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipDTO;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.models.WasteWithTipsDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

/**
 * The CategoryRepository class provides methods to interact with the Category table in the database.
 * It extends the BaseRepository class to perform CRUD operations on Category data.
 */
@Repository
public class CategoryRepository extends BaseRepository<CategoryDTO> {
    private final String tableName = "Category";
    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a CategoryRepository with the provided JdbcClient and JdbcTemplate.
     *
     * @param jdbcClient The JdbcClient instance used for executing SQL queries.
     * @param jdbcTemplate The JdbcTemplate used for querying the database.
     */
    public CategoryRepository( JdbcClient jdbcClient, JdbcTemplate jdbcTemplate ) {
        super( jdbcClient, CategoryDTO.class );
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all categories from the Category table.
     *
     * @return A list of CategoryDTO objects representing all categories.
     */
    public List<CategoryDTO> getAllCategories() {
        return getAll( tableName );
    }

    /**
     * Retrieves a category by its ID from the Category table.
     *
     * @param id The ID of the category to retrieve.
     * @return An Optional containing the CategoryDTO if found, otherwise an empty Optional.
     */
    public Optional<CategoryDTO> getCategoryById( Integer id ) {
        return getById( tableName, id );
    }

    /**
     * Creates a new category record in the Category table.
     *
     * @param categoryDTO The CategoryDTO object containing the details of the category to create.
     * @return true if the category was successfully created, false otherwise.
     */
    public boolean createNewCategory( CategoryDTO categoryDTO) {
        return createRecord(
                tableName,
                List.of( categoryDTO.name().toLowerCase(), categoryDTO.description() ),
                "INSERT INTO " + tableName + " ( name, description, lastUpdated ) VALUES ( ?, ?, CURRENT_TIMESTAMP )"
        );
    }

    /**
     * Updates an existing category record in the Category table.
     *
     * @param categoryDTO The CategoryDTO object containing the updated details.
     * @param id The ID of the category to update.
     * @return true if the category was successfully updated, false otherwise.
     */
    public boolean updateCategory(CategoryDTO categoryDTO, Integer id ) {
        return updateRecord(
                tableName,
                List.of( categoryDTO.name(), categoryDTO.description(), id ),
                "UPDATE " + tableName + " SET name = ?, description = ?, lastUpdated = CURRENT_TIMESTAMP WHERE id = ?"
        );
    }

    /**
     * Deletes a category by its ID from the Category table.
     *
     * @param id The ID of the category to delete.
     */
    public void deleteCategoryById( Integer id ) {
        delete( tableName, id );
    }

    /**
     * Retrieves a list of categories with their associated recycling tips.
     *
     * @return A list of CategoryWithTipsDTO objects, each containing a category and its recycling tips.
     */
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

    /**
     * Retrieves a specific category with its associated recycling tips by the category ID.
     *
     * @param wasteId The ID of the category to retrieve.
     * @return An Optional containing a CategoryWithTipsDTO with the category and its tips, or an empty Optional if not found.
     */
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

    /**
     * Maps SQL result rows to a list of CategoryWithTipsDTO objects.
     *
     * @param rows A list of maps representing SQL result rows.
     * @return A list of CategoryWithTipsDTO objects, each containing a category and its recycling tips.
     */
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
