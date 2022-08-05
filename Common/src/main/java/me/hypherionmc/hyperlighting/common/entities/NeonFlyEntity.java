package me.hypherionmc.hyperlighting.common.entities;

import me.hypherionmc.hyperlighting.common.entities.ai.FindSimilarEntityGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;

/**
 * @author HypherionSA
 * @date 04/08/2022
 */
public class NeonFlyEntity extends Animal implements FlyingAnimal {

    private boolean isGlowing = false;
    private DyeColor color;

    public NeonFlyEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);

        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 1.0F);

        this.color = DyeColor.byId(new Random().nextInt(DyeColor.values().length - 1));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FleeSunGoal(this, 1.1D));
        this.goalSelector.addGoal(1, new FireflySwampGoal(this, 1.1D, 28));
        this.goalSelector.addGoal(2, new FindSimilarEntityGoal(this, 1.1D, 28, NeonFlyEntity.class));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new AvoidEntityGoal(this, Player.class, 6.0F, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.4D));
        this.goalSelector.addGoal(6, new PanicGoal(this, 1D));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("isGlowing", isGlowing);
        pCompound.putInt("color", color.getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.isGlowing = pCompound.getBoolean("isGlowing");
        this.color = DyeColor.byId(pCompound.getInt("color"));
    }

    public boolean isGlowing() {
        return isGlowing;
    }

    public DyeColor getColor() {
        return color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public void setGlowing(boolean glowing) {
        isGlowing = glowing;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.FLYING_SPEED, 0.6);
    }

    @Override
    public boolean isFlying() {
        return !this.isOnGround();
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, pLevel) {
            @Override
            public boolean isStableDestination(BlockPos pPos) {
                return !pLevel.getFluidState(pPos).is(Fluids.WATER) && !pLevel.getFluidState(pPos).is(Fluids.FLOWING_WATER);
            }
        };
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    @Override
    protected boolean isFlapping() {
        return this.isFlying() && this.tickCount % 120 == 0;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
        return pLevel.getBlockState(pPos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {}

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {}

    @Override
    public float getScale() {
        return 0.5f;
    }

    public static <T extends Mob> boolean canSpawn(EntityType<NeonFlyEntity> entity, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return !levelAccessor.getLevel().isDay() && !levelAccessor.getLevel().isRainingAt(pos);
    }

    static class FireflySwampGoal extends MoveToBlockGoal {

        public FireflySwampGoal(PathfinderMob pMob, double pSpeedModifier, int pSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
            return pLevel.getBiome(pPos).is(Biomes.SWAMP) && pLevel.getBlockState(pPos).is(Blocks.OAK_LOG);
        }

        @Override
        protected int nextStartTick(PathfinderMob pCreature) {
            return mob.getLevel().random.nextInt(500);
        }
    }
}
