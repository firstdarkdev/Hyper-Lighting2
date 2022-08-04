package me.hypherionmc.hyperlighting.common.entities;

import me.hypherionmc.hyperlighting.common.entities.ai.FindSimilarEntityGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
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

        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);

        this.color = DyeColor.byId(new Random().nextInt(DyeColor.values().length - 1));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FireflySwampGoal(this, 1.1D, 24));
        this.goalSelector.addGoal(1, new FindSimilarEntityGoal(this, 1.1D, 24, NeonFlyEntity.class));
        this.goalSelector.addGoal(2, new FireflyWanderGoal());
        this.goalSelector.addGoal(3, new FloatGoal(this));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0f));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(6, new FleeSunGoal(this, 1.1D));
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
    public void load(CompoundTag pCompound) {
        super.load(pCompound);
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
                return true;
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

    public static boolean canSpawn(EntityType<NeonFlyEntity> entity, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, Random random) {
        return levelAccessor instanceof Level level && !level.isDay() && !level.isRainingAt(pos);
    }

    class FireflyWanderGoal extends Goal {

        FireflyWanderGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return NeonFlyEntity.this.navigation.isDone() && NeonFlyEntity.this.random.nextInt(10) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return NeonFlyEntity.this.navigation.isInProgress();
        }

        @Override
        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                NeonFlyEntity.this.navigation.moveTo(
                        NeonFlyEntity.this.navigation.createPath(new BlockPos(vec3), 1),
                        1.0D
                );
            }
        }

        @Nullable
        private Vec3 findPos() {
            Vec3 vec3 = NeonFlyEntity.this.getViewVector(0.0f);
            Vec3 vec32 = HoverRandomPos.getPos(NeonFlyEntity.this, 8, 7, vec3.x, vec3.z, ((float)Math.PI / 2F), 3, 1);
            return vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(NeonFlyEntity.this, 8, 4, -2, vec3.x, vec3.z, (float)Math.PI / 2F);
        }
    }

    static class FireflySwampGoal extends MoveToBlockGoal {

        public FireflySwampGoal(PathfinderMob pMob, double pSpeedModifier, int pSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange);
            this.verticalSearchStart = -2;
            this.setFlags(EnumSet.of(Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
            return pLevel.getBiome(pPos).is(Biomes.SWAMP) && pLevel.getBlockState(pPos).is(Blocks.OAK_LOG);
        }

        @Override
        protected int nextStartTick(PathfinderMob pCreature) {
            return mob.getLevel().random.nextInt(120);
        }
    }
}
