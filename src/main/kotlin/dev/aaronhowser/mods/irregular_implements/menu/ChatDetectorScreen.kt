package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider
import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.irregular_implements.menu.base.MultiStateSpriteButton
import dev.aaronhowser.mods.irregular_implements.menu.base.ScreenTextures
import dev.aaronhowser.mods.irregular_implements.packet.server_to_client.UpdateClientChatDetector
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ChatDetectorScreen(
    menu: ChatDetectorMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<ChatDetectorMenu>(menu, playerInventory, title) {

    private val rightPos: Int
        get() = this.leftPos + this.imageWidth
    private val bottomPos: Int
        get() = this.topPos + this.imageHeight

    private lateinit var toggleMessagePassButton: MultiStateSpriteButton
    private lateinit var regexStringEditBox: EditBox

    override fun init() {
        this.imageWidth = ScreenTextures.Background.ChatDetector.WIDTH
        this.imageHeight = ScreenTextures.Background.ChatDetector.HEIGHT

        this.leftPos = (this.width - this.imageWidth) / 2
        this.topPos = (this.height - this.imageHeight) / 2

        this.titleLabelX = 10
        this.titleLabelY = 10

        this.toggleMessagePassButton = MultiStateSpriteButton.Builder(this.font)
            .addStage(
                message = ModLanguageProvider.Tooltips.STOPS_MESSAGE.toComponent(),
                sprite = ScreenTextures.Sprite.ChatDetector.MESSAGE_STOP,
                spriteWidth = ScreenTextures.Sprite.ChatDetector.WIDTH,
                spriteHeight = ScreenTextures.Sprite.ChatDetector.HEIGHT
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.DOESNT_STOP_MESSAGE.toComponent(),
                sprite = ScreenTextures.Sprite.ChatDetector.MESSAGE_CONTINUE,
                spriteWidth = ScreenTextures.Sprite.ChatDetector.WIDTH,
                spriteHeight = ScreenTextures.Sprite.ChatDetector.HEIGHT
            )
            .size(
                width = 20,
                height = 20
            )
            .currentStateGetter(
                currentStateGetter = { if (this.menu.messagePassThrough) 0 else 1 }    // 1 means it stops messages
            )
            .onPress(
                onPress = ::pressToggleMessagePassButton
            )
            .build()

        this.toggleMessagePassButton.setPosition(
            this.rightPos - this.toggleMessagePassButton.width - 5,
            this.topPos + 5
        )

        val width = this.rightPos - this.leftPos

//        this.regexStringEditBox = EditBox(
//            this.font,
//            this.leftPos + 5,
//            this.bottomPos - 5 - 20,
//            width - 5 - 5,
//            20,
//            Component.literal(this.chatDetectorBlockEntity.regexString)
//        )

//        this.regexStringEditBox.setResponder(::setRegexString)
//        this.regexStringEditBox.setMaxLength(10000)
//        this.regexStringEditBox.setHint(ModLanguageProvider.Tooltips.MESSAGE_REGEX.toComponent())

        addRenderableWidget(this.toggleMessagePassButton)
//        addRenderableWidget(this.regexStringEditBox)
    }

    // Rendering

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        guiGraphics.blit(
            ScreenTextures.Background.ChatDetector.BACKGROUND,
            this.leftPos,
            this.topPos,
            0f,
            0f,
            ScreenTextures.Background.ChatDetector.WIDTH,
            ScreenTextures.Background.ChatDetector.HEIGHT,
            ScreenTextures.Background.ChatDetector.CANVAS_SIZE,
            ScreenTextures.Background.ChatDetector.CANVAS_SIZE
        )
    }

    override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        guiGraphics.drawString(
            this.font,
            this.title,
            this.titleLabelX,
            this.titleLabelY,
            4210752,
            false
        )
    }

    // Behavior

    override fun isPauseScreen(): Boolean {
        return false
    }

//    override fun tick() {
//        if (this.chatDetectorBlockEntity.isRemoved) {
//            onClose()
//        }
//
//        if (this.regexStringEditBox.value != UpdateClientChatDetector.regexString) {
//            this.regexStringEditBox.value = UpdateClientChatDetector.regexString
//        }
//
//        if (this.chatDetectorBlockEntity.stopsMessage != UpdateClientChatDetector.stopsMessage) {
//            this.chatDetectorBlockEntity.stopsMessage = UpdateClientChatDetector.stopsMessage
//        }
//    }

    override fun onClose() {
        UpdateClientChatDetector.unset()

        super.onClose()
    }

    private fun pressToggleMessagePassButton(button: Button) {
//        ModPacketHandler.messageServer(
//            ClientChangedChatDetector(
//                this.chatDetectorBlockEntity.blockPos,
//                !this.chatDetectorBlockEntity.stopsMessage,
//                regexStringEditBox.value
//            )
//        )
    }

    private var isChangingRegexString = false
    private fun setRegexString(string: String) {
        if (isChangingRegexString) return
        isChangingRegexString = true
        this.regexStringEditBox.value = string
        isChangingRegexString = false

//        ModPacketHandler.messageServer(
//            ClientChangedChatDetector(
//                this.chatDetectorBlockEntity.blockPos,
//                this.chatDetectorBlockEntity.stopsMessage,
//                string
//            )
//        )
    }

}