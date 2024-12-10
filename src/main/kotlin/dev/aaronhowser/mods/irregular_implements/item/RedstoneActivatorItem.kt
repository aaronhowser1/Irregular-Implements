package dev.aaronhowser.mods.irregular_implements.item

import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.util.RedstoneHandlerSavedData
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class RedstoneActivatorItem : Item(
    Properties()
        .stacksTo(1)
        .component(ModDataComponents.ACTIVATOR_DURATION, Duration.MEDIUM.serializedName)
) {

    enum class Duration(val ticks: Int) : StringRepresentable {
        SHORT(2),
        MEDIUM(20),
        LONG(20 * 5)

        ;

        override fun getSerializedName(): String {
            return name
        }

        fun next(): Duration {
            return when (this) {
                SHORT -> MEDIUM
                MEDIUM -> LONG
                LONG -> SHORT
            }
        }
    }

    companion object {
        fun cycleDuration(stack: ItemStack) {
            val newDuration = getDuration(stack).next()
            stack.set(ModDataComponents.ACTIVATOR_DURATION, newDuration.serializedName)
        }

        private fun getDuration(itemStack: ItemStack): Duration {
            val durationName = itemStack.get(ModDataComponents.ACTIVATOR_DURATION) ?: return Duration.MEDIUM
            return Duration.valueOf(durationName)
        }

    }

    override fun use(
        level: Level,
        player: Player,
        usedHand: InteractionHand
    ): InteractionResultHolder<ItemStack> {
        val usedStack = player.getItemInHand(usedHand)
        if (level.isClientSide) return InteractionResultHolder.pass(usedStack)
        if (!player.isSecondaryUseActive) return InteractionResultHolder.pass(usedStack)

        cycleDuration(usedStack)

        val newDuration = usedStack.get(ModDataComponents.ACTIVATOR_DURATION) ?: return InteractionResultHolder.fail(usedStack)
        val component = newDuration.toString()
            .toComponent()
            .withStyle(ChatFormatting.RED)

        player.displayClientMessage(component, true)

        return InteractionResultHolder.success(usedStack)
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level as? ServerLevel ?: return InteractionResult.PASS

        val usedStack = context.itemInHand
        val duration = getDuration(usedStack)

        val clickedPos = context.clickedPos

        RedstoneHandlerSavedData.addSignal(
            level = level,
            blockPos = clickedPos,
            duration = duration.ticks,
            strength = 15
        )

        return InteractionResult.SUCCESS
    }

    override fun appendHoverText(stack: ItemStack, context: TooltipContext, tooltipComponents: MutableList<Component>, tooltipFlag: TooltipFlag) {
        val duration = stack.get(ModDataComponents.ACTIVATOR_DURATION) ?: return

        val component = "Duration: $duration"
            .toComponent()
            .withStyle(ChatFormatting.GRAY)

        tooltipComponents.add(component)
    }

}