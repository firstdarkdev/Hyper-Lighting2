package me.hypherionmc.hyperlighting.integration;

import me.hypherionmc.craterlib.platform.Services;
import me.hypherionmc.hyperlighting.integration.shimmer.HyperLightingShimmer;

/**
 * @author HypherionSA
 * @date 07/08/2022
 */
public class HyperLightingIntegrations {

    public static void registerCommon() {

    }

    public static void registerClient() {
        if (Services.PLATFORM.isModLoaded("shimmer")) {
            HyperLightingShimmer.registerAll();
        }
    }

}
