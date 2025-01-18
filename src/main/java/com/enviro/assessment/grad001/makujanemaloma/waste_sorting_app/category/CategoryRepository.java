package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {
    private static final Logger log = LoggerFactory.getLogger(CategoryRepository.class);
    private final JdbcClient jdbcClient;

    public CategoryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Category> getAllCategories() {
        return jdbcClient.sql( "SELECT * FROM Category" )
                .query( Category.class )
                .list();
    }

    public Optional<Category> getCategoryById( Integer id ) {
        return jdbcClient.sql( "SELECT id, name, description, lastUpdated FROM Category WHERE id = :id" )
                .param( "id", id )
                .query( Category.class )
                .optional();
    }

    public void createNewCategory( Category category ) {
        var updated = jdbcClient.sql( "INSERT INTO Category ( name, description, lastUpdated ) VALUES ( ?, ?, CURRENT_TIMESTAMP )" )
                .params( List.of( category.name(), category.description() ) )
                .update();

        Assert.state( updated == 1, "Failed to create new category" + category.name() );
    }

    public void updateCategory( Category category, Integer id ) {
        var updated = jdbcClient.sql( "UPDATE Category set name = ?, description = ?, lastUpdated = CURRENT_TIMESTAMP WHERE id = ?" )
                .params( List.of( category.name(), category.description(), id ) )
                .update();

        Assert.state( updated == 1, "Failed to update category" + category.name() );
    }

    public void deleteCategoryById( Integer id ) {
        var updated = jdbcClient.sql( "DELETE FROM Category WHERE id = :id" )
                .param( "id", id )
                .update();

        Assert.state( updated == 1, "Failed to delete category" + id );
    }
}
