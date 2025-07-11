package dev.aaronhowser.mods.irregular_implements.mixin;

import dev.aaronhowser.mods.irregular_implements.datagen.tag.ModItemTagsProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
abstract public class LivingEntityRendererMixin<T extends LivingEntity> {

	@Inject(
			method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	private void irregular_implements$shouldShowNameMixin(T entity, CallbackInfoReturnable<Boolean> cir) {
		if (entity.getItemBySlot(EquipmentSlot.HEAD).is(ModItemTagsProvider.HIDE_NAME_HELMET)) {
			cir.setReturnValue(false);
		}
	}

}
