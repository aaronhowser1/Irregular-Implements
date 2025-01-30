package dev.aaronhowser.mods.irregular_implements.datagen.loot

import dev.aaronhowser.mods.irregular_implements.registry.ModBlocks
import dev.aaronhowser.mods.irregular_implements.registry.ModItems
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import java.util.function.BiConsumer

class ModChestLootSubprovider(
    registries: HolderLookup.Provider
) : LootTableSubProvider {

    override fun generate(output: BiConsumer<ResourceKey<LootTable>, LootTable.Builder>) {

        output.accept(
            LAVA_CHARM_DUNGEON,
            LootTable.lootTable().withPool(lavaCharmDungeon)
        )

        output.accept(
            LAVA_CHARM_NETHER,
            LootTable.lootTable().withPool(lavaCharmNetherPool)
        )

        output.accept(
            SUMMONING_PENDULUM_DUNGEON,
            LootTable.lootTable().withPool(summoningPendulumDungeonPool)
        )

        output.accept(
            SUMMONING_PENDULUM_STRONGHOLD,
            LootTable.lootTable().withPool(summoningPendulumStrongholdPool)
        )

        output.accept(
            MAGIC_HOOD_DUNGEON,
            LootTable.lootTable().withPool(magicHoodDungeonPool)
        )

        output.accept(
            MAGIC_HOOD_WEAPONSMITH,
            LootTable.lootTable().withPool(magicHoodWeaponsmithPool)
        )

        output.accept(
            SLIME_CUBE_DUNGEON,
            LootTable.lootTable().withPool(slimeCubeDungeonPool)
        )

        output.accept(
            SLIME_CUBE_JUNGLE_TEMPLE,
            LootTable.lootTable().withPool(slimeCubeJungleTemplePool)
        )

        output.accept(
            NUMBERED_COIL_DUNGEON,
            LootTable.lootTable().withPool(numberedCoilDungeonPool)
        )

        output.accept(
            NUMBERED_COIL_MINESHAFT,
            LootTable.lootTable().withPool(numberedCoilMineshaftPool)
        )

        //TODO: Biome Crystals

    }

    companion object {
        private fun createPoolRk(name: String): ResourceKey<LootTable> {
            return ResourceKey.create(Registries.LOOT_TABLE, OtherUtil.modResource("chests/$name"))
        }

        private fun singleItemPool(item: Item, chance: Int) = LootPool.lootPool()
            .setRolls(ConstantValue.exactly(1f))
            .add(EmptyLootItem.emptyItem().setWeight(100 - chance))
            .add(LootItem.lootTableItem(item).setWeight(chance))

        val LAVA_CHARM_DUNGEON = createPoolRk("lava_charm_dungeon")
        private const val LAVA_CHARM_DUNGEON_CHANCE = 5
        private val lavaCharmDungeon = singleItemPool(ModItems.LAVA_CHARM.get(), LAVA_CHARM_DUNGEON_CHANCE)

        val LAVA_CHARM_NETHER = createPoolRk("lava_charm_nether")
        private const val LAVA_CHARM_NETHER_CHANCE = 30
        private val lavaCharmNetherPool = singleItemPool(ModItems.LAVA_CHARM.get(), LAVA_CHARM_NETHER_CHANCE)

        val SUMMONING_PENDULUM_DUNGEON = createPoolRk("summoning_pendulum")
        private const val SUMMONING_PENDULUM_DUNGEON_CHANCE = 10
        private val summoningPendulumDungeonPool = singleItemPool(ModItems.SUMMONING_PENDULUM.get(), SUMMONING_PENDULUM_DUNGEON_CHANCE)

        val SUMMONING_PENDULUM_STRONGHOLD = createPoolRk("summoning_pendulum_stronghold")
        private const val SUMMONING_PENDULUM_STRONGHOLD_CHANCE = 50
        private val summoningPendulumStrongholdPool = singleItemPool(ModItems.SUMMONING_PENDULUM.get(), SUMMONING_PENDULUM_STRONGHOLD_CHANCE)

        val MAGIC_HOOD_DUNGEON = createPoolRk("magic_hood_dungeon")
        private const val MAGIC_HOOD_DUNGEON_CHANCE = 5
        private val magicHoodDungeonPool = singleItemPool(ModItems.MAGIC_HOOD.get(), MAGIC_HOOD_DUNGEON_CHANCE)

        val MAGIC_HOOD_WEAPONSMITH = createPoolRk("magic_hood_weaponsmith")
        private const val MAGIC_HOOD_WEAPONSMITH_CHANCE = 15
        private val magicHoodWeaponsmithPool = singleItemPool(ModItems.MAGIC_HOOD.get(), MAGIC_HOOD_WEAPONSMITH_CHANCE)

        val SLIME_CUBE_DUNGEON = createPoolRk("slime_cube_dungeon")
        private const val SLIME_CUBE_DUNGEON_CHANCE = 10
        private val slimeCubeDungeonPool = singleItemPool(ModBlocks.SLIME_CUBE.asItem(), SLIME_CUBE_DUNGEON_CHANCE)

        val SLIME_CUBE_JUNGLE_TEMPLE = createPoolRk("slime_cube_jungle_temple")
        private const val SLIME_CUBE_JUNGLE_TEMPLE_CHANCE = 50
        private val slimeCubeJungleTemplePool = singleItemPool(ModBlocks.SLIME_CUBE.asItem(), SLIME_CUBE_JUNGLE_TEMPLE_CHANCE)

        val NUMBERED_COIL_DUNGEON = createPoolRk("numbered_coil_dungeon")
        private const val NUMBERED_COIL_DUNGEON_CHANCE = 10
        private val numberedCoilDungeonPool = singleItemPool(ModBlocks.SPECTRE_COIL_NUMBER.asItem(), NUMBERED_COIL_DUNGEON_CHANCE)

        val NUMBERED_COIL_MINESHAFT = createPoolRk("numbered_coil_mineshaft")
        private const val NUMBERED_COIL_MINESHAFT_CHANCE = 8
        private val numberedCoilMineshaftPool = singleItemPool(ModBlocks.SPECTRE_COIL_NUMBER.asItem(), NUMBERED_COIL_MINESHAFT_CHANCE)
    }

}