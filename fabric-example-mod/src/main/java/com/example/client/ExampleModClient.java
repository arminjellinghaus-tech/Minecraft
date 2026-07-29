package com.example.client;

import com.example.ExampleMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
    private static final String MOD_ID = ExampleMod.MOD_ID;

    public static KeyBinding configKey;
    public static KeyBinding elytraKey;
    public static ElemHelperLogic logic;

    @Override
    public void onInitializeClient() {
        ElemHelperConfig.get();

        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.elemhelper.settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.elemhelper"
        ));

        elytraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.elemhelper.elytra",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                "category.elemhelper"
        ));

        logic = new ElemHelperLogic(Minecraft.getInstance());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKey.wasPressed()) {
                client.setScreen(new ElemHelperConfigScreen(client.screen));
            }

            while (elytraKey.wasPressed()) {
                logic.handleElytraBinding();
            }
        });
    }
}
