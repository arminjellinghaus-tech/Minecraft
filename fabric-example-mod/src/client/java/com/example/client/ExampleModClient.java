package com.example.client;

import com.example.ExampleMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class ExampleModClient implements ClientModInitializer {
    private static final String MOD_ID = ExampleMod.MOD_ID;
    public static ElemHelperLogic logic;

    @Override
    public void onInitializeClient() {
        ElemHelperConfig.get();
        logic = new ElemHelperLogic(Minecraft.getInstance());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                return;
            }

            if (client.options.keyDrop.consumeClick()) {
                if (logic != null) {
                    logic.handleElytraBinding();
                }
            }
        });
    }
}
