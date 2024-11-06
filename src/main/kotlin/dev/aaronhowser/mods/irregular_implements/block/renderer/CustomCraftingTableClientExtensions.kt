package dev.aaronhowser.mods.irregular_implements.block.renderer

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

class CustomCraftingTableClientExtensions : IClientItemExtensions {

    private val bewlr = CustomCraftingTableBEWLR()

    override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
        return bewlr
    }

}