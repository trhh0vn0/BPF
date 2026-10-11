package com.betterpathfinding.betterpathfinding.client;

import com.betterpathfinding.betterpathfinding.config.BetterPathfindingConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class BetterPathfindingConfigScreen
extends Screen {
    private final Screen parent;

    public BetterPathfindingConfigScreen(Screen parent) {
        super((Component)Component.translatable((String)"betterpathfinding.config.title"));
        this.parent = parent;
    }

    protected void init() {
        int btnW = 160;
        int btnH = 20;
        int col1 = 30;
        int col2 = col1 + btnW + 20;
        int startY = 35;
        MutableComponent on = Component.translatable((String)"betterpathfinding.config.on");
        MutableComponent off = Component.translatable((String)"betterpathfinding.config.off");
        int y = startY;
        y = this.addToggle(col1, y, btnW, btnH, BetterPathfindingConfig.FIX_SIGHT_ON_TRANSPARENT, "betterpathfinding.config.fix_sight", on, off);
        y = this.addToggle(col1, y, btnW, btnH, BetterPathfindingConfig.FIX_STUCK_DETECTION, "betterpathfinding.config.fix_stuck", on, off);
        y = this.addToggle(col1, y, btnW, btnH, BetterPathfindingConfig.FIX_FALL_DAMAGE_PATH, "betterpathfinding.config.fix_fall", on, off);
        y = this.addToggle(col1, y, btnW, btnH, BetterPathfindingConfig.FIX_POST_FEED_PATH, "betterpathfinding.config.fix_feed", on, off);
        y = startY;
        y = this.addToggle(col2, y, btnW, btnH, BetterPathfindingConfig.PATH_CACHE_ENABLED, "betterpathfinding.config.path_cache", on, off);
        y = this.addToggle(col2, y, btnW, btnH, BetterPathfindingConfig.STEERING_ENABLED, "betterpathfinding.config.steering", on, off);
        y = this.addToggle(col2, y, btnW, btnH, BetterPathfindingConfig.FREQUENCY_CONTROL_ENABLED, "betterpathfinding.config.frequency", on, off);
        y += 24;
        y = this.addToggle(col2, y, btnW, btnH, BetterPathfindingConfig.ENABLED, "betterpathfinding.config.enabled", on, off);
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)CommonComponents.GUI_DONE, b -> this.minecraft.setScreen(this.parent)).pos(this.width / 2 - 75, this.height - 28).size(150, 20).build());
    }

    private int addToggle(int x, int y, int w, int h, ModConfigSpec.BooleanValue config, String key, MutableComponent on, MutableComponent off) {
        boolean isOn = (Boolean)config.get();
        this.addRenderableWidget((GuiEventListener)Button.builder((Component)(isOn ? on : off).copy().append((Component)Component.translatable((String)key)), b -> {
            config.set((Object)((Boolean)config.get() == false ? 1 : 0));
            boolean now = (Boolean)config.get();
            b.setMessage((Component)(now ? on : off).copy().append((Component)Component.translatable((String)key)));
        }).pos(x, y).size(w, h).build());
        return y + 24;
    }

    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
