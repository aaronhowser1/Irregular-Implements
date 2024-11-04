package dev.aaronhowser.mods.irregular_implements.item

import net.minecraft.core.Holder
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.*
import net.minecraft.world.level.Level

class ImbueItem(
    val imbueHolder: Holder<MobEffect>
) : Item(
    Properties()
        .stacksTo(1)
) {

    override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.DRINK
    override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = 32

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        return ItemUtils.startUsingInstantly(level, player, usedHand)
    }

    override fun finishUsingItem(stack: ItemStack, level: Level, livingEntity: LivingEntity): ItemStack {
        val mobEffectInstance = MobEffectInstance(
            imbueHolder,
            20 * 60 * 60 * 20,
            0
        )

        livingEntity.addEffect(mobEffectInstance)

        if (livingEntity is Player && !livingEntity.hasInfiniteMaterials()) {
            stack.shrink(1)

            val bottleStack = Items.GLASS_BOTTLE.defaultInstance
            if (!livingEntity.addItem(bottleStack)) {
                livingEntity.drop(bottleStack, false)
            }
        }

        return stack
    }

}