package com.foogly.staticvines.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to disable random ticking for cave and weeping vine blocks
 */
@Mixin(GrowingPlantHeadBlock.class)
public class StaticVinesMixin {

    /**
     * Cancel random tick behavior for cave vine blocks
     */
    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    public void cancelRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "isRandomlyTicking", at = @At("HEAD"), cancellable = true)
    public void notRandomly(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}