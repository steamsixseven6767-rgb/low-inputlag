package dev.lowinputlag;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class LowInputLagConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static LowInputLagConfig instance = new LowInputLagConfig();

    public boolean enabled = true;
    /** Seconds after joining a world/server before the mod switches itself on. */
    public float enableDelaySeconds = 3.0f;
    public boolean applyEveryFrame = true;
    public boolean pollEventsBeforeLook = true;
    public float sensitivityMultiplier = 1.0f;
    public boolean disableSmoothing = true;
    /** Ask the driver for swap interval 0 while the mod is active. */
    public boolean disableVsync = true;
    public boolean debugLog = false;

    public static LowInputLagConfig get() {
        return instance;
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve(LowInputLagMod.MOD_ID + ".json");
    }

    public static void load() {
        Path p = path();
        try {
            if (Files.exists(p)) {
                LowInputLagConfig loaded = GSON.fromJson(Files.readString(p), LowInputLagConfig.class);
                if (loaded != null) instance = loaded;
            }
        } catch (Exception e) {
            LowInputLagMod.LOGGER.error("[{}] failed to read config, using defaults", LowInputLagMod.MOD_ID, e);
            instance = new LowInputLagConfig();
        }
        if (Float.isNaN(instance.sensitivityMultiplier) || instance.sensitivityMultiplier <= 0f) {
            instance.sensitivityMultiplier = 1.0f;
        }
        instance.sensitivityMultiplier = Math.min(instance.sensitivityMultiplier, 10f);
        if (Float.isNaN(instance.enableDelaySeconds) || instance.enableDelaySeconds < 0f) {
            instance.enableDelaySeconds = 3.0f;
        }
        instance.enableDelaySeconds = Math.min(instance.enableDelaySeconds, 60f);
        save();
    }

    public static void save() {
        try {
            Files.writeString(path(), GSON.toJson(instance));
        } catch (IOException e) {
            LowInputLagMod.LOGGER.error("[{}] failed to write config", LowInputLagMod.MOD_ID, e);
        }
    }
}
