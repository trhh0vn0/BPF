package com.betterpathfinding.betterpathfinding.client;

import com.betterpathfinding.betterpathfinding.client.BetterPathfindingConfigScreen;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class BetterPathfindingClientSetup {
    public static void register(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (IExtensionPoint)((IConfigScreenFactory)(modContainer, screen) -> new BetterPathfindingConfigScreen(screen)));
    }
}
