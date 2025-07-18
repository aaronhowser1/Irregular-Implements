package dev.aaronhowser.mods.irregular_implements.block

import dev.aaronhowser.mods.irregular_implements.block.block_entity.GlobalChatDetectorBlockEntity
import dev.aaronhowser.mods.irregular_implements.registry.ModBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.BlockHitResult

class GlobalChatDetectorBlock : EntityBlock, Block(
	Properties
		.ofFullCopy(Blocks.DISPENSER)
) {

	init {
		registerDefaultState(
			defaultBlockState()
				.setValue(ENABLED, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(ENABLED)
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return GlobalChatDetectorBlockEntity(pos, state)
	}

	override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(blockEntityType, ModBlockEntities.GLOBAL_CHAT_DETECTOR.get(), GlobalChatDetectorBlockEntity::tick)
	}

	override fun useWithoutItem(
		pState: BlockState,
		pLevel: Level,
		pPos: BlockPos,
		pPlayer: Player,
		pHitResult: BlockHitResult
	): InteractionResult {
		val blockEntity = pLevel.getBlockEntity(pPos) as? GlobalChatDetectorBlockEntity ?: return InteractionResult.FAIL

		pPlayer.openMenu(blockEntity)
		blockEntity.sendStringUpdate()

		return InteractionResult.SUCCESS
	}

	override fun canConnectRedstone(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction?): Boolean {
		return true
	}

	override fun isSignalSource(state: BlockState): Boolean {
		return true
	}

	override fun getDirectSignal(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
		return if (state.getValue(ENABLED)) 15 else 0
	}

	override fun getSignal(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
		return getDirectSignal(state, level, pos, direction)
	}

	companion object {
		val ENABLED: BooleanProperty = BlockStateProperties.ENABLED
	}

}