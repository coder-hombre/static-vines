package com.foogly.staticvines;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the VineTypeDetector utility class.
 * 
 * Tests only the description method that doesn't require Minecraft classes.
 */
class VineTypeDetectorTest {

    @Test
    @DisplayName("getVineTypeDescription() should return correct descriptions")
    void testGetVineTypeDescription() {
        assertEquals("Regular Vine", VineTypeDetector.getVineTypeDescription(VineType.REGULAR_VINE));
        assertEquals("Cave Vine (Main)", VineTypeDetector.getVineTypeDescription(VineType.CAVE_VINE));
        assertEquals("Cave Vine (Plant)", VineTypeDetector.getVineTypeDescription(VineType.CAVE_VINE_PLANT));
        assertEquals("Unknown", VineTypeDetector.getVineTypeDescription(null));
    }

    @Test
    @DisplayName("VineTypeDetector should handle all VineType enum values")
    void testAllVineTypeDescriptions() {
        for (VineType vineType : VineType.values()) {
            String description = VineTypeDetector.getVineTypeDescription(vineType);
            assertNotNull(description);
            assertFalse(description.isEmpty());
            assertNotEquals("Unknown", description);
        }
    }

    @Test
    @DisplayName("VineTypeDetector description method should be consistent")
    void testDescriptionConsistency() {
        String regularVineDesc1 = VineTypeDetector.getVineTypeDescription(VineType.REGULAR_VINE);
        String regularVineDesc2 = VineTypeDetector.getVineTypeDescription(VineType.REGULAR_VINE);
        assertEquals(regularVineDesc1, regularVineDesc2);
    }
}