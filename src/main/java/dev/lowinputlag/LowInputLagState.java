package dev.lowinputlag;

import net.minecraft.client.MinecraftClient;

/** Runtime gate: the mod is inactive until a world has been loaded for N seconds. */
public final class LowInputLagState {
    private static long joinedAtNanos = 0L;
    private static boolean active = false;

    private LowInputLagState() {}

    /** Called every client tick. */
    public static void update(MinecraftClient mc) {
        if (mc.world == null || mc.player == null) {
            if (active || joinedAtNanos != 0L) {
                LowInputLagMod.LOGGER.info("[{}] disabled (no world)", LowInputLagMod.MOD_ID);
            }
            joinedAtNanos = 0L;
            active = false;
            return;
        }
        if (active) return;

        long now = System.nanoTime();
        if (joinedAtNanos == 0L) {
            joinedAtNanos = now;
            return;
        }
        long delay = (long) (Math.max(0f, LowInputLagConfig.get().enableDelaySeconds) * 1.0e9);
        if (now - joinedAtNanos >= delay) {
            active = true;
            LowInputLagMod.LOGGER.info("[{}] enabled after join delay", LowInputLagMod.MOD_ID);
        }
    }

    public static boolean isActive() {
        return active;
    }
}
