package io.github.andriyko69.creeperaiupdated.mixin;

import io.github.andriyko69.creeperaiupdated.Config;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobFinalizeSpawnMixin {

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void creeperAiUpdated$finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            @Nullable SpawnGroupData spawnData,
            CallbackInfoReturnable<SpawnGroupData> cir
    ) {
        Mob mob = (Mob) (Object) this;
        if (!(mob instanceof Creeper creeper)) return;
        if (level.getLevel().isClientSide()) return;

        if (creeper.getRandom().nextDouble() < Config.poweredProb) {
            EntityDataAccessor<Boolean> DATA_IS_POWERED = CreeperAccessor.creeperAiUpdated$getDataIsPowered();
            creeper.getEntityData().set(DATA_IS_POWERED, true);
        }
    }
}