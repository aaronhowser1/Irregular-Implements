package dev.aaronhowser.mods.irregular_implements.compatibility.emi

import dev.aaronhowser.mods.irregular_implements.compatibility.emi.recipe.ModInformationRecipes
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry

@EmiEntrypoint
class ModEmiPlugin : EmiPlugin {

    override fun register(registry: EmiRegistry) {

        for (infoRecipe in ModInformationRecipes.getInformationRecipes()) {
            registry.addRecipe(infoRecipe)
        }
    }

}