package dev.aaronhowser.mods.irregular_implements.entity

import dev.aaronhowser.mods.irregular_implements.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.irregular_implements.registries.ModEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

class TimeAcceleratorEntity(
    entityType: EntityType<TimeAcceleratorEntity>,
    level: Level
) : Entity(entityType, level) {

    constructor(level: Level) : this(ModEntityTypes.TIME_ACCELERATOR.get(), level)

    companion object {
        val TICK_RATE: EntityDataAccessor<Int> = SynchedEntityData.defineId(TimeAcceleratorEntity::class.java, EntityDataSerializers.INT)
        val TIME_REMAINING: EntityDataAccessor<Int> = SynchedEntityData.defineId(TimeAcceleratorEntity::class.java, EntityDataSerializers.INT)

        const val TICK_RATE_NBT = "tick_rate"
        const val TIME_REMAINING_NBT = "time_remaining"
    }

    override fun tick() {
        val level = level() as? ServerLevel ?: return

        val pos = blockPosition()
        val blockState = level.getBlockState(pos)

        if (blockState.`is`(ModBlockTagsProvider.CANNOT_ACCELERATE)) {
            this.discard()
            return
        }

        val blockEntity = level.getBlockEntity(pos)

        @Suppress("UNCHECKED_CAST")
        val blockEntityTicker =
            blockEntity?.blockState?.getTicker(level, blockEntity.type) as? BlockEntityTicker<BlockEntity>?

        var ticked = false
        for (i in 0 until tickRate) {
            // Why that number?
            if (blockState.isRandomlyTicking && level.random.nextInt(1385) == 0) {
                blockState.randomTick(level, pos, level.random)
                ticked = true
            }

            if (blockEntityTicker != null && blockEntity.type.isValid(blockState)) {
                blockEntityTicker.tick(level, pos, blockState, blockEntity)
                ticked = true
            }

            if (!ticked) {
                this.discard()
                return
            }
        }

        timeRemaining--
        if (timeRemaining <= 0) {
            this.discard()
        }

    }

    var tickRate: Int
        get() = entityData.get(TICK_RATE)
        set(value) = entityData.set(TICK_RATE, value)

    var timeRemaining: Int
        get() = entityData.get(TIME_REMAINING)
        set(value) = entityData.set(TIME_REMAINING, value)

    override fun readAdditionalSaveData(compound: CompoundTag) {
        tickRate = compound.getInt(TICK_RATE_NBT)
        timeRemaining = compound.getInt(TIME_REMAINING_NBT)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        compound.putInt(TICK_RATE_NBT, tickRate)
        compound.putInt(TIME_REMAINING_NBT, timeRemaining)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(TICK_RATE, 1)
        builder.define(TIME_REMAINING, 0)
    }

    override fun isColliding(pos: BlockPos, state: BlockState): Boolean = false

    override fun limitPistonMovement(pos: Vec3): Vec3 = Vec3.ZERO

}