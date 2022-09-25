package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.craterlib.platform.Platform;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import net.minecraft.world.item.Item;

/**
 * @author HypherionSA
 * @date 25/09/2022
 */
public class CandleInAJar extends Item {

    private static final Properties shimmerLoaded = new Properties().tab(CommonRegistration.LIGHTS_TAB);

    public CandleInAJar() {
        super(Platform.LOADER.isModLoaded("shimmer") ? shimmerLoaded : new Properties());
    }

}
