package dev.aaronhowser.mods.irregular_implements.entity

import com.google.common.collect.HashMultimap
import dev.aaronhowser.mods.irregular_implements.registry.ModEntityTypes
import dev.aaronhowser.mods.irregular_implements.registry.ModItems
import dev.aaronhowser.mods.irregular_implements.util.ClientUtil
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.BlockAndTintGetter
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.CommonLevelAccessor
import net.minecraft.world.level.Level

class IlluminatorEntity(
    entityType: EntityType<*>,
    level: Level
) : Entity(entityType, level) {

    companion object {
        private val illuminatedChunks: HashMultimap<Level, Long> = HashMultimap.create()

        @JvmStatic
        fun isChunkIlluminated(blockPos: BlockPos, blockAndTintGetter: BlockAndTintGetter): Boolean {
            val level: Level = when (blockAndTintGetter) {
                is Level -> blockAndTintGetter

                // If it's something that can be accessed on server, but isn't a Level, return false before it tries to load client-only class
                is CommonLevelAccessor -> return false

                else -> ClientUtil.levelFromBlockAndTintGetter(blockAndTintGetter) ?: return false
            }

            val chunkPos = ChunkPos(blockPos)

            return illuminatedChunks[level].contains(chunkPos.toLong())
        }
    }

    //TODO:
    // - Fancy rendering
    // - Slowly move to the center of the chunk floating to like a couple blocks above the surface
    // - Punch it to break

    override fun onAddedToLevel() {
        super.onAddedToLevel()

        val chunkPos = ChunkPos(this.blockPosition())

        illuminatedChunks[level()].add(chunkPos.toLong())
    }

    override fun onRemovedFromLevel() {
        super.onRemovedFromLevel()

        val chunkPos = ChunkPos(this.blockPosition())
        illuminatedChunks[level()].remove(chunkPos.toLong())

        val itemEntity = ItemEntity(level(), position().x, position().y, position().z, ModItems.SPECTRE_ILLUMINATOR.toStack())
        level().addFreshEntity(itemEntity)
    }

    constructor(level: Level) : this(ModEntityTypes.ILLUMINATOR.get(), level)

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
    }
}