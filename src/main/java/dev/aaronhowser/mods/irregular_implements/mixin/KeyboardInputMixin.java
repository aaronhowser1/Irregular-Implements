package dev.aaronhowser.mods.irregular_implements.mixin;

import dev.aaronhowser.mods.irregular_implements.KeyboardInputFunctions;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input implements KeyboardInputFunctions {

    @Shadow
    @Final
    private Options options;

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    private void irregular_implements$invertWhenCollapse(boolean isSneaking, float sneakingSpeedMultiplier, CallbackInfo ci) {
        checkInvert(options);
    }
}
