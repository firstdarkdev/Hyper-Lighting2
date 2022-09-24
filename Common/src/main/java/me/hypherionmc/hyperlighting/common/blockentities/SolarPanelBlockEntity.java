package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.craterlib.api.blockentities.ITickable;
import me.hypherionmc.craterlib.api.blockentities.caps.ForgeCapability;
import me.hypherionmc.craterlib.common.blockentity.CraterBlockEntity;
import me.hypherionmc.craterlib.systems.energy.CustomEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.SolarPanel;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

/**
 * @author HypherionSA
 * @date 18/09/2022
 */
public class SolarPanelBlockEntity extends CraterBlockEntity implements ITickable {

    private CustomEnergyStorage energyStorage = new CustomEnergyStorage(2000, 50, 1000);

    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.SOLAR_PANEL.get(), pos, state);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        if (this.getBlockState().getBlock() instanceof SolarPanel && level.isDay() && level.canSeeSky(blockPos)) {
            int i = level.getBrightness(LightLayer.SKY, blockPos) - level.getSkyDarken();
            float f = level.getSunAngle(1.0F);

            if (i > 8 && this.energyStorage.getPowerLevel() < this.energyStorage.getPowerCapacity()) {
                float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
                f = f + (f1 - f) * 0.2F;
                i = Math.round((float) i * Mth.cos(f));
                i = Mth.clamp(i, 0, 15);
                this.energyStorage.receiveEnergyInternal(i, false);
            }
            this.sendUpdates();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.energyStorage.readNBT(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.energyStorage.writeNBT(tag);
    }

    @Override
    public <T> Optional<T> getForgeCapability(ForgeCapability forgeCapability, Direction direction) {
        if (forgeCapability == ForgeCapability.ENERGY && (direction == Direction.DOWN || direction == null)) {
            return (Optional<T>) Optional.of(energyStorage);
        }

        return Optional.empty();
    }
}
