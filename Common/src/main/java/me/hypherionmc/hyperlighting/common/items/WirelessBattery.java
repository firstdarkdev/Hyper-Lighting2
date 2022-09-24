package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.api.SwitchModule;
import me.hypherionmc.hyperlighting.common.blocks.SolarPanel;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
public class WirelessBattery extends Item implements SwitchModule {

    public WirelessBattery() {
        super(new Properties().tab(CommonRegistration.MACHINES_TAB).stacksTo(1));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();

        if (!level.isClientSide()) {
            if (player.getItemInHand(hand).getItem() instanceof WirelessBattery) {
                ItemStack stack = player.getItemInHand(hand);
                CompoundTag tag = stack.getOrCreateTag();
                tag.put("linked_pos", NbtUtils.writeBlockPos(pos));
                stack.setTag(tag);
                player.displayClientMessage(Component.literal("Linked to " + pos), false);
            } else {
                ItemStack stack = player.getItemInHand(hand);
                if (isLinked(stack, level)) {
                    player.displayClientMessage(Component.literal("Linked to block " + getLinkedPos(stack)), false);
                } else {
                    player.displayClientMessage(Component.literal("Not linked"), false);
                }
                return InteractionResult.PASS;
            }
        }

        return InteractionResult.SUCCESS;
    }

    public boolean isLinked(ItemStack stack, Level level) {
        CompoundTag compound = stack.getOrCreateTag();
        if (compound.getCompound("linked_pos") != null && stack.getItem() instanceof WirelessBattery) {
            BlockPos pos = getLinkedPos(stack);
            return level.getBlockState(pos).getBlock() instanceof SolarPanel;
        } else {
            return false;
        }
    }

    public BlockPos getLinkedPos(ItemStack stack) {
        CompoundTag compound = stack.getOrCreateTag();
        return NbtUtils.readBlockPos(compound.getCompound("linked_pos"));
    }
}
