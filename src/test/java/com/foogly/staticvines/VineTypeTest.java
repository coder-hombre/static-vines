package com.foogly.staticvines;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the VineType enumeration.
 * 
 * Tests only the basic enum functionality without any Minecraft dependencies.
 */
class VineTypeTest {

    @Test
    @DisplayName("VineType should have correct block IDs")
    void testVineTypeBlockIds() {
        assertEquals("minecraft:vine", VineType.REGULAR_VINE.getBlockId());
        assertEquals("minecraft:cave_vines", VineType.CAVE_VINE.getBlockId());
        assertEquals("minecraft:cave_vines_plant", VineType.CAVE_VINE_PLANT.getBlockId());
    }

    @Test
    @DisplayName("VineType enum should exist and be accessible")
    void testVineTypeExists() {
        assertNotNull(VineType.REGULAR_VINE);
        assertNotNull(VineType.CAVE_VINE);
        assertNotNull(VineType.CAVE_VINE_PLANT);
    }

    @Test
    @DisplayName("VineType should have expected string representations")
    void testVineTypeStringRepresentation() {
        assertEquals("REGULAR_VINE", VineType.REGULAR_VINE.name());
        assertEquals("CAVE_VINE", VineType.CAVE_VINE.name());
        assertEquals("CAVE_VINE_PLANT", VineType.CAVE_VINE_PLANT.name());
    }

    @Test
    @DisplayName("All VineType values should be accessible")
    void testAllVineTypeValues() {
        VineType[] values = VineType.values();
        assertEquals(3, values.length);
    }

    @Test
    @DisplayName("VineType enum should maintain consistency")
    void testVineTypeConsistency() {
        for (VineType vineType : VineType.values()) {
            assertNotNull(vineType.getBlockId());
            assertTrue(vineType.getBlockId().startsWith("minecraft:"));
        }
    }
}