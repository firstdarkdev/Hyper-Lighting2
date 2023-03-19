package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.api.inventory.CraterCreativeModeTab;
import me.hypherionmc.craterlib.network.CraterNetworkHandler;
import me.hypherionmc.craterlib.platform.Platform;
import me.hypherionmc.hyperlighting.client.config.HyperLightingClientConfig;
import me.hypherionmc.hyperlighting.integration.HyperLightingIntegrations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static me.hypherionmc.hyperlighting.Constants.MOD_ID;

public class CommonRegistration {

    public static HyperLightingClientConfig config = new HyperLightingClientConfig();
    public static final CraterCreativeModeTab LIGHTS_TAB = new CraterCreativeModeTab.Builder(new ResourceLocation(MOD_ID, "lighting")).setIcon(() -> new ItemStack(HLBlocks.ADVANCED_LANTERN)).build();
    public static final CraterCreativeModeTab MACHINES_TAB = new CraterCreativeModeTab.Builder(new ResourceLocation(MOD_ID, "machines")).setIcon(() -> new ItemStack(HLBlocks.ADVANCED_TORCH)).build();

    public static CraterNetworkHandler networkHandler = Platform.COMMON_HELPER.createPacketHandler(MOD_ID);

    public static void registerAll() {
        HLSounds.loadAll();
        HLParticles.loadAll();
        HLBlocks.loadAll();
        HLItems.loadAll();
        HLBlockEntities.loadAll();
        HLEntities.loadAll();
        HLContainers.loadAll();
        HyperLightingIntegrations.registerCommon();
        HLPackets.registerServer();
    }

}
