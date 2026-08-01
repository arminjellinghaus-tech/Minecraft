package com.example.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ElemHelperConfigScreen extends Screen {
    private final Screen parent;
    private final ElemHelperConfig config;

    public ElemHelperConfigScreen(Screen parent) {
        super(Component.literal("ElemHelper Settings"));
        this.parent = parent;
        this.config = ElemHelperConfig.get();
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = 60;

        addRenderableWidget(Button.builder(Component.literal(config.enabled ? "Helper: ON" : "Helper: OFF"), button -> {
            config.enabled = !config.enabled;
            config.save();
            clearWidgets();
            init();
        }).bounds(x, y, 200, 20).build());

        addRenderableWidget(Button.builder(Component.literal(config.autoElytra ? "Auto Elytra: ON" : "Auto Elytra: OFF"), button -> {
            config.autoElytra = !config.autoElytra;
            config.save();
            clearWidgets();
            init();
        }).bounds(x, y + 28, 200, 20).build());

        addRenderableWidget(Button.builder(Component.literal(config.armorHud ? "Armor HUD: ON" : "Armor HUD: OFF"), button -> {
            config.armorHud = !config.armorHud;
            config.save();
            clearWidgets();
            init();
        }).bounds(x, y + 56, 200, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Delay: " + config.delay), button -> {
            config.delay = Math.max(0, config.delay + 1);
            config.save();
            clearWidgets();
            init();
        }).bounds(x, y + 84, 95, 20).build());

        addRenderableWidget(Button.builder(Component.literal("- Delay"), button -> {
            config.delay = Math.max(0, config.delay - 1);
            config.save();
            clearWidgets();
            init();
        }).bounds(x + 105, y + 84, 95, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Color: " + config.colorName), button -> {
            config.cycleColor();
            config.save();
            clearWidgets();
            init();
        }).bounds(x, y + 112, 200, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Close"), button -> {
            config.save();
            close();
        }).bounds(x, this.height - 36, 200, 20).build());
    }

    public void close() {
        config.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
