package me.hypherionmc.hyperlighting.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * @author HypherionSA
 * @date 01/07/2022
 */

// TODO: Move this into CraterLib. This should not be here
public class StackUtil {

    public static ItemStack getColorStack(Block block, DyeColor color) {
        ItemStack stack = new ItemStack(block);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("color", color.getName());
        stack.setTag(tag);
        return stack;
    }

}
