package io.github.andriyko69.creeperaiupdated.mixin;

import io.github.andriyko69.creeperaiupdated.Config;
import io.github.andriyko69.creeperaiupdated.ai.UpdatedCreeperSwellGoal;
import io.github.andriyko69.creeperaiupdated.ai.UpdatedNearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperGoalsMixin {

    @Unique
    private static void creeperAiUpdatedNeoForge$removeTargetGoalsOfType(Creeper creeper) {

        ((GoalSelectorAccessor) creeper.targetSelector).creeperAiUpdated$getAvailableGoals().removeIf(wrapped -> wrapped.getGoal() instanceof NearestAttackableTargetGoal);
    }

    @Unique
    private static void creeperAiUpdatedNeoForge$removeGoalByClassName(Creeper creeper) {

        ((GoalSelectorAccessor) creeper.goalSelector).creeperAiUpdated$getAvailableGoals().removeIf(wrapped -> wrapped.getGoal().getClass().getName().equals("net.minecraft.world.entity.monster.Creeper$CreeperSwellGoal"));
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void creeperAiUpdated$registerGoalsTail(CallbackInfo ci) {
        Creeper creeper = (Creeper) (Object) this;

        creeperAiUpdatedNeoForge$removeTargetGoalsOfType(creeper);
        creeper.targetSelector.addGoal(1, new UpdatedNearestAttackableTargetGoal<>(creeper, Player.class, false));

        // Replace swell goal only when enabled + within Y range
        if (Config.canBreakWalls && creeper.getY() <= Config.maxLayer && creeper.getY() >= Config.minLayer) {
            creeperAiUpdatedNeoForge$removeGoalByClassName(creeper);
            creeper.goalSelector.addGoal(2, new UpdatedCreeperSwellGoal(creeper));
        }

        if (Config.canLeap) {
            creeper.goalSelector.addGoal(3, new LeapAtTargetGoal(creeper, 0.4F));
        }
    }
}