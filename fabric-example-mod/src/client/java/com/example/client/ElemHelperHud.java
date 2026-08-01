package com.example.client;

import net.minecraft.client.gui.DrawContext;

public class ElemHelperHud {
    public void render(DrawContext drawContext, float tickDelta) {
        if (ExampleModClient.logic != null) {
            ExampleModClient.logic.renderHud(drawContext, tickDelta);
        }
    }
}
