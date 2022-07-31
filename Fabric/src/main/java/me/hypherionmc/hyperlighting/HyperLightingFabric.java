package me.hypherionmc.hyperlighting;

import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import net.fabricmc.api.ModInitializer;

public class HyperLightingFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonRegistration.registerAll();
    }
}
