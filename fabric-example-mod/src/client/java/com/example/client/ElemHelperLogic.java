package com.example.client;

import com.example.ExampleMod;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElemHelperLogic {
    private static final Identifier ELYTRA_TEXTURE = ExampleMod.id("textures/gui/elytra_helper.png");
    private static final int HOTBAR_SIZE = 9;

    private final Minecraft client;
    private final ElemHelperConfig config;
    private boolean pendingReturnToMace;
    private int pendingSlot = -1;

    public ElemHelperLogic(Minecraft client) {
        this.client = client;
        this.config = ElemHelperConfig.get();
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(Minecraft client) {
        if (!config.enabled || client.player == null) {
            return;
        }

        LocalPlayer player = client.player;
        boolean isFlying = player.isFallFlying();
        boolean holdingMace = isHoldingItem(player, Items.MACE);
        int chestSlot = findChestSlot(player.getInventory());
        boolean hasChestSlot = chestSlot >= 0;

        if (pendingReturnToMace && isFlying && holdingMace && hasChestSlot) {
            if (!player.getEquippedStack(EquipmentSlot.CHEST).isEmpty()) {
                player.getInventory().selectedSlot = findMaceSlot(player.getInventory());
                pendingReturnToMace = false;
                pendingSlot = -1;
            }
        }

        if (isFlying && holdingMace && hasChestSlot && config.autoElytra && player.getInventory().selectedSlot == chestSlot) {
            pendingReturnToMace = true;
        }

        if (isFlying && holdingMace && hasChestSlot && pendingSlot >= 0) {
            player.getInventory().selectedSlot = pendingSlot;
            pendingReturnToMace = true;
            pendingSlot = -1;
        }
    }

    public void handleElytraBinding() {
        if (!config.enabled || client.player == null) {
            return;
        }

        LocalPlayer player = client.player;
        if (!player.isFallFlying()) {
            return;
        }

        if (!isHoldingItem(player, Items.MACE)) {
            return;
        }

        int elytraSlot = findSlotWithItem(player.getInventory(), Items.ELYTRA);
        if (elytraSlot < 0) {
            return;
        }

        player.getInventory().selectedSlot = elytraSlot;
        pendingReturnToMace = true;
    }

    public void renderHud(Font font, int screenWidth, int screenHeight, float tickDelta) {
        if (!config.enabled || !config.armorHud || client.player == null) {
            return;
        }

        LocalPlayer player = client.player;
        if (!player.isFallFlying()) {
            return;
        }

        drawArmorHud(font, screenWidth, screenHeight, player);
    }

    private void drawArmorHud(Font font, int screenWidth, int screenHeight, LocalPlayer player) {
        int x = 10;
        int y = 10;
        int size = 18;
        int spacing = 4;
        Inventory inventory = player.getInventory();
        int maceSlot = findMaceSlot(inventory);
        int chestSlot = findChestSlot(inventory);

        for (int i = 0; i < HOTBAR_SIZE; i++) {
            int slotX = x + i * (size + spacing);
            int slotY = y;
            boolean shouldHighlight = i == maceSlot || i == chestSlot;
            if (shouldHighlight) {
                client.gui.fill(slotX - 2, slotY - 2, slotX + size, slotY + size, config.highlightColor | 0x80000000);
            }
            client.getItemRenderer().renderGuiItemIcon(inventory.getStack(i), slotX, slotY);
        }

        font.drawShadow("Turtle Shell", x, y + 24, 0xFF00CC66);
        font.drawShadow("Mace/Chest", x + 70, y + 24, 0xFFB8F2C0);
    }

    public void handleRightClick(Player player) {
        if (!config.enabled || !(player instanceof LocalPlayer clientPlayer)) {
            return;
        }

        if (!clientPlayer.isFallFlying()) {
            return;
        }

        if (!isHoldingItem(clientPlayer, Items.MACE)) {
            return;
        }

        int chestSlot = findChestSlot(clientPlayer.getInventory());
        if (chestSlot < 0) {
            return;
        }

        pendingSlot = chestSlot;
        pendingReturnToMace = true;
    }

    private int findMaceSlot(Inventory inventory) {
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(Items.MACE)) {
                return i;
            }
        }
        return -1;
    }

    private int findChestSlot(Inventory inventory) {
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) {
                continue;
            }
            Item item = stack.getItem();
            if (stack.isOf(Items.ELYTRA)) {
                return i;
            }
            if (item instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == EquipmentSlot.CHEST) {
                return i;
            }
        }
        return -1;
    }

    private int findSlotWithItem(Inventory inventory, Item item) {
        for (int i = 0; i < HOTBAR_SIZE; i++) {
            if (inventory.getStack(i).isOf(item)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isHoldingItem(LocalPlayer player, Item item) {
        return player.getMainHandStack().isOf(item) || player.getOffHandStack().isOf(item);
    }
}
