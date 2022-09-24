package me.hypherionmc.hyperlighting.integration;

import me.hypherionmc.craterlib.platform.Platform;
import me.hypherionmc.hyperlighting.integration.shimmer.HyperLightingShimmer;

/**
 * @author HypherionSA
 * @date 07/08/2022
 */
public class HyperLightingIntegrations {

    public static void registerCommon() {

    }

    public static void registerClient() {
        if (Platform.LOADER.isModLoaded("shimmer")) {
            HyperLightingShimmer.registerAll();
        }
    }

}
