package dev.aaronhowser.mods.irregular_implements.item

import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider
import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider.Companion.toGrayComponent
import dev.aaronhowser.mods.irregular_implements.item.component.EntityIdentifierItemComponent
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.util.*

class FilterPlayerItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {
        fun getPlayerName(stack: ItemStack): Component? {
            return stack.get(ModDataComponents.PLAYER)?.name
        }

        fun getPlayerUuid(stack: ItemStack): UUID? {
            return stack.get(ModDataComponents.PLAYER)?.uuid
        }

        fun setPlayer(stack: ItemStack, player: Player) {
            stack.set(
                ModDataComponents.PLAYER.get(),
                EntityIdentifierItemComponent(player)
            )
        }
    }

    override fun interactLivingEntity(
        stack: ItemStack,
        player: Player,
        interactionTarget: LivingEntity,
        usedHand: InteractionHand
    ): InteractionResult {
        if (interactionTarget !is Player) return InteractionResult.FAIL

        val usedStack = player.getItemInHand(usedHand)
        setPlayer(usedStack, interactionTarget)

        return InteractionResult.SUCCESS
    }

    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val usedStack = player.getItemInHand(usedHand)
        setPlayer(usedStack, player)

        return InteractionResultHolder.success(usedStack)
    }

    override fun appendHoverText(stack: ItemStack, context: TooltipContext, tooltipComponents: MutableList<Component>, tooltipFlag: TooltipFlag) {
        val playerName = getPlayerName(stack)

        if (playerName != null) {
            val component = ModLanguageProvider.Tooltips.PLAYER_FILTER_PLAYER
                .toGrayComponent(playerName)

            tooltipComponents.add(component)
        }
    }

}