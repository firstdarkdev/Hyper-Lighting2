package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.platform.Platform;
import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.containers.BatteryNeonContainer;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
public class HLContainers {

    public static RegistrationProvider<MenuType<?>> CONTAINERS = RegistrationProvider.get(Registry.MENU, Constants.MOD_ID);

    public static final RegistryObject<MenuType<BatteryNeonContainer>> BATTERY_NEON = register("battery_neon", Platform.COMMON_HELPER.createMenuType(BatteryNeonContainer::new));

    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String key, MenuType<T> builder) {
        return CONTAINERS.register(key, () -> builder);
    }

    public static void loadAll() {}
}
