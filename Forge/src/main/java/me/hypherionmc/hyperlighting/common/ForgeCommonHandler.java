package me.hypherionmc.hyperlighting.common;

import me.hypherionmc.hyperlighting.common.entities.NeonFlyEntity;
import me.hypherionmc.hyperlighting.common.init.HLEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author HypherionSA
 * @date 05/08/2022
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeCommonHandler {

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(HLEntities.NEONFLY.get(), NeonFlyEntity.prepareAttributes().build());
    }

}
