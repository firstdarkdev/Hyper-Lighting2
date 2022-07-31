package me.hypherionmc.hyperlighting.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface LightableBlock {

    /**
     * Allows Torch Lighter tool to cycle the state of a clicked block
     * @param worldIn
     * @param state
     * @param pos
     */
    void toggleLight(Level worldIn, BlockState state, BlockPos pos);

}
