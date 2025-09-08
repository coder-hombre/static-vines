package com.foogly.staticvines;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

/**
 * Static Vines Mod - Prevents vine and cave vine growth and spreading
 * 
 * This mod intercepts block growth events to prevent vines from naturally
 * growing and spreading while maintaining manual placement functionality.
 */
@Mod(StaticVines.MODID)
public class StaticVines {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "staticvines";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * The constructor for the mod class is the first code that is run when your mod is loaded.
     * FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
     */
    public StaticVines(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        // Vine growth prevention is handled by mixins

        // No configuration needed - mixins handle everything
        
        LOGGER.info("Static Vines mod initialized - vine and cave vine random ticking disabled via mixins");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Common setup code for vine growth prevention
        LOGGER.info("Static Vines mod common setup complete");
        LOGGER.info("Random ticking disabled for vines and cave vines");
    }

    /**
     * Called when the server starts - log configuration status
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Static Vines mod active on server - vine and cave vine random ticking disabled");
    }
}
