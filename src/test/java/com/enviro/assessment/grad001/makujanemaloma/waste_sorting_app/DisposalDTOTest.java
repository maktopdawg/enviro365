package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.DisposalDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DisposalDTOTest {

    @Test
    void testDisposalDTOInitialization() {
        // Arrange
        LocalDateTime lastUpdated = LocalDateTime.now();

        DisposalDTO disposal = new DisposalDTO(
                1,
                101,
                "Recycling Center",
                "Drop off at the nearest recycling center",
                "123 Green Street, Eco City",
                lastUpdated
        );

        // Act & Assert
        assertNotNull(disposal, "DisposalDTO should not be null");
        assertEquals(1, disposal.id(), "Disposal ID should be 1");
        assertEquals(101, disposal.wasteId(), "Waste ID should be 101");
        assertEquals("Recycling Center", disposal.method(), "Disposal method should match");
        assertEquals("Drop off at the nearest recycling center", disposal.instructions(), "Disposal instructions should match");
        assertEquals("123 Green Street, Eco City", disposal.location(), "Disposal location should match");
        assertEquals(lastUpdated, disposal.lastUpdated(), "Last updated timestamp should match");
    }


    @Test
    void testDisposalDTOEqualityAndHashCode() {
        // Arrange
        LocalDateTime lastUpdated = LocalDateTime.now();

        DisposalDTO disposal1 = new DisposalDTO(
                1,
                101,
                "Recycling Center",
                "Drop off at the nearest recycling center",
                "123 Green Street, Eco City",
                lastUpdated
        );

        DisposalDTO disposal2 = new DisposalDTO(
                1,
                101,
                "Recycling Center",
                "Drop off at the nearest recycling center",
                "123 Green Street, Eco City",
                lastUpdated
        );

        // Act & Assert
        assertEquals(disposal1, disposal2, "DisposalDTO objects with the same properties should be equal");
        assertEquals(disposal1.hashCode(), disposal2.hashCode(), "Hash codes should match for equal objects");
    }

    @Test
    void testDisposalDTOWithEmptyStrings() {
        // Arrange
        LocalDateTime lastUpdated = LocalDateTime.now();

        DisposalDTO disposal = new DisposalDTO(
                2,
                102,
                "",
                "",
                "",
                lastUpdated
        );

        // Act & Assert
        assertNotNull(disposal, "DisposalDTO should not be null");
        assertEquals(2, disposal.id(), "Disposal ID should be 2");
        assertEquals(102, disposal.wasteId(), "Waste ID should be 102");
        assertEquals("", disposal.method(), "Disposal method should be an empty string");
        assertEquals("", disposal.instructions(), "Disposal instructions should be an empty string");
        assertEquals("", disposal.location(), "Disposal location should be an empty string");
        assertEquals(lastUpdated, disposal.lastUpdated(), "Last updated timestamp should match");
    }

    @Test
    void testDisposalDTOWithNullValues() {
        // Arrange
        LocalDateTime lastUpdated = LocalDateTime.now();

        DisposalDTO disposal = new DisposalDTO(
                3,
                103,
                null,
                null,
                null,
                lastUpdated
        );

        // Act & Assert
        assertNotNull(disposal, "DisposalDTO should not be null");
        assertEquals(3, disposal.id(), "Disposal ID should be 3");
        assertEquals(103, disposal.wasteId(), "Waste ID should be 103");
        assertNull(disposal.method(), "Disposal method should be null");
        assertNull(disposal.instructions(), "Disposal instructions should be null");
        assertNull(disposal.location(), "Disposal location should be null");
        assertEquals(lastUpdated, disposal.lastUpdated(), "Last updated timestamp should match");
    }

    @Test
    void testDisposalDTOInequality() {
        // Arrange
        LocalDateTime lastUpdated = LocalDateTime.now();

        DisposalDTO disposal1 = new DisposalDTO(
                4,
                104,
                "Composting",
                "Compost organic waste",
                "456 Eco Lane, Green Town",
                lastUpdated
        );

        DisposalDTO disposal2 = new DisposalDTO(
                5,
                105,
                "Landfill",
                "Dispose of at authorized landfill",
                "789 Earth Drive, Sustainable City",
                lastUpdated
        );

        // Act & Assert
        assertNotEquals(disposal1, disposal2, "DisposalDTO objects with different properties should not be equal");
    }

}
