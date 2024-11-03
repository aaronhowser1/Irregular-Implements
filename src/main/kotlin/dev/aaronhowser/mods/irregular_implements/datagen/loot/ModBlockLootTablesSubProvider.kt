package dev.aaronhowser.mods.irregular_implements.datagen.loot

import dev.aaronhowser.mods.irregular_implements.registries.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block

class ModBlockLootTablesSubProvider(
    provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

    override fun generate() {

        for (block in knownBlocks - nonDropSelfBlocks) {
            dropSelf(block)
        }

        for (block in blocksWithoutDrops) {
            add(block, noDrop())
        }

    }

    private val blocksWithoutDrops = listOf(
        ModBlocks.BLAZE_FIRE
    ).map { it.get() }.toSet()

    private val nonDropSelfBlocks: Set<Block> = blocksWithoutDrops

    override fun getKnownBlocks(): List<Block> {
        return ModBlocks.BLOCK_REGISTRY.entries.map { it.get() }
    }

}