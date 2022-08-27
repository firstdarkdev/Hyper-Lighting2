package me.hypherionmc.hyperlighting;

import me.hypherionmc.craterlib.client.gui.config.CraterConfigScreen;
import me.hypherionmc.hyperlighting.client.init.ClientRegistration;
import me.hypherionmc.hyperlighting.client.renderer.blockentity.AdvancedCampfireRenderer;
import me.hypherionmc.hyperlighting.client.renderer.entity.NeonFlyRenderer;
import me.hypherionmc.hyperlighting.common.entities.NeonFlyEntity;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import me.hypherionmc.hyperlighting.common.init.HLEntities;
import me.hypherionmc.hyperlighting.common.integration.top.TOPIntegration;
import me.hypherionmc.hyperlighting.common.worldgen.ForgeWorldGen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class HyperLightingForge {

    private ClientRegistration registration = new ClientRegistration();

    public HyperLightingForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
        CommonRegistration.registerAll();

        if (ModList.get().isLoaded(Constants.THE_ONE_PROBE)) {
            new TOPIntegration().setup();
        }

        ForgeWorldGen.registerAll(FMLJavaModLoadingContext.get().getModEventBus());
        registration.registerEvents();
    }

    public void clientInit(FMLClientSetupEvent event) {
        registration.registerAll();

        BlockEntityRenderers.register(HLBlockEntities.CAMPFIRE.get(), AdvancedCampfireRenderer::new);
        EntityRenderers.register(HLEntities.NEONFLY.get(), NeonFlyRenderer::new);
    }

    public void commonInit(FMLCommonSetupEvent event) {
        SpawnPlacements.register(HLEntities.NEONFLY.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.WORLD_SURFACE, NeonFlyEntity::canSpawn);
    }
}
