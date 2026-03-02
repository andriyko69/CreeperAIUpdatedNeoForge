package io.github.andriyko69.creeperaiupdated.ai;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class UpdatedNearestAttackableTargetGoal<T extends LivingEntity> extends TargetGoal {

    private static final int DEFAULT_RANDOM_INTERVAL = 10;

    protected final Class<T> targetType;
    protected final int randomInterval;

    @Nullable
    protected LivingEntity target;

    protected TargetingConditions targetConditions;

    // Basic constructor
    public UpdatedNearestAttackableTargetGoal(
            Mob mob,
            Class<T> targetClass,
            boolean mustSee
    ) {
        this(mob, targetClass, DEFAULT_RANDOM_INTERVAL, mustSee, false, null);
    }

    // Constructor with custom predicate
    public UpdatedNearestAttackableTargetGoal(
            Mob mob,
            Class<T> targetClass,
            boolean mustSee,
            Predicate<LivingEntity> targetPredicate
    ) {
        this(mob, targetClass, DEFAULT_RANDOM_INTERVAL, mustSee, false, targetPredicate);
    }

    // Constructor with visibility + invisibility options
    public UpdatedNearestAttackableTargetGoal(
            Mob mob,
            Class<T> targetClass,
            boolean mustSee,
            boolean mustReach
    ) {
        this(mob, targetClass, DEFAULT_RANDOM_INTERVAL, mustSee, mustReach, null);
    }

    // Full constructor
    public UpdatedNearestAttackableTargetGoal(
            Mob mob,
            Class<T> targetClass,
            int randomInterval,
            boolean mustSee,
            boolean mustReach,
            @Nullable Predicate<LivingEntity> targetPredicate
    ) {
        super(mob, mustSee, mustReach);

        this.targetType = targetClass;
        this.randomInterval = reducedTickDelay(randomInterval);

        this.setFlags(EnumSet.of(Goal.Flag.TARGET));

        this.targetConditions = TargetingConditions.forCombat()
                .range(this.getFollowDistance())
                .selector(targetPredicate)
                .ignoreLineOfSight();
    }

    @Override
    public boolean canUse() {
        if (this.randomInterval > 0 &&
                this.mob.getRandom().nextInt(this.randomInterval) != 0) {
            return false;
        }

        this.findTarget();
        return this.target != null;
    }

    protected AABB getTargetSearchArea(double followDistance) {
        return this.mob.getBoundingBox()
                .inflate(followDistance, 4.0D, followDistance);
    }

    protected void findTarget() {
        if (this.targetType != Player.class && this.targetType != ServerPlayer.class) {

            this.target = this.mob.level().getNearestEntity(
                    this.mob.level().getEntitiesOfClass(
                            this.targetType,
                            this.getTargetSearchArea(this.getFollowDistance()),
                            entity -> true
                    ),
                    this.targetConditions,
                    this.mob,
                    this.mob.getX(),
                    this.mob.getEyeY(),
                    this.mob.getZ()
            );

        } else {

            this.target = this.mob.level().getNearestPlayer(
                    this.targetConditions,
                    this.mob,
                    this.mob.getX(),
                    this.mob.getEyeY(),
                    this.mob.getZ()
            );
        }
    }

    @Override
    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }

    public void setTarget(@Nullable LivingEntity newTarget) {
        this.target = newTarget;
    }
}