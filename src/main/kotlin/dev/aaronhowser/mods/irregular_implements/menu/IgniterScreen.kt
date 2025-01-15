package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.menu.base.ChangingTextButton
import dev.aaronhowser.mods.irregular_implements.menu.base.ScreenTextures
import dev.aaronhowser.mods.irregular_implements.packet.ModPacketHandler
import dev.aaronhowser.mods.irregular_implements.packet.client_to_server.ClientClickedMenuButton
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class IgniterScreen(
    menu: IgniterMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<IgniterMenu>(menu, playerInventory, title) {

    private val rightPos: Int
        get() = this.leftPos + this.imageWidth
    private val bottomPos: Int
        get() = this.topPos + this.imageHeight

    private lateinit var changeModeButton: ChangingTextButton

    override fun init() {
        this.imageWidth = ScreenTextures.Background.Igniter.WIDTH
        this.imageHeight = ScreenTextures.Background.Igniter.HEIGHT

        this.leftPos = (this.width - this.imageWidth) / 2
        this.topPos = (this.height - this.imageHeight) / 2

        this.changeModeButton = ChangingTextButton(
            this.leftPos + 5,
            this.topPos + 5,
            this.imageWidth - 10,
            this.imageHeight - 10,
            { this.menu.mode.nameComponent },
            { ModPacketHandler.messageServer(ClientClickedMenuButton(IgniterMenu.CYCLE_MODE_BUTTON_ID)) }
        )

        this.addRenderableWidget(this.changeModeButton)
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        // Do nothing
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val i = (this.width - this.imageWidth) / 2
        val j = (this.height - this.imageHeight) / 2

        guiGraphics.blit(
            ScreenTextures.Background.Igniter.BACKGROUND,
            i,
            j,
            0,
            0,
            ScreenTextures.Background.Igniter.CANVAS_SIZE,
            ScreenTextures.Background.Igniter.CANVAS_SIZE,
        )
    }

}