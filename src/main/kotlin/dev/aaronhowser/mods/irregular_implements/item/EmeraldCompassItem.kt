package dev.aaronhowser.mods.irregular_implements.item

import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil
import dev.aaronhowser.mods.irregular_implements.util.SpecificEntity
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.math.atan2

class EmeraldCompassItem(properties: Properties) : Item(properties) {

	//TODO: Crafting recipe with Player Filter
	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		interactionTarget: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		if (interactionTarget !is Player) return InteractionResult.PASS

		val usedStack = player.getItemInHand(usedHand)
		usedStack.set(ModDataComponents.ENTITY_IDENTIFIER, SpecificEntity(interactionTarget))

		return InteractionResult.SUCCESS
	}

	override fun appendHoverText(stack: ItemStack, context: TooltipContext, tooltipComponents: MutableList<Component>, tooltipFlag: TooltipFlag) {
		val component = stack.get(ModDataComponents.ENTITY_IDENTIFIER) ?: return

		tooltipComponents.add(component.name)
	}

	companion object {
		val DEFAULT_PROPERTIES = Properties().stacksTo(1)

		val ANGLE: ResourceLocation = OtherUtil.modResource("angle")

		private const val DEFAULT = 0.52f

		//FIXME: 0.5 is slightly left rather than straight forward
		fun getAngleFloat(
			stack: ItemStack,
			localLevel: ClientLevel?,
			holdingEntity: LivingEntity?,
			int: Int
		): Float {
			if (localLevel == null || holdingEntity == null) return DEFAULT
			val itemComponent = stack.get(ModDataComponents.ENTITY_IDENTIFIER) ?: return DEFAULT

			val playerUuid = itemComponent.uuid
			val targetPlayer = localLevel.getPlayerByUUID(playerUuid) ?: return DEFAULT
			if (targetPlayer == holdingEntity) return DEFAULT

			val angleToTarget = getAngleToTargetEntity(holdingEntity, targetPlayer)
			val holderYaw = getWrappedVisualRotationY(holdingEntity)

			val adjustedYaw = angleToTarget + 0.25 - holderYaw

			return Mth.positiveModulo(adjustedYaw.toFloat(), 1f)
		}

		private fun getAngleToTargetEntity(entity: Entity, targetEntity: Entity): Double {
			val vec3 = targetEntity.position()
			return atan2(vec3.z() - entity.z, vec3.x() - entity.x) / (Math.PI * 2).toFloat()
		}

		private fun getWrappedVisualRotationY(entity: Entity): Double {
			return Mth.positiveModulo((entity.visualRotationYInDegrees / 360.0f).toDouble(), 1.0)
		}
	}

}