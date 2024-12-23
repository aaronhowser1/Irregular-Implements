package dev.aaronhowser.mods.irregular_implements.block.block_entity

import dev.aaronhowser.mods.irregular_implements.config.ServerConfig
import dev.aaronhowser.mods.irregular_implements.registry.ModBlockEntities
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.LongTag
import net.minecraft.nbt.Tag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.item.FallingBlockEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

//TODO: Menu
class BlockDestabilizerBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(ModBlockEntities.BLOCK_DESTABILIZER.get(), pPos, pBlockState) {

    companion object {
        const val STATE_NBT = "state"
        const val LAZY_NBT = "lazy"

        const val TARGET_STATE_BLOCK_NBT = "target_state_block"

        const val INVALID_BLOCKS_NBT = "invalid_blocks"
        const val ALREADY_CHECKED_NBT = "already_checked"
        const val TO_CHECK_NBT = "to_check"
        const val TARGET_BLOCKS_NBT = "target_blocks"

        const val DROP_COUNTER_NBT = "drop_counter"
        const val TARGET_BLOCKS_SORTED_NBT = "target_blocks_sorted"

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: BlockDestabilizerBlockEntity
        ) {
            if (level.isClientSide) return

            if (blockEntity.state == State.SEARCHING) {
                blockEntity.stepSearch()
            } else if (blockEntity.state == State.DROPPING) {
                blockEntity.dropNextBlock()
            }
        }
    }

    enum class State { IDLE, SEARCHING, DROPPING }

    var state: State = State.IDLE

    val alreadyChecked: HashSet<BlockPos> = hashSetOf()
    val toCheck: ArrayList<BlockPos> = arrayListOf()

    val targetBlockPositions: HashSet<BlockPos> = hashSetOf()

    // Initialized when the BE starts searching, it only accepts positions that have this BlockState
    // TL;DR: If it starts on Obsidian, targetState gets set to Obsidian and only Obsidian blocks are accepted
    var targetBlock: Block? = null

    var targetBlocksSorted: MutableList<BlockPos> = mutableListOf()
    var dropCounter: Int = 0

    // Makes it save the shape of the structure and only search there TODO: Make sure this is working once lazy can be toggled
    var isLazy: Boolean = false
        set(value) {
            field = value
            setChanged()
        }

    val invalidBlocks: HashSet<BlockPos> = hashSetOf()

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        tag.putInt(STATE_NBT, this.state.ordinal)
        tag.putBoolean(LAZY_NBT, this.isLazy)

        if (this.isLazy && this.invalidBlocks.isNotEmpty()) {
            val invalidBlocksTag = ListTag()
            for (blockPos in this.invalidBlocks) {
                val posLong = blockPos.asLong()
                val longTag = LongTag.valueOf(posLong)
                invalidBlocksTag.add(longTag)
            }
            tag.put(INVALID_BLOCKS_NBT, invalidBlocksTag)
        }

        if (this.state == State.SEARCHING) {
            val alreadyCheckedTag = ListTag()
            for (blockPos in this.alreadyChecked) {
                val posLong = blockPos.asLong()
                val longTag = LongTag.valueOf(posLong)
                alreadyCheckedTag.add(longTag)
            }
            tag.put(ALREADY_CHECKED_NBT, alreadyCheckedTag)

            val toCheckTag = ListTag()
            for (blockPos in this.toCheck) {
                val posLong = blockPos.asLong()
                val longTag = LongTag.valueOf(posLong)
                toCheckTag.add(longTag)
            }
            tag.put(TO_CHECK_NBT, toCheckTag)

            val targetBlocksTag = ListTag()
            for (blockPos in this.targetBlockPositions) {
                val posLong = blockPos.asLong()
                val longTag = LongTag.valueOf(posLong)
                targetBlocksTag.add(longTag)
            }
            tag.put(TARGET_BLOCKS_NBT, targetBlocksTag)
        }

        if (this.state == State.DROPPING) {
            tag.putInt(DROP_COUNTER_NBT, this.dropCounter)

            val targetBlocksSortedTag = ListTag()
            for (blockPos in this.targetBlocksSorted) {
                val posLong = blockPos.asLong()
                val longTag = LongTag.valueOf(posLong)
                targetBlocksSortedTag.add(longTag)
            }
            tag.put(TARGET_BLOCKS_SORTED_NBT, targetBlocksSortedTag)
        }

        if (this.targetBlock != null) {
            val targetBlockStateName = BuiltInRegistries.BLOCK.getKey(targetBlock!!).toString()
            tag.putString(TARGET_STATE_BLOCK_NBT, targetBlockStateName)
        }
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        val stateOrdinal = tag.getInt(STATE_NBT)
        this.state = State.entries[stateOrdinal]

        val lazy = tag.getBoolean(LAZY_NBT)
        this.isLazy = lazy

        if (this.isLazy && tag.contains(INVALID_BLOCKS_NBT)) {
            val invalidBlocksTag = tag.getList(INVALID_BLOCKS_NBT, Tag.TAG_LONG.toInt())

            for (tagElement in invalidBlocksTag) {
                val posLong = (tagElement as LongTag).asLong
                val blockPos = BlockPos.of(posLong)
                this.invalidBlocks.add(blockPos)
            }
        }

        if (this.state == State.SEARCHING) {
            val alreadyCheckedTag = tag.getList(ALREADY_CHECKED_NBT, Tag.TAG_LONG.toInt())
            for (tagElement in alreadyCheckedTag) {
                val posLong = (tagElement as LongTag).asLong
                val blockPos = BlockPos.of(posLong)
                this.alreadyChecked.add(blockPos)
            }

            val toCheckTag = tag.getList(TO_CHECK_NBT, Tag.TAG_LONG.toInt())
            for (tagElement in toCheckTag) {
                val posLong = (tagElement as LongTag).asLong
                val blockPos = BlockPos.of(posLong)
                this.toCheck.add(blockPos)
            }

            val targetBlocksTag = tag.getList(TARGET_BLOCKS_NBT, Tag.TAG_LONG.toInt())
            for (tagElement in targetBlocksTag) {
                val posLong = (tagElement as LongTag).asLong
                val blockPos = BlockPos.of(posLong)
                this.targetBlockPositions.add(blockPos)
            }
        }

        if (this.state == State.DROPPING) {
            val dropCounter = tag.getInt(DROP_COUNTER_NBT)
            this.dropCounter = dropCounter

            val targetBlocksSortedTag = tag.getList(TARGET_BLOCKS_SORTED_NBT, Tag.TAG_LONG.toInt())
            for (tagElement in targetBlocksSortedTag) {
                val posLong = (tagElement as LongTag).asLong
                val blockPos = BlockPos.of(posLong)
                this.targetBlocksSorted.add(blockPos)
            }
        }

        if (tag.contains(TARGET_STATE_BLOCK_NBT)) {
            val targetBlockStateName = ResourceLocation.parse(tag.getString(TARGET_STATE_BLOCK_NBT))
            val targetBlock = BuiltInRegistries.BLOCK.get(targetBlockStateName)
            this.targetBlock = targetBlock
        }
    }

    private fun dropNextBlock() {
        val dropCounter = this.dropCounter

        if (dropCounter >= this.targetBlocksSorted.size) {
            this.state = State.IDLE
            this.targetBlocksSorted.clear()
            this.targetBlock = null

            return
        }

        val level = this.level ?: return
        val checkedPos = this.targetBlocksSorted.getOrNull(dropCounter) ?: return
        val checkedState = level.getBlockState(checkedPos)

        val shouldDrop = checkedState.block == this.targetBlock

        if (shouldDrop) {
            FallingBlockEntity.fall(level, checkedPos, checkedState)
        }

        this.dropCounter++
    }

    private fun initDrop() {
        this.targetBlocksSorted = this.targetBlockPositions.sortedWith(
            compareBy<BlockPos> { it.y }
                .thenBy { it.distSqr(this.blockPos) }
        ).toMutableList()

        this.state = State.DROPPING
        this.dropCounter = 0

        this.targetBlockPositions.clear()
        this.toCheck.clear()
        this.alreadyChecked.clear()
    }

    private fun stepSearch() {
        val doneSearching = this.toCheck.isEmpty() || this.targetBlockPositions.count() >= ServerConfig.BLOCK_DESTABILIZER_LIMIT.get()
        if (doneSearching) {
            initDrop()
            return
        }

        val nextPos = this.toCheck.removeFirst()
        if (this.alreadyChecked.contains(nextPos)) return

        this.alreadyChecked.add(nextPos)

        val level = this.level ?: return
        val checkedState = level.getBlockState(nextPos)

        val shouldAdd = checkedState.block == this.targetBlock
        if (shouldAdd) {
            this.targetBlockPositions.add(nextPos)

            for (direction in Direction.entries) {
                val offsetPos = nextPos.relative(direction)
                if (!this.alreadyChecked.contains(offsetPos)) {
                    this.toCheck.add(offsetPos)
                }
            }

        } else if (this.isLazy) {
            this.invalidBlocks.add(nextPos)
        }

        val color = if (shouldAdd) 0x00FF00 else 0xFF0000
        OtherUtil.spawnIndicatorBlockDisplay(level, nextPos, color, 5)
    }

    fun initStart() {
        val level = this.level ?: return

        val facing = this.blockState.getValue(DirectionalBlock.FACING)

        val targetBlockPos = this.blockPos.relative(facing)
        val targetBlockState = level.getBlockState(targetBlockPos)

        if (targetBlockState.isAir) return
        if (targetBlockState.getDestroySpeed(level, targetBlockPos) <= 0) return

        this.targetBlock = targetBlockState.block
        this.state = State.SEARCHING

        this.toCheck.add(targetBlockPos)

        if (this.isLazy) {
            this.alreadyChecked.addAll(this.invalidBlocks)
        } else {
            this.invalidBlocks.clear()
        }
    }

    fun showLazyShape(): Boolean {
        if (state != State.IDLE) return false

        val level = this.level ?: return false

        for (blockPos in this.invalidBlocks) {
            OtherUtil.spawnIndicatorBlockDisplay(level, blockPos, 0xFF0000, 5)
        }

        return true
    }

    fun toggleLazy() {
        if (state != State.IDLE) return

        this.isLazy = !this.isLazy
        if (!this.isLazy) this.invalidBlocks.clear()
    }

    fun resetLazyShape() {
        if (this.state == State.IDLE) this.invalidBlocks.clear()
    }

    // Syncs with client
    override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(pRegistries)
    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

}