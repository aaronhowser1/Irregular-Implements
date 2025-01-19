package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider
import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.irregular_implements.menu.base.MultiStateSpriteButton
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

    private lateinit var shootModeButton: MultiStateSpriteButton
    private lateinit var toggleEffectButton: MultiStateSpriteButton
    private lateinit var delayButton: MultiStateSpriteButton
    private lateinit var redstoneModeButton: MultiStateSpriteButton

    private val background = ScreenTextures.Backgrounds.IronDropper

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

        this.redstoneModeButton = MultiStateSpriteButton.Builder(this.font)
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_PULSE.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.RedstonePulse
            )
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_CONTINUOUS_POWERED.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.RedstoneContinuousPowered
            )
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_CONTINUOUS.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.RedstoneContinuous
            )
            .size(
                size = buttonSize
            )
            .currentStateGetter(
                currentStateGetter = { this.menu.redstoneMode.ordinal }
            )
            .onPress(
                onPress = { ModPacketHandler.messageServer(ClientClickedMenuButton(IronDropperMenu.REDSTONE_BUTTON_ID)) }
            )
            .location(
                x = leftButtonX,
                y = topButtonY
            )
            .build()

        this.delayButton = MultiStateSpriteButton.Builder(this.font)
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_NO_DELAY.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.PickupZero
            )
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_FIVE_DELAY.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.PickupFive
            )
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_TWENTY_DELAY.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.PickupTwenty
            )
            .size(
                size = buttonSize
            )
            .currentStateGetter(
                currentStateGetter = { this.menu.pickupDelay.ordinal }
            )
            .onPress(
                onPress = { ModPacketHandler.messageServer(ClientClickedMenuButton(IronDropperMenu.DELAY_BUTTON_ID)) }
            )
            .location(
                x = rightButtonX,
                y = topButtonY
            )
            .build()

        this.shootModeButton = MultiStateSpriteButton.Builder(this.font)
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_RANDOM_VELOCITY.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.DirectionRandom
            )
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_EXACT_VELOCITY.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.DirectionForward
            )
            .size(
                size = buttonSize
            )
            .currentStateGetter(
                currentStateGetter = { if (this.menu.shouldShootStraight) 1 else 0 }
            )
            .onPress(
                onPress = { ModPacketHandler.messageServer(ClientClickedMenuButton(IronDropperMenu.SHOOT_MODE_BUTTON_ID)) }
            )
            .location(
                x = leftButtonX,
                y = bottomButtonY
            )
            .build()

        this.toggleEffectButton = MultiStateSpriteButton.Builder(this.font)
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_NO_EFFECTS.toComponent(),
                sprite = null
            )
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_ONLY_PARTICLES.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.EffectParticle
            )
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_ONLY_SOUND.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.EffectSound
            )
            .addState(
                message = ModLanguageProvider.Tooltips.IRON_DROPPER_BOTH_EFFECTS.toComponent(),
                menuSprite = ScreenTextures.Sprites.IronDropper.EffectBoth
            )
            .size(
                size = buttonSize
            )
            .currentStateGetter(
                currentStateGetter = { this.menu.shouldHaveEffects.ordinal }
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