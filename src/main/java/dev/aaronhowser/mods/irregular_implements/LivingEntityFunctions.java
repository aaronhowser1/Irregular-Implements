package dev.aaronhowser.mods.irregular_implements;

import dev.aaronhowser.mods.irregular_implements.datagen.tag.ModBlockTagsProvider;
import dev.aaronhowser.mods.irregular_implements.registries.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityFunctions {

    default boolean checkShouldDiscardFriction(LivingEntity livingEntity) {
        if (livingEntity.getDeltaMovement().lengthSqr() > 1f) return false;

        if (livingEntity.getItemBySlot(EquipmentSlot.FEET)
                .is(ModItems.getSUPER_LUBRICANT_BOOTS())
        ) {
            return true;
        }

        return livingEntity.level()
                .getBlockState(livingEntity.getBlockPosBelowThatAffectsMyMovement())
                .is(ModBlockTagsProvider.getSUPER_LUBRICATED());
    }

}
