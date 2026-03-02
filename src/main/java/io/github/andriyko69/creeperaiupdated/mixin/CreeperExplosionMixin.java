package io.github.andriyko69.creeperaiupdated.mixin;

import io.github.andriyko69.creeperaiupdated.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Creeper.class)
public abstract class CreeperExplosionMixin {

    @Redirect(
            method = "explodeCreeper",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"
            )
    )
    private Explosion creeperAiUpdated$redirectExplode(
            Level level,
            @Nullable Entity exploder,
            double x, double y, double z,
            float power,
            Level.ExplosionInteraction interaction
    ) {
        return level.explode(exploder, x, y, z, power, Config.fireExplosions, interaction);
    }
}