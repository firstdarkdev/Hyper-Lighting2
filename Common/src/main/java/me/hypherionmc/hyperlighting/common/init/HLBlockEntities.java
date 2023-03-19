package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.blockentities.AdvancedCampfireBlockEntity;
import me.hypherionmc.hyperlighting.common.blockentities.BatteryNeonBlockEntity;
import me.hypherionmc.hyperlighting.common.blockentities.SolarPanelBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class HLBlockEntities {

    public static final RegistrationProvider<BlockEntityType<?>> BE = RegistrationProvider.get(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static RegistryObject<BlockEntityType<AdvancedCampfireBlockEntity>> CAMPFIRE = BE.register("campfire", () -> BlockEntityType.Builder.of(AdvancedCampfireBlockEntity::new, HLBlocks.ADVANCED_CAMPFIRE.get()).build(null));

    public static RegistryObject<BlockEntityType<SolarPanelBlockEntity>> SOLAR_PANEL = BE.register("solar_panel", () -> BlockEntityType.Builder.of(SolarPanelBlockEntity::new, HLBlocks.SOLAR_PANEL.get()).build(null));

    public static RegistryObject<BlockEntityType<BatteryNeonBlockEntity>> BATTERY_NEON = BE.register("battery_neon", () -> BlockEntityType.Builder.of(BatteryNeonBlockEntity::new, HLBlocks.BATTERY_NEON.get()).build(null));

    public static void loadAll() {}
}
