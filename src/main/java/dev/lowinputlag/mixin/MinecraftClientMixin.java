package dev.lowinputlag.mixin;

import dev.lowinputlag.LowInputLagMod;
import dev.lowinputlag.LowInputLagState;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    private static boolean lowinputlag$failed = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void lowinputlag$updateState(CallbackInfo ci) {
        if (lowinputlag$failed) return;
        try {
            LowInputLagState.update((MinecraftClient) (Object) this);
        } catch (Throwable t) {
            lowinputlag$failed = true;
            LowInputLagMod.LOGGER.error("[lowinputlag] state tracking disabled after error", t);
        }
    }
}
