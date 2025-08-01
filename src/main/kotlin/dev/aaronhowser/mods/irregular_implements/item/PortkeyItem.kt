package dev.aaronhowser.mods.irregular_implements.item

import dev.aaronhowser.mods.irregular_implements.entity.PortkeyItemEntity
import dev.aaronhowser.mods.irregular_implements.item.component.LocationDataComponent
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

//TODO: Ability to hide it as other items
class PortkeyItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		val clickedPos = context.clickedPos
		val clickedFace = context.clickedFace

		val posToTeleportTo = if (level.getBlockState(clickedPos).getCollisionShape(level, clickedPos).isEmpty) {
			clickedPos
		} else {
			clickedPos.relative(clickedFace)
		}

		val usedStack = context.itemInHand

		usedStack.set(
			ModDataComponents.LOCATION,
			LocationDataComponent(level, posToTeleportTo)
		)

		return InteractionResult.SUCCESS
	}


	override fun isFoil(stack: ItemStack): Boolean {
		return stack.has(ModDataComponents.LOCATION) || super.isFoil(stack)
	}

	override fun hasCustomEntity(stack: ItemStack): Boolean = stack.has(ModDataComponents.LOCATION)
	override fun createEntity(level: Level, original: Entity, stack: ItemStack): Entity {
		return if (original is ItemEntity) PortkeyItemEntity(original) else original
	}

	//TODO: Tooltip
	override fun appendHoverText(stack: ItemStack, context: TooltipContext, tooltipComponents: MutableList<Component>, tooltipFlag: TooltipFlag) {
		FlooPouchItem.fuckJkr(tooltipComponents)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}