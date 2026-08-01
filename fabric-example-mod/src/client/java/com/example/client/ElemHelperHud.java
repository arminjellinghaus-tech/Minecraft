package com.example.client;

public class ElemHelperHud {
    public void render(Object graphics, float tickDelta) {
        if (ExampleModClient.logic != null) {
            ExampleModClient.logic.renderHud(null, 0, 0, tickDelta);
        }
    }
}
