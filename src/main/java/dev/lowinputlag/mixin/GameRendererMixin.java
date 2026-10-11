package dev.lowinputlag.mixin;

import dev.lowinputlag.LowInputLagConfig;
import dev.lowinputlag.LowInputLagMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private MinecraftClient client;

    private static boolean lowinputlag$failed = false;
    private static long lowinputlag$lastNanos = 0L;
    private static int lowinputlag$counter = 0;

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void lowinputlag$lateLatch(CallbackInfo ci) {
        if (lowinputlag$failed) return;
        try {
            LowInputLagConfig cfg = LowInputLagConfig.get();
            if (!cfg.enabled || !cfg.applyEveryFrame) return;

            MinecraftClient mc = this.client;
            if (mc.player == null || mc.currentScreen != null
                    || !mc.mouse.isCursorLocked() || !mc.isWindowFocused()) {
                lowinputlag$lastNanos = 0L;
                return;
            }

            if (cfg.pollEventsBeforeLook) {
                GLFW.glfwPollEvents();
            }

            long now = System.nanoTime();
            double dt = lowinputlag$lastNanos == 0L ? 0.0 : (now - lowinputlag$lastNanos) / 1.0e9;
            lowinputlag$lastNanos = now;

            mc.mouse.updateMouse(dt);

            if (cfg.debugLog && ++lowinputlag$counter % 300 == 0) {
                LowInputLagMod.LOGGER.info("[lowinputlag] late-latch active, frame dt={} ms", dt * 1000.0);
            }
        } catch (Throwable t) {
            lowinputlag$failed = true;
            LowInputLagMod.LOGGER.error("[lowinputlag] late-latch disabled after error", t);
        }
    }
}
