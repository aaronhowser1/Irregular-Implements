package dev.aaronhowser.mods.irregular_implements.registry

import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.recipe.ApplySpectreAnchorRecipe
import dev.aaronhowser.mods.irregular_implements.recipe.DiviningRodRecipe
import dev.aaronhowser.mods.irregular_implements.recipe.LubricateBootRecipe
import dev.aaronhowser.mods.irregular_implements.recipe.WashBootRecipe
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

    val WASH_BOOT =
        registerRecipeSerializer("wash_boot") { SimpleCraftingRecipeSerializer(::WashBootRecipe) }

    val DIVINING_ROD =
        registerRecipeSerializer("divining_rod") { SimpleCraftingRecipeSerializer(::DiviningRodRecipe) }

    val APPLY_SPECTRE_ANCHOR =
        registerRecipeSerializer("apply_spectre_anchor") { SimpleCraftingRecipeSerializer(::ApplySpectreAnchorRecipe) }

    private fun registerRecipeSerializer(
        name: String,
        factory: () -> RecipeSerializer<*>
    ): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> {
        return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
    }

}