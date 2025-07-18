package dev.aaronhowser.mods.irregular_implements.registry

import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.recipe.crafting.*
import dev.aaronhowser.mods.irregular_implements.recipe.machine.ImbuingRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeSerializers {

	val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
		DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, IrregularImplements.ID)

	val LUBRICATE_BOOT: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("lubricate_boot") { SimpleCraftingRecipeSerializer(::LubricateBootRecipe) }

	val WASH_BOOT: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("wash_boot") { SimpleCraftingRecipeSerializer(::WashBootRecipe) }

	val DIVINING_ROD: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("divining_rod") { SimpleCraftingRecipeSerializer(::DiviningRodRecipe) }

	val APPLY_SPECTRE_ANCHOR: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("apply_spectre_anchor") { SimpleCraftingRecipeSerializer(::ApplySpectreAnchorRecipe) }

	val SET_DIAPHANOUS_BLOCK: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("set_diaphanous_block") { SimpleCraftingRecipeSerializer(::SetDiaphanousBlockRecipe) }

	val INVERT_DIAPHANOUS_BLOCK: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("invert_diaphanous_block") { SimpleCraftingRecipeSerializer(::InvertDiaphanousBlockRecipe) }

	val CUSTOM_CRAFTING_TABLE: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("custom_crafting_table") { SimpleCraftingRecipeSerializer(::CustomCraftingTableRecipe) }

	val IMBUING: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("imbuing") { ImbuingRecipe.Serializer() }

	val SET_PORTKEY_DISGUISE: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("set_portkey_disguise") { SimpleCraftingRecipeSerializer(::SetPortkeyDisguiseRecipe) }

	private fun registerRecipeSerializer(
		name: String,
		factory: () -> RecipeSerializer<*>
	): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> {
		return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
	}

}