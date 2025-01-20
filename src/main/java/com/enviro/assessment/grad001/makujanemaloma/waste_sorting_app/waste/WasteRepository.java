package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.BaseRepository;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.DisposalDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class WasteRepository extends BaseRepository<WasteDTO> {
    private final String tableName = "Waste";
    private final JdbcTemplate jdbcTemplate;

    public WasteRepository(JdbcClient jdbcClient, JdbcTemplate jdbcTemplate) {
        super( jdbcClient, WasteDTO.class );
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<WasteDTO> getAllWaste() {
        return getAll( tableName );
    }

    public Optional<WasteDTO> getWaste(Integer id ) {
        return getById( tableName, id );
    }

    public boolean insertNewWaste( WasteDTO wasteDTO) {
        return createRecord(
                tableName,
                List.of( wasteDTO.name(), wasteDTO.description(), wasteDTO.categoryId() ),
                "INSERT INTO " + tableName + " ( name, description, categoryId, lastUpdated ) VALUES ( ?, ?, ?, CURRENT_TIMESTAMP )"
        );
    }

    public boolean updateWaste(WasteDTO wasteDTO, Integer id ) {
        return updateRecord(
                tableName,
                List.of( wasteDTO.name(), wasteDTO.description(), wasteDTO.categoryId(), id ),
                "UPDATE " + tableName + " SET name = ?, description = ?, categoryId = ?, lastUpdated = CURRENT_TIMESTAMP WHERE id = ?"
        );
    }

    public void deleteWasteById( Integer id ) {
        delete( tableName, id );
    }

    private List<WasteOverviewDTO> sqlDataMapper(List<Map<String, Object>> rows ) {

        Map<Integer, WasteOverviewDTO> wasteMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            Integer id = ( Integer ) row.get( "wasteId" );
            String name = ( String ) row.get("wasteName");
            String description = ( String ) row.get( "wasteDescription" );
            String categoryName = ( String ) row.get( "categoryName" );


            DisposalDTO disposal = null;
            if ( row.get( "disposalId" ) != null ) {
                disposal = new DisposalDTO(
                        ( Integer ) row.get( "disposalId" ),
                        ( Integer ) row.get( "disposalWasteId" ),
                        ( String ) row.get( "disposalMethod" ),
                        ( String ) row.get( "disposalInstructions" ),
                        ( String ) row.get( "disposalLocation" ),
                        row.get("disposalLastUpdated" ) != null
                                ? ( ( Timestamp ) row.get( "disposalLastUpdated" ) ).toLocalDateTime()
                                : null
                );
            }

            WasteOverviewDTO waste = wasteMap.get( id );
            if ( waste == null ) {
                waste = new WasteOverviewDTO(
                        id,
                        name,
                        description,
                        categoryName,
                        new ArrayList<>()
                );
                wasteMap.put( id, waste );
            }

            if ( disposal != null ) {
                waste.disposals().add( disposal );
            }
        }

        return new ArrayList<>( wasteMap.values() );
    }

    public List<WasteOverviewDTO> getAllWasteWithDisposal(String categoryId ) {

        String sql = """
                SELECT w.id AS wasteId,
                       w.name AS wasteName,
                       w.description AS wasteDescription,
                       c.name AS categoryName,
                       d.id AS disposalId,
                       d.wasteId AS disposalWasteId,
                       d.method AS disposalMethod,
                       d.instructions AS disposalInstructions,
                       d.location AS disposalLocation,
                       d.lastUpdated AS disposalLastUpdated
                FROM Waste w
                LEFT JOIN Category c ON w.categoryId = c.id
                LEFT JOIN Disposal d ON w.id = d.wasteId
                WHERE ( ? IS NULL OR c.name = ? )
            """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, categoryId, categoryId);

        return sqlDataMapper( rows );
    }

    public Optional<WasteOverviewDTO> getWasteOverviewById( Integer wasteId ) {
        String sql = """
                SELECT w.id AS wasteId,
                       w.name AS wasteName,
                       w.description AS wasteDescription,
                       c.name AS categoryName,
                       d.id AS disposalId,
                       d.wasteId AS disposalWasteId,
                       d.method AS disposalMethod,
                       d.instructions AS disposalInstructions,
                       d.location AS disposalLocation,
                       d.lastUpdated AS disposalLastUpdated
                FROM Waste w
                LEFT JOIN Category c ON w.categoryId = c.id
                LEFT JOIN Disposal d ON w.id = d.wasteId
                WHERE w.id = ?
            """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, wasteId);
        if ( rows.isEmpty() ) {
            return Optional.empty();
        }
        return Optional.ofNullable( sqlDataMapper( rows ).get( 0 ) );
    }
}
