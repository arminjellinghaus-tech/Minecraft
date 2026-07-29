package com.example.client;

import com.example.ExampleMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ElemHelperConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("elemhelper.properties");

    public boolean enabled = true;
    public boolean autoElytra = true;
    public boolean armorHud = true;
    public int delay = 2;
    public int highlightColor = 0xFF00CC66;
    public String colorName = "Turtle Green";

    private static ElemHelperConfig instance;

    public static ElemHelperConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    public static ElemHelperConfig load() {
        ElemHelperConfig config = new ElemHelperConfig();
        if (!Files.exists(CONFIG_PATH)) {
            return config;
        }

        Properties properties = new Properties();
        try (InputStream stream = Files.newInputStream(CONFIG_PATH)) {
            properties.load(stream);
        } catch (IOException e) {
            ExampleMod.LOGGER.warn("Could not load ElemHelper config", e);
            return config;
        }

        config.enabled = Boolean.parseBoolean(properties.getProperty("enabled", "true"));
        config.autoElytra = Boolean.parseBoolean(properties.getProperty("auto_elytra", "true"));
        config.armorHud = Boolean.parseBoolean(properties.getProperty("armor_hud", "true"));
        config.delay = Math.max(0, Integer.parseInt(properties.getProperty("delay", "2")));

        String colorValue = properties.getProperty("highlight_color", "0xFF00CC66");
        try {
            config.highlightColor = Integer.decode(colorValue);
        } catch (NumberFormatException ignored) {
            config.highlightColor = 0xFF00CC66;
        }

        config.colorName = properties.getProperty("color_name", "Turtle Green");
        return config;
    }

    public void save() {
        Properties properties = new Properties();
        properties.setProperty("enabled", Boolean.toString(enabled));
        properties.setProperty("auto_elytra", Boolean.toString(autoElytra));
        properties.setProperty("armor_hud", Boolean.toString(armorHud));
        properties.setProperty("delay", Integer.toString(delay));
        properties.setProperty("highlight_color", String.format("0x%06X", highlightColor & 0xFFFFFF));
        properties.setProperty("color_name", colorName);

        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (OutputStream outputStream = Files.newOutputStream(CONFIG_PATH)) {
                properties.store(outputStream, "ElemHelper config");
            }
        } catch (IOException e) {
            ExampleMod.LOGGER.warn("Could not save ElemHelper config", e);
        }
    }

    public void cycleColor() {
        if (highlightColor == 0xFF00CC66) {
            highlightColor = 0xFF4CC9F0;
            colorName = "Turtle Blue";
        } else if (highlightColor == 0xFF4CC9F0) {
            highlightColor = 0xFFFFC857;
            colorName = "Sunny Shell";
        } else {
            highlightColor = 0xFF00CC66;
            colorName = "Turtle Green";
        }
    }
}
