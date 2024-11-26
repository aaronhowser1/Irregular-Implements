package dev.aaronhowser.mods.irregular_implements.block

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.irregular_implements.registries.ModBlocks
import dev.aaronhowser.mods.irregular_implements.util.ServerScheduler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.AABB
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class EnderBridgeBlock(
    val distancePerTick: Int,
    properties: Properties = Properties
        .ofFullCopy(Blocks.OBSIDIAN)
) : DirectionalBlock(properties) {

    companion object {
        val CODEC: MapCodec<EnderBridgeBlock> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.INT
                    .fieldOf("distance_per_tick")
                    .forGetter(EnderBridgeBlock::distancePerTick),
                propertiesCodec()
            ).apply(instance, ::EnderBridgeBlock)
        }

        val ENABLED: BooleanProperty = BlockStateProperties.ENABLED

        //TODO: Config
        private const val MAX_ITERATIONS = Int.MAX_VALUE

        /**
         * Searches for an anchor block in the given direction.
         * Searches through unloaded chunks and non-full blocks.
         * Only stops searching if it reaches the maximum iterations, or if it's found an Anchor.
         */
        fun searchForAnchor(
            level: ServerLevel,
            distanceToSearch: Int,
            bridgePos: BlockPos,
            searchOrigin: BlockPos,
            direction: Direction,
            iterations: Int
        ) {
            if (iterations >= MAX_ITERATIONS) {
                turnOffBridge(level, bridgePos)
                return
            }

            val players = level.players()

            for (i in 0 until distanceToSearch) {
                val pos = searchOrigin.relative(direction, i)
                if (!level.isLoaded(pos)) continue

                if (iterations % 4 == 0) {
                    val particleCount = (1 + Mth.ceil(iterations.toDouble() / 20)).coerceIn(1, 100)
                    val particleSpeed = (0.5 + iterations / 30.0).coerceIn(0.5, 3.0)

                    for (player in players) {
                        level.sendParticles(
                            ParticleTypes.PORTAL,
                            bridgePos.x + 0.5,
                            bridgePos.y + 2.5,
                            bridgePos.z + 0.5,
                            particleCount,
                            0.0,
                            0.0,
                            0.0,
                            particleSpeed
                        )
                    }
                }

                val state = level.getBlockState(pos)
                if (state.`is`(ModBlocks.ENDER_ANCHOR)) {
                    val particleCount = (1 + Mth.ceil(iterations.toDouble() / 20)).coerceIn(10, 100)
                    for (player in players) {
                        level.sendParticles(
                            ParticleTypes.REVERSE_PORTAL,
                            pos.x + 0.5,
                            pos.y + 1.5,
                            pos.z + 0.5,
                            particleCount,
                            0.0,
                            0.0,
                            0.0,
                            0.5
                        )
                    }

                    foundAnchor(level, bridgePos, pos)
                    return
                }

                if (state.isCollisionShapeFullBlock(level, pos)) {
                    turnOffBridge(level, bridgePos)
                    return
                }
            }

            ServerScheduler.scheduleTaskInTicks(1) {
                searchForAnchor(
                    level = level,
                    distanceToSearch = distanceToSearch,
                    bridgePos = bridgePos,
                    searchOrigin = searchOrigin.relative(direction),
                    direction = direction,
                    iterations = iterations + distanceToSearch
                )
            }
        }

        private fun foundAnchor(
            level: Level,
            bridgePos: BlockPos,
            anchorPos: BlockPos
        ) {
            val entitiesOnBridge = level.getEntities(
                null,
                AABB.ofSize(bridgePos.above().toVec3(), 1.1, 2.5, 1.1)
            )

            for (entity in entitiesOnBridge) {
                entity.teleportTo(
                    anchorPos.x + 0.5,
                    anchorPos.y + 1.0,
                    anchorPos.z + 0.5
                )
            }

            turnOffBridge(level, bridgePos)
        }

        private fun turnOffBridge(
            level: Level,
            bridgePos: BlockPos
        ) {
            val state = level.getBlockState(bridgePos)
            if (!state.getValue(ENABLED)) return

            val newState = state.setValue(ENABLED, false)
            level.setBlockAndUpdate(bridgePos, newState)
        }
    }

    override fun codec(): MapCodec<EnderBridgeBlock> = CODEC

    init {
        registerDefaultState(
            stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ContactLever.ENABLED, false)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING, ENABLED)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        return defaultBlockState()
            .setValue(FACING, context.nearestLookingDirection.opposite)
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    ) {
        if (level !is ServerLevel) return

        val isPowered = level.hasNeighborSignal(pos)
        if (!isPowered) return
        if (state.getValue(ENABLED)) return

        val newState = state.setValue(ENABLED, true)
        level.setBlockAndUpdate(pos, newState)

        val direction = state.getValue(FACING)
        searchForAnchor(
            level = level,
            distanceToSearch = distancePerTick,
            bridgePos = pos,
            searchOrigin = pos.relative(direction),
            direction = direction,
            iterations = 0
        )
    }

}