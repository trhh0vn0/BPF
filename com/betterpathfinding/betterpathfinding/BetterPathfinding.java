package com.betterpathfinding.betterpathfinding;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(value="betterpathfinding")
public class BetterPathfinding {
    public static final String MOD_ID = "betterpathfinding";
    public static final String MOD_NAME = "Better Pathfinding";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterPathfinding(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("{} {} initializing...", (Object)MOD_NAME, (Object)modContainer.getModInfo().getVersion());
        modContainer.registerConfig(ModConfig.Type.COMMON, (IConfigSpec)BetterPathfindingConfig.SPEC);
        try {
            Class.forName("com.betterpathfinding.betterpathfinding.client.BetterPathfindingClientSetup").getMethod("register", ModContainer.class).invoke(null, modContainer);
        }
        catch (ClassNotFoundException classNotFoundException) {
        }
        catch (Exception e) {
            LOGGER.warn("Config GUI registration failed: {}", (Object)e.getMessage());
        }
        LOGGER.info("{} registered.", (Object)MOD_NAME);
    }
}
