package dev.aaronhowser.mods.irregular_implements.block.block_entity

import dev.aaronhowser.mods.irregular_implements.block.DiaphanousBlock
import dev.aaronhowser.mods.irregular_implements.registries.ModBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class DiaphanousBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(ModBlockEntities.DIAPHANOUS_BLOCK.get(), pPos, pBlockState) {

    var blockToRender: ItemStack = Items.STONE.defaultInstance
    var alpha: Float = 1f

    companion object {

        fun tick(level: Level, blockPos: BlockPos, blockState: BlockState) {
            val nearestPlayerDistance = level
                .players()
                .asSequence()
                .mapNotNull { if (!it.isAlive || it.isSpectator) null else it.distanceToSqr(blockPos.toVec3()) }
                .minOrNull()

            val blockEntity = level.getBlockEntity(blockPos) as? DiaphanousBlockEntity ?: return

            blockEntity.alpha = if (nearestPlayerDistance == null || nearestPlayerDistance > 4 * 4) {
                1f
            } else {
                0.1f
            }

            val newState = blockState.setValue(DiaphanousBlock.NOT_SOLID, blockEntity.alpha < 1f)
            level.setBlock(blockPos, newState, 3)
        }

    }

}