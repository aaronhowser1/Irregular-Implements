package dev.aaronhowser.mods.irregular_implements.handler.floo

import dev.aaronhowser.mods.irregular_implements.block.block_entity.FlooBrickBlockEntity
import dev.aaronhowser.mods.irregular_implements.packet.ModPacketHandler
import dev.aaronhowser.mods.irregular_implements.packet.server_to_client.BurningFlooFireplacePacket
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import java.util.*

class FlooFireplace(
	val masterUuid: UUID,
	val name: String?,
	val masterBlockPos: BlockPos
) {

	fun toTag(): CompoundTag {
		val tag = CompoundTag()
		tag.putUUID(NBT_MASTER_UUID, masterUuid)
		if (name != null) tag.putString(NBT_NAME, name)
		tag.putLong(NBT_BLOCK_POS, masterBlockPos.asLong())

		return tag
	}

	fun getBlockEntity(level: Level): FlooBrickBlockEntity? {
		return level.getBlockEntity(masterBlockPos) as? FlooBrickBlockEntity?
	}

	fun teleportFromThis(player: ServerPlayer, target: String): Boolean {
		val level = player.serverLevel()

		val data = FlooNetworkSavedData.get(level)
		val fireplace = data.findFireplace(target)

		if (fireplace == null) {
			player.displayClientMessage(Component.literal("Could not find fireplace named '$target'"), true)
			return false
		} else if (fireplace == this) {
			val name = name ?: target
			player.displayClientMessage(Component.literal("You are already at '$name'"), true)
			return false
		}

		val success = fireplace.teleportToThis(player)

		if (success) {
			val myBe = this.getBlockEntity(level)
			if (myBe != null) {
				val bricks = myBe.children + myBe.blockPos
				val packet = BurningFlooFireplacePacket(bricks)

				ModPacketHandler.messageNearbyPlayers(packet, level, this.masterBlockPos.center, 64.0)
			}
		}

		return success
	}

	fun teleportToThis(player: ServerPlayer): Boolean {
		val level = player.serverLevel()
		val be = level.getBlockEntity(this.masterBlockPos) as? FlooBrickBlockEntity

		if (be == null) {
			player.displayClientMessage(Component.literal("The fireplace at ${this.masterBlockPos} is no longer valid"), true)

			val network = FlooNetworkSavedData.get(level)
			network.removeFireplace(this.masterUuid)

			return false
		}

		val destination = this.masterBlockPos.above().bottomCenter

		player.teleportTo(
			level,
			destination.x, destination.y, destination.z,
			be.facing.toYRot(), player.xRot
		)

		if (name != null) {
			player.displayClientMessage(Component.literal("Teleported to '$name'"), true)
		}

		val bricks = be.children + be.blockPos
		val packet = BurningFlooFireplacePacket(bricks)

		ModPacketHandler.messageNearbyPlayers(packet, level, this.masterBlockPos.center, 64.0)


		return true
	}

	companion object {
		const val NBT_MASTER_UUID = "MasterUUID"
		const val NBT_NAME = "Name"
		const val NBT_BLOCK_POS = "MasterBlockPos"

		fun fromTag(tag: CompoundTag): FlooFireplace {
			val uuid = tag.getUUID(NBT_MASTER_UUID)
			val name = tag.getString(NBT_NAME).ifBlank { null }
			val blockPos = BlockPos.of(tag.getLong(NBT_BLOCK_POS))

			return FlooFireplace(uuid, name, blockPos)
		}

	}

}