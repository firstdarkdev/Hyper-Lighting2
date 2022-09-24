package me.hypherionmc.hyperlighting.common.containers;

import me.hypherionmc.craterlib.systems.SimpleInventory;
import me.hypherionmc.hyperlighting.common.blockentities.BatteryNeonBlockEntity;
import me.hypherionmc.hyperlighting.common.init.HLContainers;
import me.hypherionmc.hyperlighting.common.items.WirelessBattery;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
public class BatteryNeonContainer extends AbstractContainerMenu {

    private final BatteryNeonBlockEntity blockEntity;

    public BatteryNeonContainer(int windowID, Inventory inventory, FriendlyByteBuf buf) {
        this(windowID, inventory.player.level, buf.readBlockPos(), inventory);
    }

    public BatteryNeonContainer(int windowID, Level level, BlockPos pos, Inventory inventory) {
        super(HLContainers.BATTERY_NEON.get(), windowID);
        this.blockEntity = (BatteryNeonBlockEntity) level.getBlockEntity(pos);
        Container inventory1 = blockEntity.getInventory().getItemHandler();

        this.addSlot(new Slot(inventory1, 0, 27, 20));
        this.addSlot(new Slot(inventory1, 1, 7, 20));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 1, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } if (index == 1) {
                if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (itemstack1.getItem() instanceof DyeItem) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof WirelessBattery) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    public BatteryNeonBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
