package me.hypherionmc.hyperlighting.common.integration.wthit.dataproviders;

import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.IServerDataProvider;
import me.hypherionmc.hyperlighting.common.blockentities.AdvancedCampfireBlockEntity;
import net.minecraft.nbt.CompoundTag;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class CampfireDataProvider implements IServerDataProvider<AdvancedCampfireBlockEntity> {

    @Override
    public void appendServerData(CompoundTag data, IServerAccessor<AdvancedCampfireBlockEntity> accessor, IPluginConfig config) {
        CompoundTag tag = new CompoundTag();
        accessor.getTarget().saveAdditional(tag);
        data.put("hl_campfire", tag);
    }

}
