package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.client.gui.tabs.CreativeTabBuilder;
import me.hypherionmc.hyperlighting.client.config.HyperLightingClientConfig;
import me.hypherionmc.hyperlighting.integration.HyperLightingIntegrations;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static me.hypherionmc.hyperlighting.Constants.MOD_ID;

public class CommonRegistration {

    public static HyperLightingClientConfig config = new HyperLightingClientConfig();
    public static final CreativeModeTab LIGHTS_TAB = CreativeTabBuilder.builder(MOD_ID, "lighting").setIcon(() -> new ItemStack(HLBlocks.ADVANCED_LANTERN)).build();
    public static final CreativeModeTab MACHINES_TAB = CreativeTabBuilder.builder(MOD_ID, "machines").setIcon(() -> new ItemStack(HLBlocks.ADVANCED_TORCH)).build();


    public static void registerAll() {
        HLSounds.loadAll();
        HLParticles.loadAll();
        HLBlocks.loadAll();
        HLItems.loadAll();
        HLBlockEntities.loadAll();
        HLEntities.loadAll();
        HyperLightingIntegrations.registerCommon();
    }

}
