package me.hypherionmc.hyperlighting;

import me.hypherionmc.craterlib.client.gui.config.CraterConfigScreen;
import me.hypherionmc.hyperlighting.client.init.ClientRegistration;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class HyperLightingForge {

    public HyperLightingForge() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
        CommonRegistration.registerAll();
    }

    public void clientInit(FMLClientSetupEvent event) {
        new ClientRegistration().registerAll();
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new CraterConfigScreen(CommonRegistration.config, screen)));
    }
}
