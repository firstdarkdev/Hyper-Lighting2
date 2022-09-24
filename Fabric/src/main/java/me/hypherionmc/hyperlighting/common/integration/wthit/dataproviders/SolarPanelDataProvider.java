package me.hypherionmc.hyperlighting.common.integration.wthit.dataproviders;

import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.IServerDataProvider;
import me.hypherionmc.hyperlighting.common.blockentities.SolarPanelBlockEntity;
import net.minecraft.nbt.CompoundTag;

/**
 * @author HypherionSA
 * @date 18/09/2022
 */
public class SolarPanelDataProvider implements IServerDataProvider<SolarPanelBlockEntity> {

    @Override
    public void appendServerData(CompoundTag data, IServerAccessor<SolarPanelBlockEntity> accessor, IPluginConfig config) {
        CompoundTag tag = new CompoundTag();
        accessor.getTarget().saveAdditional(tag);
        data.put("hl_solar", tag);
    }
}
