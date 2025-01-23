package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * BaseRepository is an abstract class providing basic CRUD (Create, Read, Update, Delete)
 * operations for interacting with a relational database using Spring's JdbcClient.
 * This class is intended to be extended by specific repository classes for different entities.
 *
 * @param <T> The type of entity the repository will handle.
 */
public abstract class BaseRepository<T> {
    protected final JdbcClient jdbcClient;
    private final Class<T> entityClass;

    /**
     * Constructs a BaseRepository with the specified JdbcClient and entity class.
     *
     * @param jdbcClient The JdbcClient instance used for executing SQL queries.
     * @param entityClass The Class type of the entity the repository manages.
     */
    protected BaseRepository( JdbcClient jdbcClient, Class<T> entityClass ) {
        this.jdbcClient = jdbcClient;
        this.entityClass = entityClass;
    }

    /**
     * Fetches all records from the specified table and returns them as a list of entities.
     *
     * @param tableName The name of the table to query.
     * @return A list of entities of type T representing the records in the table.
     */
    public List<T> getAll( String tableName ) {
        return jdbcClient.sql( "SELECT * FROM " + tableName )
                .query( entityClass )
                .list();
    }

    /**
     * Fetches a single record by its ID from the specified table.
     *
     * @param tableName The name of the table to query.
     * @param id The ID of the record to fetch.
     * @return An Optional containing the entity of type T if found, otherwise an empty Optional.
     */
    public Optional<T> getById(String tableName, Integer id ) {
        return jdbcClient.sql( "SELECT * FROM " + tableName + " WHERE id = :id" )
                .param( "id", id )
                .query( entityClass )
                .optional();
    }

    /**
     * Creates a new record in the specified table using the provided SQL statement and parameters.
     *
     * @param tableName The name of the table where the record will be inserted.
     * @param params The parameters to be used in the SQL INSERT statement.
     * @param sqlStatement The SQL INSERT statement to execute.
     * @return true if the record was successfully created (one row affected), false otherwise.
     */
    public boolean createRecord( String tableName, List<Object> params, String sqlStatement ) {
        var updated = jdbcClient.sql( sqlStatement )
                .params( params )
                .update();
        return updated == 1;
    }

    /**
     * Updates an existing record in the specified table using the provided SQL statement and parameters.
     *
     * @param tableName The name of the table where the record will be updated.
     * @param params The parameters to be used in the SQL UPDATE statement.
     * @param sqlStatement The SQL UPDATE statement to execute.
     * @return true if the record was successfully updated (one row affected), false otherwise.
     */
    public boolean updateRecord( String tableName, List<Object> params, String sqlStatement ) {
        var updated = jdbcClient.sql( sqlStatement )
                .params( params )
                .update();
        return updated == 1;
    }

    /**
     * Deletes a record from the specified table by its ID.
     *
     * @param tableName The name of the table where the record will be deleted.
     * @param id The ID of the record to delete.
     * @throws IllegalStateException if the delete operation does not affect exactly one row.
     */
    public void delete( String tableName, Integer id ) {
        var updated = jdbcClient.sql( "DELETE FROM " + tableName + " WHERE id = :id" )
                .param( "id", id )
                .update();
        Assert.state( updated == 1, "Failed to delete record with id: " + id + " in table: " + tableName );
    }
}
