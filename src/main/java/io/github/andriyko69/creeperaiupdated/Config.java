package io.github.andriyko69.creeperaiupdated;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = CreeperAiUpdated.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MIN_LAYER = BUILDER.comment("Minimum Y layer where creepers can spawn with the breaching ability.(Sea level = 62)").defineInRange("minLayer", -64, -64, 320);
    public static final ModConfigSpec.IntValue MAX_LAYER = BUILDER.comment("Maximum Y layer where creepers can spawn with the breaching ability.(Sea level = 62)").defineInRange("maxLayer", 320, -64, 320);
    public static final ModConfigSpec.DoubleValue POWERED_PROB = BUILDER.comment("Probability of creepers spawning powered.").defineInRange("powered_prob", 0.3D, 0.0D, 1.0D);
    public static final ModConfigSpec.IntValue FOLLOW_RANGE = BUILDER.comment("Maximum distance a creeper can see and follow you. (32 = vanilla)").defineInRange("follow_range", 32, 0, 2048);
    public static final ModConfigSpec.IntValue BREACH_RANGE = BUILDER.comment("Maximum distance a creeper can decide to explode.").defineInRange("breach_range", 30, 0, 2048);
    public static final ModConfigSpec.BooleanValue CAN_BREAK_WALLS = BUILDER.comment("Creeper ability to breach trough walls or not").define("can_break_walls", true);
    public static final ModConfigSpec.BooleanValue CAN_LEAP = BUILDER.comment("Creeper ability to leap at targets").define("can_leap", true);
    public static final ModConfigSpec.BooleanValue FIRE_EXPLOSIONS = BUILDER.comment("Creeper explosions cause fire").define("fire_explosions", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int minLayer;
    public static int maxLayer;
    public static double poweredProb;
    public static int followRange;
    public static int breachRange;
    public static boolean canBreakWalls;
    public static boolean canLeap;
    public static boolean fireExplosions;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        minLayer = MIN_LAYER.get();
        maxLayer = MAX_LAYER.get();
        poweredProb = POWERED_PROB.get();
        followRange = FOLLOW_RANGE.get();
        breachRange = BREACH_RANGE.get();
        canBreakWalls = CAN_BREAK_WALLS.get();
        canLeap = CAN_LEAP.get();
        fireExplosions = FIRE_EXPLOSIONS.get();
    }
}
