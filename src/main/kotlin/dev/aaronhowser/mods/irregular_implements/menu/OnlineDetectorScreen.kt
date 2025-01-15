package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.menu.base.ScreenTextures
import dev.aaronhowser.mods.irregular_implements.menu.base.ScreenWithStrings
import dev.aaronhowser.mods.irregular_implements.packet.ModPacketHandler
import dev.aaronhowser.mods.irregular_implements.packet.client_to_server.ClientChangedMenuString
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class OnlineDetectorScreen(
    menu: OnlineDetectorMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<OnlineDetectorMenu>(menu, playerInventory, title), ScreenWithStrings {

    private val rightPos: Int
        get() = this.leftPos + this.imageWidth
    private val bottomPos: Int
        get() = this.topPos + this.imageHeight

    private lateinit var usernameEditBox: EditBox

    override fun init() {
        this.imageWidth = ScreenTextures.Background.OnlineDetector.WIDTH
        this.imageHeight = ScreenTextures.Background.OnlineDetector.HEIGHT

        this.leftPos = (this.width - this.imageWidth) / 2
        this.topPos = (this.height - this.imageHeight) / 2

        val editBoxHeight = 20

        this.usernameEditBox = EditBox(
            this.font,
            this.leftPos + 5,
            this.bottomPos - 5 - editBoxHeight,
            this.imageWidth - 5 - 5,
            editBoxHeight,
            Component.empty()
        )

        this.usernameEditBox.setCanLoseFocus(false)
        this.usernameEditBox.setTextColor(-1)
        this.usernameEditBox.setTextColorUneditable(-1)
        this.usernameEditBox.setResponder(::setUsername)

        this.addRenderableWidget(this.usernameEditBox)
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        guiGraphics.blit(
            ScreenTextures.Background.OnlineDetector.BACKGROUND,
            this.leftPos,
            this.topPos,
            0f,
            0f,
            ScreenTextures.Background.OnlineDetector.WIDTH,
            ScreenTextures.Background.OnlineDetector.HEIGHT,
            ScreenTextures.Background.OnlineDetector.CANVAS_SIZE,
            ScreenTextures.Background.OnlineDetector.CANVAS_SIZE
        )
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false)
    }

    // Behavior

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == 256) {
            this.minecraft?.player?.closeContainer()
        }

        return if (!this.usernameEditBox.keyPressed(keyCode, scanCode, modifiers) && !this.usernameEditBox.canConsumeInput()) {
            super.keyPressed(keyCode, scanCode, modifiers)
        } else {
            true
        }
    }

    override fun resize(minecraft: Minecraft, width: Int, height: Int) {
        val currentRegexString = this.usernameEditBox.value
        super.resize(minecraft, width, height)
        this.usernameEditBox.value = currentRegexString
    }

    override fun isPauseScreen(): Boolean {
        return false
    }

    private fun setUsername(string: String) {
        if (this.menu.setUsername(string)) {
            ModPacketHandler.messageServer(
                ClientChangedMenuString(
                    OnlineDetectorMenu.USERNAME_STRING_ID,
                    string
                )
            )
        }
    }

    override fun receivedString(stringId: Int, regexString: String) {
        if (stringId == OnlineDetectorMenu.USERNAME_STRING_ID) {
            this.usernameEditBox.value = regexString
        }
    }

}