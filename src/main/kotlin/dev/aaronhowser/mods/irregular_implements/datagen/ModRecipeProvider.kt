package dev.aaronhowser.mods.irregular_implements.datagen

import dev.aaronhowser.mods.irregular_implements.registries.ModBlocks
import dev.aaronhowser.mods.irregular_implements.registries.ModItems
import net.minecraft.advancements.Criterion
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, lookupProvider) {

    override fun buildRecipes(recipeOutput: RecipeOutput) {
        for (shapedRecipe in shapedRecipes) {
            shapedRecipe.save(recipeOutput)
        }
    }

    //TODO: Potions of Collapse

    private sealed class IngredientType {
        data class TagKeyIng(val tagKey: TagKey<Item>) : IngredientType()
        data class ItemLikeIng(val item: ItemLike) : IngredientType()
    }

    private fun <T : IngredientType> makeShapedRecipe(
        output: ItemLike,
        count: Int,
        patterns: List<String>,
        definitions: Map<Char, T>,
        unlockedByName: String = "has_log",
        unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
    ): ShapedRecipeBuilder {
        var temp = ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, count)

        for (pattern in patterns) {
            temp = temp.pattern(pattern)
        }

        for (definition in definitions) {
            temp = when (val ing = definition.value) {
                is IngredientType.TagKeyIng -> temp.define(definition.key, ing.tagKey)
                is IngredientType.ItemLikeIng -> temp.define(definition.key, ing.item)
                else -> error("Unknown ingredient type: $ing")
            }
        }

        return temp.unlockedBy(unlockedByName, unlockedByCriterion)
    }

    private fun <T : IngredientType> makeShapedRecipe(
        output: ItemLike,
        patterns: List<String>,
        definitions: Map<Char, T>,
        unlockedByName: String = "has_log",
        unlockedByCriterion: Criterion<*> = has(ItemTags.LOGS)
    ): ShapedRecipeBuilder {
        return makeShapedRecipe(output, 1, patterns, definitions, unlockedByName, unlockedByCriterion)
    }

    private val shapedRecipes: List<ShapedRecipeBuilder> = listOf(
        makeShapedRecipe(
            ModBlocks.FERTILIZED_DIRT,
            2,
            listOf("FBF", "BDB", "FBF"),
            mapOf(
                'F' to IngredientType.ItemLikeIng(Items.ROTTEN_FLESH),
                'B' to IngredientType.ItemLikeIng(Items.BONE_MEAL),
                'D' to IngredientType.ItemLikeIng(Items.DIRT)
            )
        ),
        makeShapedRecipe(
            ModBlocks.PLAYER_INTERFACE,
            listOf("OEO", "OSO", "OPO"),
            mapOf(
                'O' to IngredientType.TagKeyIng(Tags.Items.OBSIDIANS),
                'E' to IngredientType.ItemLikeIng(Items.ENDER_CHEST),
                'S' to IngredientType.ItemLikeIng(Items.NETHER_STAR),
                'P' to IngredientType.ItemLikeIng(ModItems.STABLE_ENDER_PEARL)
            )
        ),
        makeShapedRecipe(
            ModBlocks.LAPIS_GLASS,
            listOf("GGG", "GLG", "GGG"),
            mapOf(
                'G' to IngredientType.TagKeyIng(Tags.Items.GLASS_BLOCKS),
                'L' to IngredientType.TagKeyIng(Tags.Items.STORAGE_BLOCKS_LAPIS)
            )
        ),
        makeShapedRecipe(
            ModBlocks.LAPIS_LAMP,
            listOf(" L ", "LRL", " L "),
            mapOf(
                'L' to IngredientType.TagKeyIng(Tags.Items.GEMS_LAPIS),
                'R' to IngredientType.ItemLikeIng(Items.REDSTONE_LAMP)
            )
        ),
        makeShapedRecipe(
            ModBlocks.DYEING_MACHINE,
            listOf(" G ", "RCB", " W "),
            mapOf(
                'G' to IngredientType.TagKeyIng(Tags.Items.DYES_RED),
                'R' to IngredientType.TagKeyIng(Tags.Items.DYES_RED),
                'C' to IngredientType.ItemLikeIng(Items.CRAFTING_TABLE),
                'B' to IngredientType.TagKeyIng(Tags.Items.DYES_BLUE),
                'W' to IngredientType.ItemLikeIng(Items.BLACK_WOOL)
            )
        ),
        makeShapedRecipe(
            ModBlocks.ONLINE_DETECTOR,
            listOf("SRS", "RLR", "SRS"),
            mapOf(
                'S' to IngredientType.TagKeyIng(Tags.Items.STONES),
                'R' to IngredientType.ItemLikeIng(Items.REPEATER),
                'L' to IngredientType.TagKeyIng(Tags.Items.GEMS_LAPIS)
            )
        ),
        makeShapedRecipe(
            ModBlocks.CHAT_DETECTOR,
            listOf("SRS", "RDR", "SRS"),
            mapOf(
                'S' to IngredientType.TagKeyIng(Tags.Items.STONES),
                'R' to IngredientType.ItemLikeIng(Items.REPEATER),
                'D' to IngredientType.TagKeyIng(Tags.Items.DYES_RED)
            )
        ),
        makeShapedRecipe(
            ModBlocks.ENDER_BRIDGE,
            listOf("EEE", "ERP", "EEE"),
            mapOf(
                'E' to IngredientType.TagKeyIng(Tags.Items.END_STONES),
                'R' to IngredientType.TagKeyIng(Tags.Items.DUSTS_REDSTONE),
                'P' to IngredientType.ItemLikeIng(ModItems.STABLE_ENDER_PEARL)
            )
        ),
        makeShapedRecipe(
            ModBlocks.PRISMARINE_ENDER_BRIDGE,
            listOf("SCS", "CEC", "SCS"),
            mapOf(
                'S' to IngredientType.ItemLikeIng(Items.PRISMARINE_SHARD),
                'C' to IngredientType.ItemLikeIng(Items.PRISMARINE_CRYSTALS),
                'E' to IngredientType.ItemLikeIng(ModBlocks.ENDER_BRIDGE)
            )
        )
    )

}