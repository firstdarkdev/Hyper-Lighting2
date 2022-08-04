package me.hypherionmc.hyperlighting.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

/**
 * This code is dirty like my underpants, but like said underpants, it still works :)
 */
public class FindSimilarEntityGoal extends Goal {

    protected final PathfinderMob mob;
    public final double speedModifier;

    protected int nextStartTick;
    protected int tryTicks;
    private int maxStayTicks;
    protected BlockPos blockPos = BlockPos.ZERO;
    private final int searchRange;
    private final int verticalSearchRange;
    protected int verticalSearchStart;

    protected final Class<? extends LivingEntity> findType;

    public FindSimilarEntityGoal(PathfinderMob pMob, double pSpeedModifier, int pSearchRange, Class<? extends LivingEntity> clazz) {
        this.mob = pMob;
        this.speedModifier = pSpeedModifier;
        this.searchRange = pSearchRange;
        this.verticalSearchRange = 1;
        this.findType = clazz;
    }

    @Override
    public boolean canUse() {
        if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            return this.findNearestEntity();
        }
    }

    protected int nextStartTick(PathfinderMob pCreature) {
        return reducedTickDelay(200 + pCreature.getRandom().nextInt(200));
    }

    @Override
    public boolean canContinueToUse() {
        return this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200 && this.isValidTarget(this.mob.level);
    }

    protected void moveMobToEntity() {
        this.mob.getNavigation().moveTo((double)((float)this.blockPos.getX()) + 0.5D, (double)(this.blockPos.getY() + 1), (double)((float)this.blockPos.getZ()) + 0.5D, this.speedModifier);
    }

    @Override
    public void start() {
        this.moveMobToEntity();
        this.tryTicks = 0;
        this.maxStayTicks = this.mob.getRandom().nextInt(this.mob.getRandom().nextInt(1200) + 1200) + 1200;
    }

    public double acceptedDistance() {
        return 1.0D;
    }

    protected BlockPos getMoveToTarget() {
        return this.blockPos.above();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        BlockPos blockpos = this.getMoveToTarget();
        if (!blockpos.closerThan(new Vec3i(this.mob.getBlockX(), this.mob.getBlockY(), this.mob.getBlockZ()), this.acceptedDistance())) {
            ++this.tryTicks;
            if (this.shouldRecalculatePath()) {
                this.mob.getNavigation().moveTo((double)((float)blockpos.getX()) + 0.5D, (double)blockpos.getY(), (double)((float)blockpos.getZ()) + 0.5D, this.speedModifier);
            }
        } else {
            --this.tryTicks;
        }
    }

    public boolean shouldRecalculatePath() {
        return this.tryTicks % 240 == 0;
    }

    protected boolean findNearestEntity() {
        int i = this.searchRange;
        int j = this.verticalSearchRange;
        BlockPos blockpos = this.mob.blockPosition();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int k = this.verticalSearchStart; k <= j; k = k > 0 ? -k : 1 - k) {
            for(int l = 0; l < i; ++l) {
                for(int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1) {
                    for(int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1) {
                        blockpos$mutableblockpos.setWithOffset(blockpos, i1, k - 1, j1);
                        return this.mob.isWithinRestriction(blockpos$mutableblockpos) && this.isValidTarget(this.mob.level);
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidTarget(Level level) {
        Entity entity = level.getNearestEntity(level.getEntitiesOfClass(this.findType, this.mob.getBoundingBox().inflate(this.searchRange, 3.0D, this.searchRange), (p_148124_) -> true), TargetingConditions.forNonCombat().range(searchRange), this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        if (entity != null) {
            this.blockPos = entity.getOnPos();
        }
        return entity != null;
    }

}
