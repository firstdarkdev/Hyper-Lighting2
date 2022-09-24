package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.systems.reg.BlockRegistryObject;
import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.blocks.*;
import net.minecraft.core.Registry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class HLBlocks {

    public static RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registry.BLOCK_REGISTRY, Constants.MOD_ID);

    /* Torches */
    public static BlockRegistryObject<Block> ADVANCED_TORCH = register("advanced_torch", () -> new AdvancedTorchBlock("advanced_torch", DyeColor.ORANGE, CommonRegistration.LIGHTS_TAB));

    /* Lanterns */
    public static BlockRegistryObject<Block> ADVANCED_LANTERN = register("advanced_lantern", () -> new AdvancedLanternBlock("advanced_lantern", DyeColor.ORANGE, CommonRegistration.LIGHTS_TAB));

    /* CampFires */
    public static BlockRegistryObject<Block> ADVANCED_CAMPFIRE = register("advanced_campfire", () -> new AdvancedCampfire("advanced_campfire", DyeColor.ORANGE, CommonRegistration.LIGHTS_TAB));


    /* Candles */
    public static BlockRegistryObject<Block> ADVANCED_CANDLE = register("advanced_candle", () -> new AdvancedCandleBlock("advanced_candle", DyeColor.ORANGE, CommonRegistration.LIGHTS_TAB));

    /* Machines */
    public static BlockRegistryObject<Block> SOLAR_PANEL = register("solar_panel", () -> new SolarPanel("solar_panel"));


    public static void loadAll() {}

    public static <B extends Block> BlockRegistryObject<B> register(String name, Supplier<? extends B> block) {
        final var ro = BLOCKS.<B>register(name, block);
        return BlockRegistryObject.wrap(ro);
    }
}
