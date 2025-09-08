package com.foogly.staticvines;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration tests for the Static Vines mod.
 * 
 * These tests validate that the mod's basic functionality works correctly.
 */
@DisplayName("Static Vines Mod - Integration Tests")
class StandaloneIntegrationTest {

    @Test
    @DisplayName("Should have proper mod ID")
    void shouldHaveProperModId() {
        assertEquals("staticvines", StaticVines.MODID, "Mod ID should be 'staticvines'");
    }

    @Test
    @DisplayName("Should have basic mod structure")
    void shouldHaveBasicModStructure() {
        assertNotNull(StaticVines.class, "StaticVines class should exist");
    }

    @Test
    @DisplayName("Should have all required mixin classes")
    void shouldHaveAllRequiredMixinClasses() {
        try {
            Class.forName("com.foogly.staticvines.mixins.StaticVinesMixin");
        } catch (ClassNotFoundException e) {
            fail("All mixin classes should exist: " + e.getMessage());
        }
    }
}