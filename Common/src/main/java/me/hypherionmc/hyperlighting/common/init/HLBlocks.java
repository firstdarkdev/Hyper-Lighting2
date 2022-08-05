package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.systems.reg.BlockRegistryObject;
import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.blocks.AdvancedTorchBlock;
import net.minecraft.core.Registry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class HLBlocks {

    public static RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registry.BLOCK_REGISTRY, Constants.MOD_ID);

    /* Torches */
    public static BlockRegistryObject<Block> ADVANCED_TORCH = register("advanced_torch", () -> new AdvancedTorchBlock("advanced_torch", DyeColor.ORANGE, CommonRegistration.LIGHTS_TAB));

    public static void loadAll() {}

    public static <B extends Block> BlockRegistryObject<B> register(String name, Supplier<? extends B> block) {
        final var ro = BLOCKS.<B>register(name, block);
        return BlockRegistryObject.wrap(ro);
    }
}
