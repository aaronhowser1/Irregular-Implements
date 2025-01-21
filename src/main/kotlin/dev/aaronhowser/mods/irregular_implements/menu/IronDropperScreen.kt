package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider
import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.irregular_implements.menu.base.MultiStageSpriteButton
import dev.aaronhowser.mods.irregular_implements.menu.base.ScreenTextures
import dev.aaronhowser.mods.irregular_implements.packet.ModPacketHandler
import dev.aaronhowser.mods.irregular_implements.packet.client_to_server.ClientClickedMenuButton
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class IronDropperScreen(
    menu: IronDropperMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<IronDropperMenu>(menu, playerInventory, title) {

    private val rightPos: Int
        get() = this.leftPos + this.imageWidth
    private val bottomPos: Int
        get() = this.topPos + this.imageHeight

    private lateinit var shootModeButton: MultiStageSpriteButton
    private lateinit var toggleEffectButton: MultiStageSpriteButton
    private lateinit var delayButton: MultiStageSpriteButton
    private lateinit var redstoneModeButton: MultiStageSpriteButton

    private val background = ScreenTextures.Background.IronDropper

    override fun init() {
        this.imageWidth = background.width
        this.imageHeight = background.height

        this.leftPos = (this.width - this.imageWidth) / 2
        this.topPos = (this.height - this.imageHeight) / 2

        this.titleLabelX = (this.imageWidth - font.width(this.title)) / 2

        val buttonSize = 20
        val spaceBetween = 5

        val rightButtonX = this.rightPos - spaceBetween - buttonSize - 2
        val leftButtonX = rightButtonX - spaceBetween - buttonSize

        val topButtonY = this.topPos + spaceBetween + buttonSize / 2 + spaceBetween
        val bottomButtonY = topButtonY + buttonSize + spaceBetween

        this.redstoneModeButton = MultiStageSpriteButton.Builder(this.font)
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_PULSE.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.RedstonePulse
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_CONTINUOUS_POWERED.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.RedstoneContinuousPowered
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_CONTINUOUS.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.RedstoneContinuous
            )
            .size(
                size = buttonSize
            )
            .currentStageGetter(
                currentStageGetter = { this.menu.redstoneMode.ordinal }
            )
            .onPress(
                onPress = { ModPacketHandler.messageServer(ClientClickedMenuButton(IronDropperMenu.REDSTONE_BUTTON_ID)) }
            )
            .location(
                x = leftButtonX,
                y = topButtonY
            )
            .build()

        this.delayButton = MultiStageSpriteButton.Builder(this.font)
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_NO_DELAY.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.PickupZero
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_FIVE_DELAY.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.PickupFive
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_TWENTY_DELAY.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.PickupTwenty
            )
            .size(
                size = buttonSize
            )
            .currentStageGetter(
                currentStageGetter = { this.menu.pickupDelay.ordinal }
            )
            .onPress(
                onPress = { ModPacketHandler.messageServer(ClientClickedMenuButton(IronDropperMenu.DELAY_BUTTON_ID)) }
            )
            .location(
                x = rightButtonX,
                y = topButtonY
            )
            .build()

        this.shootModeButton = MultiStageSpriteButton.Builder(this.font)
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_RANDOM_VELOCITY.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.DirectionRandom
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_EXACT_VELOCITY.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.DirectionForward
            )
            .size(
                size = buttonSize
            )
            .currentStageGetter(
                currentStageGetter = { if (this.menu.shouldShootStraight) 1 else 0 }
            )
            .onPress(
                onPress = { ModPacketHandler.messageServer(ClientClickedMenuButton(IronDropperMenu.SHOOT_MODE_BUTTON_ID)) }
            )
            .location(
                x = leftButtonX,
                y = bottomButtonY
            )
            .build()

        this.toggleEffectButton = MultiStageSpriteButton.Builder(this.font)
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_NO_EFFECTS.toComponent(),
                sprite = null
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_ONLY_PARTICLES.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.EffectParticle
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_ONLY_SOUND.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.EffectSound
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_BOTH_EFFECTS.toComponent(),
                menuSprite = ScreenTextures.Sprite.IronDropper.EffectBoth
            )
            .size(
                size = buttonSize
            )
            .currentStageGetter(
                currentStageGetter = { this.menu.shouldHaveEffects.ordinal }
            )
            .onPress(
                onPress = { ModPacketHandler.messageServer(ClientClickedMenuButton(IronDropperMenu.EFFECTS_BUTTON_ID)) }
            )
            .location(
                x = rightButtonX,
                y = bottomButtonY
            )
            .build()

        this.addRenderableWidget(this.shootModeButton)
        this.addRenderableWidget(this.toggleEffectButton)
        this.addRenderableWidget(this.delayButton)
        this.addRenderableWidget(this.redstoneModeButton)
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }

    //TODO: Are ij needed?
    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        val i = (this.width - this.imageWidth) / 2
        val j = (this.height - this.imageHeight) / 2

        this.background.render(
            guiGraphics,
            i,
            j
        )
    }

}