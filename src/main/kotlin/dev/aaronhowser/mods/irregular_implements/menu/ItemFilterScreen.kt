package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider
import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.irregular_implements.menu.base.ChangingColorButton
import dev.aaronhowser.mods.irregular_implements.menu.base.MultiStageSpriteButton
import dev.aaronhowser.mods.irregular_implements.menu.base.ScreenTextures
import dev.aaronhowser.mods.irregular_implements.packet.ModPacketHandler
import dev.aaronhowser.mods.irregular_implements.packet.client_to_server.ClientClickedMenuButton
import dev.aaronhowser.mods.irregular_implements.util.FilterEntry
import dev.aaronhowser.mods.irregular_implements.util.FilterEntry.Companion.isNullOrEmpty
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil.getComponent
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

//TODO: Make buttons function
class ItemFilterScreen(
    menu: ItemFilterMenu,
    playerInventory: Inventory,
    title: Component
) : AbstractContainerScreen<ItemFilterMenu>(menu, playerInventory, title) {

    private val background = ScreenTextures.Backgrounds.ItemFilter

    private val leftButtons: MutableSet<Button> = mutableSetOf()
    private val rightButtons: MutableSet<Button> = mutableSetOf()

    private lateinit var invertBlacklistButton: Button

    override fun init() {
        this.imageWidth = background.width
        this.imageHeight = background.height

        this.inventoryLabelY = this.imageHeight - 94

        this.leftPos = (this.width - this.imageWidth) / 2
        this.topPos = (this.height - this.imageHeight) / 2

        setButtons()
    }

    private fun setButtons() {
        this.leftButtons.clear()
        this.rightButtons.clear()

        addToggleBlacklistButton()

        for (index in 0 until 9) {
            addLeftButton(index)
            addRightButtons(index)
        }
    }

    private fun addToggleBlacklistButton() {

        val x = this.leftPos + this.imageWidth - 24
        val y = this.topPos + 5

        val onPress = {
            ModPacketHandler.messageServer(ClientClickedMenuButton(ItemFilterMenu.TOGGLE_BLACKLIST_BUTTON_ID))
        }

        this.invertBlacklistButton = MultiStageSpriteButton.Builder(this.font)
            .location(x, y)
            .size(16)
            .addStage(
                message = ModLanguageProvider.Tooltips.WHITELIST.toComponent(),
                menuSprite = ScreenTextures.Sprites.ItemFilter.Whitelist
            )
            .addStage(
                message = ModLanguageProvider.Tooltips.BLACKLIST.toComponent(),
                menuSprite = ScreenTextures.Sprites.ItemFilter.Blacklist
            )
            .currentStageGetter(
                currentStageGetter = { if (this.menu.isBlacklist) 1 else 0 }
            )
            .onPress(onPress)
            .build()

        this.addRenderableWidget(this.invertBlacklistButton)
    }

    // Toggles between Item Filter and Tag Filter
    private fun addLeftButton(index: Int) {
        val x = this.leftPos + 8 + index * 18
        val y = this.topPos + 15

        val width = 8
        val height = 8

        val buttonId = ItemFilterMenu.getLeftButtonId(index)

        val button = ChangingColorButton(
            x = x,
            y = y,
            width = width,
            height = height,
            messagesGetter = {
                val filterAtIndex = this.menu.filter?.getOrNull(index)

                listOf(
                    ModLanguageProvider.Tooltips.ITEM_FILTER_ITEM.toComponent().withStyle(
                        if (filterAtIndex is FilterEntry.Item) ChatFormatting.GRAY else ChatFormatting.DARK_GRAY
                    ),
                    ModLanguageProvider.Tooltips.ITEM_FILTER_TAG.toComponent().withStyle(
                        if (filterAtIndex is FilterEntry.Item) ChatFormatting.DARK_GRAY else ChatFormatting.GRAY
                    )
                )
            },
            colorGetter = {
                val filterAtIndex = this.menu.filter?.getOrNull(index)

                if (filterAtIndex is FilterEntry.Item) {
                    0xFF5969FF.toInt()
                } else {
                    0xFF00B7A2.toInt()
                }
            },
            font = this.font,
            onPress = {
                ModPacketHandler.messageServer(ClientClickedMenuButton(buttonId))
            }
        )

        button.visible = !this.menu.filter?.getOrNull(index).isNullOrEmpty()

        this.leftButtons.add(button)
        this.addRenderableWidget(button)
    }

    // If it's an Item Filter, toggles between requiring the same components or not
    // If it's a Tag Filter, cycles which Tag it's filtering
    private fun addRightButtons(index: Int) {
        val x = this.leftPos + 8 + index * 18 + 9
        val y = this.topPos + 15

        val width = 8
        val height = 8

        val buttonId = ItemFilterMenu.getRightButtonId(index)

        val button = ChangingColorButton(
            x = x,
            y = y,
            width = width,
            height = height,
            messagesGetter = {
                when (
                    val filterAtIndex = this.menu.filter?.getOrNull(index)
                ) {
                    is FilterEntry.Item -> {
                        listOf(
                            ModLanguageProvider.Tooltips.ITEM_FILTER_IGNORE_COMPONENTS.toComponent().withStyle(
                                if (filterAtIndex.requireSameComponents) ChatFormatting.DARK_GRAY else ChatFormatting.GRAY
                            ),
                            ModLanguageProvider.Tooltips.ITEM_FILTER_REQUIRE_COMPONENTS.toComponent().withStyle(
                                if (filterAtIndex.requireSameComponents) ChatFormatting.GRAY else ChatFormatting.DARK_GRAY
                            )
                        )
                    }

                    is FilterEntry.Tag -> {
                        val itemTags = filterAtIndex.backupStack.tags.toList()

                        itemTags.map {
                            it.getComponent().withStyle(
                                if (it == filterAtIndex.tagKey) ChatFormatting.GRAY else ChatFormatting.DARK_GRAY
                            )
                        }
                    }

                    else -> {
                        listOf(Component.empty())
                    }
                }
            },
            colorGetter = {
                val filterAtIndex = this.menu.filter?.getOrNull(index)

                if (filterAtIndex is FilterEntry.Item) {
                    if (filterAtIndex.requireSameComponents) {
                        0xFF37C63C.toInt()
                    } else {
                        0xFFFF5623.toInt()
                    }
                } else {
                    0xFF444444.toInt()
                }

            },
            font = this.font,
            onPress = {
                ModPacketHandler.messageServer(ClientClickedMenuButton(buttonId))
            }
        )

        button.visible = !this.menu.filter?.getOrNull(index).isNullOrEmpty()

        this.rightButtons.add(button)
        this.addRenderableWidget(button)
    }

    override fun containerTick() {
        super.containerTick()

        for (buttonIndex in this.leftButtons.indices) {
            val button = this.leftButtons.elementAtOrNull(buttonIndex) ?: continue

            button.visible = !this.menu.filter?.getOrNull(buttonIndex).isNullOrEmpty()
        }

        for (buttonIndex in this.rightButtons.indices) {
            val button = this.rightButtons.elementAtOrNull(buttonIndex) ?: continue

            button.visible = !this.menu.filter?.getOrNull(buttonIndex).isNullOrEmpty()
        }
    }

    // Render stuff

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
        super.render(guiGraphics, mouseX, mouseY, partialTick)
        this.renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        this.background.render(
            guiGraphics,
            this.leftPos,
            this.topPos,
        )
    }

}