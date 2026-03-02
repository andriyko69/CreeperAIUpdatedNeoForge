package io.github.andriyko69.creeperaiupdated;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CreeperAiUpdated.MODID)
public class CreeperAiUpdated {
    public static final String MODID = "creeperaiupdatedneoforge";

    public CreeperAiUpdated(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
