package dev.lowinputlag.mixin;

import dev.lowinputlag.LowInputLagConfig;
import dev.lowinputlag.LowInputLagMod;
import dev.lowinputlag.LowInputLagState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private double cursorDeltaX;
    @Shadow private double cursorDeltaY;

    private static boolean lowinputlag$failed = false;

    @Inject(method = "updateMouse", at = @At("HEAD"))
    private void lowinputlag$beforeUpdateMouse(CallbackInfo ci) {
        if (lowinputlag$failed) return;
        try {
            LowInputLagConfig cfg = LowInputLagConfig.get();
            if (!cfg.enabled || !LowInputLagState.isActive()) return;
            if (!((Mouse) (Object) this).isCursorLocked()) return;

            if (cfg.disableSmoothing) {
                this.client.options.smoothCameraEnabled = false;
            }
            float m = cfg.sensitivityMultiplier;
            if (m != 1.0f) {
                this.cursorDeltaX *= m;
                this.cursorDeltaY *= m;
            }
        } catch (Throwable t) {
            lowinputlag$failed = true;
            LowInputLagMod.LOGGER.error("[lowinputlag] MouseMixin disabled after error", t);
        }
    }
}
