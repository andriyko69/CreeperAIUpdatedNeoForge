package io.github.andriyko69.creeperaiupdated.ai;

import io.github.andriyko69.creeperaiupdated.Config;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class UpdatedCreeperSwellGoal extends Goal {
    private final Creeper creeper;
    private LivingEntity attackingEntity;
    private boolean normalExplosion = false;

    public UpdatedCreeperSwellGoal(Creeper creeper) {
        this.creeper = creeper;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.creeper.getTarget();

        if (this.creeper.getSwellDir() > 0) {
            return true;
        } else if (livingentity != null) {
            normalExplosion = this.creeper.distanceToSqr(livingentity) < 9.0D;
            return (normalExplosion || preBreakWall(livingentity));
        }
        return false;
    }

    private boolean preBreakWall(LivingEntity livingEntity) {
        if (breakWall(livingEntity)) {
            Path p = creeper.getNavigation().createPath(livingEntity, 0);
            if (p != null && p.getNodeCount() > 6) {
                creeper.getNavigation().moveTo(p, 1.0);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean breakWall(LivingEntity livingentity) {
        return creeper.tickCount > 60 && !creeper.isPathFinding() && creeper.distanceToSqr(livingentity) < Config.breachRange;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.creeper.getNavigation().stop();
        this.attackingEntity = this.creeper.getTarget();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.attackingEntity = null;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.attackingEntity == null) {
            this.creeper.setSwellDir(-1);
        } else if (this.normalExplosion && this.creeper.distanceToSqr(this.attackingEntity) > 49.0D) {
            this.creeper.setSwellDir(-1);
        } else {
            this.creeper.setSwellDir(1);
        }
    }
}
