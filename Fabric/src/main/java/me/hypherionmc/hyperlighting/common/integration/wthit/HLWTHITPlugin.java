package me.hypherionmc.hyperlighting.common.integration.wthit;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.blockentities.AdvancedCampfireBlockEntity;
import me.hypherionmc.hyperlighting.common.blockentities.SolarPanelBlockEntity;
import me.hypherionmc.hyperlighting.common.blocks.AdvancedCampfire;
import me.hypherionmc.hyperlighting.common.blocks.SolarPanel;
import me.hypherionmc.hyperlighting.common.integration.wthit.dataproviders.CampfireDataProvider;
import me.hypherionmc.hyperlighting.common.integration.wthit.dataproviders.SolarPanelDataProvider;
import me.hypherionmc.hyperlighting.common.integration.wthit.providers.CampfireProvider;
import me.hypherionmc.hyperlighting.common.integration.wthit.providers.SolarPanelProvider;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class HLWTHITPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        Constants.LOG.info("Registering WTHIT Plugins");
        //registrar.addComponent(new FogMachineProvider(), TooltipPosition.BODY, FogMachineBlock.class);

        // Campfire
        registrar.addBlockData(new CampfireDataProvider(), AdvancedCampfireBlockEntity.class);
        registrar.addComponent(new CampfireProvider(), TooltipPosition.BODY, AdvancedCampfire.class);

        // Solar Panel
        registrar.addBlockData(new SolarPanelDataProvider(), SolarPanelBlockEntity.class);
        registrar.addComponent(new SolarPanelProvider(), TooltipPosition.BODY, SolarPanel.class);
    }
}
