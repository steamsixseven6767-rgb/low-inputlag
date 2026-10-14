package dev.lowinputlag;

import net.minecraft.client.MinecraftClient;

/** Runtime gate: the mod is inactive until a world has been loaded for N seconds. */
public final class LowInputLagState {
    private static long joinedAtNanos = 0L;
    private static boolean active = false;
    private static boolean vsyncOverridden = false;
    private static int vsyncTicks = 0;

    private LowInputLagState() {}

    /** Called every client tick. */
    public static void update(MinecraftClient mc) {
        if (mc.world == null || mc.player == null) {
            if (active || joinedAtNanos != 0L) {
                LowInputLagMod.LOGGER.info("[{}] disabled (no world)", LowInputLagMod.MOD_ID);
            }
            if (vsyncOverridden) restoreVsync(mc);
            joinedAtNanos = 0L;
            active = false;
            return;
        }
        if (active) {
            applyVsync(mc);
            return;
        }

        long now = System.nanoTime();
        if (joinedAtNanos == 0L) {
            joinedAtNanos = now;
            return;
        }
        long delay = (long) (Math.max(0f, LowInputLagConfig.get().enableDelaySeconds) * 1.0e9);
        if (now - joinedAtNanos >= delay) {
            active = true;
            LowInputLagMod.LOGGER.info("[{}] enabled after join delay", LowInputLagMod.MOD_ID);
            applyVsync(mc);
        }
    }

    public static boolean isActive() {
        return active;
    }

    private static void applyVsync(MinecraftClient mc) {
        LowInputLagConfig cfg = LowInputLagConfig.get();
        if (!cfg.enabled || !cfg.disableVsync) {
            if (vsyncOverridden) restoreVsync(mc);
            return;
        }
        // Re-apply every ~5 s in case the game or the launcher resets the swap interval.
        if (!vsyncOverridden || ++vsyncTicks % 100 == 0) {
            mc.getWindow().setVsync(false);
            if (!vsyncOverridden) {
                LowInputLagMod.LOGGER.info("[{}] vsync requested off", LowInputLagMod.MOD_ID);
            }
            vsyncOverridden = true;
        }
    }

    private static void restoreVsync(MinecraftClient mc) {
        try {
            mc.getWindow().setVsync(mc.options.getEnableVsync().getValue());
            LowInputLagMod.LOGGER.info("[{}] vsync restored", LowInputLagMod.MOD_ID);
        } finally {
            vsyncOverridden = false;
            vsyncTicks = 0;
        }
    }
}
