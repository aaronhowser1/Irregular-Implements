package dev.aaronhowser.mods.irregular_implements.block.block_entity

import dev.aaronhowser.mods.irregular_implements.block.block_entity.base.RedstoneInterfaceBlockEntity
import dev.aaronhowser.mods.irregular_implements.block.block_entity.base.RedstoneToolLinkable
import dev.aaronhowser.mods.irregular_implements.registries.ModBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class RedstoneInterfaceBasicBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : RedstoneToolLinkable, RedstoneInterfaceBlockEntity(
    ModBlockEntities.REDSTONE_INTERFACE.get(),
    pPos,
    pBlockState
) {

    companion object {
        fun tick(level: Level, blockPos: BlockPos, blockState: BlockState) {
            val blockEntity = level.getBlockEntity(blockPos) as? RedstoneInterfaceBasicBlockEntity ?: return

            val linkedPos = blockEntity.linkedPos ?: return
            if (!level.isLoaded(linkedPos)) return


        }
    }

    override var linkedPos: BlockPos? = null
        set(value) {
            field = value
            setChanged()
        }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        val linkedPos = linkedPos
        if (linkedPos != null) {
            tag.putLong(RedstoneToolLinkable.LINKED_POS_NBT, linkedPos.asLong())
        }
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        if (tag.contains(RedstoneToolLinkable.LINKED_POS_NBT)) {
            linkedPos = BlockPos.of(tag.getLong(RedstoneToolLinkable.LINKED_POS_NBT))
        }
    }

}