package dev.aaronhowser.mods.irregular_implements.item

import dev.aaronhowser.mods.irregular_implements.item.component.SpecificEntityItemComponent
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import kotlin.math.atan2

class EmeraldCompassItem : Item(Properties().stacksTo(1)) {

    companion object {

        val ANGLE: ResourceLocation = OtherUtil.modResource("angle")

        fun itemPropertyFunction(
            stack: ItemStack,
            localLevel: ClientLevel?,
            holdingEntity: LivingEntity?,
            int: Int
        ): Float {
            if (localLevel == null || holdingEntity == null) return 0f
            val itemComponent = stack.get(ModDataComponents.SPECIFIC_ENTITY) ?: return 0f

            val playerUuid = itemComponent.uuid
            val targetPlayer = localLevel.getPlayerByUUID(playerUuid) ?: return 0f
            if (targetPlayer == holdingEntity) return 0f

            val angleToTarget = getAngleFromEntityToPos(holdingEntity, targetPlayer.blockPosition())
            val holderYaw = getWrappedVisualRotationY(holdingEntity)

            val adjustedYaw = angleToTarget + 0.25 - holderYaw

            return Mth.positiveModulo(adjustedYaw.toFloat(), 1f)
        }

        private fun getAngleFromEntityToPos(entity: Entity, pos: BlockPos): Double {
            val vec3 = Vec3.atCenterOf(pos)
            return atan2(vec3.z() - entity.z, vec3.x() - entity.x) / (Math.PI * 2).toFloat()
        }

        private fun getWrappedVisualRotationY(entity: Entity): Double {
            return Mth.positiveModulo((entity.visualRotationYInDegrees / 360.0f).toDouble(), 1.0)
        }
    }

    //TODO: Crafting recipe with ID Card
    override fun interactLivingEntity(
        stack: ItemStack,
        player: Player,
        interactionTarget: LivingEntity,
        usedHand: InteractionHand
    ): InteractionResult {
        if (interactionTarget !is Player) return InteractionResult.PASS

        val usedStack = player.getItemInHand(usedHand)
        usedStack.set(ModDataComponents.SPECIFIC_ENTITY, SpecificEntityItemComponent(interactionTarget))

        return InteractionResult.SUCCESS
    }

}