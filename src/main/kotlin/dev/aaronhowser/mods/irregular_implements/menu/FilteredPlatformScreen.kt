package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.menu.base.BaseScreen
import dev.aaronhowser.mods.irregular_implements.menu.base.ScreenTextures
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class FilteredPlatformScreen(
    menu: FilteredPlatformMenu,
    playerInventory: Inventory,
    title: Component
) : BaseScreen<FilteredPlatformMenu>(menu, playerInventory, title) {

    override val background = ScreenTextures.Background.FilteredSuperLubricantPlatform

    override var showInventoryLabel: Boolean = false
    override var showTitleLabel: Boolean = false

}