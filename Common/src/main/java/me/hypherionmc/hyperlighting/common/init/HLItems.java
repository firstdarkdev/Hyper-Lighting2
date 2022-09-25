package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.items.CandleInAJar;
import me.hypherionmc.hyperlighting.common.items.LighterTool;
import me.hypherionmc.hyperlighting.common.items.WirelessBattery;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class HLItems {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM_REGISTRY, Constants.MOD_ID);

    /* Tools */
    public static RegistryObject<Item> TORCH_TOOL = register("lighter_tool", LighterTool::new);

    /* Machines */
    public static RegistryObject<Item> WIRELESS_BATTERY = register("wireless_battery", WirelessBattery::new);

    /* Lights */
    public static RegistryObject<Item> CANDLE_IN_A_JAR = register("candle_jar", CandleInAJar::new);

    public static void loadAll() {}

    public static <T extends Item> RegistryObject<T> register(String name, Supplier<? extends T> item) {
        return ITEMS.register(name, item);
    }

}
