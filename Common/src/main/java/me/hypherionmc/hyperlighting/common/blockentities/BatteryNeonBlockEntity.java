package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.craterlib.api.blockentities.ITickable;
import me.hypherionmc.craterlib.common.blockentity.CraterBlockEntity;
import me.hypherionmc.craterlib.systems.SimpleInventory;
import me.hypherionmc.craterlib.systems.energy.CustomEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.BatteryNeon;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import me.hypherionmc.hyperlighting.common.items.WirelessBattery;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
public class BatteryNeonBlockEntity extends CraterBlockEntity implements ITickable {

    private final CustomEnergyStorage energyStorage = new CustomEnergyStorage(500, 20, 0);
    private final SimpleInventory inventory = new SimpleInventory(2, 1);

    private boolean isCharging = false;

    public BatteryNeonBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.BATTERY_NEON.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("isCharging", isCharging);
        energyStorage.writeNBT(tag);
        inventory.writeNBT(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.isCharging = tag.getBoolean("isCharging");
        this.energyStorage.readNBT(tag);
        inventory.clearContent();
        inventory.readNBT(tag);
    }

    public boolean isCharging() {
        return isCharging;
    }

    public CustomEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public void sendUpdates() {
        BlockState state = level.getBlockState(this.getBlockPos());
        this.level.blockEntityChanged(this.getBlockPos());
        this.level.sendBlockUpdated(this.getBlockPos(), this.level.getBlockState(this.getBlockPos()), state, 3);
        this.setChanged();
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (level.getGameTime() % 20L == 0L) {
            ItemStack stack = inventory.getItemHandler().getItem(0);
            if (!stack.isEmpty() && stack.getItem() instanceof WirelessBattery battery) {
                if (battery.isLinked(stack, level)) {
                    BlockPos pos = battery.getLinkedPos(stack);

                    if (level.getBlockEntity(pos) instanceof SolarPanelBlockEntity solarPanel) {
                        CustomEnergyStorage storage = solarPanel.getEnergyStorage();

                        if (storage.extractEnergy(20, true) > 0 && this.energyStorage.receiveEnergy(20, true) > 0) {
                            this.isCharging = true;
                            storage.extractEnergy(this.energyStorage.receiveEnergy(20, false), false);
                        } else {
                            this.isCharging = false;
                        }
                    }
                } else {
                    isCharging = false;
                }
            }
        }

        if (level.getGameTime() % 40L == 0L) {
            if (level.getBlockState(blockPos).getValue(BatteryNeon.LIT)) {
                this.energyStorage.extractEnergy(1, false);
            }
        }
        this.sendUpdates();
    }
}
