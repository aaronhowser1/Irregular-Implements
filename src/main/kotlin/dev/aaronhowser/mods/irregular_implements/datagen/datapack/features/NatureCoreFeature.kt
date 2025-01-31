package dev.aaronhowser.mods.irregular_implements.datagen.datapack.features

import dev.aaronhowser.mods.irregular_implements.datagen.tag.ModBiomeTagsProvider
import dev.aaronhowser.mods.irregular_implements.registry.ModBlocks
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

class NatureCoreFeature : Feature<NoneFeatureConfiguration>(NoneFeatureConfiguration.CODEC) {

    companion object {
        private val natureCore: BlockState = ModBlocks.NATURE_CORE.get().defaultBlockState()

        private fun getLogFromBiome(biome: Holder<Biome>): BlockState {
            return when {
                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_BIRCH) -> Blocks.BIRCH_LOG

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_DARK_OAK) -> Blocks.DARK_OAK_LOG

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_JUNGLE) -> Blocks.JUNGLE_LOG

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_SPRUCE) -> Blocks.SPRUCE_LOG

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_ACACIA) -> Blocks.ACACIA_LOG

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_MANGROVE) -> Blocks.MANGROVE_LOG

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_OAK) -> Blocks.OAK_LOG
                else -> Blocks.OAK_LOG
            }.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)
        }

        private fun getLeavesFromBiome(biome: Holder<Biome>): BlockState {
            return when {
                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_BIRCH) -> Blocks.BIRCH_LEAVES

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_DARK_OAK) -> Blocks.DARK_OAK_LEAVES

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_JUNGLE) -> Blocks.JUNGLE_LEAVES

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_SPRUCE) -> Blocks.SPRUCE_LEAVES

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_ACACIA) -> Blocks.ACACIA_LEAVES

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_MANGROVE) -> Blocks.MANGROVE_LEAVES

                biome.`is`(ModBiomeTagsProvider.NATURE_CORE_OAK) -> Blocks.OAK_LEAVES
                else -> Blocks.OAK_LEAVES
            }.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true)
        }

    }

    override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
        val origin = context.origin()
        val level = context.level()
        val random = context.random()

        val biome = level.getBiome(origin)

        val log = getLogFromBiome(biome)
        val leaves = getLeavesFromBiome(biome)

        for (dX in -1..1) for (dY in -1..1) for (dZ in -1..1) {

            val offsetPos = origin.offset(dX, dY, dZ)

            if (dY == 0) {
                if (dX == 0 && dZ == 0) {
                    level.setBlock(offsetPos, natureCore, 1 or 3)
                } else if (dX == 0 || dZ == 0) {
                    level.setBlock(offsetPos, leaves, 1 or 3)
                } else {
                    level.setBlock(offsetPos, log, 1 or 3)
                }

                continue
            }

            if ((dX == 0 || dZ == 0) && dX != dZ) {
                level.setBlock(offsetPos, log, 1 or 3)
            } else {
                level.setBlock(offsetPos, leaves, 1 or 3)
            }

        }

        val positionAbove = origin.above(4)

        val chestPlaceRadius = 7
        var chestTries = 0

        while (chestTries < chestPlaceRadius * chestPlaceRadius) {
            val dX = random.nextInt(chestPlaceRadius * 2) - chestPlaceRadius
            val dZ = random.nextInt(chestPlaceRadius * 2) - chestPlaceRadius

            var chestPos = positionAbove.offset(dX, 0, dZ)
            while (!level.isOutsideBuildHeight(chestPos) && level.isEmptyBlock(chestPos)) {
                chestPos = chestPos.below()
            }

            chestPos = chestPos.above()
            if (!level.isEmptyBlock(chestPos)) {
                chestTries++
                continue
            }

            level.setBlock(chestPos, Blocks.CHEST.defaultBlockState(), 1 or 3)
            break
        }

        return true
    }

}