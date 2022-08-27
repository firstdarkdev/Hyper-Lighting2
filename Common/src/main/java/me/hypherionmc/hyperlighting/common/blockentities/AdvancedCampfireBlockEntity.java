package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.craterlib.api.blockentities.ISidedTickable;
import me.hypherionmc.hyperlighting.common.blocks.AdvancedCampfire;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class AdvancedCampfireBlockEntity extends BlockEntity implements Clearable, ISidedTickable {

    private static final int BURN_COOL_SPEED = 2;
    private static final int NUM_SLOTS = 4;

    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    private final int[] cookingProgress = new int[4];
    private final int[] cookingTime = new int[4];

    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);

    public AdvancedCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.CAMPFIRE.get(), pos, state);
    }

    @Override
    public void serverTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        boolean isDirty = false;
        AdvancedCampfireBlockEntity be = (AdvancedCampfireBlockEntity) blockEntity;

        for (int i = 0; i < be.items.size(); i++) {
            ItemStack inStack = be.items.get(i);
            if (!inStack.isEmpty()) {
                isDirty = true;
                int time = be.cookingProgress[i]++;
                if (be.cookingProgress[i] >= be.cookingTime[i]) {
                    Container container = new SimpleContainer(inStack);
                    ItemStack outStack = be.quickCheck.getRecipeFor(container, level).map(r -> r.assemble(container)).orElse(inStack);
                    Containers.dropItemStack(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), outStack);
                    be.items.set(i, ItemStack.EMPTY);
                    level.sendBlockUpdated(blockPos, blockState, blockState, 3);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState));
                }
            }
        }

        if (isDirty) {
            setChanged(level, blockPos, blockState);
        }
    }

    public static void cooldownTick(Level level, BlockPos blockPos, BlockState state, AdvancedCampfireBlockEntity be) {
        boolean isDirty = false;

        for (int i = 0; i < be.items.size(); ++i) {
            if (be.cookingProgress[i] > 0) {
                isDirty = true;
                be.cookingProgress[i] = Mth.clamp(be.cookingProgress[i] - 2, 0, be.cookingTime[i]);
            }
        }

        if (isDirty) {
            setChanged(level, blockPos, state);
        }
    }

    @Override
    public void clientTick(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        AdvancedCampfireBlockEntity campfireBlockEntity = (AdvancedCampfireBlockEntity) blockEntity;
        RandomSource randomSource = level.random;
        int i;
        if (randomSource.nextFloat() < 0.11F) {
            for(i = 0; i < randomSource.nextInt(2) + 2; ++i) {
                AdvancedCampfire.makeParticles(level, blockPos, blockState.getValue(AdvancedCampfire.SIGNAL_FIRE), false);
            }
        }

        i = blockState.getValue(AdvancedCampfire.FACING).get2DDataValue();

        for(int j = 0; j < campfireBlockEntity.items.size(); ++j) {
            if (!campfireBlockEntity.items.get(j).isEmpty() && randomSource.nextFloat() < 0.2F) {
                Direction direction = Direction.from2DDataValue(Math.floorMod(j + i, 4));
                float f = 0.3125F;
                double d = (double)blockPos.getX() + 0.5 - (double)((float)direction.getStepX() * 0.3125F) + (double)((float)direction.getClockWise().getStepX() * 0.3125F);
                double e = (double)blockPos.getY() + 0.5;
                double g = (double)blockPos.getZ() + 0.5 - (double)((float)direction.getStepZ() * 0.3125F) + (double)((float)direction.getClockWise().getStepZ() * 0.3125F);

                for(int k = 0; k < 4; ++k) {
                    level.addParticle(ParticleTypes.SMOKE, d, e, g, 0.0, 5.0E-4, 0.0);
                }
            }
        }
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items.clear();
        ContainerHelper.loadAllItems(compoundTag, this.items);
        int[] is;
        if (compoundTag.contains("CookingTimes", 11)) {
            is = compoundTag.getIntArray("CookingTimes");
            System.arraycopy(is, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, is.length));
        }

        if (compoundTag.contains("CookingTotalTimes", 11)) {
            is = compoundTag.getIntArray("CookingTotalTimes");
            System.arraycopy(is, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, is.length));
        }
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items, true);
        compoundTag.putIntArray("CookingTimes", this.cookingProgress);
        compoundTag.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundTag, this.items, true);
        return compoundTag;
    }

    public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack itemStack) {
        return this.items.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.quickCheck.getRecipeFor(new SimpleContainer(new ItemStack[]{itemStack}), this.level);
    }

    public boolean placeFood(@Nullable Entity entity, ItemStack itemStack, int i) {
        for(int j = 0; j < this.items.size(); ++j) {
            ItemStack itemStack2 = this.items.get(j);
            if (itemStack2.isEmpty()) {
                this.cookingTime[j] = i;
                this.cookingProgress[j] = 0;
                this.items.set(j, itemStack.split(1));
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(entity, this.getBlockState()));
                this.markUpdated();
                return true;
            }
        }

        return false;
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        this.items.clear();
    }

    public void dowse() {
        if (this.level != null) {
            this.markUpdated();
        }
    }
}
