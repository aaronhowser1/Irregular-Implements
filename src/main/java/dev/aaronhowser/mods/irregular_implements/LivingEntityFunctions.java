package dev.aaronhowser.mods.irregular_implements;

import dev.aaronhowser.mods.irregular_implements.datagen.tag.ModBlockTagsProvider;
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityFunctions {

	default boolean checkShouldDiscardFriction() {
		LivingEntity livingEntity = (LivingEntity) this;

		if (livingEntity.getDeltaMovement().lengthSqr() > 1f) return false;

		if (livingEntity
				.getItemBySlot(EquipmentSlot.FEET)
				.has(ModDataComponents.LUBRICATED)
		) return true;

		return livingEntity.level()
				.getBlockState(livingEntity.getBlockPosBelowThatAffectsMyMovement())
				.is(ModBlockTagsProvider.SUPER_LUBRICATED);
	}

}
