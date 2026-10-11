package dev.lowinputlag.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mouse.class)
public interface MouseAccessor {
    @Invoker("updateMouse")
    void lowinputlag$callUpdateMouse(double timeDelta);
}
