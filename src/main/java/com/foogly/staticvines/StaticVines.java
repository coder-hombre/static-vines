package com.foogly.staticvines;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.foogly.staticvines.config.StaticVinesConfig;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

/**
 * Static Vines Mod - Prevents GrowingPlantHeadBlock growth and spreading
 * 
 * This mod intercepts block growth events to prevent things that extend GrowingPlantHeadBlock from naturally
 * growing and spreading while maintaining manual placement functionality.
 */
@Mod(StaticVines.MODID)
public class StaticVines {
    public static final String MODID = "staticvines";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * The constructor for the mod class is the first code that is run when your mod is loaded.
     * FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
     */
    public StaticVines(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register configuration
        modContainer.registerConfig(ModConfig.Type.COMMON, StaticVinesConfig.SPEC);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        LOGGER.info("Static Vines mod initialized - Hello?");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Static Vines mod common setup complete - Is Anyone There?");
    }

    /**
     * Called when the server starts - log configuration status
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Static Vines mod active on server - There you are!" );
    }
}
