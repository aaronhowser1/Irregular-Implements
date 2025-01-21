package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.menu.base.BaseScreen
import dev.aaronhowser.mods.irregular_implements.menu.base.MultiStageSpriteButton
import dev.aaronhowser.mods.irregular_implements.menu.base.ScreenTextures
import dev.aaronhowser.mods.irregular_implements.packet.ModPacketHandler
import dev.aaronhowser.mods.irregular_implements.packet.client_to_server.ClientClickedMenuButton
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class InventoryTesterScreen(
    menu: InventoryTesterMenu,
    playerInventory: Inventory,
    title: Component
) : BaseScreen<InventoryTesterMenu>(menu, playerInventory, title) {

    override val background: ScreenTextures.Background = ScreenTextures.Background.InventoryTester

    private lateinit var invertButton: MultiStageSpriteButton

    override fun baseInit() {
        this.titleLabelX = 35
        this.inventoryLabelX = 35
        this.inventoryLabelY = this.imageHeight - 94

        //TODO: Update placement and size
        this.invertButton = MultiStageSpriteButton.Builder(this.font)
            .size(20)
            .location(
                x = this.leftPos + 90,
                y = this.topPos + 18
            )
            .addStage(
                message = Component.literal("Uninverted"),
                sprite = ScreenTextures.Sprite.InventoryTester.Uninverted
            )
            .addStage(
                message = Component.literal("Inverted"),
                sprite = ScreenTextures.Sprite.InventoryTester.Inverted
            )
            .currentStageGetter(
                currentStageGetter = { if (this.menu.isInverted) 1 else 0 }
            )
            .onPress(
                onPress = { ModPacketHandler.messageServer(ClientClickedMenuButton(InventoryTesterMenu.TOGGLE_INVERSION_BUTTON_ID)) }
            )
            .build()

        this.addRenderableWidget(this.invertButton)
    }

}