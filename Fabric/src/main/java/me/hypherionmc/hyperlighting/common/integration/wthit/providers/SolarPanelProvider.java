package me.hypherionmc.hyperlighting.common.integration.wthit.providers;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

/**
 * @author HypherionSA
 * @date 18/09/2022
 */
public class SolarPanelProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        CompoundTag compound = accessor.getServerData().getCompound("hl_solar");
        int powerLevel = compound.getInt("powerLevel");
        int level = (int) (((float) powerLevel / 2000) * 100);
        tooltip.addLine(Component.literal("Power Level : " + ChatFormatting.YELLOW + level + "%"));
    }
}
