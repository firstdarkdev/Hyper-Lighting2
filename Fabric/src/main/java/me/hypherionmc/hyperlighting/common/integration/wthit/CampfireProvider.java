package me.hypherionmc.hyperlighting.common.integration.wthit;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class CampfireProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        CompoundTag compound = accessor.getServerData().getCompound("hl_campfire");
        int[] cookingTimes = new int[4];
        int[] cookingTotalTimes = new int[4];
        NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

        inventory.clear();
        ContainerHelper.loadAllItems(compound, inventory);
        if (compound.contains("CookingTimes", 11)) {
            int[] aint = compound.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, cookingTimes, 0, Math.min(cookingTotalTimes.length, aint.length));
        }

        if (compound.contains("CookingTotalTimes", 11)) {
            int[] aint1 = compound.getIntArray("CookingTotalTimes");
            System.arraycopy(aint1, 0, cookingTotalTimes, 0, Math.min(cookingTotalTimes.length, aint1.length));
        }

        if (inventory.isEmpty()) {
            tooltip.addLine(Component.literal(ChatFormatting.RED + "Empty"));
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                if (!inventory.get(i).isEmpty()) {
                    int progress = (int) ((float) cookingTimes[i] / cookingTotalTimes[i] * 100);
                    tooltip.addLine(Component.literal(inventory.get(i).getDisplayName().getString() + " : " + ChatFormatting.YELLOW + progress + "%"));
                }
            }
        }
    }
}
