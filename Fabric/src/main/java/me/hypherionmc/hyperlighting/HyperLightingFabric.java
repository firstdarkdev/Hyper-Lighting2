package me.hypherionmc.hyperlighting;

import me.hypherionmc.hyperlighting.common.entities.NeonFlyEntity;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLEntities;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class HyperLightingFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonRegistration.registerAll();

        FabricDefaultAttributeRegistry.register(HLEntities.FIREFLY.get(), NeonFlyEntity.prepareAttributes());
    }
}
