package me.hypherionmc.hyperlighting.client.init;

import me.hypherionmc.craterlib.api.rendering.CustomRenderType;
import me.hypherionmc.craterlib.client.events.ColorRegistrationEvent;
import me.hypherionmc.craterlib.client.registry.ClientRegistry;
import me.hypherionmc.craterlib.events.CraterEventBus;
import me.hypherionmc.craterlib.platform.Services;
import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.integration.HyperLightingIntegrations;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

/**
 * @author HypherionSA
 * @date 17/06/2022
 */
public class ClientRegistration {

    public void registerAll() {
        Services.CLIENT_HELPER.registerCustomRenderTypes(HLBlocks.BLOCKS.getEntries(), HLItems.ITEMS.getEntries());
        HyperLightingIntegrations.registerClient();
    }

    public void registerEvents() {
        CraterEventBus.register(ColorRegistrationEvent.BLOCKS.class, this::registerBlockColors);
        CraterEventBus.register(ColorRegistrationEvent.ITEMS.class, this::registerItemColors);
    }

    public void registerBlockColors(ColorRegistrationEvent.BLOCKS event) {
        ClientRegistry.registerBlockColors(event.getColors(), HLBlocks.BLOCKS);
    }

    public void registerItemColors(ColorRegistrationEvent.ITEMS event) {
        ClientRegistry.registerItemColors(event.getColors(), HLItems.ITEMS);
    }

}
