package dev.aaronhowser.mods.irregular_implements.block.block_entity

import dev.aaronhowser.mods.irregular_implements.menu.iron_dropper.IronDropperMenu
import dev.aaronhowser.mods.irregular_implements.registry.ModBlockEntities
import dev.aaronhowser.mods.irregular_implements.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.Position
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.DispenserBlock
import net.minecraft.world.level.block.DropperBlock.FACING
import net.minecraft.world.level.block.entity.DispenserBlockEntity
import net.minecraft.world.level.block.state.BlockState

class IronDropperBlockEntity(
	pPos: BlockPos,
	pBlockState: BlockState
) : DispenserBlockEntity(ModBlockEntities.IRON_DROPPER.get(), pPos, pBlockState) {

	var shouldShootStraight: Boolean = false
		private set(value) {
			field = value
			setChanged()
		}

	enum class EffectsMode(val hasParticles: Boolean, val hasSound: Boolean) {
		NONE(false, false),
		PARTICLES(true, false),
		SOUND(false, true),
		BOTH(true, true);

		fun next(): EffectsMode {
			return when (this) {
				NONE -> PARTICLES
				PARTICLES -> SOUND
				SOUND -> BOTH
				BOTH -> NONE
			}
		}
	}

	var effectsMode: EffectsMode = EffectsMode.BOTH
		private set(value) {
			field = value
			setChanged()
		}

	enum class PickupDelay(val ticks: Int) {
		ZERO(0),
		FIVE(5),
		TWENTY(20);

		fun next(): PickupDelay {
			return when (this) {
				ZERO -> FIVE
				FIVE -> TWENTY
				TWENTY -> ZERO
			}
		}
	}

	var pickupDelay: PickupDelay = PickupDelay.TWENTY
		private set(value) {
			field = value
			setChanged()
		}

	enum class RedstoneMode {
		PULSE,
		CONTINUOUS_POWERED,
		CONTINUOUS;

		fun next(): RedstoneMode {
			return when (this) {
				PULSE -> CONTINUOUS_POWERED
				CONTINUOUS_POWERED -> CONTINUOUS
				CONTINUOUS -> PULSE
			}
		}
	}

	var redstoneMode: RedstoneMode = RedstoneMode.PULSE
		private set(value) {
			field = value
			setChanged()
		}

	private val containerData = object : SimpleContainerData(CONTAINER_DATA_SIZE) {

		override fun get(index: Int): Int {
			return when (index) {
				SHOOT_STRAIGHT_INDEX -> if (this@IronDropperBlockEntity.shouldShootStraight) 1 else 0
				EFFECTS_MODE_INDEX -> EffectsMode.entries.indexOf(this@IronDropperBlockEntity.effectsMode)
				PICKUP_DELAY_INDEX -> PickupDelay.entries.indexOf(this@IronDropperBlockEntity.pickupDelay)
				REDSTONE_MODE_INDEX -> RedstoneMode.entries.indexOf(this@IronDropperBlockEntity.redstoneMode)
				else -> 0
			}
		}

		override fun set(index: Int, value: Int) {
			when (index) {
				SHOOT_STRAIGHT_INDEX -> this@IronDropperBlockEntity.shouldShootStraight = value != 0
				EFFECTS_MODE_INDEX -> this@IronDropperBlockEntity.effectsMode = EffectsMode.entries[value]
				PICKUP_DELAY_INDEX -> this@IronDropperBlockEntity.pickupDelay = PickupDelay.entries[value]
				REDSTONE_MODE_INDEX -> this@IronDropperBlockEntity.redstoneMode = RedstoneMode.entries[value]
			}
		}
	}

	val dispenseBehavior = object : DefaultDispenseItemBehavior() {
		override fun playSound(blockSource: BlockSource) {
			if (this@IronDropperBlockEntity.effectsMode.hasSound) super.playSound(blockSource)
		}

		override fun playAnimation(blockSource: BlockSource, direction: Direction) {
			if (this@IronDropperBlockEntity.effectsMode.hasParticles) super.playAnimation(blockSource, direction)
		}

		override fun execute(blockSource: BlockSource, chosenStack: ItemStack): ItemStack {
			val direction = blockSource.state().getValue(FACING)
			val position = DispenserBlock.getDispensePosition(blockSource)
			val stackToShoot = chosenStack.split(1)

			val speed = 6

			shoot(blockSource.level, stackToShoot, speed, direction, position, this@IronDropperBlockEntity.shouldShootStraight)

			return chosenStack
		}

		private fun shoot(level: Level, stack: ItemStack, speed: Int, facing: Direction, position: Position, shootForward: Boolean) {
			val x = position.x()
			val y = position.y() - if (facing.axis == Direction.Axis.Y) 0.125 else 0.15625
			val z = position.z()

			val itemEntity = ItemEntity(level, x, y, z, stack)

			if (shootForward) {
				itemEntity.setDeltaMovement(
					facing.stepX * speed * 0.1,
					facing.stepY * speed * 0.1,
					facing.stepZ * speed * 0.1
				)
			} else {
				val offset = level.random.nextDouble() * 0.1 + 0.2

				itemEntity.setDeltaMovement(
					level.random.triangle(facing.stepX.toDouble() * offset, 0.0172275 * speed.toDouble()),
					level.random.triangle(0.2, 0.0172275 * speed.toDouble()),
					level.random.triangle(facing.stepZ.toDouble() * offset, 0.0172275 * speed.toDouble())
				)
			}

			itemEntity.setPickUpDelay(this@IronDropperBlockEntity.pickupDelay.ticks)

			level.addFreshEntity(itemEntity)
		}
	}

	private fun tick() {
		val level = this.level as? ServerLevel ?: return

		val shouldDispense = level.gameTime % 4 == 0L && (
				this.redstoneMode == RedstoneMode.CONTINUOUS ||
						(this.redstoneMode == RedstoneMode.CONTINUOUS_POWERED && level.hasNeighborSignal(this.worldPosition))
				)

		if (!shouldDispense) return

		ModBlocks.IRON_DROPPER.get().dispenseFrom(level, this.blockState, this.worldPosition)
	}

	override fun getDefaultName(): Component {
		return ModBlocks.IRON_DROPPER.get().name
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return IronDropperMenu(containerId, playerInventory, this, containerData)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putBoolean(SHOOT_STRAIGHT_NBT, this.shouldShootStraight)
		tag.putInt(EFFECTS_MODE_NBT, this.effectsMode.ordinal)
		tag.putInt(PICKUP_DELAY_NBT, this.pickupDelay.ordinal)
		tag.putInt(REDSTONE_MODE_NBT, this.redstoneMode.ordinal)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		this.shouldShootStraight = tag.getBoolean(SHOOT_STRAIGHT_NBT)
		this.effectsMode = EffectsMode.entries[tag.getInt(EFFECTS_MODE_NBT)]
		this.pickupDelay = PickupDelay.entries[tag.getInt(PICKUP_DELAY_NBT)]
		this.redstoneMode = RedstoneMode.entries[tag.getInt(REDSTONE_MODE_NBT)]
	}

	// Syncs with client
	override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(pRegistries)
	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	companion object {
		const val CONTAINER_DATA_SIZE = 4

		const val SHOOT_STRAIGHT_INDEX = 0
		const val EFFECTS_MODE_INDEX = 1
		const val PICKUP_DELAY_INDEX = 2
		const val REDSTONE_MODE_INDEX = 3

		const val SHOOT_STRAIGHT_NBT = "ShootStraight"
		const val EFFECTS_MODE_NBT = "EffectsMode"
		const val PICKUP_DELAY_NBT = "PickupDelay"
		const val REDSTONE_MODE_NBT = "RedstoneMode"

		fun tick(
			level: Level,
			pos: BlockPos,
			state: BlockState,
			blockEntity: IronDropperBlockEntity
		) {
			blockEntity.tick()
		}
	}

}