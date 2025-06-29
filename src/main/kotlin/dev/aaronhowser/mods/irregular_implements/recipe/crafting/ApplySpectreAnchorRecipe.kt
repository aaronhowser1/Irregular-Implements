package dev.aaronhowser.mods.irregular_implements.recipe.crafting

import dev.aaronhowser.mods.irregular_implements.compatibility.emi.ModEmiPlugin.Companion.ingredient
import dev.aaronhowser.mods.irregular_implements.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.registry.ModItems
import dev.aaronhowser.mods.irregular_implements.registry.ModRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.util.Unit
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class ApplySpectreAnchorRecipe(
	craftingCategory: CraftingBookCategory = CraftingBookCategory.MISC
) : CustomRecipe(craftingCategory) {

	override fun matches(input: CraftingInput, level: Level): Boolean {
		val amountAnchors = input.items().count { anchorIngredient.test(it) }
		val amountNonAnchors = input.items().count { isApplicable(it) }

		return amountAnchors == 1 && amountNonAnchors == 1
	}

	override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack {
		val nonAnchor = input.items().first { isApplicable(it) }

		val result = nonAnchor.copyWithCount(1) //TODO: Figure out how to let you do entire stacks without duping
		result.set(ModDataComponents.IS_ANCHORED, Unit.INSTANCE)

		return result
	}

	override fun canCraftInDimensions(width: Int, height: Int): Boolean {
		return width * height >= 2
	}

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.APPLY_SPECTRE_ANCHOR.get()
	}

	companion object {
		val anchorIngredient: Ingredient = ModItems.SPECTRE_ANCHOR.ingredient

		fun isApplicable(itemStack: ItemStack): Boolean {
			return !itemStack.isEmpty
					&& !itemStack.`is`(ModItemTagsProvider.SPECTRE_ANCHOR_BLACKLIST)
					&& !itemStack.has(ModDataComponents.IS_ANCHORED)
		}
	}
}