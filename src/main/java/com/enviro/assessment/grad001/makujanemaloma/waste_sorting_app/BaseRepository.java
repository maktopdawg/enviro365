package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T> {
    protected final JdbcClient jdbcClient;
    private final Class<T> entityClass;

    protected BaseRepository( JdbcClient jdbcClient, Class<T> entityClass ) {
        this.jdbcClient = jdbcClient;
        this.entityClass = entityClass;
    }

    public List<T> getAll( String tableName ) {
        return jdbcClient.sql( "SELECT * FROM " + tableName )
                .query( entityClass )
                .list();
    }

    public Optional<T> getById(String tableName, Integer id ) {
        return jdbcClient.sql( "SELECT * FROM " + tableName + " WHERE id = :id" )
                .param( "id", id )
                .query( entityClass )
                .optional();
    }

    public boolean createRecord( String tableName, List<Object> params, String sqlStatement ) {
        var updated = jdbcClient.sql( sqlStatement )
                .params( params )
                .update();
        return updated == 1;
    }

    public boolean updateRecord( String tableName, List<Object> params, String sqlStatement ) {
        var updated = jdbcClient.sql( sqlStatement )
                .params( params )
                .update();
        return updated == 1;
    }

    public void delete( String tableName, Integer id ) {
        var updated = jdbcClient.sql( "DELETE FROM " + tableName + " WHERE id = :id" )
                .param( "id", id )
                .update();
        Assert.state( updated == 1, "Failed to delete record with id: " + id + " in table: " + tableName );
    }
}
