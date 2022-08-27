package me.hypherionmc.hyperlighting.common.integration.top;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ITheOneProbePlugin;
import me.hypherionmc.hyperlighting.common.integration.top.overrides.TOPCampfireInfoProvider;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class TOPIntegration implements ITheOneProbePlugin {

    private static ITheOneProbe theOneProbe;

    @Override
    public void onLoad(ITheOneProbe apiInstance) {
        TOPIntegration.theOneProbe = apiInstance;

        theOneProbe.registerBlockDisplayOverride(new TOPCampfireInfoProvider());
    }

    public static IProbeConfig getProbeConfig() {
        return theOneProbe.createProbeConfig();
    }
}
