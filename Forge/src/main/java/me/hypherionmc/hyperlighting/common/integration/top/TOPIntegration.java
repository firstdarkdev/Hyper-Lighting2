package me.hypherionmc.hyperlighting.common.integration.top;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ITheOneProbe;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.integration.top.overrides.TOPCampfireInfoProvider;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Function;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class TOPIntegration implements Function<ITheOneProbe, Void> {

    private static ITheOneProbe theOneProbe;

    public void setup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendIMC);
    }

    public void sendIMC(InterModEnqueueEvent event) {
        Constants.LOG.info("Registering TOP integration");
        InterModComms.sendTo(Constants.THE_ONE_PROBE, "getTheOneProbe", TOPIntegration::new);
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        TOPIntegration.theOneProbe = theOneProbe;

        TOPCampfireInfoProvider topCampfireInfoProvider = new TOPCampfireInfoProvider();
        theOneProbe.registerBlockDisplayOverride(topCampfireInfoProvider);
        return null;
    }

    public static IProbeConfig getProbeConfig() {
        return theOneProbe.createProbeConfig();
    }
}
