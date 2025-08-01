package dev.aaronhowser.mods.irregular_implements.datagen.loot

import dev.aaronhowser.mods.irregular_implements.block.BeanSproutBlock
import dev.aaronhowser.mods.irregular_implements.block.LotusBlock
import dev.aaronhowser.mods.irregular_implements.registry.ModBlocks
import dev.aaronhowser.mods.irregular_implements.registry.ModItems
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator

class ModBlockLootTablesSubProvider(
	provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

	override fun generate() {

		for (block in this.knownBlocks - this.nonDropSelfBlocks) {
			dropSelf(block)
		}

		for (block in this.noDropBlocks) {
			add(block, noDrop())
		}

		for (block in this.dropsDirtWithoutSilkTouch) {
			add(block) { createSingleItemTableWithSilkTouch(it, Items.DIRT) }
		}

		add(ModBlocks.COMPRESSED_SLIME_BLOCK.get()) { createSingleItemTable(Blocks.SLIME_BLOCK) }

		lotus()
		beanSprout()
		spectreLeaves()
		sakanade()
		beanPod()
	}

	private fun beanPod() {
		val enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT)

		add(ModBlocks.BEAN_POD.get()) {
			LootTable.lootTable()
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.add(
							LootItem.lootTableItem(ModBlocks.BEAN_POD)
								.`when`(hasSilkTouch())
						)
				)
				.withPool(
					LootPool.lootPool()
						.add(
							LootItem.lootTableItem(ModItems.GOLDEN_EGG)
								.`when`(doesNotHaveSilkTouch())
						)
				)
				.withPool(
					LootPool.lootPool()
						.add(
							LootItem.lootTableItem(Items.IRON_INGOT)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(8f, 20f)))
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5f, 3))
								.`when`(doesNotHaveSilkTouch())
						)
				)
				.withPool(
					LootPool.lootPool()
						.add(
							LootItem.lootTableItem(Items.GOLD_INGOT)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(4f, 15f)))
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5f, 3))
								.`when`(doesNotHaveSilkTouch())
						)
				)
				.withPool(
					LootPool.lootPool()
						.add(
							LootItem.lootTableItem(Items.DIAMOND)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 5f)))
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5f, 3))
								.`when`(doesNotHaveSilkTouch())
						)
				)
				.withPool(
					LootPool.lootPool()
						.add(
							LootItem.lootTableItem(Items.IRON_INGOT)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(0f, 2f)))
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5f, 3))
								.`when`(doesNotHaveSilkTouch())
						)
				)
				.withPool(
					LootPool.lootPool()
						.add(
							LootItem.lootTableItem(ModItems.BEAN)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(4f, 8f)))
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantments.getOrThrow(Enchantments.FORTUNE), 0.5f, 3))
								.`when`(doesNotHaveSilkTouch())
						)
				)
		}

	}

	private fun sakanade() {

		add(ModBlocks.SAKANADE_SPORES.get()) {
			createSilkTouchOrShearsDispatchTable(
				it,
				applyExplosionCondition(
					ModBlocks.SAKANADE_SPORES.get(),
					LootItem.lootTableItem(ModBlocks.SAKANADE_SPORES.get())
				)
			)
		}

	}

	private fun spectreLeaves() {
		add(ModBlocks.SPECTRE_LEAVES.get()) {
			val enchantmentLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT)

			createLeavesDrops(it, ModBlocks.SPECTRE_SAPLING.get(), *NORMAL_LEAVES_SAPLING_CHANCES)
				.withPool(
					LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1f))
						.`when`(this.doesNotHaveSilkTouch())
						.add(
							this.applyExplosionCondition(
								ModBlocks.SPECTRE_LEAVES.get(),
								LootItem.lootTableItem(ModItems.ECTOPLASM.get())
							)
								.`when`(
									BonusLevelTableCondition.bonusLevelFlatChance(
										enchantmentLookup.getOrThrow(Enchantments.FORTUNE), 0.005f, 0.0055555557f, 0.00625f, 0.008333334f, 0.025f
									)
								)
						)
				)
		}
	}

	private fun beanSprout() {
		add(ModBlocks.BEAN_SPROUT.get()) {
			super.applyExplosionDecay(
				it,
				LootTable.lootTable()
					.withPool(
						LootPool.lootPool()
							.add(LootItem.lootTableItem(ModItems.BEAN.get()))
					)
					.withPool(
						LootPool.lootPool()
							.`when`(
								LootItemBlockStatePropertyCondition.hasBlockStateProperties(it)
									.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BeanSproutBlock.AGE, BeanSproutBlock.MAXIMUM_AGE))
							)
							.add(
								LootItem.lootTableItem(ModItems.BEAN.get())
									.apply(
										ApplyBonusCount.addBonusBinomialDistributionCount(
											this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE),
											0.5714286f,
											3
										)
									)
							)
					)
			)
		}
	}

	private fun lotus() {
		add(ModBlocks.LOTUS.get()) {
			super.applyExplosionDecay(
				it,
				LootTable.lootTable()
					.withPool(
						LootPool.lootPool()
							.`when`(
								LootItemBlockStatePropertyCondition.hasBlockStateProperties(it)
									.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LotusBlock.AGE, LotusBlock.MAXIMUM_AGE))
							)
							.add(LootItem.lootTableItem(ModItems.LOTUS_BLOSSOM.get()))
					)
					.withPool(
						LootPool.lootPool()
							.add(LootItem.lootTableItem(ModItems.LOTUS_SEEDS.get()))
					)
			)
		}
	}

	private val dropsDirtWithoutSilkTouch = buildList {
		for (dyeColor in DyeColor.entries) {
			add(ModBlocks.getColoredGrass(dyeColor).get())
		}

		add(ModBlocks.FERTILIZED_DIRT.get())
	}

	private val noDropBlocks = listOf(
		ModBlocks.BLAZE_FIRE,
		ModBlocks.LOTUS,
		ModBlocks.BEAN_STALK,
		ModBlocks.LESSER_BEAN_STALK,
		ModBlocks.SPECTRE_BLOCK,
		ModBlocks.SPECTRE_CORE
	).map { it.get() }.toSet()

	private val nonDropSelfBlocks: Set<Block> = buildSet {
		addAll(dropsDirtWithoutSilkTouch)
		addAll(noDropBlocks)

		add(ModBlocks.COMPRESSED_SLIME_BLOCK.get())
		add(ModBlocks.SPECTRE_LEAVES.get())
		add(ModBlocks.SAKANADE_SPORES.get())
		add(ModBlocks.BEAN_POD.get())
	}

	override fun getKnownBlocks(): List<Block> {
		return ModBlocks.BLOCK_REGISTRY.entries.map { it.get() }
	}

	var noEmpty = true

	override fun add(block: Block, builder: LootTable.Builder) {

		println("Adding block ${block.name.string}")
		val wasNoEmpty = noEmpty

		super.add(block, builder)

		val hasEmpty = this.map.toString().contains("minecraft:empty")
		if (hasEmpty && wasNoEmpty) {
			println("Here!!! It was ${block.name.string}")
			noEmpty = false
		}
	}

}