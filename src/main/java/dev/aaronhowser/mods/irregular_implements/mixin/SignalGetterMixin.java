package dev.aaronhowser.mods.irregular_implements.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.aaronhowser.mods.irregular_implements.PoweredRedstoneInterfaces;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.SignalGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SignalGetter.class)
public interface SignalGetterMixin {

    @ModifyReturnValue(
            method = "getSignal",
            at = @At("RETURN")
    )
    default int getSignal(int original, BlockPos pos, Direction direction) {
        return original >= 15 || !(this instanceof PoweredRedstoneInterfaces f)
                ? original
                : Math.max(original, f.irregular_implements$getStrongPower(pos, direction));
    }

}
