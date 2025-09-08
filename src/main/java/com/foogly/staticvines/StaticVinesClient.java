package com.foogly.staticvines;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
// No configuration screen imports needed

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = StaticVines.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = StaticVines.MODID, value = Dist.CLIENT)
public class StaticVinesClient {
    public StaticVinesClient(ModContainer container) {
        // No configuration screen needed - mod works automatically
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Client setup for Static Vines mod
        StaticVines.LOGGER.info("Static Vines client setup complete");
    }
}
