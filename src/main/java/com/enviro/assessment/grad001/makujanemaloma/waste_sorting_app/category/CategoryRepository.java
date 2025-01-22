package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository extends BaseRepository<CategoryDTO> {
    private final String tableName = "Category";

    public CategoryRepository(JdbcClient jdbcClient) {
        super( jdbcClient, CategoryDTO.class );
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
}
