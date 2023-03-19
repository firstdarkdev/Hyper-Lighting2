package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.craterlib.platform.Platform;
import me.hypherionmc.craterlib.systems.internal.CreativeTabRegistry;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.minecraft.world.item.Item;

/**
 * @author HypherionSA
 * @date 25/09/2022
 */
public class CandleInAJar extends Item {

    private static final Properties shimmerLoaded = new Properties(); //.tab(CommonRegistration.LIGHTS_TAB);

    public CandleInAJar() {
        super(new Properties().stacksTo(1));
        if (Platform.LOADER.isModLoaded("shimmer")) {
            CreativeTabRegistry.setCreativeTab(CommonRegistration.LIGHTS_TAB, () -> this);
        }
    }

}
