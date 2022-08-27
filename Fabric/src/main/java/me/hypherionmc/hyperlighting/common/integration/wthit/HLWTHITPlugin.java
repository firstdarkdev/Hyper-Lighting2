package me.hypherionmc.hyperlighting.common.integration.wthit;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.blockentities.AdvancedCampfireBlockEntity;
import me.hypherionmc.hyperlighting.common.blocks.AdvancedCampfire;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class HLWTHITPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        Constants.LOG.info("Registering WTHIT Plugins");
        //registrar.addComponent(new FogMachineProvider(), TooltipPosition.BODY, FogMachineBlock.class);

        registrar.addBlockData(new CampfireDataProvider(), AdvancedCampfireBlockEntity.class);
        registrar.addComponent(new CampfireProvider(), TooltipPosition.BODY, AdvancedCampfire.class);
    }
}
